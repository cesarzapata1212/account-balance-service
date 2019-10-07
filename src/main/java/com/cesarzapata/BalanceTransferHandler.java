package com.cesarzapata;

import com.cesarzapata.common.TransactionalHandler;
import com.cesarzapata.core.AccountsImpl;
import com.cesarzapata.core.BalanceTransfer;
import com.cesarzapata.core.TransactionsImpl;
import com.jcabi.jdbc.JdbcSession;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import static com.cesarzapata.common.Validate.notBlank;
import static com.cesarzapata.common.Validate.notNull;

public class BalanceTransferHandler implements TransactionalHandler {

    @Override
    public void handle(@NotNull Context context, JdbcSession session) {
        BalanceTransferRequest req = context.bodyAsClass(BalanceTransferRequest.class);
        AccountsImpl accounts = new AccountsImpl(session);
        TransactionsImpl transactions = new TransactionsImpl(session);
        BalanceTransferResult result = new BalanceTransfer(accounts, transactions).transfer(validate(req));
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
}
