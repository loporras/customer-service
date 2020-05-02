package academy.digitallab.store.customer.service;

import academy.digitallab.store.customer.entity.Customer;
import academy.digitallab.store.customer.entity.Region;
import academy.digitallab.store.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public Customer findCustomerByNumberId(String numberId) { return customerRepository.findByNumberId(numberId); }

    @Override
    public List<Customer> findCustomerByLastName(String lastName) {
        return customerRepository.finByLastName(lastName);
    }

    @Override
    public List<Customer> findCustomerByRegion(Region region) {
       return customerRepository.findByRegion(region);
    }

    @Override
    public List<Customer> findAllCustomer() { return customerRepository.findAll(); }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer customerDB = customerRepository.findByNumberId(customer.getNumberId());
        if(customerDB==null){
            customer.setState("CREATED");
            return customerRepository.save(customer);
        }
        else{
            return customerDB;
        }
    }

    @Override
    public Customer deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer==null){
            return null;
        }
        else{
            customer.setState("DELETED");
            Customer  customerDB = customerRepository.save(customer);
            return customerDB;
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customerDB = customerRepository.findById(customer.getId()).orElse(null);
        if(customerDB==null){
            return null;
        }
        else{
            customerDB.setEmail(customer.getEmail());
            customerDB.setFirstName(customer.getFirstName());
            customerDB.setLastName(customer.getLastName());
            customerDB.setPhotoUrl(customer.getPhotoUrl());
            Customer customerUp = customerRepository.save(customerDB);
            return customerUp;
        }

    }
}
