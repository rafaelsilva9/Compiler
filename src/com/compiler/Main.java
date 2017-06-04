package com.compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//            FileReader file = new FileReader(args[0]);
            FileReader file = new FileReader("C:\\Users\\Rafael\\Downloads\\Teste_IF.txt");
            BufferedReader bufferedReader = new BufferedReader(file);
            Cursor cursor = new Cursor(bufferedReader);
            Parser parser = new Parser();
            parser.procces(cursor);
            file.close();
    }
}
