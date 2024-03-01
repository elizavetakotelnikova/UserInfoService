package com.labs.lab1;
import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.account.CreateAccountDTO;
import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.bank.CentralBank;
import com.labs.lab1.entities.bank.CreateBankDTO;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.entities.transaction.CreateCheckingAccount;
import com.labs.lab1.entities.transaction.CreateCreditAccount;
import com.labs.lab1.entities.transaction.ReplenishTransaction;
import com.labs.lab1.models.RangeConditionsInfo;
import com.labs.lab1.services.CommandInvokerImpl;
import com.labs.lab1.entities.transaction.WithdrawTransaction;
import com.labs.lab1.valueObjects.AccountType;
import com.labs.lab1.valueObjects.TransactionState;
import exceptions.IncorrectArgumentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class Lab1ApplicationTests {

    CentralBank centralBank;
    Bank testBank;
    Customer testCustomer;
    @BeforeEach
    void setUp() {
        centralBank = CentralBank.getInstance();
        var savingListConditions = new ArrayList<RangeConditionsInfo>();
        savingListConditions.add(new RangeConditionsInfo(0, 50000, 6));
        savingListConditions.add(new RangeConditionsInfo(50000, 100000000, 11));
        var bankInfo = new CreateBankDTO("ITMO bank", savingListConditions, 10,
                110, 7, 50000);
        try {
            testBank = centralBank.createBank(bankInfo);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        testCustomer = Customer.builder().firstName("Maria").lastName("Chistyakova").build();
    }
    @Test
    void withdrawIncorrectAmount() {
        Account account = null;
        try {
            account = testBank.createAccount(testCustomer, new CreateAccountDTO(5000, 6, AccountType.SavingsAccount, null));
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        var commandInvoker = new CommandInvokerImpl();
        var secondTransaction = new WithdrawTransaction(account, 7000);
        commandInvoker.Consume(new WithdrawTransaction(account, 50));
        assert(account.getBalance() == 4950);
        commandInvoker.Consume(secondTransaction);
        assert(account.getBalance() == 4950);
        assert(secondTransaction.getState() == TransactionState.Rollback);
    }

    @Test
    void withdrawFromCreditAccount() {
        var commandInvoker = new CommandInvokerImpl();
        Account account = null;
        try {
            account = testBank.createCreditAccount(testCustomer, 50000); //commandInvoker.Consume(new CreateCreditCommand(testCustommer, 50000);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        commandInvoker.Consume(new WithdrawTransaction(account, 50));
        commandInvoker.Consume(new WithdrawTransaction(account, 500));
        assert(account.getBalance() == (0 - 50 - 500 - testBank.getBaseCreditCommission()));
    }

    @Test
    void undoReplenish() {
        var commandInvoker = new CommandInvokerImpl();
        Account account = null;
        try {
            account = testBank.createCreditAccount(testCustomer, 50000); //commandInvoker.Consume(new CreateCreditCommand(testCustommer, 50000);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        var replenish = new ReplenishTransaction(account, 50);
        commandInvoker.Consume(replenish);
        commandInvoker.Consume(new WithdrawTransaction(account, 500));
        testBank.rollbackTransaction(replenish.getId());
        assert(account.getBalance() == (0 - 500 - testBank.getBaseCreditCommission()));
    }

}
