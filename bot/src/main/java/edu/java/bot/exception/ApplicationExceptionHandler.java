package edu.java.bot.exception;

import edu.java.bot.dto.response.ApiErrorResponse;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NotNull MethodArgumentNotValidException ex,
        @NotNull HttpHeaders headers,
        @NotNull HttpStatusCode status,
        @NotNull WebRequest request
    ) {
        return handleIncorrectRequest(ex, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
        @NotNull TypeMismatchException ex,
        @NotNull HttpHeaders headers,
        @NotNull HttpStatusCode status,
        @NotNull WebRequest request
    ) {
        return handleIncorrectRequest(ex, status);
    }

    private ResponseEntity<Object> handleIncorrectRequest(
        Exception ex,
        HttpStatusCode status
    ) {
        return new ResponseEntity<>(
            new ApiErrorResponse(
                "Некорректные параметры запроса",
                String.valueOf(status.value()),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
            ),
            status
        );
    }
}
