package com.cesarzapata.common;

import com.jcabi.jdbc.JdbcSession;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public interface TransactionalHandler {

    void handle(@NotNull Context ctx, @NotNull JdbcSession session);

}
