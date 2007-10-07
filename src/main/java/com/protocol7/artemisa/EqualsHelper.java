package com.protocol7.artemisa;

public class EqualsHelper {

    public static boolean equalsOrBothNull(Object o1, Object o2) {
        if(o1 == null && o2 == null) {
            return true;
        } else if(o1 != null) {
            return o1.equals(o2);
        } else {
            return false;
        }
    }
}
