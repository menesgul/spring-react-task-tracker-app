package com.tracker.tasks.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.tracker.tasks.domain.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice //  Tüm controller’larda oluşan hataları tek bir yerde yakalamaya yarar
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class}) //  @ diyor ki"IllegalArgumentException hatası fırlarsa bu metodu çalıştır".
    public ResponseEntity<ErrorResponse> handleException(
            RuntimeException ex, WebRequest request
            /*
RuntimeException ex: Fırlayan hatanın kendisi.
WebRequest request: O hatanın hangi endpoint’te oluştuğu gibi bilgileri içeriyor.
ResponseEntity<ErrorResponse>: HTTP response'u olarak ErrorResponse objesi dönüyoruz.
             */
    ) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), //  400 sayısını verir.
               ex.getMessage(),
               request.getDescription(false) // request.getDescription(false) → Bu hatanın hangi URL'de oluştuğu gibi bilgi verir.
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        //  bununla yukarıda olusturdugumuz ErrorResponse objesini HTTP cevabı olarak return ediyoruz.
        }
    }
