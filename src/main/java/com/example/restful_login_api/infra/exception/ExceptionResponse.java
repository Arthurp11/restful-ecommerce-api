package com.example.restful_login_api.infra.exception;

public record ExceptionResponse(java.time.Instant timestamp, String message, String details) {}
