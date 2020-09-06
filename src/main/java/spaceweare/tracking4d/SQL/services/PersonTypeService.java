package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.PersonTypeDao;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.PersonType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonTypeService {

    private final PersonTypeDao personTypeDao;
    private final PersonDao personDao;
    public PersonTypeService(PersonTypeDao personTypeDao, PersonDao personDao) {
        this.personTypeDao = personTypeDao;
        this.personDao = personDao;
    }

    public PersonType create(PersonType personType){
        if(personTypeDao.findPersonTypeByName(personType.getName()).isPresent())
        {
            return personTypeDao.findPersonTypeByName(personType.getName()).get();
        }
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

    public List<Map<Object, Object>> getNames() {
        List<Map<Object, Object>> names = new ArrayList<>();
        for (PersonType personType: personTypeDao.findAll()
             ) {
            Map<Object, Object> name = new HashMap<>();
            name.put("id", personType.getId());
            name.put("name", personType.getName());
            name.put("abbr", personType.getName());
            names.add(name);
        }
        return names;
    }

    public Person personWithType(String personTypeName, String personRut) {
        PersonType personType = personTypeDao.findPersonTypeByName(personTypeName).get();
        Person person = personDao.findPersonByRut(personRut).get();
        person.setType(personType);
        return personDao.save(person);
    }
}