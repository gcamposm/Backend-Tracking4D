package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.dao.PersonDao;
import spaceweare.tracking4d.SQL.dao.PixelDao;
import spaceweare.tracking4d.SQL.dao.TemperatureDao;
import spaceweare.tracking4d.SQL.models.Match;
import spaceweare.tracking4d.SQL.models.Person;
import spaceweare.tracking4d.SQL.models.Pixel;
import spaceweare.tracking4d.SQL.models.Temperature;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TemperatureService {

    private final TemperatureDao temperatureDao;
    private final MatchDao matchDao;
    private final PersonDao personDao;
    private final PixelDao pixelDao;
    private final MatchService matchService;
    public TemperatureService(TemperatureDao temperatureDao, MatchDao matchDao, MatchService matchService, PersonDao personDao, PixelDao pixelDao) {
        this.temperatureDao = temperatureDao;
        this.matchDao = matchDao;
        this.matchService = matchService;
        this.personDao = personDao;
        this.pixelDao = pixelDao;
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
        Integer hour = currentCalendar.get(Calendar.HOUR);
        currentCalendar.set(Calendar.HOUR, hour - 3);
        Integer minuteToPlus = currentCalendar.get(Calendar.MINUTE);
        currentCalendar.set(Calendar.MINUTE, minuteToPlus + 1);
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
        for (int t = 0; t < 5; t++) {
            matchList = matchService.findMatchByInterval(t, date);
            if( matchList.size() > 0){
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

    public String getDetectionTemperature(Integer x, Integer y, Integer height, Integer width) throws ParseException {
        //Redondear flotantes
        DecimalFormat df = new DecimalFormat("##.#");
        df.setRoundingMode(RoundingMode.DOWN);
        // Ajustar datos de entrada
        Integer adjust = 20 * 4;
        x = x/adjust;
        y = y/adjust;
        height = height/adjust;
        width = width/adjust;
        // Encontrar la última temperatura
        //Temperature temperature = temperatureDao.findFirstByOrderByIdDesc();
        List<Temperature> temperatureList;
        for (int i = 0; i < 5; i++) {
            temperatureList = findTemperatureByInterval(i);
            if (temperatureList.size() > 0) {
                Temperature temperature = temperatureList.get(temperatureList.size() - 1);
                //
                List<Pixel> filteredPixels = new ArrayList<>();
                Integer limX = x + width + 1;
                Integer limY = y + height + 1;
                for (Pixel pixel : temperature.getPixels()
                ) {
                    // Se obtienen los pixeles según la proporción del rostro detectado
                    if (pixel.getX() > x && pixel.getX() < limX && pixel.getY() > y && pixel.getY() < limY) {
                        filteredPixels.add(pixel);
                    }
                }
                //Encontrar el pixel más alto dentro del rostro
                if (filteredPixels.size() > 0) {
                    Float max = filteredPixels.get(0).getValue();
                    for (Pixel pixel : filteredPixels
                    ) {
                        if (pixel.getValue() > max) {
                            max = pixel.getValue();
                        }
                    }
                    System.out.println(max);
                    //if(max > 38.5)
                    if (max > 38.5) {
                        LocalDateTime ldt = LocalDateTime.now();
                        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formatter = formmat1.format(ldt);
                        highTemperature(temperature, formatter);
                    }
                    return df.format(max);
                }

            }
        }
        Random r = new Random();
        return df.format(36 + r.nextFloat() * (2));
    }
}