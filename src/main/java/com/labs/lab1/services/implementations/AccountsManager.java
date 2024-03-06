package com.labs.lab1.services.implementations;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.account.CheckingAccount;
import com.labs.lab1.entities.account.CreditAccount;
import com.labs.lab1.entities.account.SavingsAccount;
import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.models.RangeConditionsInfo;
import com.labs.lab1.valueObjects.AccountState;
import exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@AllArgsConstructor
public class AccountsManager {
    private Bank bank;
    /**
     * creating credit account
     * @param customer customer
     * @param limit credit limit
     * @return new credit account
     * @throws IncorrectArgumentsException when credit limit is not set
     */
    public Account createCreditAccount(Customer customer, double limit) throws IncorrectArgumentsException {
        if (limit == 0) throw new IncorrectArgumentsException("Not enough information");
        var createdAccount = new CreditAccount(customer.getId(), bank.getId(), 0, bank.getNotVerifiedLimit(),
                AccountState.NotVerified,
                bank.getBaseCreditCommission(),
                limit,
                bank.getLoanRate()
        );
        if (customer.checkVerification()) createdAccount.setState(AccountState.Verified);
        bank.getAccounts().add(createdAccount);
        return createdAccount;
    }
    public void checkAccountsVerification(Customer customer) {
        if (customer.checkVerification()) bank.getAccounts().stream().filter(x -> x.getUserId() == customer.getId()).forEach(x -> x.setState(AccountState.Verified));
    }
    /**
     * creating checking account
     * @param customer customer
     * @return new checking account
     */
    public Account createCheckingAccount(Customer customer) {
        var createdAccount = new CheckingAccount(customer.getId(), bank.getId(), 0,
                AccountState.NotVerified, bank.getCheckingAccountPercentage());
        if (customer.getAddress() != null && customer.getPassportData() != null) {
            createdAccount.setState(AccountState.Verified);
        }
        bank.getAccounts().add(createdAccount);
        return createdAccount;
    }
    /**
     * find conditions for saving account based on range
     * @param amount amount of money
     * @return range and percentage for given amount of money
     */
    public RangeConditionsInfo findConditions(double amount) {
        return bank.getSavingsAccountsConditions().stream().filter(
                conditions -> amount >= conditions.getStartAmount() && amount <= conditions.getEndAmount()
        ).findAny().get();
    }
    /**
     * creating savings account
     * @param customer customer
     * @param amount amount of money
     * @param monthsQuantity months quantity
     * @return created account
     * @throws IncorrectArgumentsException when months quantity or amount is not set
     */
    public Account createSavingsAccount(Customer customer, double amount, int monthsQuantity) throws IncorrectArgumentsException {
        if (monthsQuantity == 0 || amount == 0) throw new IncorrectArgumentsException("Incorrect data, cannot create account");
        var foundPercentage = findConditions(amount);
        var createdAccount = new SavingsAccount(customer.getId(), bank.getId(), amount, bank.getNotVerifiedLimit(),
                AccountState.NotVerified,
                LocalDate.now().plusMonths(monthsQuantity),
                foundPercentage.getPercentage());
        if (customer.getAddress() != null && customer.getPassportData() != null)
            createdAccount.setState(AccountState.Verified);
        bank.getAccounts().add(createdAccount);
        return createdAccount;
    }

}
