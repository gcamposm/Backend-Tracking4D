package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.dao.TemperatureDao;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.models.Temperature;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TemperatureService {

    private final TemperatureDao temperatureDao;
    private final MatchDao matchDao;
    private final MatchService matchService;
    public TemperatureService(TemperatureDao temperatureDao, MatchDao matchDao, MatchService matchService) {
        this.temperatureDao = temperatureDao;
        this.matchDao = matchDao;
        this.matchService = matchService;
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

    public Match highTemperature(Temperature temperature) {
        List<Match> matchList;
        System.out.println("here 5");
        for (int i = 5; i > 0; i--) {
            matchList = matchService.findMatchByInterval(i);
            if( matchList.size() == 1){
                Match match = matchList.get(0);
                match.setTemperature(temperature);
                return matchDao.save(match);
            }
        }
        System.out.println("here 9");
        return null;
    }
}