package com.company.neu;

public class AnalysisTableItem {
    private char Vn;
    private char Vt;
    private int changeToNextGrammar;

    public AnalysisTableItem(char vn, char vt, int changeToNextGrammar) {
        Vn = vn;
        Vt = vt;
        this.changeToNextGrammar = changeToNextGrammar;
    }

    public char getVt() {
        return Vt;
    }

    public int getChangeToNextGrammar() {
        return changeToNextGrammar;
    }
}
