//package com.jpmc.midascore.repository;
//
//import com.jpmc.midascore.entity.TransactionRecord;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {
//}

package com.jpmc.midascore.repository;

// Temporarily commenting out to find the correct imports
// Check if TransactionRecord is in foundation package or has a different name
// import com.jpmc.midascore.entity.TransactionRecord;

import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// For now, let's use the Transaction from foundation package
import com.jpmc.midascore.foundation.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {

    // Using the foundation Transaction class for now
    // We'll need to adjust these based on the actual Transaction structure

    // Check if user exists (has any transactions as sender)
    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.senderId = :senderId")
    boolean existsBySenderId(@Param("senderId") Long senderId);

    // Check if user exists (has any transactions as recipient)
    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.recipientId = :recipientId")
    boolean existsByRecipientId(@Param("recipientId") Long recipientId);

    // Sum all transaction amounts for a specific user (as sender - money sent out)
    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM Transaction t WHERE t.senderId = :userId")
    Float sumAmountBySenderId(@Param("userId") Long userId);

    // Sum all transaction amounts for a specific user (as recipient - money received)
    @Query("SELECT COALESCE(SUM(t.amount), 0.0) FROM Transaction t WHERE t.recipientId = :userId")
    Float sumAmountByRecipientId(@Param("userId") Long userId);
}