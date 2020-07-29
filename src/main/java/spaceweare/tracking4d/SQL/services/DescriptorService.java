package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.dao.DescriptorDao;
import spaceweare.tracking4d.SQL.dao.DetectionDao;
import spaceweare.tracking4d.SQL.models.Customer;
import spaceweare.tracking4d.SQL.models.Descriptor;
import spaceweare.tracking4d.SQL.models.Detection;

import java.util.List;

@Service
public class DescriptorService {

    private final DescriptorDao descriptorDao;
    private final CustomerDao customerDao;
    private final DetectionDao detectionDao;
    public DescriptorService(DescriptorDao descriptorDao, CustomerDao customerDao, DetectionDao detectionDao) {
        this.descriptorDao = descriptorDao;
        this.customerDao = customerDao;
        this.detectionDao = detectionDao;
    }

    public Descriptor create(Descriptor descriptor){
        return descriptorDao.save(descriptor);
    }

    public Descriptor readById(Integer id){
        if(descriptorDao.findById(id).isPresent()){
            return descriptorDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Descriptor> readAll(){
        return descriptorDao.findAll();
    }

    public Descriptor update(Descriptor descriptor, Integer id){
        if(descriptorDao.findById(id).isPresent()){
            Descriptor descriptorFound = descriptorDao.findById(id).get();
            descriptorFound.setCustomer(descriptor.getCustomer());
            descriptorFound.setDetections(descriptor.getDetections());
            descriptorFound.setPath(descriptor.getPath());
            return descriptorDao.save(descriptorFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(descriptorDao.findById(id).isPresent()){
            descriptorDao.delete(descriptorDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }

    public Descriptor chargeData(List<Float> descriptorList, String path, String userName) {
        String[] data = userName.split(" ");
        String firstName = data[0];
        String lastName = data[1];
        if(!customerDao.findCustomerByFirstNameAndLastName(firstName, lastName).isPresent())
        {
            System.out.println("No lo encontré");
            Customer customer = new Customer();
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            return createDescriptorWithCustomer(customerDao.save(customer), descriptorList, path);
        }
        Customer customer = customerDao.findCustomerByFirstNameAndLastName(firstName, lastName).get();
        return createDescriptorWithCustomer(customer, descriptorList, path);
    }

    private Descriptor createDescriptorWithCustomer(Customer customer, List<Float> descriptorList, String path) {
        Descriptor descriptor = new Descriptor();
        descriptor.setPath(path);
        descriptor.setCustomer(customer);
        descriptorDao.save(descriptor);
        for (Float descriptorFor: descriptorList
        ) {
            Detection detection = new Detection();
            detection.setDescriptor(descriptor);
            detection.setValue(descriptorFor);
            detectionDao.save(detection);
        }
        return descriptorDao.save(descriptor);
    }
}