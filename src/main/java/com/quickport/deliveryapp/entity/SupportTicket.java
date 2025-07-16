package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    // Many complains or queries can be raised by one user
    @ManyToOne
    @JoinColumn(name = "raised_by")
    private User user;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (status == null) this.status = TicketStatus.OPEN;
    }

}
