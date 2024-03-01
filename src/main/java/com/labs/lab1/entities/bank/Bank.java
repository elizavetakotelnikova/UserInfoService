package com.labs.lab1.entities.bank;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.account.CheckingAccount;
import com.labs.lab1.entities.account.CreditAccount;
import com.labs.lab1.entities.account.SavingsAccount;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.entities.transaction.Command;
import com.labs.lab1.models.*;
import com.labs.lab1.entities.account.CreateAccountDTO;
import com.labs.lab1.services.*;
import com.labs.lab1.valueObjects.AccountState;
import com.labs.lab1.valueObjects.AccountType;
import exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Bank implements CustomerCreatable, AccountCreatable, PercentageCreditable, Observable {
    private UUID id;
    @Setter
    private String name;
    private List<Customer> customers = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<RangeConditionsInfo> savingsAccountsConditions;
    private List<Command> transactions;
    private List<NotificationGetable> subscribers;
    private double checkingAccountPercentage;
    private double baseCreditCommission;
    private double loanRate;
    private double notVerifiedLimit;

    public Bank(String name, List<RangeConditionsInfo> savingsAccountsConditions,
                double checkingAccountPercentage, double baseCreditCommission, double loanRate, double notVerifiedLimit) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.savingsAccountsConditions = savingsAccountsConditions;
        this.checkingAccountPercentage = checkingAccountPercentage;
        this.baseCreditCommission = baseCreditCommission;
        this.loanRate = loanRate;
        this.notVerifiedLimit = notVerifiedLimit;
    }
    @Override
    public void RegisterObserver(NotificationGetable subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void RemoveObserver(NotificationGetable subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void NotifyObservers(String message) {
        subscribers.forEach(x -> x.getNotification(message));
    }

    @Override
    public Customer createCustomer(Address address, PassportData passportData, String  firstName, String lastName) throws IncorrectArgumentsException {
        if (!firstName.isEmpty() && !lastName.isEmpty()) throw new IncorrectArgumentsException("Required fields are not set");
        var createdCustomer = new Customer(firstName, lastName, address, passportData);
        customers.add(createdCustomer);
        return createdCustomer;
    }
    public Account createCreditAccount(Customer customer, double limit) throws IncorrectArgumentsException {
        if (limit == 0) throw new IncorrectArgumentsException("Not enough information");
        var createdAccount = new CreditAccount(customer.getId(), id, 0, notVerifiedLimit,
                AccountState.NotVerified,
                baseCreditCommission,
                limit,
                loanRate
        );
        if (customer.getAddress() != null && customer.getPassportData() != null) {
            createdAccount.setState(AccountState.Verified);
        }
        accounts.add(createdAccount);
        return createdAccount;
    }
    public Account createCheckingAccount(Customer customer) {
        var createdAccount = new CheckingAccount(customer.getId(), id, 0,
                notVerifiedLimit,  AccountState.NotVerified, checkingAccountPercentage);
        if (customer.getAddress() != null && customer.getPassportData() != null) {
            createdAccount.setState(AccountState.Verified);
        }
        accounts.add(createdAccount);
        return createdAccount;
    }
    public RangeConditionsInfo findConditions(double amount) {
        RangeConditionsInfo foundPercentage = savingsAccountsConditions.stream().filter(
                conditions -> amount >= conditions.getStartAmount() && amount <= conditions.getEndAmount()
        ).findAny().get();
        return foundPercentage;
    }
    public Account createSavingsAccount(Customer customer, double amount, int monthsQuantity) throws IncorrectArgumentsException {
        //есть максимальная сумма вклада
        if (monthsQuantity == 0 || amount == 0) throw new IncorrectArgumentsException("Incorrect data, cannot create account");
        var foundPercentage = findConditions(amount);
        var createdAccount = new SavingsAccount(customer.getId(), id, amount, notVerifiedLimit,
                AccountState.NotVerified,
                LocalDate.now().plusMonths(monthsQuantity),
                foundPercentage.getPercentage());
        if (customer.getAddress() != null && customer.getPassportData() != null) {
            createdAccount.setState(AccountState.Verified);
        }
        accounts.add(createdAccount);
        return createdAccount;
    }
    @Override
    public Account createAccount(Customer customer, CreateAccountDTO info) throws IncorrectArgumentsException {
        Account createdAccount = null;
        if (info.getType() == AccountType.SavingsAccount) createdAccount = createSavingsAccount(customer, info.getAmount(), info.getMonthsQuantity());
        if (info.getType() == AccountType.CheckingAccount) createdAccount = createCheckingAccount(customer);
        if (info.getType() == AccountType.CreditAccount) createdAccount = createCreditAccount(customer, info.getLimit());
        if (createdAccount != null && customer.getAddress() != null && customer.getPassportData() != null) {
            createdAccount.setState(AccountState.Verified);
        }
       return createdAccount;
    }

    @Override
    public void updateAccount() {
        accounts.stream().filter(x -> x instanceof Updatable).forEach(x -> ((Updatable) x).makeRegularUpdate());
    }

    public void changeBaseCommission(double updatedCommission) {
        baseCreditCommission = updatedCommission;
        for (Account account : accounts) {
            if (account instanceof CreditAccount) {
                ((CreditAccount) (account)).setCommissionRate(baseCreditCommission);
                var customer = customers.stream().filter(x -> x.getId().equals(account.getUserId())).findAny().get();
                if (subscribers.contains(customer)) customer.getNotification("New commission set by bank");
            }
        }
    }
    public void changeSavingsPercentageCommission(List<RangeConditionsInfo> updatedSavingsAccountConditions) {
        //проценты по депозитам не меняются до срока истечения договора
        savingsAccountsConditions = updatedSavingsAccountConditions;
        for (Account account : accounts) {
            if (account instanceof SavingsAccount) {
                //((SavingsAccount) (account)).setPercentage(findConditions(account.getBalance()).getPercentage());
                var customer = customers.stream().filter(x -> x.getId().equals(account.getUserId())).findAny().get();
                if (subscribers.contains(customer)) customer.getNotification("New conditions offered by bank, see updated conditions");
            }
        }
    }
    public void changeCheckingAccountsPercentage(double updatedCheckingAccountPercentage) {
        checkingAccountPercentage = updatedCheckingAccountPercentage;
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                ((CheckingAccount) (account)).setPercentage(updatedCheckingAccountPercentage);
                var customer = customers.stream().filter(x -> x.getId().equals(account.getUserId())).findAny().get();
                if (subscribers.contains(customer)) customer.getNotification("New balance percentage set by bank");
            }
        }
    }
    public void rollbackTransaction(UUID id) {
        for (Account account : accounts) {
            try {
                account.getTransactionsHistory().stream().filter(x -> x.getId() == id).findAny().get().undo();
            }
            catch (Exception e) {
                continue;
            };
        }
    }
}
