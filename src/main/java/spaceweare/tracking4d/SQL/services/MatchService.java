package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.CameraDao;
import spaceweare.tracking4d.SQL.dao.ContactDao;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.Match;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class MatchService {

    private final MatchDao matchDao;
    private final PersonDao personDao;
    private final CameraDao cameraDao;
    private final ContactDao contactDao;
    public MatchService(MatchDao matchDao, PersonDao personDao, CameraDao cameraDao, ContactDao contactDao) {
        this.matchDao = matchDao;
        this.personDao = personDao;
        this.cameraDao = cameraDao;
        this.contactDao = contactDao;
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
            matchFound.setPerson(match.getPerson());
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
        Person person = personDao.findPersonByFirstNameAndLastName(firstName, lastName).get();
        if(person != null)
        {
            return person.getFirstName();
        }
        return "No encontré al pinche wero, debe ser NN";
    }

    public String registerDetection(String detection) {
        System.out.println(detection);
        return detection;
    }

    public List<Match> withFilteredMatches(List<String> rutList, Integer cameraId) {
        List<Match> matches = new ArrayList<>();
        System.out.println("cameraID "+cameraId);
        for (String rut:rutList
             ) {
            if(personDao.findPersonByRut(rut).isPresent()){
                Match match = new Match();
                //match.setCompany();
                match.setPerson(personDao.findPersonByRut(rut).get());
                match.setHour(LocalDateTime.now());
                if(cameraDao.findCameraById(cameraId).isPresent())
                {
                    match.setCamera(cameraDao.findCameraById(cameraId).get());
                }
                matchDao.save(match);
                matches.add(match);
            }
        }
        return matches;
    }

    public List<Match> getMatchesByDate(Date firstDate, Date secondDate) {
        Instant firstCurrent = firstDate.toInstant();
        Instant secondCurrent = secondDate.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault());
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(1);
        return matchDao.findMatchByHourBetween(firstLocalDate, secondLocalDate);
    }

    public List<Match> filterByCustomer(List<Match> matchListPerDay, Integer customerId) {
        List<Match> matchListByCustomer = new ArrayList<>();
        for (Match match:matchListPerDay
             ) {
            if(match.getPerson().getId().equals(customerId)) {
                matchListByCustomer.add(match);
            }
        }
        return matchListByCustomer;
    }

    public List<Match> getIncomeOutcome(Date day, Integer customerId) {
        List<Match> matchListPerDay = getMatchesByDate(day, day);
        List<Match> matchListByCustomer = filterByCustomer(matchListPerDay, customerId);
        Integer matchIdIn = 0;
        Integer matchIdOut = 0;
        for (Match match:matchListByCustomer
             ) {
                if (matchIdIn.equals(0)) {
                    matchIdIn = match.getId();
                }
                if (matchIdOut.equals(0)) {
                    matchIdOut = match.getId();
                }
                if (matchDao.findById(matchIdIn).get().getHour().compareTo(match.getHour()) > 0) {
                    matchIdIn = match.getId();
                }
                if (matchDao.findById(matchIdOut).get().getHour().compareTo(match.getHour()) < 0) {
                    matchIdOut = match.getId();
                }
        }
        List<Match> matchList = new ArrayList<>();
        if(matchDao.findById(matchIdIn).isPresent())
        {
            Match matchIn = matchDao.findById(matchIdIn).get();
            matchList.add(matchIn);
        }
        if(matchDao.findById(matchIdOut).isPresent())
        {
            Match matchOut = matchDao.findById(matchIdOut).get();
            matchList.add(matchOut);
        }
        return matchList;
    }

    public List<Match> getAllByContact(Integer contactId) {
        if(contactDao.findById(contactId).isPresent()) {
            return matchDao.findAllByContact(contactDao.findById(contactId).get());
        }
        return new ArrayList<>();
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public List<Map<Object, Object>> getMatchesByDateWithRandomLocation(Date firstDate, Date secondDate) {
        List<Match> matchList = getMatchesByDate(firstDate, secondDate);
        List<Map<Object, Object>> matches = new ArrayList<>();
        for (Match match:matchList
             ){
            Map<Object, Object> matchInHash = new HashMap<>();
            matchInHash.put("match", match);
            matchInHash.put("top", getRandomNumber(70, 85));
            matchInHash.put("left", getRandomNumber(33, 50));
            matches.add(matchInHash);
        }
        return matches;
    }
}