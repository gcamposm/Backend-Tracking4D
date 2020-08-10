package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.AreaDao;
import spaceweare.tracking4d.SQL.models.Area;
import java.util.List;

@Service
public class AreaService {

    private final AreaDao areaDao;
    public AreaService(AreaDao areaDao) {
        this.areaDao = areaDao;
    }

    public Area create(Area area){
        return areaDao.save(area);
    }

    public Area readById(Integer id){
        if(areaDao.findById(id).isPresent()){
            return areaDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Area> readAll(){
        return areaDao.findAll();
    }

    public Area update(Area area, Integer id){
        if(areaDao.findById(id).isPresent()){
            Area areaFound = areaDao.findById(id).get();
            areaFound.setName(area.getName());
            areaFound.setLocal(area.getLocal());
            areaFound.setCameraList(area.getCameraList());
            return areaDao.save(areaFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(areaDao.findById(id).isPresent()){
            areaDao.delete(areaDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}