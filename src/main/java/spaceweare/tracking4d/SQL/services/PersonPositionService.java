package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.dao.PersonPositionDao;
import spaceweare.tracking4d.SQL.models.Camera;
import spaceweare.tracking4d.SQL.models.PersonPosition;

import java.util.List;

@Service
public class PersonPositionService {

    private final PersonPositionDao personPositionDao;
    public PersonPositionService(PersonPositionDao personPositionDao) {
        this.personPositionDao = personPositionDao;
    }

    public PersonPosition create(PersonPosition personPosition){
        return personPositionDao.save(personPosition);
    }

    public PersonPosition readById(Integer id){
        if(personPositionDao.findById(id).isPresent()){
            return personPositionDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<PersonPosition> readAll(){
        return personPositionDao.findAll();
    }

    public PersonPosition update(PersonPosition personPosition, Integer id){
        if(personPositionDao.findById(id).isPresent()){
            PersonPosition personPositionFound = personPositionDao.findById(id).get();
            personPositionFound.setName(personPosition.getName());
            personPositionFound.setPersonList(personPosition.getPersonList());
            return personPositionDao.save(personPositionFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(personPositionDao.findById(id).isPresent()){
            personPositionDao.delete(personPositionDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}