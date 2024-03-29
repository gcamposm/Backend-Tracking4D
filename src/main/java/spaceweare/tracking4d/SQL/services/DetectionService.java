package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Helpers.DateHelper;
import spaceweare.tracking4d.SQL.dao.*;
import spaceweare.tracking4d.SQL.dto.models.DetectionDayStat;
import spaceweare.tracking4d.SQL.models.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DetectionService {

    private final PersonDao personDao;
    private final ImageDao imageDao;
    private final DetectionDao detectionDao;
    private final CameraDao cameraDao;
    private final MatchDao matchDao;
    public DetectionService(DetectionDao detectionDao, PersonDao personDao, ImageDao imageDao, CameraDao cameraDao, MatchDao matchDao) {
        this.detectionDao = detectionDao;
        this.personDao = personDao;
        this.imageDao = imageDao;
        this.cameraDao = cameraDao;
        this.matchDao = matchDao;
    }

    public Detection create(Detection detection){
        return detectionDao.save(detection);
    }

    public Detection readById(Integer id){
        if(detectionDao.findById(id).isPresent()){
            return detectionDao.findById(id).get();
        }
        else{
            return  null;
        }
    }

    public List<Detection> readAll(){
        return detectionDao.findAll();
    }

    public Detection update(Detection detection, Integer id){
        if(detectionDao.findById(id).isPresent()){
            Detection detectionFound = detectionDao.findById(id).get();
            detectionFound.setImage(detection.getImage());
            detectionFound.setValue(detection.getValue());
            detectionFound.setClock(detection.getClock());
            return detectionDao.save(detectionFound);
        }
        return null;
    }

    public String delete(Integer id){
        if(detectionDao.findById(id).isPresent()){
            detectionDao.delete(detectionDao.findById(id).get());
            return "deleted";
        }
        else {
            return null;
        }
    }

    public Image saveUnknown(List<Float> unknown, Integer cameraId) {
        Camera camera = cameraDao.findById(cameraId).get();
        Person person = new Person();
        person.setUnknown(true);
        personDao.save(person);
        person.setFirstName("unknown "+ person.getId().toString());
        Image image = new Image();
        image.setPerson(person);
        imageDao.save(image);
        for (Float descriptorFor: unknown
        ) {
            Detection detection = new Detection();
            detection.setImage(image);
            detection.setValue(descriptorFor);
            detection.setClock(LocalDateTime.now());
            detectionDao.save(detection);
        }
        List<Image> cameraImages = camera.getImages();
        cameraImages.add(image);
        camera.setImages(cameraImages);
        cameraDao.save(camera);
        return imageDao.save(image);
    }

    public List<DetectionDayStat> getVisitsBetweenDates(Date firstDate, Date secondDate){
        secondDate = new Date(secondDate.getTime() + TimeUnit.DAYS.toMillis( 1 ));
        List<LocalDateTime> localDateTimeList = DateHelper.intervalDateToList(firstDate, secondDate, 0);
        List<DetectionDayStat> detectionDayStatList = new ArrayList<>();
        for (LocalDateTime localDateTime: localDateTimeList
        ) {
            DetectionDayStat detectionDayStat = getStat(localDateTime);
            detectionDayStatList.add(detectionDayStat);
        }
        return detectionDayStatList;
    }
    private DetectionDayStat getStat(LocalDateTime date){

        LocalDateTime datePlus1Day = date.plusDays(1);
        List<Detection> detectionList = detectionDao.findDetectionByclockBetween(date, datePlus1Day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatDateTime = date.format(formatter);
        DetectionDayStat detectionDayStat = getTotalByDay(detectionList.size(), matchDao.findMatchByHourBetween(date, datePlus1Day).size());
        detectionDayStat.setDay(date);
        detectionDayStat.setFormattedDate(formatDateTime);
        return detectionDayStat;

    }
    private DetectionDayStat getTotalByDay(Integer unknowns, Integer matches){
        DetectionDayStat detectionDayStat = new DetectionDayStat();
        detectionDayStat.setUnknown(unknowns);
        detectionDayStat.setMatches(matches);
        detectionDayStat.setTotal(unknowns + matches);
        return detectionDayStat;
    }
}