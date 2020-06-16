/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import java.util.ArrayList;
import parser.AST;
import lex_analyzator.Token;
import parser.AST.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dzhohar
 */
public class Interpreter<T> {

    private HashMap<String, Integer> map;
    private AST tree;

    public Interpreter(AST tree) {
        map = new HashMap<>();
        this.tree = tree;
    }

    public int interpret() {

        return visit(tree);
    }

    public int visit_BinOp(AST node) {
        BinOp op = (BinOp) node;
        int left = visit(op.getLeft());
        int right = visit(op.getRight());
        if (op.getToken() == Token.PLUSSYM) {
            return visit(op.getLeft()) + visit(op.getRight());
        } else if (op.getToken() == Token.MULTISYM) {
            return visit(op.getLeft()) * visit(op.getRight());
        } else if (op.getToken() == Token.MINUSSYM) {
            return visit(op.getLeft()) - visit(op.getRight());
        } else if (op.getToken() == Token.DIVIDE) {
            return visit(op.getLeft()) / visit(op.getRight());
        } else if (op.getToken() == Token.EQSYM) {
            int result = visit(op.getLeft()) == visit(op.getRight()) ? 1 : 0;
            return result;
        } else if (op.getToken() == Token.GTRSYM) {
            return left > right ? 1 : 0;
        } else if (op.getToken() == Token.GEQSYM) {
            return left >= right ? 1 : 0;
        } else if (op.getToken() == Token.LESSSYM) {
            return left < right ? 1 : 0;
        } else if (op.getToken() == Token.LEQSYM) {
            return left <= right ? 1 : 0;
        } else if (op.getToken() == Token.NEQSYM) {
            return left != right ? 1 : 0;
        }
        return 0;

    }

    public int visitProgram(AST node) {
        Program program = (Program) node;
        visit(program.getBlock());
        return 0;
    }

    public int visitBlock(Block node) {
        for (List<AST> declNode : node.getDeclNodes()) {
            for (AST var : declNode) {
                visit(var);

            }
        }
        visit(node.getCompound());
        return 0;
    }

    public int visitVarDecl() {
        return 0;
    }

    public int visitType() {
        return 0;
    }

    public int visit_Num(AST node) {
        Num num = (Num) node;
        return num.getNumber();
    }

    public int visitVar(AST node) {
        Var var = (Var) node;
        String name = var.getValue();
        int val = map.get(name);
        return val;
    }

    public int visitProcedureCall(AST node) {
        ProcedureCall procedureCall = (ProcedureCall) node;

        ArrayList<AST> params = procedureCall.getParams();
        for (AST param : params) {
            visit(param);
        }

        return 0;
    }

    public int visit_Compound(AST node) {
        Compound compound = (Compound) node;
        for (int i = 0; i < compound.getChildren().size(); i++) {
            visit(compound.getChildren().get(i));
        }
        return 0;
    }

    public int visit_Assign(AST node) {
        Assign assign = (Assign) node;
        Var var = (Var) assign.getLeft();
        int r = visit(assign.getRight());
        map.put(var.getValue(), r);
        return 0;

    }

    public int visit_NoOp(AST node) {
        return 0;
    }

    public int visit_UnaryOP(AST node) {
        UnaryOp op = (UnaryOp) node;
        if (op.getToken() == Token.PLUSSYM) {
            int resl = +visit(op.getNode());
            return resl;
        } else if (op.getToken() == Token.MINUSSYM) {
            int resl = -visit(op.getNode());
            return resl;
        }
        return 0;
    }

    public int visitProcedure(AST node) {
        ProcedureDecl pd = (ProcedureDecl) node;
        visit(pd.getBlock());
        return 0;
    }

    public int visitConditionalNode(AST node) {
        ConditionalNode cond = (ConditionalNode) node;
        if (visit(cond.getExpression()) == 1) {
            for (AST ast : cond.getTrueBlock()) {
                visit(ast);
            }
        } else {
            for (AST ast : cond.getTrueBlock()) {
                visit(ast);
            }
        }
        return 0;
    }

    public int visitWhileDoNode(AST node) {
        WhileDoNode wdn = (WhileDoNode) node;
        while (visit(wdn.getExpr()) != 0) {
            visit(wdn.getBlock());
        }
        return 0;
    }

    public int visitOutNode(AST node) {
        Out out = (Out) node;
        System.out.println(out.getValues());
        return 0;
    }

    public int visit(AST node) {
        if (node instanceof Num) {
            return visit_Num(node);
        } else if (node instanceof BinOp) {
            return visit_BinOp(node);
        } else if (node instanceof UnaryOp) {
            return visit_UnaryOP(node);
        } else if (node instanceof Compound) {
            return visit_Compound(node);
        } else if (node instanceof Assign) {
            return visit_Assign(node);
        } else if (node instanceof NoOp) {
            return visit_NoOp(node);
        } else if (node instanceof Var) {
            return visitVar(node);
        } else if (node instanceof VarDecl) {
            return visitVarDecl();
        } else if (node instanceof Type) {
            return visitType();
        } else if (node instanceof Program) {
            return visitProgram(node);
        } else if (node instanceof Block) {
            return visitBlock((Block) node);
        } else if (node instanceof ProcedureDecl) {
            return visitProcedure(node);
        } else if (node instanceof ProcedureCall) {
            return visitProcedureCall(node);
        } else if (node instanceof ConditionalNode) {
            return visitConditionalNode(node);
        } else if (node instanceof WhileDoNode) {
            return visitWhileDoNode(node);
        } else if (node instanceof Out) {
            return visitOutNode(node);
        };
        return 0;
    }

    public void print() {
        for (Map.Entry<String, Integer> en : map.entrySet()) {
            String key = en.getKey();
            Integer val = en.getValue();
            System.out.println(key + ": " + val);
        }
    }

}
