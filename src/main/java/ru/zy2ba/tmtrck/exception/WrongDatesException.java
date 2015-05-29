package ru.zy2ba.tmtrck.exception;

/**
 * Created by Zy2ba on 29.05.2015.
 */
public class WrongDatesException extends Exception {
    public WrongDatesException(){
        super();
    }

    WrongDatesException(String msg){
        super(msg);
    }
}
