//package com.jpmc.midascore.entity;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "transaction_record")
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Builder
//public class TransactionRecord {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//    private double amount;
//
//    @Column(name = "incentive_amount")
//    private double incentiveAmount;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sender_id")
//    private UserRecord sender;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "recipient_id")
//    private UserRecord recipient;
//
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//}

package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_record")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Column(name = "incentive_amount")
    private double incentiveAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    @CreationTimestamp
    private LocalDateTime createdAt;
}