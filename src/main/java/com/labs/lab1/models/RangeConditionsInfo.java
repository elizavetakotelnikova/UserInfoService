package com.labs.lab1.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RangeConditionsInfo {
    private double startAmount;
    private double endAmount;
    private double percentage; // откуда 100 уверенность что будет ограничение?
}
