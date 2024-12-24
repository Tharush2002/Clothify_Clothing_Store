package exceptions;

public class NoPasswordMatchFoundException extends Exception{
    public NoPasswordMatchFoundException(String str){
        super(str);
    }
}
