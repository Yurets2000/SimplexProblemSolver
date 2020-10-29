package com.yube;

public enum Symbol {
    GREATER(">="), LESS("<="), EQUAL("=");

    private final String value;

    Symbol(String value) {
        this.value = value;
    }

    public static Symbol invertSymbol(Symbol symbol) {
        switch (symbol) {
            case EQUAL:
                return Symbol.EQUAL;
            case LESS:
                return Symbol.GREATER;
            case GREATER:
                return Symbol.LESS;
        }
        throw new IllegalArgumentException("Неможливо інвертувати знак");
    }

    public String getValue() {
        return value;
    }
}
