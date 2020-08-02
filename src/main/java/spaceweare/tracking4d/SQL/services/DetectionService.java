package spaceweare.tracking4d.SQL.services;

import org.springframework.stereotype.Service;
import spaceweare.tracking4d.Helpers.DateHelper;
import spaceweare.tracking4d.SQL.dao.CustomerDao;
import spaceweare.tracking4d.SQL.dao.DetectionDao;
import spaceweare.tracking4d.SQL.dao.ImageDao;
import spaceweare.tracking4d.SQL.dto.models.DetectionDayStat;
import spaceweare.tracking4d.SQL.models.Customer;
import spaceweare.tracking4d.SQL.models.Detection;
import spaceweare.tracking4d.SQL.models.Image;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DetectionService {

    private final CustomerDao customerDao;
    private final ImageDao imageDao;
    private final DetectionDao detectionDao;
    public DetectionService(DetectionDao detectionDao, CustomerDao customerDao, ImageDao imageDao) {
        this.detectionDao = detectionDao;
        this.customerDao = customerDao;
        this.imageDao = imageDao;
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

    public Image saveUnknown(List<String> unknown) {
        Customer customer = new Customer();
        customerDao.save(customer);
        customer.setFirstName("unknown "+customer.getId().toString());
        Image image = new Image();
        image.setCustomer(customer);
        imageDao.save(image);
        for (String descriptorFor: unknown
        ) {
            Detection detection = new Detection();
            detection.setImage(image);
            detection.setValue(descriptorFor);
            detectionDao.save(detection);
        }
        return imageDao.save(image);
    }

    public List<DetectionDayStat> getVisitsBetweenDates(Date firstDate, Date secondDate){
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
        System.out.println("Date: " + date);
        System.out.println("Date + 1: " + datePlus1Day);
        List<Detection> detectionList = detectionDao.findDetectionByclockBetween(date, datePlus1Day);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatDateTime = date.format(formatter);
        System.out.println("---------------------------");
        DetectionDayStat detectionDayStat = getTotalByDay(detectionList);
        detectionDayStat.setDay(date);
        detectionDayStat.setFormattedDate(formatDateTime);
        System.out.println(detectionDayStat);
        System.out.println("---------------------------");
        return detectionDayStat;

    }
    private DetectionDayStat getTotalByDay(List<Detection> detections){
        Integer unknowns = 0;
        Integer matches = 0;
        for (Detection detection: detections
        ) {
            if(detection.getImage().getCustomer().getUnknown()) {
                unknowns = unknowns + 1;
            }
            else{
                matches = matches + 1;
            }
        };
        DetectionDayStat detectionDayStat = new DetectionDayStat();
        detectionDayStat.setUnknown(unknowns);
        detectionDayStat.setMatches(matches);
        detectionDayStat.setTotal(unknowns + matches);
        return detectionDayStat;
    }
}