package spaceweare.tracking4d.SQL.services;

import spaceweare.tracking4d.SQL.dao.RegionDao;
import spaceweare.tracking4d.SQL.models.Region;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegionService {

    private final RegionDao regionDao;
    public RegionService(RegionDao regionDao) {
        this.regionDao = regionDao;
    }

    public Region create(Region region){
        return regionDao.save(region);
    }

    public Region readById(Integer id){
        if(regionDao.findById(id).isPresent()){
            return regionDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Region> readAll(){
        return regionDao.findAll();
    }

    public Region update(Region region, Integer id){
        if(regionDao.findById(id).isPresent()){
            Region regionFound = regionDao.findById(id).get();
            regionFound.setCreatedAt(region.getCreatedAt());
            regionFound.setName(region.getName());
            regionFound.setNumber(region.getNumber());
            regionFound.setAddresses(region.getAddresses());
            regionFound.setCommuneList(region.getCommuneList());
            regionFound.setDeletedAt(region.getDeletedAt());
            regionFound.setUpdatedAt(LocalDateTime.now());
            return regionDao.save(region);
        }
        return null;
    }

    public String delete(Integer id){
        if(regionDao.findById(id).isPresent()){
            regionDao.delete(regionDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}