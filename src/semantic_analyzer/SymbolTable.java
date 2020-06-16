/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic_analyzer;

import semantic_analyzer.Symbol;
import semantic_analyzer.Symbol.BuiltInTypeSymbol;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dzhohar
 */
public class SymbolTable {

    private final HashMap<String, Symbol> table;

    public SymbolTable() {
        table = new HashMap<>();
        define(new BuiltInTypeSymbol("integer"));
        define(new BuiltInTypeSymbol("real"));
        define(new BuiltInTypeSymbol("string"));
    }

    @Override
    public String toString() {
        String s = "Symbols:[";
        for (Map.Entry<String, Symbol> en : table.entrySet()) {
            String key = en.getKey();
            Symbol val = en.getValue();
            s += String.valueOf(val) + ",";
        }
        s += " ]";
        return s;
    }

    public void define(Symbol symbol) {
        System.out.println("Define:" + symbol);
        table.put(symbol.getName(), symbol);

    }

    public Symbol lookup(String name) {
        System.out.println("Lookup: " + name);
        Symbol symbol = table.get(name);
        return symbol;
    }

//    public static void main(String[] args) {
//        SymbolTable table = new SymbolTable();
//        BuiltInTypeSymbol symbol = new BuiltInTypeSymbol("INTEGER");
////        table.define(symbol);
//        VarSymbol varSymbol = new VarSymbol("a", symbol);
//        table.define(varSymbol);
//        System.out.println(table);
//
//    }
}
