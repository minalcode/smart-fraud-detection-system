package com.artificial.smart_fraud_detection_system.Controller;

import com.artificial.smart_fraud_detection_system.Model.Transaction;
import com.artificial.smart_fraud_detection_system.Service.FraudDetectionService;
import com.artificial.smart_fraud_detection_system.dto.TransactionRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraud")
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;

    public FraudDetectionController(
            FraudDetectionService fraudDetectionService) {
        this.fraudDetectionService = fraudDetectionService;
    }

    @PostMapping("/check")
    public ResponseEntity<Transaction> checkTransaction(
            @RequestBody TransactionRequestDto dto) {

        Transaction response =
                fraudDetectionService.checkTransaction(dto);

        return ResponseEntity.ok(response);
    }
}