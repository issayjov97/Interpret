/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic_analyzer;

import java.util.ArrayList;
import parser.AST;

/**
 *
 * @author dzhohar
 */
public class Symbol {

    private String name;
    private Symbol type;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Symbol type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Symbol getType() {
        return type;
    }

    static class BuiltInTypeSymbol extends Symbol {

        public BuiltInTypeSymbol(String name) {
            super(name);
        }

        @Override
        public String toString() {
            return this.getName();
        }

    }

    static class VarSymbol extends Symbol {

        public VarSymbol(String name, Symbol type) {
            super(name, type);
        }

        @Override
        public String toString() {
            return "< " + this.getName() + ":" + this.getType() + " >";
        }

    }

    static class ProcedureSymbol extends Symbol{



        public ProcedureSymbol(String name) {
            super(name);

        }
    
        @Override
        public String toString() {
            return "ProcedureSymbol{" + "name=" + this.getName() + '}';
        }

    }
}
