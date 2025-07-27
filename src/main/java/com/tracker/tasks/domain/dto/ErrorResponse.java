package com.tracker.tasks.domain.dto;

public record ErrorResponse(
        int status,
        String message,
        String details // endpoint bilgisi .
) {
}
/*
record classı sayesinde Java  şunları otomatik oluşturuyormuş:

-private final değişkenleri

-constructor

-getter'lar (status(), message(), details())

-equals(), hashCode() ve toString() metodları

-Kısacası: record, kısa yoldan veri taşıyan sınıf yazmamızı sağlar.
 */