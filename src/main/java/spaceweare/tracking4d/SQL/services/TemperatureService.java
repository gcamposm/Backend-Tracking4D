package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.TemperatureDao;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.Temperature;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TemperatureService {

    private final TemperatureDao temperatureDao;
    private final MatchDao matchDao;
    private final PersonDao personDao;
    private final MatchService matchService;
    public TemperatureService(TemperatureDao temperatureDao, MatchDao matchDao, MatchService matchService, PersonDao personDao) {
        this.temperatureDao = temperatureDao;
        this.matchDao = matchDao;
        this.matchService = matchService;
        this.personDao = personDao;
    }

    public Temperature create(Temperature temperature){
        return temperatureDao.save(temperature);
    }

    public Temperature readById(Integer id){
        if(temperatureDao.findById(id).isPresent()){
            return temperatureDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Temperature> readAll(){
        return temperatureDao.findAll();
    }

    public Temperature update(Temperature temperature, Integer id){
        if(temperatureDao.findById(id).isPresent()){
            Temperature temperatureFound = temperatureDao.findById(id).get();
            temperatureFound.setValue(temperature.getValue());
            temperatureFound.setDetectedHour(temperature.getDetectedHour());
            temperatureFound.setMatchList(temperature.getMatchList());
            return temperatureDao.save(temperatureFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(temperatureDao.findById(id).isPresent()){
            temperatureDao.delete(temperatureDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }

    public List<Temperature> findTemperatureByInterval(Integer interval){
        Calendar currentCalendar = Calendar.getInstance();
        Date secondCurrentDate = currentCalendar.getTime();
        Integer minute = currentCalendar.get(Calendar.MINUTE);
        currentCalendar.set(Calendar.MINUTE, minute - interval);
        Date firstCurrentDate = currentCalendar.getTime();

        Instant firstCurrentInstant = firstCurrentDate.toInstant();
        Instant secondCurrentInstant = secondCurrentDate.toInstant();
        LocalDateTime firstCurrentLocal = LocalDateTime.ofInstant(firstCurrentInstant,
                ZoneId.systemDefault());
        LocalDateTime secondCurrentLocal = LocalDateTime.ofInstant(secondCurrentInstant,
                ZoneId.systemDefault());
        return temperatureDao.findTemperatureByDetectedHourBetween(firstCurrentLocal, secondCurrentLocal);
    }

    public Match highTemperature(Temperature temperature, String date) throws ParseException {
        List<Match> matchList;
        for (int i = 1; i < 5; i++) {
            matchList = matchService.findMatchByInterval(i, date);
            if( matchList.size() == 1){
                Match match = matchList.get(0);
                match.setHighTemperature(true);
                match.setTemperature(temperature);
                Person person = match.getPerson();
                person.setCovid(true);
                person.setNewAlert(true);
                personDao.save(person);
                return matchDao.save(match);
            }
        }
        return null;
    }
}