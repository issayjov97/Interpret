/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import lex_analyzator.Token;

/**
 *
 * @author dzhohar
 */
public class LexerError extends Exception{

    public LexerError(String message) {
        super(message);
    }
    

}
