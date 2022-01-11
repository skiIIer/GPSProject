package com.model;

public enum State{
    SCHEDULED(0), ACTIVE(1), DONE(2);

    private final int value;

    State(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }

    public String getState(int value) {
        switch (value){
            case 0:
                return "SCHEDULED";
            case 1:
                return "ACTIVE";
            case 2:
                return "DONE";
            default:
                return "SEM STATE";
        }
    }
}
