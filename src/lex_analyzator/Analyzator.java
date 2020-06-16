package lex_analyzator;

import exceptions.LexerError;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import jdk.nashorn.api.tree.BreakTree;

public class Analyzator {

    private String path;
    private StringBuilder input;
    private String lexema;
    private Token token;
    private String errorMessage = "";
    private boolean vycerpany = false;

    public Analyzator(String path) {
        this.path = path;
        this.input = new StringBuilder();

        try {
            Scanner scan = new Scanner(new File(path));
            System.out.println("====== Input program ======");
            while (scan.hasNext()) {
                String line = scan.nextLine();
                input.append(line);
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.getCause().getMessage();
        }

    }

    public void moveAhead() throws LexerError {
        if (vycerpany) {
            return;
        }

        if (input.length() == 0) {
            vycerpany = true;
            return;
        }
        deletSpace();
        if (findToken()) {
            if(token== Token.LCURLYBRACESYM){
                deleteComments();
                moveAhead();
            }
            System.out.println(token + ":" + lexema);
            return;
        }
        if (input.length() > 0) {
            throw new LexerError("Neznamy symbol: " + token + input.charAt(0));
        }
    }

    public void deletSpace() {
        while (input.length() > 0 && input.charAt(0) == (char) 32) {
            input.delete(0, 1);
        }

    }

    public void deleteComments() {
        while (input.length() > 0 && input.charAt(0) != '}') {
            input.delete(0, 1);
        }
        input.delete(0, 1);
    }

    public boolean findToken() {
        for (Token t : Token.values()) {
            int end = t.end(input.toString());

            if (end != -1) {
                token = t;
                lexema = input.substring(0, end);
                input.delete(0, end);
                return true;
            }

        }
        token = null;
        return false;
    }

    public char getNextChar() {
        if (isVycerpany()) {
            return ' ';
        }
        return input.charAt(0);
    }

    public Token Token() {
        return token;
    }

    public String Lexema() {
        return lexema;
    }

    public boolean isSuccessful() {
        return errorMessage.isEmpty();
    }

    public String errorMessage() {
        return errorMessage;
    }

    public boolean isVycerpany() {
        return vycerpany;
    }
}
