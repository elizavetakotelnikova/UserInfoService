/*package com.labs.lab1.services;
import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.customer.Customer;

import java.lang.reflect.AccessFlag;
import java.util.ArrayList;
import java.util.List;

public class BankNotificationCenter implements Observable {
    private final List<NotificationGetable> subscribedCustomers = new ArrayList<>();
    private Bank bank;
    public BankNotificationCenter(Bank bank) {
        this.bank = bank;
    }
    @Override
    public void RegisterObserver(NotificationGetable subscriber) {
        subscribedCustomers.add(subscriber);
    }

    @Override
    public void RemoveObserver(NotificationGetable subscriber) {
        subscribedCustomers.remove(subscriber);
    }

    @Override
    public void NotifyObserversByAccountType(Account account, String message) {
        for (Customer customer: subscribedCustomers) {
            var accountList = bank.getAccounts().stream().filter(x -> x.getUserId() == customer.getId()).toList();
            if (accountList.stream().anyMatch(account.getClass()::isInstance)) {
                customer.getNotification(message);
            };
        }
    }
}*/
