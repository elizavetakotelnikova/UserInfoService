package com.example.ownermicroservice.exceptions;

public class ValidationException extends Exception
{
    public ValidationException() {}

    public ValidationException(String message)
    {
        super(message);
    }
}

