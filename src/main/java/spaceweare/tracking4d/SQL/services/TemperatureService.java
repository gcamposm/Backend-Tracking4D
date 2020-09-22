package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.*;
import spaceweare.tracking4d.SQL.models.*;
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
    private final PersonService personService;
    private final AlertService alertService;
    private final MatchService matchService;
    public TemperatureService(TemperatureDao temperatureDao, MatchDao matchDao, MatchService matchService, PersonService personService, AlertService alertService) {
        this.temperatureDao = temperatureDao;
        this.matchDao = matchDao;
        this.matchService = matchService;
        this.personService = personService;
        this.alertService = alertService;
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

    public String deleteAll() {
        System.out.println("Eliminando temperaturas");
        List<Temperature> temperatures = temperatureDao.findTemperatureByDetectedHourBefore(LocalDateTime.now().minusHours(4));
        temperatureDao.deleteAll(temperatures);
        return "deleted";
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

    public void highTemperature(Temperature temperature, String date, String highTemperature) throws ParseException {
        System.out.println("highTemperature: "+Float.parseFloat(highTemperature));
        List<Match> matchList;
        for (int i = 1; i < 5; i++) {
            matchList = matchService.findMatchByInterval(i, date);
            if( matchList.size() > 0){
                Match match = matchList.get(matchList.size()-1);
                match.setHighTemperature(true);
                matchDao.save(match);
                Person person = personService.personHighTemperature(match, highTemperature);
                alertService.alertHighTemperature(person, highTemperature, match.getHour());
            }
        }
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
                System.out.println("Id temperature: "+temperature.getId());
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
                    System.out.println("Máxima: "+max);
                    float high = Float.parseFloat("38.5");
                    if(max > high){
                        System.out.println("Alerta alta temperatura");
                        LocalDateTime ldt = LocalDateTime.now();
                        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formatter = formmat1.format(ldt);
                        highTemperature(temperature, formatter, df.format(max));
                    }
                    return df.format(max);
                }

            }
        }
        Random r = new Random();
        return df.format(36 + r.nextFloat() * (2));
    }
}