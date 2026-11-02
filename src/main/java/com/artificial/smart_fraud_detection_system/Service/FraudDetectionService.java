package com.artificial.smart_fraud_detection_system.Service;

import com.artificial.smart_fraud_detection_system.Model.Transaction;
import com.artificial.smart_fraud_detection_system.Repository.TransactionRepository;
import com.artificial.smart_fraud_detection_system.dto.TransactionRequestDto;
import com.artificial.smart_fraud_detection_system.config.FraudDetectionConfig;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FraudDetectionService {

    private final TransactionRepository transactionRepository;

    public FraudDetectionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction checkTransaction(TransactionRequestDto dto) {

        if (dto.getAccountId() == null || dto.getAccountId().isBlank()) {
            throw new IllegalArgumentException("Account ID is required");
        }

        if (dto.getAmount() == null ||
                dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (dto.getLocation() == null || dto.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location is required");
        }

        if (dto.getDeviceId() == null || dto.getDeviceId().isBlank()) {
            throw new IllegalArgumentException("Device ID is required");
        }

        int riskScore = 0;

        List<String> reasons = new ArrayList<>();

        // Rule 1: Transaction amount checking
        if (dto.getAmount().compareTo(new BigDecimal("100000")) >= 0) {

            riskScore += FraudDetectionConfig.HIGH_AMOUNT_SCORE;
            reasons.add("Very high transaction amount");

        } else if (dto.getAmount().compareTo(new BigDecimal("50000")) >= 0) {

            riskScore += FraudDetectionConfig.LOW_AMOUNT_SCORE;
            reasons.add("High transaction amount");
        }

        // Rule 2: Frequent transaction checking
        LocalDateTime tenMinutesAgo =
                LocalDateTime.now().minusMinutes(
                        FraudDetectionConfig.TIME_WINDOW_MINUTES
                );

        long recentTransactions =
                transactionRepository.countByAccountIdAndTransactionTimeAfter(
                        dto.getAccountId(),
                        tenMinutesAgo
                );

        if (recentTransactions >= 3) {

            riskScore += FraudDetectionConfig.FREQUENT_TRANSACTION_SCORE;
            reasons.add("Too many transactions in a short time");
        }

        // Rule 3: New device checking
        if (dto.isNewDevice()) {

            riskScore += FraudDetectionConfig.NEW_DEVICE_SCORE;
            reasons.add("Transaction from a new device");
        }

        // Rule 4: Unusual location checking
        if (dto.isUnusualLocation()) {

            riskScore += FraudDetectionConfig.UNUSUAL_LOCATION_SCORE;
            reasons.add("Unusual transaction location");
        }

        // Maximum risk score is 100
        riskScore = Math.min(riskScore, 100);

        // Transaction status
        String status;

        if (riskScore >= FraudDetectionConfig.BLOCK_LIMIT) {

            status = "BLOCKED";

        } else if (riskScore >= FraudDetectionConfig.REVIEW_LIMIT) {

            status = "REVIEW";

        } else {

            status = "APPROVED";
        }

        Transaction transaction = new Transaction();

        transaction.setAccountId(dto.getAccountId());
        transaction.setAmount(dto.getAmount());
        transaction.setLocation(dto.getLocation());
        transaction.setDeviceId(dto.getDeviceId());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setRiskScore(riskScore);
        transaction.setTransactionStatus(status);

        transaction.setRiskReason(
                reasons.isEmpty()
                        ? "No suspicious activity detected"
                        : String.join(", ", reasons)
        );

        return transactionRepository.save(transaction);
    }
}