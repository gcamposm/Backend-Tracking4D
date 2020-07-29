package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Customer;
import spaceweare.tracking4d.SQL.models.Match;
import java.util.List;

@Service
public class MatchService {

    private final MatchDao matchDao;
    private final CustomerDao customerDao;
    public MatchService(MatchDao matchDao, CustomerDao customerDao) {
        this.matchDao = matchDao;
        this.customerDao = customerDao;
    }

    public Match create(Match match){
        return matchDao.save(match);
    }

    public Match readById(Integer id){
        if(matchDao.findById(id).isPresent()){
            return matchDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Match> readAll(){
        return matchDao.findAll();
    }

    public Match update(Match match, Integer id){
        if(matchDao.findById(id).isPresent()){
            Match matchFound = matchDao.findById(id).get();
            matchFound.setName(match.getName());
            matchFound.setCompany(match.getCompany());
            matchFound.setHour(match.getHour());
            matchFound.setCustomer(match.getCustomer());
            return matchDao.save(matchFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(matchDao.findById(id).isPresent()){
            matchDao.delete(matchDao.findById(id).get());
            return "deleted";
        }
        else{
            return  null;
        }
    }

    public String createByDetection(String match) {
        String[] data = match.split("=");
        match = data[1];
        data = match.split("&");
        String name = data[0];
        data = name.split(java.util.regex.Pattern.quote("+"));
        String firstName = data[0];
        String lastName = data[1];
        Customer customer = customerDao.findCustomerByFirstNameAndLastName(firstName, lastName).get();
        if(customer != null)
        {
            return customer.getFirstName();
        }
        return "No encontré al pinche wero, debe ser NN";
    }

    public String registerDetection(String detection) {
        System.out.println(detection);
        return detection;
    }
}