package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.PixelDao;
import spaceweare.tracking4d.SQL.models.Pixel;
import java.util.List;

@Service
public class PixelService {

    private final PixelDao pixelDao;
    public PixelService(PixelDao pixelDao) {
        this.pixelDao = pixelDao;
    }

    public Pixel create(Pixel pixel){
        return pixelDao.save(pixel);
    }

    public Pixel readById(Integer id){
        if(pixelDao.findById(id).isPresent()){
            return pixelDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Pixel> readAll(){
        return pixelDao.findAll();
    }

    public Pixel update(Pixel pixel, Integer id){
        if(pixelDao.findById(id).isPresent()){
            Pixel pixelFound = pixelDao.findById(id).get();
            pixelFound.setValue(pixel.getValue());
            pixelFound.setTemperature(pixel.getTemperature());
            return pixelDao.save(pixelFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(pixelDao.findById(id).isPresent()){
            pixelDao.delete(pixelDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}