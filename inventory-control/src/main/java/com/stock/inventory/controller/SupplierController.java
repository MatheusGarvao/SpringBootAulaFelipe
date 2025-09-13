package com.stock.inventory.controller;

import com.stock.inventory.repository.entity.Supplier;
import com.stock.inventory.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        return supplier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        try {
            Supplier savedSupplier = supplierService.saveSupplier(supplier);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplierDetails) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDetails);
            return ResponseEntity.ok(updatedSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Supplier>> searchSuppliers(@RequestParam String name) {
        List<Supplier> suppliers = supplierService.searchSuppliersByName(name);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/document")
    public ResponseEntity<Supplier> getSupplierByDocument(@RequestParam String document) {
        Optional<Supplier> supplier = supplierService.getSupplierByDocument(document);
        return supplier.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}