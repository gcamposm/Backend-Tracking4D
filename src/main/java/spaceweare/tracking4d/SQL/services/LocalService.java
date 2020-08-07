package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.LocalDao;
import spaceweare.tracking4d.SQL.models.Local;
import java.util.List;

@Service
public class LocalService {

    private final LocalDao localDao;
    public LocalService(LocalDao localDao) {
        this.localDao = localDao;
    }

    public Local create(Local local){
        return localDao.save(local);
    }

    public Local readById(Integer id){
        if(localDao.findById(id).isPresent()){
            return localDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Local> readAll(){
        return localDao.findAll();
    }

    public Local update(Local local, Integer id){
        if(localDao.findById(id).isPresent()){
            Local localFound = localDao.findById(id).get();
            localFound.setName(local.getName());
            localFound.setCompany(local.getCompany());
            localFound.setAreaList(local.getAreaList());
            return localDao.save(localFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(localDao.findById(id).isPresent()){
            localDao.delete(localDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}