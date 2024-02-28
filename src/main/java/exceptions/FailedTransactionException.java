package exceptions;

public class FailedTransactionException extends Exception
{
    public FailedTransactionException() {}

    public FailedTransactionException(String message)
    {
        super(message);
    }
}
