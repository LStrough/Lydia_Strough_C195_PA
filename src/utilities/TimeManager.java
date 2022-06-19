package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeManager {
    public static ObservableList<LocalTime> businessHourInit(int start) {
        ObservableList<LocalTime> timeList = FXCollections.observableArrayList();

        for(int i = start; i < 24; i++) {
            timeList.add(LocalTime.of(i, 0));
        }
        return timeList;
    }

    public static ObservableList<LocalTime> dynamicBusinessHoursInit(ZoneId osZId, ZoneId businessZId, LocalTime businessHourStart, int workHours) {
        ObservableList<LocalTime> timeList = FXCollections.observableArrayList();
        ZonedDateTime businessZDT = ZonedDateTime.of(LocalDate.now(), businessHourStart, businessZId);
        ZonedDateTime localZDT = ZonedDateTime.ofInstant(businessZDT.toInstant(),osZId);
        int localStartingHour = localZDT.getHour();
        int totalHours = localStartingHour + workHours;
        int midnightOrGreater = 0;
        for(int i = localStartingHour; i <= totalHours; i++) {
            if(i<24) {
                //System.out.println(i);
                timeList.add(LocalTime.of(i,0));
            }
            if(i>23) {
                timeList.add(LocalTime.of(midnightOrGreater,0));
                midnightOrGreater += 1;
            }
        }
        for(LocalTime lt : timeList)
            System.out.println(lt);
        return null;
    }
}
