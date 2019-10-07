package com.cesarzapata.core;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    public static final Money ZERO = new Money("0");

    private final BigDecimal value;

    public Money(String value) {
        this.value = new BigDecimal(value);
    }

    public Money(BigDecimal value) {
        this.value = value;
    }

    public Money minus(Money other) {
        return new Money(
                this.value.subtract(
                        other.value()
                )
        );
    }

    public Money plus(Money other) {
        return new Money(
                this.value.add(other.value)
        );
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return value.compareTo(money.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}
