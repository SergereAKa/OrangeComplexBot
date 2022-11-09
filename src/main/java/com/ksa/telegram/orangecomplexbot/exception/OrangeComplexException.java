package com.ksa.telegram.orangecomplexbot.exception;


import java.util.Objects;

public class OrangeComplexException extends RuntimeException{

    public OrangeComplexException(){
        super();
    }

    public OrangeComplexException(Throwable cause ){
        super(cause);
    }

    public OrangeComplexException(String format, Object ... args){
        super(String.format(format, args));
    }

    public OrangeComplexException(Throwable cause, String format, Object ... args){
        super(String.format(format, args), cause);
    }

    public OrangeComplexException(String message, Throwable cause){
        super(message, cause);
    }


}
