package com.artificial.smart_fraud_detection_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {

    private String accountId;

    private BigDecimal amount;

    private String location;

    private String deviceId;

    private boolean newDevice;

    private boolean unusualLocation;
}