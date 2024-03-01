package com.labs.lab1.entities.transaction;

import com.labs.lab1.entities.bank.CentralBank;
import com.labs.lab1.entities.customer.Customer;
import exceptions.IncorrectArgumentsException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateSavingsAccount implements Command {
    private Customer customer;
    private double amount;
    private Integer monthsQuantity;
    @Override
    public void execute() {
        var bank = CentralBank.getInstance().banks.stream().filter(x -> x.getCustomers().contains(customer)).findAny().get();
        try {
            bank.createSavingsAccount(customer, amount, monthsQuantity);
        } catch (IncorrectArgumentsException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void undo() {
        throw new RuntimeException("Unable to undo");
    }
}
