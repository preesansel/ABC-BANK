package com.example.controller;



import com.example.exceptions.ResourceNotFoundException;
import com.example.model.Customer;
import com.example.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") int id) {
        try {
            Customer customer = customerService.getOneCustomer(id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") int id) {
        try {
            boolean deleted = customerService.deleteCustomer(id);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticateUser(@RequestParam String username, @RequestParam String password) {
        boolean authenticated = customerService.authenticateUser(username, password);
        return new ResponseEntity<>(authenticated, HttpStatus.OK);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Void> depositMoney(@PathVariable("id") int id, @RequestParam float amount) {
        try {
            customerService.depositMoney(id, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawMoney(@PathVariable("id") int id, @RequestParam float amount) {
        try {
            customerService.withdrawMoney(id, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Float> viewBankBalance(@PathVariable("id") int id) {
        try {
            float balance = customerService.viewBankBalance(id);
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Void> updateCustomer(@PathVariable("id") int id, @RequestBody Customer customer) {
        customer.setId(id);
        try {
            customerService.updateCustomer(customer);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{senderId}/transfer/{recipientId}")
    public ResponseEntity<Void> transferMoney(@PathVariable("senderId") int senderId,
                                              @PathVariable("recipientId") int recipientId,
                                              @RequestParam float amount) {
        try {
            customerService.transferMoney(senderId, recipientId, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/close")
    public ResponseEntity<Void> closeAccount(@PathVariable("id") int id) {
        try {
            boolean closed = customerService.closeAccount(id);
            if (closed) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

