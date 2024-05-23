package com.example.catmicroservice.exceptions;

public class IncorrectArgumentsException extends Exception
{
    public IncorrectArgumentsException() {}

    public IncorrectArgumentsException(String message)
    {
        super(message);
    }
}
