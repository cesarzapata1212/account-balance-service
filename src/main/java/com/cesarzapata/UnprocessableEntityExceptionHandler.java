package com.cesarzapata;

import io.javalin.http.Context;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class UnprocessableEntityExceptionHandler implements io.javalin.http.ExceptionHandler<RuntimeException> {
    @Override
    public void handle(@NotNull RuntimeException exception, @NotNull Context ctx) {
        ctx.status(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        ctx.json(new ErrorResponse(
                HttpStatus.SC_UNPROCESSABLE_ENTITY,
                exception.getMessage()
        ));
    }
}
