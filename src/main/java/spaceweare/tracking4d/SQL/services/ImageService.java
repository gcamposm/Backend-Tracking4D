package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.models.Image;

import java.util.List;

@Service
public class ImageService {

    private final ImageDao imageDao;
    public ImageService(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public Image create(Image image){
        return imageDao.save(image);
    }

    public Image readById(Integer id){
        if(imageDao.findById(id).isPresent()){
            return imageDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Image> readAll(){
        return imageDao.findAll();
    }

    public Image update(Image image, Integer id){
        if(imageDao.findById(id).isPresent()){
            Image imageFound = imageDao.findById(id).get();
            imageFound.setName(image.getName());
            imageFound.setCompany(image.getCompany());
            imageFound.setExtension(image.getExtension());
            imageFound.setCustomer(image.getCustomer());
            return imageDao.save(imageFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(imageDao.findById(id).isPresent()){
            imageDao.delete(imageDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}