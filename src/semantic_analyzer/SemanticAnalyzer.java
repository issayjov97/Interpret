/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic_analyzer;

import java.util.ArrayList;
import parser.AST;
import parser.AST.Assign;
import parser.AST.BinOp;
import parser.AST.*;
import semantic_analyzer.Symbol.*;
import java.util.List;

/**
 *
 * @author dzhohar
 */
public class SemanticAnalyzer {

    private SymbolTable table;

    public SymbolTable getTable() {
        return table;
    }

    public SemanticAnalyzer() {
        table = new SymbolTable();
    }

    public void visit_BinOp(AST node) {
        BinOp op = (BinOp) node;
        visit(op.getLeft());
        visit(op.getRight());

    }

    public void visitProgram(AST node) {
        Program program = (Program) node;
        visit(program.getBlock());
    }

    public void visitBlock(Block node) {
        for (List<AST> declNode : node.getDeclNodes()) {
            for (AST var : declNode) {
                visit(var);
            }
        }
        visit(node.getCompound());
    }

    public void visitVarDecl(AST node) {
        VarDecl varDecl = (VarDecl) node;
        String name = varDecl.getType().getValue();
        Symbol typeSymbol = table.lookup(name);
        String varName = varDecl.getVar().getValue();
        VarSymbol vs = new VarSymbol(varName, typeSymbol);
        table.define(vs);
    }

    public void visit_Num(AST node) {
    }

    public void visitVar(AST node) {
        Var var = (Var) node;
        String name = var.getValue();
        Symbol s = table.lookup(name);
        if (s == null) {
            throw new Error("Name Error");
        }
    }

    public void visit_Compound(AST node) {
        Compound compound = (Compound) node;
        for (int i = 0; i < compound.getChildren().size(); i++) {
            visit(compound.getChildren().get(i));
        }
    }

    public void visit_NoOp(AST node) {

    }

    public void visit_UnaryOP(AST node) {
        UnaryOp op = (UnaryOp) node;
        visit(op.getNode());
    }

    public void visitAssign(AST node) {
        Assign assign = (Assign) node;
        String varName = assign.getLeft().getValue();
        Symbol varSymbol = table.lookup(varName);
        if (varSymbol == null) {
            throw new VerifyError("Name error");
        }
        visit(assign.getRight());

    }

    public void visitProcedureCall(AST node) {
        ProcedureCall procedureCall = (ProcedureCall) node;
        ArrayList<AST> params = procedureCall.getParams();
        for (AST param : params) {
            visit(param);
        }

    }

    public void visitProcedureDecl(AST node) {
        ProcedureDecl procedureDecl = (ProcedureDecl) node;
        table.define(new ProcedureSymbol(procedureDecl.getName()));
    }

    public void visit(AST node) {
        if (node instanceof Num) {
            visit_Num(node);
        } else if (node instanceof BinOp) {
            visit_BinOp(node);
        } else if (node instanceof UnaryOp) {
            visit_UnaryOP(node);
        } else if (node instanceof Compound) {
            visit_Compound(node);
        } else if (node instanceof Assign) {
            visitAssign(node);
        } else if (node instanceof NoOp) {
            visit_NoOp(node);
        } else if (node instanceof Var) {
            visitVar(node);
        } else if (node instanceof VarDecl) {
            visitVarDecl(node);
        } else if (node instanceof Program) {
            visitProgram(node);
        } else if (node instanceof Block) {
            visitBlock((Block) node);
        } else if (node instanceof ProcedureDecl) {
            visitProcedureDecl(node);
        } else if (node instanceof ProcedureCall) {
            visitProcedureCall(node);
        }
    }

}
