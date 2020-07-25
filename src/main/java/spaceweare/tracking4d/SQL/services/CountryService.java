package spaceweare.tracking4d.SQL.services;

import spaceweare.tracking4d.SQL.dao.CountryDao;
import spaceweare.tracking4d.SQL.models.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private final CountryDao countryDao;
    public CountryService(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    public Country create(Country country){
        return countryDao.save(country);
    }

    public Country readById(Integer id){
        if(countryDao.findById(id).isPresent()){
            return countryDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Country> readAll(){
        return countryDao.findAll();
    }

    public Country update(Country country, Integer id){
        if(countryDao.findById(id).isPresent()){
            Country countryFound = countryDao.findById(id).get();
            countryFound.setName(country.getName());
            countryFound.setAddresses(country.getAddresses());
            return countryDao.save(country);
        }
        return null;
    }

    public String delete(Integer id){
        if(countryDao.findById(id).isPresent()){
            countryDao.delete(countryDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}