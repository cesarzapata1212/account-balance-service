package com.cesarzapata;

import java.math.BigDecimal;
import java.util.Objects;

public class BalanceTransferRequest {

    private Account sourceAccount;
    private Account destinationAccount;
    private BigDecimal amount;

    public BalanceTransferRequest() {
    }

    public BalanceTransferRequest(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static class Account {

        private String accountNumber;
        private String sortCode;

        public Account() {
        }

        public Account(String accountNumber, String sortCode) {
            this.accountNumber = accountNumber;
            this.sortCode = sortCode;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getSortCode() {
            return sortCode;
        }

        public void setSortCode(String sortCode) {
            this.sortCode = sortCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Account)) return false;
            Account account = (Account) o;
            return accountNumber.equals(account.accountNumber) &&
                    sortCode.equals(account.sortCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountNumber, sortCode);
        }
    }

}
