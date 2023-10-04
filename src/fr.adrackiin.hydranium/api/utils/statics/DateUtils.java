package fr.adrackiin.hydranium.api.utils.statics;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String[] getDate(long ms){
        Date currentDate = new Date(ms);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
        String dates = dateFormat.format(currentDate);
        String[] date = dates.split("-");
        return new String[]{date[2], date[1], date[0], date[3], date[4]};
    }

    public static String[] changeDateFormat(String date){ //YYYY-MM-DD hh:mm-ss --> Date
        String[] temp = date.split(" ");
        String[] temp1 = temp[0].split("-");
        String[] temp2 = temp[1].split(":");
        return new String[]{temp1[2], temp1[1], temp1[0], temp2[0], temp2[1], temp2[2]};
    }
 
    
}
