package main;



import exceptions.LexerError;
import exceptions.ParserError;
import interpreter.Interpreter;
import semantic_analyzer.SemanticAnalyzer;
import parser.AST;
import parser.Parser;
import lex_analyzator.Analyzator;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dzhohar
 */
public class Main {

    public static void main(String[] args)  {
        try {
            Analyzator analyzator = new Analyzator("source.txt");
            Parser parser = new Parser(analyzator);
            System.out.println("====== Tokens ======");
            AST tree = parser.parse();
            System.out.println("====== SemanticAnalyzer ======");
            SemanticAnalyzer table = new SemanticAnalyzer();
           table.visit(tree);
                System.out.println("====== Interpreter ======");
            Interpreter interpreter = new Interpreter(tree);
            interpreter.interpret();
            interpreter.print();


        } catch (ParserError | LexerError ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
