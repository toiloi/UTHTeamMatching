package org.example.uthteammatching.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "lecturer_requests")
@Data
public class LecturerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UthUser user;

    @Column(columnDefinition = "NVARCHAR(1000)", nullable = false)
    private String expertise;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String experience;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String reason;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column
    private String certificatesPath;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime processedAt;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private UthUser processedBy;
} 