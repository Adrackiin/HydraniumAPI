package fr.adrackiin.hydranium.api.utils.statics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

public class MathUtils {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int getMaxInt(Collection<Integer> ints){
        int max = 0;
        for(int integer : ints){
            if(integer > max){
                max = integer;
            }
        }
        return max;
    }

    public static float getValue(String value){
        return Float.parseFloat(value);
    }

    public static float surround(float toSurround, float min, float max){
        if(toSurround < min){
            return min;
        } else if(toSurround > max){
            return max;
        }
        return toSurround;
    }

    public static boolean isInteger(float modifier){
        return ((int)modifier == modifier);
    }


}
