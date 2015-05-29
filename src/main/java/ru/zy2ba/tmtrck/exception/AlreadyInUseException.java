package ru.zy2ba.tmtrck.exception;

/**
 * Created by Zy2ba on 19.05.2015.
 */
public class AlreadyInUseException extends Exception {
    public AlreadyInUseException(){
        super();
    }

    public AlreadyInUseException(String msg){
        super(msg);
    }
}
