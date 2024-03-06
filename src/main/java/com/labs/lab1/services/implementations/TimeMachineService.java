package com.labs.lab1.services.implementations;
import com.labs.lab1.services.interfaces.Updatable;

/**
 * service for speeding up time and see updates
 */
public class TimeMachineService {
    /**
     * updates account based on provided months quantity
     * @param updatable to be updated
     * @param monthsQuantity quantity of months
     */
    public void speedUpTime(Updatable updatable, int monthsQuantity) {
        for (int i = 0; i < monthsQuantity; i++) {
            updatable.makeRegularUpdate();
        }
    }
}
