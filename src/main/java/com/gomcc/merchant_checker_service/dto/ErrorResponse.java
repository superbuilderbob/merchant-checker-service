package com.gomcc.merchant_checker_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    /**
     Sample error response body:
     {
        "errorCode": RESOURCE_NOT_FOUND,
        "message": "Resource not found for the id: 123",
        "status": 404,
        "timestamp": "2026-01-01T11:00:00Z",
        "path": "merchant/check"
     }
     */
    private String errorCode;
    private String message;
    private int status;
    private Instant timestamp;
    private String path;
}
