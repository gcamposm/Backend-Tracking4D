package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.ContactDao;
import spaceweare.tracking4d.SQL.models.Contact;

import java.util.List;

@Service
public class ContactService {

    private final ContactDao contactDao;
    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    public Contact create(Contact contact){
        return contactDao.save(contact);
    }

    public Contact readById(Integer id){
        if(contactDao.findById(id).isPresent()){
            return contactDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Contact> readAll(){
        return contactDao.findAll();
    }

    public Contact update(Contact contact, Integer id){
        if(contactDao.findById(id).isPresent()){
            Contact contactFound = contactDao.findById(id).get();
            contactFound.setHour(contact.getHour());
            contactFound.setOneHourLater(contact.getOneHourLater());
            contactFound.setMatches(contact.getMatches());
            return contactDao.save(contactFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(contactDao.findById(id).isPresent()){
            contactDao.delete(contactDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }
}