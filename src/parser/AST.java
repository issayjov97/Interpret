/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import lex_analyzator.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dzhohar
 */
public class AST {

    public static class BinOp extends AST {

        private AST left;
        private AST right;
        private Token token;

        public BinOp(AST left, Token token, AST right) {
            this.left = left;
            this.right = right;
            this.token = token;
        }

        public AST getLeft() {
            return left;
        }

        public AST getRight() {
            return right;
        }

        public Token getToken() {
            return token;
        }

        @Override
        public String toString() {
            return "BinOp{" + "left=" + left + ", right=" + right + ", token=" + token;
        }

    }

    public static class Assign extends AST {

        private Var left;
        private AST right;
        private Token token;

        public Assign(Var left, Token token, AST right) {
            this.left = left;
            this.right = right;
            this.token = token;
        }

        public Var getLeft() {
            return left;
        }

        public AST getRight() {
            return right;
        }

        public Token getToken() {
            return token;
        }

        public void setToken(Token token) {
            this.token = token;
        }

    }

    public static class Block extends AST {

        private Compound compound;
        private ArrayList<List<AST>> declNodes;

        public Block(Compound compound, ArrayList<List<AST>> declNodes) {
            this.compound = compound;
            this.declNodes = declNodes;
        }

        public Compound getCompound() {
            return compound;
        }

        public ArrayList<List<AST>> getDeclNodes() {
            return declNodes;
        }

    }

    public static class Compound extends AST {

        ArrayList<AST> children;

        public Compound() {
            children = new ArrayList<>();
        }

        public void append(AST node) {
            children.add(node);
        }

        public ArrayList<AST> getChildren() {
            return children;
        }

    }

    public static class UnaryOp extends AST {

        private Token token;
        private AST node;

        public UnaryOp(Token token, AST node) {
            this.token = token;
            this.node = node;
        }

        public Token getToken() {
            return token;
        }

        public AST getNode() {
            return node;
        }

    }

    public static class ConditionalNode extends AST {

        private AST expression;
        private  ArrayList<AST> trueBlock;
        private AST falseBlock;

        public ConditionalNode(AST expression,  ArrayList<AST> trueBlock, AST falseBlock) {
            this.expression = expression;
            this.trueBlock = trueBlock;
            this.falseBlock = falseBlock;
        }

        public AST getExpression() {
            return expression;
        }

        public  ArrayList<AST> getTrueBlock() {
            return trueBlock;
        }

        public AST getFalseBlock() {
            return falseBlock;
        }

    }

    public static class NoOp extends AST {

    }

    public static class Num extends AST {

        private int number;

        public Num(int num) {
            this.number = num;
        }

        public int getNumber() {
            return number;
        }

    }

    public static class IdentifierNode extends AST {

        private String value;

        public IdentifierNode(String value) {
            this.value = value;
        }

    }

    public static class ProcedureDecl extends AST {

        private String name;
        private Block block;
        private ArrayList<List<Param>> params;

        public ProcedureDecl(String name, Block block) {
            this.name = name;
            this.block = block;
        }

        public ProcedureDecl(String name, Block block, ArrayList<List<Param>> params) {
            this.name = name;
            this.block = block;
            this.params = params;
        }

        public String getName() {
            return name;
        }

        public Block getBlock() {
            return block;
        }

    }

    public static class Program extends AST {

        private String name;
        private Block block;

        public Program(String name, Block block) {
            this.name = name;
            this.block = block;
        }

        public String getName() {
            return name;
        }

        public Block getBlock() {
            return block;
        }

    }

    public static class Var extends AST {

        private Token token;
        private String value;

        public Var(Token token, String value) {
            this.token = token;
            this.value = value;
        }

        public Token getToken() {
            return token;
        }

        public String getValue() {
            return value;
        }

    }

    public static class VarDecl extends AST {

        private Type type;
        private Var var;

        public VarDecl(Type type, Var var) {
            this.type = type;
            this.var = var;
        }

        public Type getType() {
            return type;
        }

        public Var getVar() {
            return var;
        }

    }

    public static class WhileDoNode extends AST {

        private AST expr;
        private AST block;

        public WhileDoNode(AST expr, AST block) {
            this.expr = expr;
            this.block = block;
        }

        public AST getExpr() {
            return expr;
        }

        public AST getBlock() {
            return block;
        }

    }

    public static class Type extends AST {

        private Token token;
        private String value;

        public Type(Token token, String value) {
            this.token = token;
            this.value = value;
        }

        public Token getToken() {
            return token;
        }

        public String getValue() {
            return value;
        }
    }

    public static class ProcedureCall extends AST {

        private String name;
        private Token token;
        private ArrayList<AST> params;

        public ProcedureCall(String name, Token token, ArrayList<AST> params) {
            this.name = name;
            this.token = token;
            this.params = params;
        }

        public String getName() {
            return name;
        }

        public Token getToken() {
            return token;
        }

        public ArrayList<AST> getParams() {
            return params;
        }

    }

    public static class Param extends AST {

        private Var node;
        private Type type;

        public Param(Var node, Type type) {
            this.node = node;
            this.type = type;
        }

        public Var getNode() {
            return node;
        }

        public Type getType() {
            return type;
        }

    }

    public static class Out extends AST{

        private String values;

        public Out(String values) {
            this.values = values;
        }

        public String getValues() {
            return values;
        }
        
    }
}
