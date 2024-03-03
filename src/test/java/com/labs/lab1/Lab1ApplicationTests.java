package com.labs.lab1;
import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.bank.Bank;
import com.labs.lab1.entities.bank.CentralBank;
import com.labs.lab1.entities.bank.CreateBankDTO;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.entities.transaction.ReplenishTransaction;
import com.labs.lab1.models.RangeConditionsInfo;
import com.labs.lab1.services.CommandInvokerImpl;
import com.labs.lab1.entities.transaction.WithdrawTransaction;
import com.labs.lab1.services.TimeMachineService;
import com.labs.lab1.valueObjects.TransactionState;
import exceptions.IncorrectArgumentsException;
import exceptions.NotVerifiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        try {
            testCustomer =  testBank.createCustomer(null, null, "Maria", "Chistyakova");
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void withdrawIncorrectAmount() {
        Account account = null;
        try {
            account = testBank.createSavingsAccount(testCustomer, 5000, 6);
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

    @Test
    void timeServiceTest() {
        Account account = null;
        try {
            account = testBank.createSavingsAccount(testCustomer, 5000, 2);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        var timeService = new TimeMachineService();
        var speededUpAccount = timeService.speedUpTime(account, 2);
        assert(speededUpAccount.getBalance() == 5618);
    }

    @Test
    void creditLimitTest() {
        Account account = null;
        try {
            account = testBank.createCreditAccount(testCustomer, 50000);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        var commandInvoker = new CommandInvokerImpl();
        var transaction = new WithdrawTransaction(account, 1000000);
        commandInvoker.Consume(transaction);
        assert(transaction.getState() == TransactionState.Rollback);
    }

    @Test
    void notVerifiedExceptionTesting() {
        Account account = null;
        try {
            account = testBank.createCreditAccount(testCustomer, 500000);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        Account finalAccount = account;
        assertThrows(NotVerifiedException.class, () -> finalAccount.withdraw(60000));
    }

    @Test
    void notificationsTest() {
        // creating customer mock
        Customer customer = mock(Customer.class);
        testBank.getCustomers().add(customer);
        Mockito.when(customer.getId()).thenReturn(UUID.randomUUID());
        // creating account
        try {
            testBank.createCreditAccount(customer, 50000);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
        // subscribe mock to notifications
        testBank.RegisterObserver(customer);
        // change commission => notify subscribers
        testBank.changeBaseCommission(200);
        // verify
        verify(customer, times(1)).getNotification("New commission set by bank");
    }

}
