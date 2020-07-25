package drii.pingeso.backend.SQL.services;

import drii.pingeso.backend.SQL.dao.CommuneDao;
import drii.pingeso.backend.SQL.models.Commune;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommuneService {

    private final CommuneDao communeDao;
    public CommuneService(CommuneDao communeDao) {
        this.communeDao = communeDao;
    }

    public Commune create(Commune commune){
        return communeDao.save(commune);
    }

    public Commune readById(Integer id){
        if(communeDao.findById(id).isPresent()){
            return communeDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Commune> readAll(){
        return communeDao.findAll();
    }

    public Commune update(Commune commune, Integer id){
        if(communeDao.findById(id).isPresent()){
            Commune communeFound = communeDao.findById(id).get();
            communeFound.setCreatedAt(commune.getCreatedAt());
            communeFound.setRegion(commune.getRegion());
            communeFound.setDeletedAt(commune.getDeletedAt());
            communeFound.setUpdatedAt(LocalDateTime.now());
            communeFound.setName(commune.getName());
            communeFound.setAddresses(commune.getAddresses());
            return communeDao.save(communeFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(communeDao.findById(id).isPresent()){
            communeDao.delete(communeDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}