package exceptions;

public class NoCategoryMatchFoundException extends Exception{
    public NoCategoryMatchFoundException(String str){
        super(str);
    }
}
