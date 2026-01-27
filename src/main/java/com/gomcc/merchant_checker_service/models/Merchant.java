package com.gomcc.merchant_checker_service.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
@Data()
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Merchant extends BaseEntity{

    @Column(name="name", nullable = false)
    @NotNull
    @NotBlank
    private String name;

    @Column(name="mcc", nullable = false)
    @NotNull
    @NotBlank
    private Long mcc; // cannot be null

    @Column(name="description", nullable = false)
    @NotNull
    @NotBlank
    private String description; // cannot be null

    @Column(name="mode_of_payment", nullable = false)
    @NotNull
    @NotBlank
    private MerchantModeOfPayment mode; // cannot be null



}
