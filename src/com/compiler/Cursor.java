package com.compiler;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Rafael on 12/03/2017.
 */
public class Cursor {
    private BufferedReader bufferedReader;
    private Character character;
    private int column;
    private int line;
    private boolean eof;

    public Cursor(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        this.column = 0;
        this.line = 1;
        this.character = new Character(' ');
        getNext();
    }

    public boolean hasNext() {
        boolean eof = false;
        try{
            eof = !bufferedReader.ready();
        } catch (IOException e){
            e.printStackTrace();
        }
        return !eof;
    }

    public int getColumn() {
        return column;
    }

    public Character getNext() {
        try{
                if(character == '\n') {
                    line ++;
                    column = 1;
                } else if(character == '\t') {
                    column += 4;
                } else {
                    column ++;
                }
                return character = ((char)bufferedReader.read());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ' ';
    }

    public char getActualCharacter() {
        return character;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }

    public boolean isEof() {
        return eof;
    }
}
