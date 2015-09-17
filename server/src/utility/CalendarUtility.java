package utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CalendarUtility {

    public static HashMap<Integer,String> calendar;


    public static void loadCalendar() {
        calendar=new HashMap<Integer, String>();
        calendar.put(1, "Gennaio");
        calendar.put(2, "Febbraio");
        calendar.put(3, "Marzo");
        calendar.put(4, "Aprile");
        calendar.put(5, "Maggio");
        calendar.put(6, "Giugno");
        calendar.put(7, "Luglio");
        calendar.put(8, "Agosto");
        calendar.put(9, "Settembre");
        calendar.put(10, "Ottobre");
        calendar.put(11, "Novembre");
        calendar.put(12, "Dicembre");
    }

    public String getCurrentData(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return (dateFormat.format(date));
    }

    public static HashMap<Integer,String> getCalendar(){
        if (calendar==null)
            loadCalendar();

        return calendar;
    }

}
