package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.DescriptorDao;
import spaceweare.tracking4d.SQL.models.Descriptor;

import java.util.List;

@Service
public class DescriptorService {

    private final DescriptorDao descriptorDao;
    public DescriptorService(DescriptorDao descriptorDao) {
        this.descriptorDao = descriptorDao;
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
            //descriptorFound.setDescriptor(descriptor.getDescriptor());
            descriptorFound.setMatch(descriptor.getMatch());
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
}