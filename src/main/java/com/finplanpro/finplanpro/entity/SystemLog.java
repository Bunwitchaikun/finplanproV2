package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_logs")
@Getter
@Setter
@NoArgsConstructor
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public SystemLog(String eventType, String username, String description, String ipAddress) {
        this.eventType = eventType;
        this.username = username;
        this.description = description;
        this.ipAddress = ipAddress;
        this.createdAt = LocalDateTime.now();
    }
}
