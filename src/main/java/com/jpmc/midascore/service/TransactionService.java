package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    /**
     * Validate and record a transaction.
     * Returns true if successfully recorded, false otherwise.
     */
    @Transactional
    public boolean processTransaction(Long senderId, Long recipientId, double amount) {
        if (amount <= 0) {
            log.warn("Invalid transaction amount: {}", amount);
            return false;
        }

        // Fetch sender & recipient
        UserRecord sender = userRepository.findById(senderId).orElse(null);
        UserRecord recipient = userRepository.findById(recipientId).orElse(null);

        if (sender == null) {
            log.warn("Sender not found: {}", senderId);
            return false;
        }
        if (recipient == null) {
            log.warn("Recipient not found: {}", recipientId);
            return false;
        }
        if (sender.getBalance() < amount) {
            log.warn("Insufficient funds for {} (balance={}, amount={})",
                    sender.getName(), sender.getBalance(), amount);
            return false;
        }

        // Call the incentives API
        double incentiveAmount = 0.0;
        try {
            String incentiveApiUrl = "http://localhost:8080/incentive";
            Transaction transactionForApi = new Transaction(senderId, recipientId, (float) amount);
            Incentive incentiveResponse = restTemplate.postForObject(incentiveApiUrl, transactionForApi, Incentive.class);

            if (incentiveResponse != null) {
                incentiveAmount = incentiveResponse.getAmount();
            }
        } catch (Exception e) {
            log.error("Failed to get incentive amount for transaction: " + e.getMessage());
            return false;
        }

        // Update balances
        sender.setBalance(sender.getBalance() - (float) amount);
        recipient.setBalance(recipient.getBalance() + (float) amount + (float) incentiveAmount);

        // Persist users & transaction
        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = TransactionRecord.builder()
                .sender(sender)
                .recipient(recipient)
                .amount(amount)
                .incentiveAmount(incentiveAmount)
                .build();
        transactionRepository.save(record);

        log.info("Transaction SUCCESS: {} -> {} | amount={}, incentive={}",
                sender.getName(), recipient.getName(), amount, incentiveAmount);
        log.info("Updated balances: sender={} balance={}, recipient={} balance={}",
                sender.getName(), sender.getBalance(),
                recipient.getName(), recipient.getBalance());

        return true;
    }

    /**
     * Helper method for debugging balances (like for waldorf).
     */
    public float getBalanceByName(String name) {
        UserRecord user = userRepository.findByName(name);
        if (user == null) {
            log.warn("User not found: {}", name);
            return -1;
        }
        log.info("Balance check: {} has balance={}", user.getName(), user.getBalance());
        return user.getBalance();
    }
}