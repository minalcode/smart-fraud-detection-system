package com.artificial.smart_fraud_detection_system.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class FraudDetectionConfig {

    public static final int LOW_AMOUNT_SCORE = 20;

    public static final int HIGH_AMOUNT_SCORE = 30;

    public static final int FREQUENT_TRANSACTION_SCORE = 30;

    public static final int NEW_DEVICE_SCORE = 20;

    public static final int UNUSUAL_LOCATION_SCORE = 30;

    public static final int REVIEW_LIMIT = 40;

    public static final int BLOCK_LIMIT = 70;

    public static final int TIME_WINDOW_MINUTES = 10;
}