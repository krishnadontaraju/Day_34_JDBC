public class Exceptions extends Exception {

    exceptionType type;

    public Exceptions(exceptionType type , String message){
        super(message);
        this.type = type;

    }

    enum exceptionType{
        NULL_INPUT,
        SQL_EXCEPTION;

    }
}
