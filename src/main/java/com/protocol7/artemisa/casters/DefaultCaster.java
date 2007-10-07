package com.protocol7.artemisa.casters;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


// demuxer?
public class DefaultCaster implements Caster {

    public static class PrimitivesCaster implements Caster {

        public Object cast(Class<?> clazz, Object value) {
            String stringValue = (String) value;
            Object castValue;

            if(clazz == String.class) {
                return stringValue;
            } else if (clazz == Boolean.TYPE || clazz == Boolean.class) {
                castValue = new Boolean(stringValue);
            } else if (clazz == Byte.TYPE || clazz == Byte.class) {
                castValue = new Byte(stringValue);
            } else if (
                (clazz == Character.TYPE || clazz == Character.class)
                    && stringValue.length() == 1) {
                castValue = new Character(stringValue.charAt(0));
            } else if (clazz == Double.TYPE || clazz == Double.class) {
                castValue = new Double(stringValue);
            } else if (clazz == Float.TYPE || clazz == Float.class) {
                castValue = new Float(stringValue);
            } else if (clazz == Integer.TYPE || clazz == Integer.class) {
                castValue = new Integer(stringValue);
            } else if (clazz == Long.TYPE || clazz == Long.class) {
                castValue = new Long(stringValue);
            } else if (clazz == Short.TYPE || clazz == Short.class) {
                castValue = new Short(stringValue);
            } else if (clazz == BigDecimal.class) {
                castValue = new BigDecimal(stringValue);
            } else if (clazz == BigInteger.class) {
                castValue = new BigInteger(stringValue);
            } else {
                throw new IllegalArgumentException("Not a primitive type");
            }
            
            return castValue;
        }
        
    }

    public static class ListCaster implements Caster {

        public Object cast(Class<?> clazz, Object string) {
            String stringValue = (String) string;
            
            List<String> list = new ArrayList<String>();
            String[] values = stringValue.split(",");
            
            for (int i = 0; i < values.length; i++) {
                list.add(values[i].trim());
            }
            
            return list;
        }
    }

    public static class FileCaster implements Caster {
        public Object cast(Class<?> clazz, Object string) {
            return new File((String) string);
        }
    }
    
    public static class DateCaster implements Caster {
        
        private DateFormat format = new ISODateFormat();
        
        public DateCaster() {
            // default
        } 
        
        public DateCaster(DateFormat format) {
            this.format = format;
        }
        
        public Object cast(Class<?> clazz, Object string) {
            try {
                
                if(clazz == Date.class) {
                    return format.parse((String)string);
                } else if(clazz == Calendar.class) {
                    if(format instanceof ISODateFormat) {
                        return ((ISODateFormat)format).parseCalendar((String)string);
                    } else {
                        Date date = format.parse((String)string);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        return calendar;
                    }
                } else {
                    throw new IllegalArgumentException("Not a date or calendar");
                }

            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date");
            }
        }
    }
    
    public static class InetAddressCaster implements Caster {
        public Object cast(Class<?> clazz, Object string) {
            try {
                return InetAddress.getByName((String)string);
            } catch (UnknownHostException e) {
                throw new RuntimeException("Unknown host: " + string, e);
            }
        }
    }

    public static class URLCaster implements Caster {
        public Object cast(Class<?> clazz, Object string) {
            try {
                if(clazz == URL.class) {
                    return new URL((String)string);
                } else if(clazz == URI.class) {
                    return new URI((String)string);
                } else {
                    throw new IllegalArgumentException("Not a URI or URL");
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException("Malformed URL: " + string, e);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Malformed URL: " + string, e);
            }
        }
    }
    
    public static class ArrayCaster implements Caster {

        public Object cast(Class<?> clazz, Object string) {
            String stringValue = (String) string;
            
            if(clazz.isArray()) {
                String[] values = stringValue.split(",");
                Object castArray = Array.newInstance(clazz.getComponentType(), values.length);
                
                for (int i = 0; i < values.length; i++) {
                    Array.set(castArray, i, cast(clazz.getComponentType(), values[i].trim()));
                } 
                
                return castArray;        
            } else {
                throw new IllegalArgumentException("Not an array");
            }
        }
    }
    
    public static class ArrayType {};
    
    private final static Caster PRIMITIVES_CASTER = new PrimitivesCaster();
    private final static Caster ARRAY_CASTER = new ArrayCaster();
    private final static Caster LIST_CASTER = new ListCaster();
    private final static Caster FILE_CASTER = new FileCaster();
    private final static Caster URL_CASTER = new URLCaster();
    private final static Caster INETADDRESS_CASTER = new InetAddressCaster();
    private final static Caster DATE_CASTER = new DateCaster();
    private final static Map<Class<?>, Caster> DEFAULT_CASTERS = new HashMap<Class<?>, Caster>();
    static {
        DEFAULT_CASTERS.put(String.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Boolean.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Boolean.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Byte.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Byte.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Character.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Character.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Double.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Double.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Float.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Float.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Integer.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Integer.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Long.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Long.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Short.TYPE, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(Short.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(BigDecimal.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(BigInteger.class, PRIMITIVES_CASTER);
        DEFAULT_CASTERS.put(ArrayType.class, ARRAY_CASTER);
        DEFAULT_CASTERS.put(List.class, LIST_CASTER);
        DEFAULT_CASTERS.put(File.class, FILE_CASTER);
        DEFAULT_CASTERS.put(URL.class, URL_CASTER);
        DEFAULT_CASTERS.put(URI.class, URL_CASTER);
        DEFAULT_CASTERS.put(InetAddress.class, INETADDRESS_CASTER);
        DEFAULT_CASTERS.put(Date.class, DATE_CASTER);
        DEFAULT_CASTERS.put(Calendar.class, DATE_CASTER);
    }
    
    
    private Map<Class<?>, Caster> casters = DEFAULT_CASTERS;
    
    public DefaultCaster() {
    }

    public DefaultCaster(Map<Class<?>, Caster> casters) {
        for (Entry<Class<?>, Caster> entry : casters.entrySet()) {
            casters.put(entry.getKey(), entry.getValue());
        }
    }
    
    public Object cast(Class<?> clazz, Object value) {
        if(value instanceof String) {
            Class<?> lookupClass;
            if(clazz.isArray()) {
                lookupClass = ArrayType.class;
            } else {
                lookupClass = clazz;
            }
            
            Caster caster = casters.get(lookupClass);
            
            if(caster != null) {
                return caster.cast(clazz, value);
            } else {
                throw new RuntimeException("Unable to cast \""+value+"\" as a "+clazz.getName());
            }
        } else {
            return value;
        }
    }
}
