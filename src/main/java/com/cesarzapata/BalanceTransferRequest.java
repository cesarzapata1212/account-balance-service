package com.cesarzapata;

public class BalanceTransferRequest {

    private Account sourceAccount;
    private Account destinationAccount;
    private String amount;

    public BalanceTransferRequest() {
    }

    public BalanceTransferRequest(Account sourceAccount, Account destinationAccount, String amount) {
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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
    }

}
