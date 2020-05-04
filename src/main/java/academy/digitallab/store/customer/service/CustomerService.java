package academy.digitallab.store.customer.service;

import academy.digitallab.store.customer.entity.Customer;
import academy.digitallab.store.customer.entity.Region;

import java.util.List;

public interface CustomerService {

    public Customer findCustomerByNumberId(String numberId);
    public Customer findByIdCustomer(Long id);
    public List<Customer> findCustomerByLastName(String lastName);
    public List<Customer> findCustomerByRegion(Region region);
    public List<Customer> findAllCustomer();


    public Customer createCustomer(Customer customer);
    public Customer deleteCustomer(Long id);
    public Customer updateCustomer(Customer customer);


}
