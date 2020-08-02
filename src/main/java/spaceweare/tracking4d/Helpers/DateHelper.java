package spaceweare.tracking4d.Helpers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateHelper {

    public static List<LocalDateTime> intervalDateToList(Date firstDate, Date secondDate, Integer plusDays){
        Instant firstCurrent = firstDate.toInstant();
        Instant secondCurrent = secondDate.toInstant();
        LocalDateTime firstLocalDate = LocalDateTime.ofInstant(firstCurrent,
                ZoneId.systemDefault());
        LocalDateTime secondLocalDate = LocalDateTime.ofInstant(secondCurrent,
                ZoneId.systemDefault()).plusDays(plusDays);

        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        //Add localdatetimes to the vector
        while(firstLocalDate.isBefore(secondLocalDate)){
            localDateTimeList.add(firstLocalDate);
            firstLocalDate = firstLocalDate.plusDays(1);
        }
        return localDateTimeList;
    }

}
