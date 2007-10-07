/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */  

package com.protocol7.artemisa;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import com.protocol7.artemisa.casters.Caster;



public class ClassUtils {

    public static Class<?> getPropertyType(Object target, String propertyName) {
        PropertyDescriptor setter = getDescriptor(target.getClass(), propertyName);
        
        return setter.getPropertyType();
    }

    public static void setProperty(Object target, String propertyName, Object propertyValue, Caster caster) {
        PropertyDescriptor setter = getDescriptor(target.getClass(), propertyName);
        
        if(setter == null) {
            return;
        }

        Object castValue = caster.cast(setter.getPropertyType(), propertyValue);
        
        setPropertyInternal(target, setter, castValue);
    }
    
    private static void setPropertyInternal(Object target, PropertyDescriptor setter, Object castValue) {
        Method setterMethod = setter.getWriteMethod();
        
        if(setter != null && setterMethod != null) {
            try {
                setterMethod.invoke(target, new Object[]{castValue});
            } catch (Exception e) {
                throw new RuntimeException("Failed invoking setter " + setter.getDisplayName() + " on " + target, e);
            }
        } else {
            throw new RuntimeException("Property \"" + setter.getDisplayName() + "\" is not settable on class "+ target.getClass());
        }
        
    }
    
    public static String normalizePropertyName(String propertyName){
        StringTokenizer st = new StringTokenizer(propertyName, "-");
        
        if(st.countTokens() > 1) {
            StringBuffer sb = new StringBuffer();
            
            // add first unchanged
            sb.append(st.nextToken());
            
            while(st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                
                if(token.length() > 0) {
                    sb.append(Character.toUpperCase(token.charAt(0)));
                    sb.append(token.substring(1));
                }
            }
            
            return sb.toString();
        } else {
            return propertyName;
        }
        
    }
    
    private static PropertyDescriptor getDescriptor(Class<?> clazz, String propertyName) {
        propertyName = normalizePropertyName(propertyName);
        
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Failed to introspect class: " + clazz);
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if(propertyDescriptor.getName().equals(propertyName)) {
                return propertyDescriptor;
            }
        }
        
        return null;
    }
}
