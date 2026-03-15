/**
 * To remove BaseEntity due to Jackson deserialization issue
 */

//package com.gomcc.merchant_checker_service.model;
//
//
//import com.fasterxml.jackson.annotation.JsonSubTypes;
//import com.fasterxml.jackson.annotation.JsonTypeInfo;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//
//import java.io.Serializable;
//import java.time.ZonedDateTime;
//import java.time.ZoneId;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@SuperBuilder
//@MappedSuperclass
//@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS, property = "@class")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = Merchant.class)
//})
//public abstract class BaseEntity{
//
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull
//    private ZonedDateTime createdAt;
//    @NotNull
//    private ZonedDateTime updatedAt;
//
//    @PrePersist
//    public void prePersist() {
//        createdAt = ZonedDateTime.now(ZoneId.of("Z"));
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        updatedAt = ZonedDateTime.now(ZoneId.of("Z"));
//    }
//}
