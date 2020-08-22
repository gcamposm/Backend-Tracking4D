package spaceweare.tracking4d.SQL.services;

import spaceweare.tracking4d.SQL.dao.AddressDao;
import spaceweare.tracking4d.SQL.dao.CommuneDao;
import spaceweare.tracking4d.SQL.dao.RegionDao;
import spaceweare.tracking4d.SQL.models.Address;
import spaceweare.tracking4d.SQL.models.Commune;
import spaceweare.tracking4d.SQL.models.Region;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressService {

    private final RegionDao regionDao;
    private final CommuneDao communeDao;
    private final AddressDao addressDao;
    public AddressService(RegionDao regionDao, CommuneDao communeDao, AddressDao addressDao) {
        this.regionDao = regionDao;
        this.communeDao = communeDao;
        this.addressDao = addressDao;
    }

    public Address create(Address address){
        return addressDao.save(address);
    }

    public Address readById(Integer id){
        if(addressDao.findById(id).isPresent()){
            return addressDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Address> readAll(){
        return addressDao.findAll();
    }

    public Address update(Address address, Integer id){
        if(addressDao.findById(id).isPresent()){
            Address addressFound = addressDao.findById(id).get();
            addressFound.setCommune(address.getCommune());
            addressFound.setComplement(address.getComplement());
            addressFound.setCountry(address.getCountry());
            addressFound.setDirectionNumber(address.getDirectionNumber());
            addressFound.setCreatedAt(address.getCreatedAt());
            addressFound.setRegion(address.getRegion());
            addressFound.setDirectionNumber(address.getDirectionNumber());
            addressFound.setStreetName(address.getStreetName());
            addressFound.setDeletedAt(address.getDeletedAt());
            addressFound.setPerson(address.getPerson());
            addressFound.setUpdatedAt(LocalDateTime.now());
            return addressDao.save(addressFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(addressDao.findById(id).isPresent()){
            addressDao.delete(addressDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }

    public Region findRegionByName(String name){
        try {
            return regionDao.findRegionByName(name);
        }catch (Exception e){
            throw new EntityNotFoundException("Could not found the region with name: " + name + " Error: " + e.getMessage());
        }
    }

    public Commune findCommuneByName(String name){
        try {
            return communeDao.findCommuneByName(name);
        }catch (Exception e){
            throw new EntityNotFoundException("Could not found the commune with name: " + name + " Error: " + e.getMessage());
        }
    }
}