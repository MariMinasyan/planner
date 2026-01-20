package am.aua.planner.exceptions;

public class MalformedStringException extends Exception{
    public MalformedStringException(){
        super("String was malformed.");
    }

    public MalformedStringException(String msg){
        super(msg);
    }
}
