package com.compiler;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Rafael on 12/03/2017.
 */
public class Cursor {
    private BufferedReader bufferedReader;
    private char character;
    private int column;
    private int line;

    public Cursor(BufferedReader bufferedReader){
        this.bufferedReader = bufferedReader;
        this.column = 0;
        this.line = 1;
    }

    public boolean eof() {
        boolean eof = true;
        try{
            eof = !bufferedReader.ready();
        } catch (IOException e){
            e.printStackTrace();
        }
        return eof;
    }

    public int getColumn() {
        return column;
    }

    public Character getNext() {
        try{
            if(!eof()){
                if(character == '\n'){
                    line ++;
                    column = 1;
                } else {
                    column ++;
                }
                return character = ((char)bufferedReader.read());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
}
