package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.AlertDao;
import spaceweare.tracking4d.SQL.models.Alert;
import spaceweare.tracking4d.SQL.models.Area;

import java.util.List;

@Service
public class AlertService {

    private final AlertDao alertDao;
    public AlertService(AlertDao alertDao) {
        this.alertDao = alertDao;
    }

    public Alert create(Alert alert){
        return alertDao.save(alert);
    }

    public Alert readById(Integer id){
        if(alertDao.findById(id).isPresent()){
            return alertDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Alert> readAll(){
        return alertDao.findAll();
    }

    public Alert update(Alert alert, Integer id){
        if(alertDao.findById(id).isPresent()){
            Alert alertFound = alertDao.findById(id).get();
            alertFound.setDate(alert.getDate());
            alertFound.setTemperature(alert.getTemperature());
            alertFound.setActive(alert.getActive());
            alertFound.setPerson(alert.getPerson());
            return alertDao.save(alertFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(alertDao.findById(id).isPresent()){
            alertDao.delete(alertDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }

    public List<Alert> active() {
        return alertDao.findAllByActive(true);
    }

    public Alert softDelete(Integer id) {
        if(alertDao.findAlertById(id).isPresent())
        {
            Alert alert = alertDao.findAlertById(id).get();
            alert.setActive(false);
            return alertDao.save(alert);
        }
        return null;
    }
}