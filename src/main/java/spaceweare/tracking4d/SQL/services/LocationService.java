package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.LocationDao;
import spaceweare.tracking4d.SQL.models.Location;
import java.util.List;

@Service
public class LocationService {

    private final LocationDao locationDaoDao;
    public LocationService(LocationDao locationDaoDao) {
        this.locationDaoDao = locationDaoDao;
    }

    public Location create(Location location){
        return locationDaoDao.save(location);
    }

    public Location readById(Integer id){
        if(locationDaoDao.findById(id).isPresent()){
            return locationDaoDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Location> readAll(){
        return locationDaoDao.findAll();
    }

    public Location update(Location location, Integer id){
        if(locationDaoDao.findById(id).isPresent()){
            Location locationFound = locationDaoDao.findById(id).get();
            locationFound.setLatitude(location.getLatitude());
            locationFound.setLongitude(location.getLongitude());
            locationFound.setMatchList(location.getMatchList());
            return locationDaoDao.save(locationFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(locationDaoDao.findById(id).isPresent()){
            locationDaoDao.delete(locationDaoDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}