package com.T82.review.domain.dto.response;

public record ErrorResponse (
        String field,
        String message){
}
