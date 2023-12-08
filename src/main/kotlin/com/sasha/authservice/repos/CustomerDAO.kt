package com.sasha.authservice.repos

import com.sasha.authservice.models.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerDAO : CrudRepository<Customer, Long>