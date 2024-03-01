package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.account.Account;
import com.labs.lab1.entities.bank.CentralBank;
import com.labs.lab1.entities.customer.Customer;
import com.labs.lab1.valueObjects.TransactionState;
import exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CreateCreditAccount implements Command {
    private Customer customer;
    private double creditLimit;
    @Override
    public void execute() {
        var bank = CentralBank.getInstance().banks.stream().filter(x -> x.getCustomers().contains(customer)).findAny().get();
        try {
            bank.createCreditAccount(customer, creditLimit);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void undo() {
        throw new RuntimeException("Unable to undo");
    }
}
