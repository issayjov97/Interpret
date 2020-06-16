/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import exceptions.LexerError;
import exceptions.ParserError;
import lex_analyzator.Token;
import lex_analyzator.Analyzator;
import parser.AST.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dzhohar
 */
public class Parser {

    private final Analyzator analyzator;
    private Token token;
    private String value;
    private Token curToken;
    private String curValue;

    public Parser(Analyzator analyzator) {
        this.analyzator = analyzator;

    }

    public Program program() throws ParserError, LexerError {
        eat(Token.PROGRAM);
        Var varNode = variable();
        String progName = varNode.getValue();
        eat(Token.SEMICOLON);
        Block block = block();
        Program program = new Program(progName, block);
        eat(Token.DOT);
        return program;
    }

    public Compound compoundStatement() throws ParserError, LexerError {
        eat(Token.BEGIN_KEYWORD);
        ArrayList<AST> nodes = statementList();
        eat(Token.END_KEYWORD);
        Compound root = new Compound();
        for (AST node : nodes) {
            root.append(node);
        }
        return root;
    }

    public ArrayList<AST> statementList() throws ParserError, LexerError {
        AST node = statement();
        ArrayList<AST> results = new ArrayList();
        results.add(node);
        while (curToken == Token.SEMICOLON) {
            eat(Token.SEMICOLON);
            AST a = statement();
            results.add(a);
            if (curToken == Token.IDENTIFIER) {
                break;
            }
        }
        return results;
    }

    public AST statement() throws ParserError, LexerError {
        token = curToken;
        value = curValue;
        AST node = null;
        if (token == Token.BEGIN_KEYWORD) {
            node = compoundStatement();
        } else if (token == Token.IF_KEYWORD) {
            node = conditionalStatement();
        }  else if (token == Token.WHILE_KEYWORD) {
            node = whileStatement();
        } else if (token == Token.OUT) {
            node = outStatement();
        }else if (token == Token.IDENTIFIER && analyzator.getNextChar()=='(') {
            node = procCallStatement();
        }else if (token == Token.IDENTIFIER) {
            node = assignmentStatement();
        } 
        else {
            node = empty();
        }
        return node;
    }
    
    public Out outStatement() throws ParserError, LexerError{
        eat(Token.OUT);
        eat(Token.QUOTE);
        String result = "";
        while(curToken != Token.QUOTE){
            result+= curValue;
            eat(Token.IDENTIFIER);
        }
        Out out = new Out(result);
        eat(Token.QUOTE);
        return out;
    }

    public Assign assignmentStatement() throws ParserError, LexerError {

        Var left = variable();
        token = curToken;
        eat(Token.BECOMESYM);
        AST right = expr();
        Assign node = new Assign(left, token, right);
        return node;
    }

    public AST.ConditionalNode conditionalStatement() throws ParserError, LexerError {
        eat(Token.IF_KEYWORD);
        AST expr = boolean_expr();
        AST falseStatement = null;
        eat(Token.THEN_KEYWORD);
        ArrayList<AST> trueStatement = statementList();
        if (curToken == Token.ELSE_KEYWORD) {
            eat(Token.ELSE_KEYWORD);
            falseStatement = statement();
        }
        return new ConditionalNode(expr, trueStatement, falseStatement);
    }

    public AST whileStatement() throws ParserError, LexerError {
        eat(Token.WHILE_KEYWORD);
        AST expr = boolean_expr();
        eat(Token.DO_KEYWORD);
        AST loopStatement = statement();
        return new WhileDoNode(expr, loopStatement);
    }

    public ProcedureCall procCallStatement() throws ParserError, LexerError {
        token = curToken;
        value = curValue;
        String procName = curValue;
        eat(Token.IDENTIFIER);
        eat(Token.LPARENTSYM);
        ArrayList<AST> params = new ArrayList<>();
        if (curToken != Token.RPARENTSYM) {
            AST param = expr();
            params.add(param);
            while (curToken == Token.COMMA) {
                eat(Token.COMMA);
                param = expr();
                params.add(param);
            }
          
        }
        eat(Token.RPARENTSYM);
        return new ProcedureCall(procName, token, params);
    }

    public AST boolean_expr() throws ParserError, LexerError {

        AST result = expr();
        while (curToken == Token.EQSYM || curToken == Token.NEQSYM || curToken == Token.LESSSYM
                || curToken == Token.LEQSYM || curToken == Token.GTRSYM || curToken == Token.GEQSYM) {
            token = curToken;
            if (token == Token.EQSYM) {
                eat(Token.EQSYM);
            } else if (token == Token.NEQSYM) {
                eat(Token.NEQSYM);
            } else if (token == Token.LESSSYM) {
                eat(Token.LESSSYM);
            } else if (token == Token.LEQSYM) {
                eat(Token.LEQSYM);
            } else if (token == Token.GTRSYM) {
                eat(Token.GTRSYM);
            } else if (token == Token.GEQSYM) {
                eat(Token.GEQSYM);
            }
            result = new BinOp(result, token, expr());

        }
        return result;

    }

    public Var variable() throws ParserError, LexerError {

        Var node = new Var(curToken, curValue);
        eat(Token.IDENTIFIER);
        return node;
    }

    public NoOp empty() {
        return new NoOp();
    }

    public Block block() throws ParserError, LexerError {

        ArrayList<List<AST>> declNodes = declarations();
        Compound compound = compoundStatement();
        Block node = new Block(compound, declNodes);
        return node;
    }

    public ArrayList<List<AST>> declarations() throws ParserError, LexerError {
        ArrayList<List<AST>> declarations = new ArrayList<>();
        ArrayList<AST> declaration = new ArrayList<>();
        if (curToken == Token.VAR) {
            eat(Token.VAR);
            while (curToken == Token.IDENTIFIER) {
                declaration = variableDeclaration();
                declarations.add(declaration);
                eat(Token.SEMICOLON);
            }
        }

        while (curToken == Token.PROCEDURE) {
            ProcedureDecl procedureDecl = procedure_declaration();
            declaration.add(procedureDecl);
            eat(Token.SEMICOLON);
        }
        return declarations;
    }

    public ProcedureDecl procedure_declaration() throws ParserError, LexerError {
        ArrayList<List<Param>> paramList = null;
        eat(Token.PROCEDURE);
        String procName = curValue;
        eat(Token.IDENTIFIER);
        if (curToken == Token.LPARENTSYM) {
            eat(Token.LPARENTSYM);
            paramList = formal_parametr_list();
            eat(Token.RPARENTSYM);
        }
        eat(Token.SEMICOLON);
        Block block = block();
        ProcedureDecl procDecl = new ProcedureDecl(procName, block, paramList);
        return procDecl;
    }

    public ArrayList<List<Param>> formal_parametr_list() throws ParserError, LexerError {
        ArrayList<List<Param>> paramList = new ArrayList<>();
        ArrayList<Param> params = formal_parametrs();
        paramList.add(params);
        while (curToken == Token.SEMICOLON) {
            eat(Token.SEMICOLON);
            paramList.add(formal_parametrs());
        }
        return paramList;
    }

    public ArrayList<Param> formal_parametrs() throws ParserError, LexerError {
        ArrayList<Param> params = new ArrayList<>();
        ArrayList<Var> varNodes = new ArrayList<>();
        varNodes.add(new Var(curToken, curValue));
        eat(Token.IDENTIFIER);

        while (curToken == Token.COMMA) {
            eat(Token.COMMA);
            varNodes.add(new Var(curToken, curValue));
            eat(Token.IDENTIFIER);
        }
        eat(Token.COLON);
        Type nodeType = typeSpec();
        for (Var varNode : varNodes) {
            params.add(new Param(varNode, nodeType));
        }
        return params;
    }

    public ArrayList<AST> variableDeclaration() throws ParserError, LexerError {

        ArrayList<AST> varNodes = new ArrayList<>();
        ArrayList<AST> varDeclarations = new ArrayList<>();
        varNodes.add(new Var(curToken, curValue));
        eat(Token.IDENTIFIER);

        while (curToken == Token.COMMA) {
            eat(Token.COMMA);
            varNodes.add(new Var(curToken, curValue));
            eat(Token.IDENTIFIER);
        }
        eat(Token.COLON);
        Type nodeType = typeSpec();
        for (AST varNode : varNodes) {
            varDeclarations.add(new VarDecl(nodeType, (Var) varNode));
        }
        return varDeclarations;
    }

    public Type typeSpec() throws ParserError, LexerError {
        token = curToken;
        value = curValue;
        if (token == Token.INTSYM) {
            eat(Token.INTSYM);
        }
        else if(token == Token.STRING){
            eat(Token.STRING);
        }
        Type node = new Type(token, value);
        return node;
    }

    public AST expr() throws ParserError, LexerError {
        AST result = term();
        while (curToken == Token.MINUSSYM || curToken == Token.PLUSSYM) {
            token = curToken;
            value = curValue;
            if (token == Token.PLUSSYM) {
                eat(Token.PLUSSYM);
            } else {
                eat(Token.MINUSSYM);
            }
            result = new BinOp(result, token, term());
        }

        return result;
    }

    private AST term() throws ParserError, LexerError {
        AST result = factor();
        while (curToken == Token.MULTISYM || curToken == Token.DIVIDE) {
            token = curToken;
            value = curValue;
            if (token == Token.MULTISYM) {
                eat(Token.MULTISYM);
            } else if (token == Token.DIVIDE) {
                eat(Token.DIVIDE);
            }
            result = new BinOp(result, token, factor());
        }

        return result;
    }

    private AST factor() throws ParserError, LexerError {
        value = curValue;
        token = curToken;
        AST result = null;
        if (token == Token.PLUSSYM) {
            eat(Token.PLUSSYM);
            result = new UnaryOp(token, factor());
            return result;
        } else if (token == Token.MINUSSYM) {
            eat(Token.MINUSSYM);
            result = new UnaryOp(token, factor());
            return result;
        } else if (token == Token.LPARENTSYM) {
            eat(Token.LPARENTSYM);
            result = expr();
            eat(Token.RPARENTSYM);
            return result;
        } else if (token == Token.INTEGER_CONST) {
            eat(Token.INTEGER_CONST);
            return new Num(Integer.parseInt(value));
        } else {
            result = variable();
        }
        return result;

    }

    private void eat(Token type) throws ParserError, LexerError {
        if (type == curToken) {
            analyzator.moveAhead();
            if (!analyzator.isVycerpany()) {
                curValue = analyzator.Lexema();
                curToken = analyzator.Token();
            } else {
                curToken = null;
                curValue = null;
            }
        } else {
            throw new ParserError("Unexpected token: " + curToken);
        }

    }

    public AST parse() throws ParserError, LexerError {
        analyzator.moveAhead();
        curValue = analyzator.Lexema();
        curToken = analyzator.Token();

        AST node = program();
        return node;
    }

}
