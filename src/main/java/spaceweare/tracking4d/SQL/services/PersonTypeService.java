package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.PersonTypeDao;
import spaceweare.tracking4d.SQL.models.PersonType;
import java.util.List;

@Service
public class PersonTypeService {

    private final PersonTypeDao personTypeDao;
    public PersonTypeService(PersonTypeDao personTypeDao) {
        this.personTypeDao = personTypeDao;
    }

    public PersonType create(PersonType personType){
        return personTypeDao.save(personType);
    }

    public PersonType readById(Integer id){
        if(personTypeDao.findById(id).isPresent()){
            return personTypeDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<PersonType> readAll(){
        return personTypeDao.findAll();
    }

    public PersonType update(PersonType personType, Integer id){
        if(personTypeDao.findById(id).isPresent()){
            PersonType personTypeFound = personTypeDao.findById(id).get();
            personTypeFound.setName(personType.getName());
            personTypeFound.setPersonList(personType.getPersonList());
            return personTypeDao.save(personTypeFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(personTypeDao.findById(id).isPresent()){
            personTypeDao.delete(personTypeDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}