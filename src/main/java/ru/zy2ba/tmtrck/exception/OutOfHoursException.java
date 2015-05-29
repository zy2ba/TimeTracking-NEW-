package ru.zy2ba.tmtrck.exception;

/**
 * Created by Zy2ba on 28.05.2015.
 */
public class OutOfHoursException extends Exception {
    public OutOfHoursException(){
        super();
    }

    public OutOfHoursException(String msg){
        super(msg);
    }
}
