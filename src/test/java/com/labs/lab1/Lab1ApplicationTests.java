package com.labs.lab1;
import com.labs.lab1.entities.account.CreateAccountDTO;
import com.labs.lab1.entities.bank.CentralBank;
import com.labs.lab1.entities.bank.CreateBankDTO;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.models.RangeConditionsInfo;
import com.labs.lab1.services.CommandInvokerImpl;
import com.labs.lab1.entities.transaction.WithdrawTransaction;
import com.labs.lab1.valueObjects.AccountType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class Lab1ApplicationTests {

    @Test
    void withdrawIncorrectAmount() {
        //
        var centralBank = CentralBank.getInstance();
        var savingListConditions = new ArrayList<RangeConditionsInfo>();
        savingListConditions.add(new RangeConditionsInfo(0, 50000, 6));
        savingListConditions.add(new RangeConditionsInfo(50000, 100000000, 11));
        var bankInfo = new CreateBankDTO("ITMO bank", savingListConditions, 10,
                110, 7, 50000);
        var bank = centralBank.createBank(bankInfo);
        var customer = Customer.builder().firstName("Maria").lastName("Chistyakova").build();
        var account = bank.createAccount(customer, new CreateAccountDTO(5000, 6, AccountType.SavingsAccount, null));
        var commandInvoker = new CommandInvokerImpl();
        commandInvoker.Consume(new WithdrawTransaction(account, 50));
    }

}
