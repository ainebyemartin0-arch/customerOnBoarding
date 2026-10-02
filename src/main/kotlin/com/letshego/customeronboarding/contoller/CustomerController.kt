package com.letshego.customeronboarding.controller

import com.letshego.customeronboarding.model.Customer
import com.letshego.customeronboarding.repository.CustomerRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val repository: CustomerRepository) {

    // 1. CREATE: Register a new customer
    @PostMapping
    fun createCustomer(@RequestBody customer: Customer): ResponseEntity<Customer> {
        val savedCustomer = repository.save(customer)
        return ResponseEntity(savedCustomer, HttpStatus.CREATED)
    }

    // 2. READ ALL: Fetch all customers
    @GetMapping
    fun getAllCustomers(): List<Customer> = repository.findAll()

    // 3. READ ONE: Fetch a single customer by ID
    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: Long): ResponseEntity<Customer> {
        return repository.findById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    // 4. UPDATE: Modify an existing customer record
    @PutMapping("/{id}")
    fun updateCustomer(
        @PathVariable id: Long,
        @RequestBody details: Customer
    ): ResponseEntity<Customer> {
        return repository.findById(id).map { existingCustomer ->
            existingCustomer.name = details.name
            existingCustomer.age = details.age
            existingCustomer.religion = details.religion
            existingCustomer.nationalId = details.nationalId
            ResponseEntity.ok(repository.save(existingCustomer))
        }.orElse(ResponseEntity.notFound().build())
    }

    // 5. DELETE: Remove a customer record by ID
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