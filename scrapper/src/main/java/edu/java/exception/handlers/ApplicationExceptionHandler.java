package edu.java.exception.handlers;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.exception.ScrapperException;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
        @NotNull ServletRequestBindingException ex,
        @NotNull HttpHeaders headers,
        @NotNull HttpStatusCode status,
        @NotNull WebRequest request
    ) {
        return handleIncorrectRequest(ex, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
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

    @ExceptionHandler(ScrapperException.class)
    public ResponseEntity<ApiErrorResponse> handleScrapperException(ScrapperException ex) {
        return new ResponseEntity<>(
            new ApiErrorResponse(
                ex.getDescription(),
                String.valueOf(ex.getStatus().value()),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
            ),
            ex.getStatus()
        );
    }
}
