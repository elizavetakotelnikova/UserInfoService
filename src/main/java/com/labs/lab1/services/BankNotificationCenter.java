package com.labs.lab1.services;
import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.customer.Customer;
import java.util.List;

/*public class BankNotificationCenter implements Observable {
    private List<NotificationGetable> subscribedCustomers;
    @Override
    public void RegisterObserver(NotificationGetable subscriber) {
        subscribedCustomers.add(subscriber);
    }

    @Override
    public void RemoveObserver(NotificationGetable subscriber) {
        subscribedCustomers.remove(subscriber);
    }

    @Override
    public void NotifyObserversByAccountType(Bank bank, String message) {
        for (Customer customer : subscribedCustomers) {
            var accountList = bank.getAccounts().stream().filter(x -> x.getUserId() == customer.getId()).toList();
            if (accountList.stream().anyMatch(x -> x instanceof accountType.getClass()) customer.getNotification(message));
        }
    }
}*/
