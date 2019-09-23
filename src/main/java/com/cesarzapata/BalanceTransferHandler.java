package com.cesarzapata;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;

public class BalanceTransferHandler implements Handler {
    private final DataSource dataSource;

    public BalanceTransferHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void handle(@NotNull Context context) {
        BalanceTransferRequest balanceTransferRequest = context.bodyAsClass(BalanceTransferRequest.class);
        BalanceTransferResult result = new BalanceTransfer(new AccountsImpl(dataSource)).transfer(balanceTransferRequest);
        context.json(result);
    }
}
