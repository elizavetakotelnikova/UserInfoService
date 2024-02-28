package exceptions;

public class IncorrectArgumentsException extends Exception
{
    public IncorrectArgumentsException() {}

    public IncorrectArgumentsException(String message)
    {
        super(message);
    }
}
