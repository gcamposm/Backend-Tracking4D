package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.models.Camera;
import java.util.List;

@Service
public class CameraService {

    private final CameraDao cameraDao;
    public CameraService(CameraDao cameraDao) {
        this.cameraDao = cameraDao;
    }

    public Camera create(Camera camera){
        return cameraDao.save(camera);
    }

    public Camera readById(Integer id){
        if(cameraDao.findById(id).isPresent()){
            return cameraDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Camera> readAll(){
        return cameraDao.findAll();
    }

    public Camera update(Camera camera, Integer id){
        if(cameraDao.findById(id).isPresent()){
            Camera cameraFound = cameraDao.findById(id).get();
            cameraFound.setValue(camera.getValue());
            cameraFound.setArea(camera.getArea());
            cameraFound.setMatchList(camera.getMatchList());
            return cameraDao.save(cameraFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(cameraDao.findById(id).isPresent()){
            cameraDao.delete(cameraDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}