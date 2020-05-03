package academy.digitallab.store.customer.controller;

import academy.digitallab.store.customer.entity.Customer;
import academy.digitallab.store.customer.entity.Region;
import academy.digitallab.store.customer.messages.ErrorMessages;
import academy.digitallab.store.customer.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @GetMapping
    public ResponseEntity<List<Customer>> findAllCustomer(@RequestParam(name = "regionId", required = false) Long regionId){

        log.info(" CustomerController --> findAllCustomer --> regionId=> {}", regionId );
        List<Customer> customers = new ArrayList<>();
        if(regionId==null) {
            customers = customerService.findAllCustomer();
            if (customers.isEmpty()) {
                log.error(" CustomerController --> findAllCustomer --> list empty ");
                return ResponseEntity.noContent().build();
            } else {
                log.info(" CustomerController --> findAllCustomer --> list customer --> ok ");
                return ResponseEntity.ok(customers);
            }
        }

        else{
            Region region = new Region();
            region.setId(regionId);
            customers = customerService.findCustomerByRegion(region);
            if(customers == null){
                log.error(" CustomerController --> findAllCustomer --> list customer by region --> not found ");
                return ResponseEntity.notFound().build();
            }
            log.error(" CustomerController --> findAllCustomer --> list customer by region --> ok ");
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findByIdCustomer(@PathVariable (name = "id", required = true) Long id){

        log.info(" CustomerController --> findByIdCustomer --> id=>" , id );
        Customer customer = customerService.findByIdCustomer(id);
        if(customer == null){
            log.error(" CustomerController --> findByIdCustomer --> id => {} not found" , id );
            return ResponseEntity.notFound().build();
        }
        log.info(" CustomerController --> findByIdCustomer --> {id=> {} ok ",id );
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result){
        log.info(" CustomerController --> createCustomer --> customer=> {}" , customer.toString() );
        if(result.hasErrors()){
            String resultValid = validDataCustomer(result);
            log.error("InvoiceController --> createCustomer --> Error: "+ resultValid);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resultValid);
        }
        Customer customerDB = customerService.createCustomer(customer);
        if(customerDB==null){
            log.error("InvoiceController --> createCustomer --> Error: Customer create DB");
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable(name = "id") Long id){
        log.info(" CustomerController --> updateCustomer --> customer=> {} id {}" , customer.toString(), id );
        customer.setId(id);
        Customer customerDB = customerService.updateCustomer(customer);
        if(customerDB==null){
            log.error("CustomerController --> updateCustomer --> customerDB null");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customerDB);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable(name = "id") Long id){
        log.info(" CustomerController --> updateCustomer --> deleteCustomer=> id {} " , id );
        Customer customerDB = customerService.deleteCustomer(id);
        if(customerDB==null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerDB);
    }

    public String validDataCustomer(BindingResult result){
        List<Map<String, String>> errors= result.getFieldErrors().stream()
                .map( err -> {
                            Map <String, String> e = new HashMap<>();
                            e.put(err.getField(), err.getDefaultMessage());
                            return e;
                        }
                ).collect(Collectors.toList());

        ErrorMessages errorMessages = ErrorMessages.builder()
                .code("01")
                .messages(errors).build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString ="";
        try{
            jsonString = mapper.writeValueAsString(errorMessages);
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }


}
