package drii.pingeso.backend.SQL.services;

import drii.pingeso.backend.SQL.dao.PersonDao;
import drii.pingeso.backend.SQL.models.Person;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonService {

    private final PersonDao personDao;
    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public Person create(Person person){
        return personDao.save(person);
    }

    public Person readById(Integer id){
        if(personDao.findById(id).isPresent()){
            return personDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Person> readAll(){
        return personDao.findAll();
    }

    public Person update(Person person, Integer id){
        if(personDao.findById(id).isPresent()){
            Person personFound = personDao.findById(id).get();
            personFound.setFirstName(person.getFirstName());
            personFound.setLastName(person.getLastName());
            personFound.setRut(person.getRut());
            personFound.setActivity(person.getActivity());
            personFound.setCompany(person.getCompany());
            personFound.setGenre(person.getGenre());
            personFound.setImages(person.getImages());
            personFound.setMail(person.getMail());
            personFound.setPhoneNumber(person.getPhoneNumber());
            personFound.setUser(person.getUser());
            return personDao.save(personFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(personDao.findById(id).isPresent()){
            personDao.delete(personDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }
}