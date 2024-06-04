package com.example.service;


import com.example.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getOneCustomer(int id);
    void addCustomer(Customer customer);
    boolean deleteCustomer(int id);
    boolean authenticateUser(String username, String password);
    void withdrawMoney(int id, float amount);
    float viewBankBalance(int id);
    int getIdByUsernameAndPassword(String username, String password);
    void depositMoney(int id, float amount);
    void updateCustomer(Customer customer);
    void transferMoney(int senderId, int recipientId, float amount);
    boolean isValidRecipient(int recipientId);
    boolean closeAccount(int id);
}
