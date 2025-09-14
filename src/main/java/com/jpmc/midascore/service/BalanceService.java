//package com.jpmc.midascore.service;
//
//import com.jpmc.midascore.foundation.Balance;
//import com.jpmc.midascore.repository.TransactionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class BalanceService {
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    public Balance getBalanceForUser(String userId) {
//        try {
//            // Convert string userId to Long
//            Long userIdLong = Long.parseLong(userId);
//
//            // Check if user exists as either sender or recipient
//            boolean userExistsAsSender = transactionRepository.existsBySenderId(userIdLong);
//            boolean userExistsAsRecipient = transactionRepository.existsByRecipientId(userIdLong);
//
//            if (!userExistsAsSender && !userExistsAsRecipient) {
//                // Return balance of 0 for non-existent users
//                return new Balance(0.0f);
//            }
//
//            // Calculate balance: money received - money sent
//            Float amountReceived = transactionRepository.sumAmountByRecipientId(userIdLong);
//            Float amountSent = transactionRepository.sumAmountBySenderId(userIdLong);
//
//            // Handle null cases
//            if (amountReceived == null) {
//                amountReceived = 0.0f;
//            }
//            if (amountSent == null) {
//                amountSent = 0.0f;
//            }
//
//            float balance = amountReceived - amountSent;
//
//            return new Balance(balance);
//
//        } catch (NumberFormatException e) {
//            // If userId is not a valid number, return 0 balance
//            return new Balance(0.0f);
//        }
//    }
//}

package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BalanceService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Balance getBalanceForUser(String userId) {
        try {
            Long userIdLong = Long.parseLong(userId);

            // Fetch all transactions for the user
            List<TransactionRecord> allTransactions = transactionRepository.findAll();

            float balance = 0.0f;
            for (TransactionRecord record : allTransactions) {
                if (record.getRecipient() != null && record.getRecipient().getId() == userIdLong) {
                    balance += record.getAmount() + record.getIncentiveAmount();
                }
                if (record.getSender() != null && record.getSender().getId() == userIdLong) {
                    balance -= record.getAmount();
                }
            }

            return new Balance(balance);

        } catch (NumberFormatException e) {
            return new Balance(0.0f);
        }
    }
}