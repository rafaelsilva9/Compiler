package com.compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
            FileReader file = new FileReader("C:/Users/Rafael/Documents/FileToRead.txt");
            BufferedReader bufferedReader = new BufferedReader(file);
            Scanner scanner = new Scanner();

            Cursor cursor = new Cursor(bufferedReader);

            while(!cursor.isLastCharacterProcessed()) {

                Token token = scanner.process(cursor);
                if(token != null){
                    System.out.println(token.getClassification() + " " + token.getLexeme());
                }
            }

            file.close();
    }
}
