package com.labs.lab1.entities.bank;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.account.CheckingAccount;
import com.labs.lab1.entities.account.CreditAccount;
import com.labs.lab1.entities.account.SavingsAccount;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.models.Address;
import com.labs.lab1.models.CreateAccountDTO;
import com.labs.lab1.models.PassportData;
import com.labs.lab1.models.SavingsAccountsConditions;
import com.labs.lab1.services.AccountCreatable;
import com.labs.lab1.services.CustomerCreatable;
import com.labs.lab1.services.PercentageCreditable;
import com.labs.lab1.services.Updatable;
import com.labs.lab1.valueObjects.AccountState;
import com.labs.lab1.valueObjects.AccountType;
import exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class Bank implements CustomerCreatable, AccountCreatable, PercentageCreditable {
    private UUID id;
    private String name;
    private List<Customer> customers = new ArrayList<>();
    private List<SavingsAccountsConditions> savingsAccountsConditions = new ArrayList<>();
    private List<Updatable> accounts = new ArrayList<>();
    private double checkingAccountPercentage;
    private double baseCommission;

    public Bank(String name, List<Customer> customers, List<SavingsAccountsConditions> savingsAccountsConditions,
                List<Updatable> accounts, double checkingAccountPercentage, double baseCommission) {
        this.name = name;
        this.customers = customers;
        this.savingsAccountsConditions = savingsAccountsConditions;
        this.accounts = accounts;
        this.checkingAccountPercentage = checkingAccountPercentage;
        this.baseCommission = baseCommission;
    }

    @Override
    public Customer createCustomer(Address address, PassportData passportData, String  firstName, String lastName) throws IncorrectArgumentsException {
        if (!firstName.isEmpty() && !lastName.isEmpty()) throw new IncorrectArgumentsException("Required fields are not set");
        return new Customer(firstName, lastName, address, passportData);

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
       return createdAccount;
    }
    public Account createCreditAccount(Customer customer, CreateAccountDTO info) {
        return new CreditAccount(UUID.randomUUID(), customer.getId(), id, 0,
                AccountState.NotVerified,
                baseCommission,
                info.getLimit()
                );
    }
    public Account createCheckingAccount(Customer customer, CreateAccountDTO info) {
        return new CheckingAccount(UUID.randomUUID(), customer.getId(), id, 0,
                AccountState.NotVerified,
                checkingAccountPercentage
        );
    }
    public Account createSavingsAccount(Customer customer, CreateAccountDTO info) {
        //есть максимальная сумма вклада
        SavingsAccountsConditions foundPercentage = savingsAccountsConditions.stream().filter(
                conditions -> info.getAmount() >= conditions.getStartAmount() && info.getAmount() <=conditions.getEndAmount()
        ).findAny().get();
        return new SavingsAccount(UUID.randomUUID(), customer.getId(), id, info.getAmount() ,
                AccountState.NotVerified,
                LocalDate.now().plusMonths(info.getMonthsQuantity()),
                foundPercentage.getPercentage());
    }

    @Override
    public void updateAccount() {
        accounts.forEach(Updatable::makeRegularUpdate);
    }
}
