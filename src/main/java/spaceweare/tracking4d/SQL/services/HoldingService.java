package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.HoldingDao;
import spaceweare.tracking4d.SQL.models.Holding;
import java.util.List;

@Service
public class HoldingService {

    private final HoldingDao holdingDao;
    public HoldingService(HoldingDao holdingDao) {
        this.holdingDao = holdingDao;
    }

    public Holding create(Holding holding){
        return holdingDao.save(holding);
    }

    public Holding readById(Integer id){
        if(holdingDao.findById(id).isPresent()){
            return holdingDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Holding> readAll(){
        return holdingDao.findAll();
    }

    public Holding update(Holding holding, Integer id){
        if(holdingDao.findById(id).isPresent()){
            Holding holdingFound = holdingDao.findById(id).get();
            holdingFound.setName(holding.getName());
            holdingFound.setCompanyList(holding.getCompanyList());
            return holdingDao.save(holdingFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(holdingDao.findById(id).isPresent()){
            holdingDao.delete(holdingDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}