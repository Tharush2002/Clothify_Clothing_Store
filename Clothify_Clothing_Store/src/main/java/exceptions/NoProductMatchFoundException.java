package exceptions;

public class NoProductMatchFoundException extends Exception{
    public NoProductMatchFoundException(String str){
        super(str);
    }
}
