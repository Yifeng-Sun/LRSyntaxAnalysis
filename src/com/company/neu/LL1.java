package com.company.neu;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class LL1 {
    private String[] grammar = {"E->TY", "Y->+T{G+}Y", "Y->-T{G-}Y", "Y->e", "T->FX", "X->*F{G*}X", "X->/F{G/}X", "X->e", "F->i{Pi}", "F->(E)"};
    private AnalysisTableGenerator analysisTableGenerator = new AnalysisTableGenerator();
    private char E = analysisTableGenerator.getGrammar()[0].charAt(0);
    private Stack<Character> SYN;//语法栈
    private Stack<String> SEM;//语义栈
    private ArrayList<Quadruple> QT;//四元式区
    private char lastW = 0; //上个单词
    private char w = 0; //当前单词
    private char x = 0; // 当前栈顶
    public void printQT() {
        QT.forEach(quadruple -> System.out.println(quadruple.toString()));
    }



    public boolean analyze(String String) {
        analysisTableGenerator.setGrammar(grammar);
        SYN = new Stack<>();
        SEM = new Stack<>();
        QT = new ArrayList<>();
        analysisTableGenerator.init();
        int cursor = 0;
        SYN.push(String.charAt(String.length() - 1)); // 推入分号
        SYN.push(E); // 推入E
        int state = 1;
        int t = 1; //用于生成四元式
        while (true) {
            switch (state) {
                case 1:
                    lastW = w;
                    w = String.charAt(cursor++);// NEXT(w)
                    state = 2;
                    break;
                case 2:
                    x = SYN.pop();// POP(x)
                    if (x == '{') {
                        char operation = SYN.pop();
                        char symbol = SYN.pop();
                        if (SYN.pop() == '}') {
                            if (operation == 'G') {
                                String rightData = SEM.pop();// 一次pop 栈顶
                                String leftData = SEM.pop(); // 两次pop 次栈顶
                                Quadruple quadruple = new Quadruple(symbol, leftData, rightData, "t" + t++);
                                QT.add(quadruple);
                                SEM.push(quadruple.getResult());//一次push
                            } else if (operation == 'P') { // push
                                SEM.push(String.valueOf(lastW));
                            }
                        }
                        state = 2;
                    } else
                        state = 3;
                    break;
                case 3:
                    if (analysisTableGenerator.getVt().contains(x)) { // 看看表里有吗
                        state = 4; // 有 进状态4
                    } else {
                        state = 5; // 没 新建一个
                    }
                    break;
                case 4:
                    if (x == w) {
                        state = 1;
                    } else if (x == 'i' && Character.isLetter(w)) {
                        state = 1;
                    } else {
                        return false;
                    }
                    break;
                case 5:
                    if (analysisTableGenerator.getVn().contains(x)) {
                        int nextGrammar = -1;
                        for (Map.Entry<Character, ArrayList<AnalysisTableItem>> items : analysisTableGenerator.getAnalysisTable().entrySet()) {
                            if (items.getKey() == x) {
                                for (AnalysisTableItem item : items.getValue()) {
                                    if (item.getVt() == w || (item.getVt() == 'i' && Character.isLetter(w))) { // 如果符号能匹配上，或者寻找项的时候出现字母
                                        nextGrammar = item.getChangeToNextGrammar();
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        if (nextGrammar == -1) return false;
                        else {
                            String rightOfNextGrammar = grammar[nextGrammar].split("->")[1];
                            for (int i = rightOfNextGrammar.length() - 1; i >= 0; i--) {
                                if (rightOfNextGrammar.charAt(i) != 'e') SYN.push(rightOfNextGrammar.charAt(i));
                            }
                            state = 2;
                        }
                    } else {
                        state = 6;
                    }
                    break;
                case 6:
                    return w == ';';
            }
        }
    }

    public static void main(String[] args) {
	// write your code here
        LL1 ll1 = new LL1();
        boolean isValid = ll1.analyze("a*(b+c);");
        System.out.println(isValid);
        if(isValid){
            ll1.printQT();
        }
    }
}
