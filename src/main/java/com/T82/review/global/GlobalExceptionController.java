package com.T82.review.global;

import com.T82.review.domain.dto.response.ErrorResponse;
import com.T82.review.exception.DuplicateReviewException;
import com.T82.review.exception.EventDeleteException;
import com.T82.review.exception.NoReviewException;
import com.T82.review.exception.UserDeleteException;
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
    public ResponseEntity<ErrorResponse> duplicateReviewException(DuplicateReviewException ex) {
        ErrorResponse error = new ErrorResponse("review", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    // 이벤트가 조회되지 않을때 에러처리
    @ExceptionHandler(NoReviewException.class)
    public ResponseEntity<ErrorResponse> noReviewException(NoReviewException ex) {
        ErrorResponse error = new ErrorResponse("review", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    //    회원 탈퇴된 상태일때 예외처리
    @ExceptionHandler(UserDeleteException.class)
    public ResponseEntity<ErrorResponse> userDeleteException(UserDeleteException ex) {
        ErrorResponse error = new ErrorResponse("user", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    //    회원 탈퇴된 상태일때 예외처리
    @ExceptionHandler(EventDeleteException.class)
    public ResponseEntity<ErrorResponse> eventDeleteException(EventDeleteException ex) {
        ErrorResponse error = new ErrorResponse("event", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
