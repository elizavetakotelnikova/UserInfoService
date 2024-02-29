package exceptions;

public class NotVerifiedException extends Exception
{
    public NotVerifiedException() {}

    public NotVerifiedException(String message)
    {
        super(message);
    }
}
