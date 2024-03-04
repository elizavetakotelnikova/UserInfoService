package com.labs.lab1.services;

public interface Observable
{
    void RegisterObserver(NotificationGetable subscriber);
    void RemoveObserver(NotificationGetable subscriber);
    void NotifyObservers(String message);
}
