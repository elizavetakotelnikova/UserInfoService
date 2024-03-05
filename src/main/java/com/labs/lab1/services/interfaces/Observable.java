package com.labs.lab1.services.interfaces;

import com.labs.lab1.services.interfaces.NotificationGetable;

public interface Observable
{
    void RegisterObserver(NotificationGetable subscriber);
    void RemoveObserver(NotificationGetable subscriber);
    void NotifyObservers(String message);
}
