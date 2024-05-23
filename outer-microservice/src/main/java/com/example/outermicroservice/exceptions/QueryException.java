package com.example.outermicroservice.exceptions;

public class QueryException extends Exception
{
    public QueryException() {}

    public QueryException(String message)
    {
        super(message);
    }
}
