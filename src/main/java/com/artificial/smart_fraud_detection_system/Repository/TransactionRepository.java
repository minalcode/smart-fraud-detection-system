package com.artificial.smart_fraud_detection_system.Repository;

import com.artificial.smart_fraud_detection_system.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    long countByAccountIdAndTransactionTimeAfter(
            String accountId,
            LocalDateTime time
    );
}