package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.dao.PixelDao;
import spaceweare.tracking4d.SQL.dao.TemperatureDao;
import spaceweare.tracking4d.SQL.models.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PixelService {

    private final PixelDao pixelDao;
    private final MatchDao matchDao;
    private final TemperatureDao temperatureDao;
    private final TemperatureService temperatureService;
    public PixelService(PixelDao pixelDao, MatchDao matchDao, TemperatureDao temperatureDao, TemperatureService temperatureService) {
        this.pixelDao = pixelDao;
        this.matchDao = matchDao;
        this.temperatureDao = temperatureDao;
        this.temperatureService = temperatureService;
    }

    public Pixel create(Pixel pixel){
        return pixelDao.save(pixel);
    }

    public Pixel readById(Integer id){
        if(pixelDao.findById(id).isPresent()){
            return pixelDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Pixel> readAll(){
        return pixelDao.findAll();
    }

    public Pixel update(Pixel pixel, Integer id){
        if(pixelDao.findById(id).isPresent()){
            Pixel pixelFound = pixelDao.findById(id).get();
            pixelFound.setValue(pixel.getValue());
            pixelFound.setTemperature(pixel.getTemperature());
            return pixelDao.save(pixelFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(pixelDao.findById(id).isPresent()){
            pixelDao.delete(pixelDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }

    public Object saveTemperature(List<Float> pixels, String date) {
        // Cambiar de string a localdatetime
        String[] parts = date.split("\\.");
        date = parts[0];
        System.out.println(date);
        String str = "1986-04-08 12:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        //System.out.println(localDate.toString());
        //Definir la temperatura
        System.out.println("here 2");
        Temperature temperature = new Temperature();
        temperature.setDetectedHour(localDate);
        temperatureDao.save(temperature);
        System.out.println("here 3");
        //Encontrar el match correspondiente
        Match match = temperatureService.highTemperature(temperature);
        if(match != null)
        {
            System.out.println("here 4");
            for (Float value: pixels
            ) {
                Pixel pixel = new Pixel();
                pixel.setTemperature(temperature);
                pixel.setValue(value);
            }
            // Se maneja el retorno
            Map<Object, Object> json = new HashMap<>();
            json.put("match", match);
            json.put("temperature", temperature);
            return json;
        }
        else{
            return null;
        }
    }
}