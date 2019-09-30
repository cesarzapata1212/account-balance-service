package com.cesarzapata;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class BalanceTransferHandler implements Handler {
    private final Accounts accounts;

    public BalanceTransferHandler(@NotNull Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public void handle(@NotNull Context context) {
        BalanceTransferRequest req = context.bodyAsClass(BalanceTransferRequest.class);
        BalanceTransferResult result = new BalanceTransfer(accounts).transfer(validate(req));
        context.json(result);
    }

    private BalanceTransferRequest validate(BalanceTransferRequest req) {
        notNull(req.getSourceAccount(), "Invalid sourceAccount provided");
        notBlank(req.getSourceAccount().getAccountNumber(), "Invalid sourceAccount.accountNumber provided");
        notBlank(req.getSourceAccount().getSortCode(), "Invalid sourceAccount.sortCode provided");
        notNull(req.getDestinationAccount(), "Invalid destinationAccount provided");
        notBlank(req.getDestinationAccount().getAccountNumber(), "Invalid destinationAccount.accountNumber provided");
        notBlank(req.getDestinationAccount().getSortCode(), "Invalid destinationAccount.sortCode provided");
        notNull(req.getAmount(), "Invalid amount provided");
        return req;
    }

    private void notBlank(String s, String msg) {
        if (StringUtils.isBlank(s)) {
            throw new IllegalArgumentException(msg);
        }
    }

    private void notNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
