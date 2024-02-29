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

@Builder
@Getter
@AllArgsConstructor
public class Bank implements CustomerCreatable, AccountCreatable, PercentageCreditable {
    private UUID id;
    @Setter
    private String name;
    private List<Customer> customers = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<RangeConditionsInfo> savingsAccountsConditions;
    private List<Command> transactions;
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
    public Customer createCustomer(Address address, PassportData passportData, String  firstName, String lastName) throws IncorrectArgumentsException {
        if (!firstName.isEmpty() && !lastName.isEmpty()) throw new IncorrectArgumentsException("Required fields are not set");
        var createdCustomer = new Customer(firstName, lastName, address, passportData);
        customers.add(createdCustomer);
        return createdCustomer;
    }
    @Override
    public Account createAccount(Customer customer, CreateAccountDTO info) {
        Account createdAccount = null;
        if (info.getType() == AccountType.SavingsAccount) createdAccount = createSavingsAccount(customer, info);
        if (info.getType() == AccountType.CheckingAccount) createdAccount = createCheckingAccount(customer, info);
        if (info.getType() == AccountType.CreditAccount) createdAccount = createCreditAccount(customer, info);
        if (createdAccount != null && customer.getAddress() != null && customer.getPassportData() != null) {
            createdAccount.setState(AccountState.Verified);
        }
        accounts.add(createdAccount);
       return createdAccount;
    }
    public Account createCreditAccount(Customer customer, CreateAccountDTO info) {
        return new CreditAccount(customer.getId(), id, 0, notVerifiedLimit,
                AccountState.NotVerified,
                baseCreditCommission,
                info.getLimit(),
                loanRate
                 );
    }
    public Account createCheckingAccount(Customer customer, CreateAccountDTO info) {
        return new CheckingAccount(customer.getId(), id, 0,
                notVerifiedLimit,  AccountState.NotVerified, checkingAccountPercentage);
    }
    public RangeConditionsInfo findConditions(double amount) {
        RangeConditionsInfo foundPercentage = savingsAccountsConditions.stream().filter(
                conditions -> amount >= conditions.getStartAmount() && amount <= conditions.getEndAmount()
        ).findAny().get();
        return foundPercentage;
    }
    public Account createSavingsAccount(Customer customer, CreateAccountDTO info) {
        //есть максимальная сумма вклада
        var foundPercentage = findConditions(info.getAmount());
        return new SavingsAccount(customer.getId(), id, info.getAmount(), notVerifiedLimit,
                AccountState.NotVerified,
                LocalDate.now().plusMonths(info.getMonthsQuantity()),
                foundPercentage.getPercentage());
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
                customer.getNotification("New commission set by bank");
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
                customer.getNotification("New conditions offered by bank, see updated conditions");
            }
        }
    }
    public void changeCheckingAccountsPercentage(double updatedCheckingAccountPercentage) {
        checkingAccountPercentage = updatedCheckingAccountPercentage;
        for (Account account : accounts) {
            if (account instanceof CheckingAccount) {
                ((CheckingAccount) (account)).setPercentage(updatedCheckingAccountPercentage);
                var customer = customers.stream().filter(x -> x.getId().equals(account.getUserId())).findAny().get();
                customer.getNotification("New balance percentage set by bank");
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
