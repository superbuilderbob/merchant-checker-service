package com.gomcc.merchant_checker_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Merchant data model
 *
 * Merchant entity represents immutable record of merchant
 *
 * ------------------------------
 * merchant_id: int
 * merchant_name: string
 * merchant_category_code: int
 * merchant_description: enum
 * mode_of_payment: enum
 * created_at: timestamp
 * updated_at: timestamp
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Merchant{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime updatedAt;

    @Column(name="name", nullable = false)
    @NotNull
    @NotBlank
    private final String name;

    @Column(name="mcc", nullable = false)
    @NotNull
    @NotBlank
    private final Long mcc; // cannot be null

    @Column(name="description", nullable = false)
    @NotNull
    @NotBlank
    private final String description; // cannot be null

    @Column(name="mode_of_payment", nullable = false)
    @NotNull
    @NotBlank
    @Enumerated(EnumType.STRING)
    private final MerchantModeOfPayment mode; // cannot be null

    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now(ZoneId.of("Z"));
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = ZonedDateTime.now(ZoneId.of("Z"));
    }
}
