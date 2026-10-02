package com.letshego.customeronboarding.controller

import com.letshego.customeronboarding.model.Customer
import com.letshego.customeronboarding.repository.CustomerRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val repository: CustomerRepository) {

    // CREATE: Register new customer
    @PostMapping
    fun createCustomer(@RequestBody customer: Customer): ResponseEntity<Customer> {
        val savedCustomer = repository.save(customer)
        return ResponseEntity(savedCustomer, HttpStatus.CREATED)
    }

    // READ ALL: Get list of all customers
    @GetMapping
    fun getAllCustomers(): List<Customer> = repository.findAll()

    // READ ONE: Get customer by ID
    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: Long): ResponseEntity<Customer> {
        return repository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    // UPDATE: Update customer details by ID
    @PutMapping("/{id}")
    fun updateCustomer(
        @PathVariable id: Long,
        @RequestBody updatedDetails: Customer
    ): ResponseEntity<Customer> {
        return repository.findById(id).map { existingCustomer ->
            existingCustomer.name = updatedDetails.name
            existingCustomer.age = updatedDetails.age
            existingCustomer.religion = updatedDetails.religion
            existingCustomer.nationalId = updatedDetails.nationalId
            ResponseEntity.ok(repository.save(existingCustomer))
        }.orElse(ResponseEntity.notFound().build())
    }

    // DELETE: Remove customer record by ID
    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long): ResponseEntity<Void> {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}