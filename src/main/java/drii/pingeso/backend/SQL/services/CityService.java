package drii.pingeso.backend.SQL.services;

import drii.pingeso.backend.SQL.dao.CityDao;
import drii.pingeso.backend.SQL.models.City;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CityService {

    private final CityDao cityDao;
    public CityService(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public City create(City city){
        return cityDao.save(city);
    }

    public City readById(Integer id){
        if(cityDao.findById(id).isPresent()){
            return cityDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<City> readAll(){
        return cityDao.findAll();
    }

    public City update(City city, Integer id){
        if(cityDao.findById(id).isPresent()){
            City cityFound = cityDao.findById(id).get();
            cityFound.setName(city.getName());
            cityFound.setAddresses(city.getAddresses());
            cityFound.setCreatedAt(city.getCreatedAt());
            cityFound.setDeletedAt(city.getDeletedAt());
            cityFound.setUpdatedAt(LocalDateTime.now());
            return cityDao.save(cityFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(cityDao.findById(id).isPresent()){
            cityDao.delete(cityDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}