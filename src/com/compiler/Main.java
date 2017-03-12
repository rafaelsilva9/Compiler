package com.compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
            FileReader file = new FileReader("C:/Users/Rafael/Documents/FileToRead.txt");
            BufferedReader bufferedReader = new BufferedReader(file);
            Scanner scanner = new Scanner();
            String line = bufferedReader.readLine();

            Cursor cursor = new Cursor(line,0);

            while (cursor.getLine() != null){

                while (!cursor.eof()){
                    Token token = scanner.process(line, cursor);

                    if(token != null){
                        System.out.println(token.getClassification() + " " + token.getLexeme());
                    }
                }

                line = bufferedReader.readLine();
                cursor.updateCursor(line, 0);
            }

            file.close();
    }
}
