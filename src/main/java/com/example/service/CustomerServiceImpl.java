package com.example.service;



import com.example.exceptions.ResourceNotFoundException;
import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getOneCustomer(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }

    @Override
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean deleteCustomer(int id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        return customerRepository.findByUsernameAndPassword(username, password) != null;
    }

    @Override
    public void withdrawMoney(int id, float amount) {
        Customer customer = getOneCustomer(id);
        float currentBalance = customer.getBalance();
        if (currentBalance < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        customer.setBalance(currentBalance - amount);
        customerRepository.save(customer);
    }

    @Override
    public float viewBankBalance(int id) {
        return getOneCustomer(id).getBalance();
    }

    @Override
    public int getIdByUsernameAndPassword(String username, String password) {
        Customer customer = customerRepository.findByUsernameAndPassword(username, password);
        if (customer != null) {
            return customer.getId();
        } else {
            throw new ResourceNotFoundException("User not found with provided username and password.");
        }
    }

    @Override
    public void depositMoney(int id, float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative.");
        }
        Customer customer = getOneCustomer(id);
        float currentBalance = customer.getBalance();
        customer.setBalance(currentBalance + amount);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void transferMoney(int senderId, int recipientId, float amount) {
        Customer sender = getOneCustomer(senderId);
        Customer recipient = getOneCustomer(recipientId);

        float senderBalance = sender.getBalance();
        if (senderBalance < amount) {
            throw new IllegalArgumentException("Insufficient balance for transfer.");
        }

        sender.setBalance(senderBalance - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        customerRepository.save(sender);
        customerRepository.save(recipient);
    }

    @Override
    public boolean isValidRecipient(int recipientId) {
        return customerRepository.existsById(recipientId);
    }

    @Override
    public boolean closeAccount(int id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }
    }
}
