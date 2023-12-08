package com.gertjan.ccproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class DataObject {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;


}
