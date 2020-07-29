package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.DetectionDao;
import spaceweare.tracking4d.SQL.models.Detection;

import java.util.List;

@Service
public class DetectionService {

    private final DetectionDao detectionDao;
    public DetectionService(DetectionDao detectionDao) {
        this.detectionDao = detectionDao;
    }

    public Detection create(Detection detection){
        return detectionDao.save(detection);
    }

    public Detection readById(Integer id){
        if(detectionDao.findById(id).isPresent()){
            return detectionDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Detection> readAll(){
        return detectionDao.findAll();
    }

    public Detection update(Detection detection, Integer id){
        if(detectionDao.findById(id).isPresent()){
            Detection detectionFound = detectionDao.findById(id).get();
            detectionFound.setImage(detection.getImage());
            detectionFound.setValue(detection.getValue());
            return detectionDao.save(detectionFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(detectionDao.findById(id).isPresent()){
            detectionDao.delete(detectionDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}