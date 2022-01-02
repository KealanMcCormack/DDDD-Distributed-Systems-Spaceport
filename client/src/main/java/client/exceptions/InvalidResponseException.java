package client.exceptions;

public class InvalidResponseException extends RuntimeException{

    public InvalidResponseException(String msg){
        super(msg);
    }

    public InvalidResponseException(String msg, Throwable e){
        super(msg, e);
    }

}
