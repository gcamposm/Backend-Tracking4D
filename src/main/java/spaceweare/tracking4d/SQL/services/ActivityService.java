package spaceweare.tracking4d.SQL.services;

import spaceweare.tracking4d.SQL.dao.ActivityDao;
import spaceweare.tracking4d.SQL.models.Activity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityDao activityDao;
    public ActivityService(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public Activity create(Activity activity){
        return activityDao.save(activity);
    }

    public Activity readById(Integer id){
        if(activityDao.findById(id).isPresent()){
            return activityDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Activity> readAll(){
        return activityDao.findAll();
    }

    public Activity update(Activity activity, Integer id){
        if(activityDao.findById(id).isPresent()){
            Activity activityFound = activityDao.findById(id).get();
            activityFound.setName(activity.getName());
            activityFound.setCustomerList(activity.getCustomerList());
            return activityDao.save(activityFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(activityDao.findById(id).isPresent()){
            activityDao.delete(activityDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}