package com.stock.inventory.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String identificationDocument; // CNPJ ou CPF
    private String email;
    private String phone;
    private String address;
}
