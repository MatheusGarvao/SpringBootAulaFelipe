package com.stock.inventory.service;

import com.stock.inventory.repository.SupplierRepository;
import com.stock.inventory.repository.entity.Supplier;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier saveSupplier(Supplier supplier) {
        if (supplierRepository.existsByIdentificationDocument(supplier.getIdentificationDocument())) {
            throw new RuntimeException("Supplier with document '" + supplier.getIdentificationDocument() + "' already exists");
        }
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, Supplier patch) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com o ID: " + id));

        if (patch.getIdentificationDocument() != null) {
            String newDoc = patch.getIdentificationDocument();
            String currentDoc = supplier.getIdentificationDocument();
            if (!newDoc.equals(currentDoc) && supplierRepository.existsByIdentificationDocument(newDoc)) {
                throw new RuntimeException("Supplier with document '" + newDoc + "' already exists");
            }
        }

        final BeanWrapper src = new BeanWrapperImpl(patch);
        String[] nullProps = Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> src.getPropertyValue(name) == null)
                .toArray(String[]::new);

        String[] ignore = Stream.concat(
                Arrays.stream(nullProps),
                Arrays.stream(new String[] { "id" })
        ).toArray(String[]::new);

        BeanUtils.copyProperties(patch, supplier, ignore);

        return supplierRepository.save(supplier);
    }


    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        supplierRepository.delete(supplier);
    }

    public List<Supplier> searchSuppliersByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<Supplier> getSupplierByDocument(String document) {
        return supplierRepository.findByIdentificationDocument(document);
    }

    @Transactional(readOnly = true)
    public Supplier refOrNotFound(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new EntityNotFoundException("Fornecedor não encontrado com o ID: " + id);
        }
        return supplierRepository.getReferenceById(id);
    }
}