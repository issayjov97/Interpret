package lex_analyzator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dzhohar
 */
public enum Token {
    PROGRAM("program"),
    VAR("var"),
    INTEGER_CONST("\\d+"),
    PROCEDURE("procedure"),
    BEGIN_KEYWORD("begin"),
    WHILE_KEYWORD("while"),
    DO_KEYWORD("do"),
    ELSE_KEYWORD("else"),
    END_KEYWORD("end"),
    THEN_KEYWORD("then"),
    IF_KEYWORD("if"),
    VARSYM("var"),
    INTSYM("integer"),
    OUT("out"),
    QUOTE("\""),
    STRING("string"),
    IDENTIFIER("\\w+"),
    SEMICOLON(";"),
    COMMA(","),
    EQSYM("=="),
    BECOMESYM(":="),
    COLON(":"),
    DOT("\\."),
    PLUSSYM("\\+"),
    MINUSSYM("-"),
    MULTISYM("\\*"),
    DIVIDE("/"),
    LEQSYM("<="),
    GEQSYM(">="),
    NEQSYM("<>"),
    GTRSYM(">"), 
    LESSSYM("<"),
    LPARENTSYM("\\("),
    RPARENTSYM("\\)"),
    LCURLYBRACESYM("\\{"),
    RCURLYBRACESYM("\\}"),;
    Pattern p;

    private Token(String regex) {

        p = Pattern.compile("^" + regex);
    }

    public int end(String s) {
        Matcher m = p.matcher(s);
        if (m.find()) {
            return m.end();
        }
        return -1;
    }

}
