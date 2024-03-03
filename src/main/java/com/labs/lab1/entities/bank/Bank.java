package com.labs.lab1.entities.bank;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.account.CheckingAccount;
import com.labs.lab1.entities.account.CreditAccount;
import com.labs.lab1.entities.account.SavingsAccount;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.entities.transaction.Command;
import com.labs.lab1.entities.transaction.Transaction;
import com.labs.lab1.entities.transaction.TransferTransaction;
import com.labs.lab1.entities.transaction.WithdrawTransaction;
import com.labs.lab1.models.*;
import com.labs.lab1.services.*;
import com.labs.lab1.valueObjects.AccountState;
import com.labs.lab1.valueObjects.TransactionState;
import exceptions.IncorrectArgumentsException;
import exceptions.NotVerifiedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Bank implements CustomerCreatable, PercentageCreditable, Observable {
    private UUID id;
    @Setter
    private String name;
    private List<Customer> customers = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<RangeConditionsInfo> savingsAccountsConditions = new ArrayList<>();
    private List<Command> transactions = new ArrayList<>();
    private List<NotificationGetable> subscribers = new ArrayList<>();
    //BankNotificationCenter bankNotificationCenter = new BankNotificationCenter(this);
    private double checkingAccountPercentage;
    private double baseCreditCommission;
    private double loanRate;
    private double notVerifiedLimit;

    /**
     *
     * @param name - bank name
     * @param savingsAccountsConditions - conditions for savings account
     * @param checkingAccountPercentage - balance percentage for checking account
     * @param baseCreditCommission - commission for credit accounts
     * @param loanRate - rate for credit accounts
     * @param notVerifiedLimit - withdraw/replenish limit for not verified accounts
     */
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

    /**
     * subscribing to bank notification
     * @param subscriber
     */
    @Override
    public void RegisterObserver(NotificationGetable subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * removing notifications' subscriber
     * @param subscriber
     */
    @Override
    public void RemoveObserver(NotificationGetable subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * notifying all subscibers
     * @param message
     */
    @Override
    public void NotifyObservers(String message) {
        subscribers.forEach(x -> x.getNotification(message));
    }

    /**
     * creating customer and adding to customers list
     * @param address
     * @param passportData
     * @param firstName
     * @param lastName
     * @return new customer
     * @throws IncorrectArgumentsException when first name or last name is not set
     */
    @Override
    public Customer createCustomer(Address address, PassportData passportData, String  firstName, String lastName) throws IncorrectArgumentsException {
        if (firstName.isEmpty() || lastName.isEmpty()) throw new IncorrectArgumentsException("Required fields are not set");
        var createdCustomer = new Customer(firstName, lastName, address, passportData);
        customers.add(createdCustomer);
        return createdCustomer;
    }

    /**
     * creating credit account
     * @param customer
     * @param limit
     * @return new credit account
     * @throws IncorrectArgumentsException when credit limit is not set
     */
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

    /**
     * creating checking account
     * @param customer
     * @return new checking account
     */
    public Account createCheckingAccount(Customer customer) {
        var createdAccount = new CheckingAccount(customer.getId(), id, 0,
               AccountState.NotVerified, checkingAccountPercentage);
        if (customer.getAddress() != null && customer.getPassportData() != null) {
            createdAccount.setState(AccountState.Verified);
        }
        accounts.add(createdAccount);
        return createdAccount;
    }

    /**
     * creating savings account
     * @param customer
     * @param amount
     * @param monthsQuantity
     * @return
     * @throws IncorrectArgumentsException when months quantity or amount is not set
     */

    public Account createSavingsAccount(Customer customer, double amount, int monthsQuantity) throws IncorrectArgumentsException {
        //max sum exists
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
    /**
     * find conditions for saving account based on range
     * @param amount
     * @return range and percentage for given amount of money
     */
    public RangeConditionsInfo findConditions(double amount) {
        RangeConditionsInfo foundPercentage = savingsAccountsConditions.stream().filter(
                conditions -> amount >= conditions.getStartAmount() && amount <= conditions.getEndAmount()
        ).findAny().get();
        return foundPercentage;
    }

    /**
     * update every account in a bank
     */
    @Override
    public void updateAccount() {
        accounts.stream().filter(x -> x instanceof Updatable).forEach(x -> ((Updatable) x).makeRegularUpdate());
    }

    /**
     * changing commission for credit accounts and notifying subscribed users
     * @param updatedCommission new commission
     */
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

    /**
     * changing conditions for saving account and notifying subscribed users
     * @param updatedSavingsAccountConditions
     */
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

    /**
     * changing checking accounts percentage and notifying subscribed users
     * @param updatedCheckingAccountPercentage
     */
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

    /**
     * provides transaction
     * @param transaction transaction to be done
     * @throws NotVerifiedException if account is not verified and amount is bigger than allowed limit
     */
    public void makeTransaction(Transaction transaction) throws NotVerifiedException {
        if (transaction instanceof WithdrawTransaction) {
            if (((WithdrawTransaction) transaction).getAccount().getState() == AccountState.NotVerified &&
                    ((WithdrawTransaction) transaction).getAmount() > notVerifiedLimit) {
                transaction.setState(TransactionState.Rollback);
                throw new NotVerifiedException("Transaction cannot be done, account not verified");
            }
        }
        if (transaction instanceof TransferTransaction) {
            if (((TransferTransaction) transaction).getWithdrawAccount().getState() == AccountState.NotVerified &&
                    ((TransferTransaction) transaction).getAmount() > notVerifiedLimit) {
                transaction.setState(TransactionState.Rollback);
                throw new NotVerifiedException("Transaction cannot be done, account not verified");
            }
        }
        transaction.execute();
    }
    /**
     * rollback transaction
     * @param id transaction id
     */
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
