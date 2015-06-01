package ru.zy2ba.tmtrck.exception;

/**
 * @author Zy2ba
 * @since 19.05.2015
 */
public class AlreadyInUseException extends Exception {
    public AlreadyInUseException(){
        super();
    }

    public AlreadyInUseException(String msg){
        super(msg);
    }
}
