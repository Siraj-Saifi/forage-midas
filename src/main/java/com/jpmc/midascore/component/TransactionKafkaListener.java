package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class TransactionKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionKafkaListener.class);
    private int transactionCount = 0;

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(@Payload Transaction transaction,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                                  @Header(KafkaHeaders.OFFSET) long offset) {

        transactionCount++;

        logger.info("Received transaction #{} from topic: {}, partition: {}, offset: {}",
                transactionCount, topic, partition, offset);
        logger.info("Transaction details - Amount: {}",
                transaction.getAmount());

        // For debugging purposes - capture first 4 transaction amounts
        if (transactionCount <= 4) {
            System.out.println("*** FIRST 4 TRANSACTIONS *** Transaction #" + transactionCount +
                    " Amount: " + transaction.getAmount());
        }

        // Process the transaction here (for now, just logging)
        processTransaction(transaction);
    }

    private void processTransaction(Transaction transaction) {
        // TODO: Add actual transaction processing logic in future tasks
        logger.info("Processing transaction: {}", transaction);
    }
}