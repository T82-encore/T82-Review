package com.T82.review.global;

import com.T82.review.domain.dto.response.ErrorResponse;
import com.T82.review.exception.DuplicateReviewException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionController {
    //    Valid 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(new ErrorResponse(error.getField(), error.getDefaultMessage())));
        return ResponseEntity.badRequest().body(errors);
    }

    // Password 일치하지 않을때 예외 처리
    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<ErrorResponse> passwordException(DuplicateReviewException ex) {
        ErrorResponse error = new ErrorResponse("review", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
