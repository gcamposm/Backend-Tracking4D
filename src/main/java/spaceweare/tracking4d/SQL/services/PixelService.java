package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.SQL.dao.MatchDao;
import spaceweare.tracking4d.SQL.dao.PixelDao;
import spaceweare.tracking4d.SQL.dao.TemperatureDao;
import spaceweare.tracking4d.SQL.models.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public Pixel create(Pixel pixel) {
        return pixelDao.save(pixel);
    }

    public Pixel readById(Integer id) {
        if (pixelDao.findById(id).isPresent()) {
            return pixelDao.findById(id).get();
        } else {
            return null;
        }
    }

    public List<Pixel> readAll() {
        return pixelDao.findAll();
    }

    public Pixel update(Pixel pixel, Integer id) {
        if (pixelDao.findById(id).isPresent()) {
            Pixel pixelFound = pixelDao.findById(id).get();
            pixelFound.setValue(pixel.getValue());
            pixelFound.setTemperature(pixel.getTemperature());
            return pixelDao.save(pixelFound);
        }
        return null;
    }

    public String delete(Integer id) {
        if (pixelDao.findById(id).isPresent()) {
            pixelDao.delete(pixelDao.findById(id).get());
            return "deleted";
        } else {
            return null;
        }
    }

    public String deleteAll() {
        pixelDao.deleteAll();
        return "deleted";
    }

    public Object saveTemperature(List<Float> pixels, String date) throws ParseException {
        // Cambiar de string a localdatetime
        String[] parts = date.split("\\.");
        date = parts[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        //Inicializar la temperatura
        Temperature temperature = new Temperature();
        temperature.setDetectedHour(localDate);
        temperatureDao.save(temperature);
        //Encontrar el match correspondiente
        Integer x = 0;
        Integer y = 0;
        //Reducir el número de pixeles
        List<Float> filteredPixels = new ArrayList<>();
        for (int i = 0; i < 32; i = i + 4) {
            for (int j = 0; j < 24; j = j + 4) {
                Pixel pixel = new Pixel();
                pixel.setX(x);
                pixel.setY(y);
                pixel.setTemperature(temperature);
                // Manejo de ubicaciones
                filteredPixels.add(pixels.get(((i * 24) + 0) + (j + 0)));
                filteredPixels.add(pixels.get(((i * 24) + 1) + (j + 0)));
                filteredPixels.add(pixels.get(((i * 24) + 2) + (j + 0)));
                filteredPixels.add(pixels.get(((i * 24) + 3) + (j + 0)));
                filteredPixels.add(pixels.get(((i * 24) + 0) + (j + 1)));
                filteredPixels.add(pixels.get(((i * 24) + 1) + (j + 1)));
                filteredPixels.add(pixels.get(((i * 24) + 2) + (j + 1)));
                filteredPixels.add(pixels.get(((i * 24) + 3) + (j + 1)));
                filteredPixels.add(pixels.get(((i * 24) + 0) + (j + 2)));
                filteredPixels.add(pixels.get(((i * 24) + 1) + (j + 2)));
                filteredPixels.add(pixels.get(((i * 24) + 2) + (j + 2)));
                filteredPixels.add(pixels.get(((i * 24) + 3) + (j + 2)));
                filteredPixels.add(pixels.get(((i * 24) + 0) + (j + 3)));
                filteredPixels.add(pixels.get(((i * 24) + 1) + (j + 3)));
                filteredPixels.add(pixels.get(((i * 24) + 2) + (j + 3)));
                filteredPixels.add(pixels.get(((i * 24) + 3) + (j + 3)));

                pixel.setValue(Collections.max(filteredPixels));


                /*Float sum = pixels.get(((i*24)+0)+(j+0)) + pixels.get(((i*24)+1)+(j+0))+ pixels.get(((i*24)+2)+(j+0))+ pixels.get(((i*24)+3)+(j+0))+
                            pixels.get(((i*24)+0)+(j+1)) + pixels.get(((i*24)+1)+(j+1))+ pixels.get(((i*24)+2)+(j+1))+ pixels.get(((i*24)+3)+(j+1))+
                            pixels.get(((i*24)+0)+(j+2)) + pixels.get(((i*24)+1)+(j+2))+ pixels.get(((i*24)+2)+(j+2))+ pixels.get(((i*24)+3)+(j+2))+
                            pixels.get(((i*24)+0)+(j+3)) + pixels.get(((i*24)+1)+(j+3))+ pixels.get(((i*24)+2)+(j+3))+ pixels.get(((i*24)+3)+(j+3));
                pixel.setValue(sum/16);
                System.out.println("El valor a guardar es: "+(sum/16));*/
                /*Float sum = pixels.get(((i*24)+0)+(j+0)) + pixels.get(((i*24)+1)+(j+0))+ pixels.get(((i*24)+2)+(j+0))+ pixels.get(((i*24)+3)+(j+0))+ pixels.get(((i*24)+4)+(j+0)) + pixels.get(((i*24)+5)+(j+0))+ pixels.get(((i*24)+6)+(j+0))+ pixels.get(((i*24)+7)+(j+0))+
                        pixels.get(((i*24)+0)+(j+1)) + pixels.get(((i*24)+1)+(j+1))+ pixels.get(((i*24)+2)+(j+1))+ pixels.get(((i*24)+3)+(j+1))+ pixels.get(((i*24)+4)+(j+1)) + pixels.get(((i*24)+5)+(j+1))+ pixels.get(((i*24)+6)+(j+1))+ pixels.get(((i*24)+7)+(j+1))+
                        pixels.get(((i*24)+0)+(j+2)) + pixels.get(((i*24)+1)+(j+2))+ pixels.get(((i*24)+2)+(j+2))+ pixels.get(((i*24)+3)+(j+2))+ pixels.get(((i*24)+4)+(j+2)) + pixels.get(((i*24)+5)+(j+2))+ pixels.get(((i*24)+6)+(j+2))+ pixels.get(((i*24)+7)+(j+2))+
                        pixels.get(((i*24)+0)+(j+3)) + pixels.get(((i*24)+1)+(j+3))+ pixels.get(((i*24)+2)+(j+3))+ pixels.get(((i*24)+3)+(j+3))+ pixels.get(((i*24)+4)+(j+3)) + pixels.get(((i*24)+5)+(j+3))+ pixels.get(((i*24)+6)+(j+3))+ pixels.get(((i*24)+7)+(j+3))+
                        pixels.get(((i*24)+0)+(j+4)) + pixels.get(((i*24)+1)+(j+4))+ pixels.get(((i*24)+2)+(j+4))+ pixels.get(((i*24)+3)+(j+4))+ pixels.get(((i*24)+4)+(j+4)) + pixels.get(((i*24)+5)+(j+4))+ pixels.get(((i*24)+6)+(j+4))+ pixels.get(((i*24)+7)+(j+4))+
                        pixels.get(((i*24)+0)+(j+5)) + pixels.get(((i*24)+1)+(j+5))+ pixels.get(((i*24)+2)+(j+5))+ pixels.get(((i*24)+3)+(j+5))+ pixels.get(((i*24)+4)+(j+5)) + pixels.get(((i*24)+5)+(j+5))+ pixels.get(((i*24)+6)+(j+5))+ pixels.get(((i*24)+7)+(j+5))+
                        pixels.get(((i*24)+0)+(j+6)) + pixels.get(((i*24)+1)+(j+6))+ pixels.get(((i*24)+2)+(j+6))+ pixels.get(((i*24)+3)+(j+6))+ pixels.get(((i*24)+4)+(j+6)) + pixels.get(((i*24)+5)+(j+6))+ pixels.get(((i*24)+6)+(j+6))+ pixels.get(((i*24)+7)+(j+6))+
                        pixels.get(((i*24)+0)+(j+7)) + pixels.get(((i*24)+1)+(j+7))+ pixels.get(((i*24)+2)+(j+7))+ pixels.get(((i*24)+3)+(j+7))+ pixels.get(((i*24)+4)+(j+7)) + pixels.get(((i*24)+5)+(j+7))+ pixels.get(((i*24)+6)+(j+7))+ pixels.get(((i*24)+7)+(j+7));
                pixel.setValue(sum/64);*/
                x++;
                if (x == 8) {
                    x = 0;
                    y++;
                }
                pixelDao.save(pixel);
            }
        }
        // Se maneja el retorno
        Map<Object, Object> json = new HashMap<>();
        json.put("temperature", temperature);
        json.put("filteredPixels", filteredPixels);
        return json;
    }
}