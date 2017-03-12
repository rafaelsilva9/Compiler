package com.compiler;

/**
 * Created by Rafael on 12/03/2017.
 */
public class Cursor {
    private String line;
    private int column;

    public Cursor(String line, int column){
        this.line = line;
        this.column = column;
    }

    public void updateCursor(String line, int column){
        this.line = line;
        this.column = column;
    }

    public void move(){
        if(!eof()){
            column ++;
        }
    }

    public boolean eof(){
        return column >= line.length();
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
