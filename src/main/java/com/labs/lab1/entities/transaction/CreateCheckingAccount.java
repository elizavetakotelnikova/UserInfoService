package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.bank.CentralBank;
import com.labs.lab1.entities.customer.Customer;
import exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateCheckingAccount implements Command {
    private Customer customer;
    @Override
    public void execute() {
        var bank = CentralBank.getInstance().banks.stream().filter(x -> x.getCustomers().contains(customer)).findAny().get();
        bank.createCheckingAccount(customer);
    }
    @Override
    public void undo() {
        throw new RuntimeException("Unable to undo");
    }
}
