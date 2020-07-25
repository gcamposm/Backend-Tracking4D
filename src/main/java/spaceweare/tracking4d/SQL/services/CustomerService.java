package spaceweare.tracking4d.SQL.services;

import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.models.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Customer create(Customer customer){
        return customerDao.save(customer);
    }

    public Customer readById(Integer id){
        if(customerDao.findById(id).isPresent()){
            return customerDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Customer> readAll(){
        return customerDao.findAll();
    }

    public Customer update(Customer customer, Integer id){
        if(customerDao.findById(id).isPresent()){
            Customer customerFound = customerDao.findById(id).get();
            customerFound.setFirstName(customer.getFirstName());
            customerFound.setLastName(customer.getLastName());
            customerFound.setRut(customer.getRut());
            customerFound.setActivity(customer.getActivity());
            customerFound.setCompany(customer.getCompany());
            customerFound.setGenre(customer.getGenre());
            customerFound.setImages(customer.getImages());
            customerFound.setMail(customer.getMail());
            customerFound.setPhoneNumber(customer.getPhoneNumber());
            customerFound.setUser(customer.getUser());
            return customerDao.save(customerFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(customerDao.findById(id).isPresent()){
            customerDao.delete(customerDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}