package com.argusoft.medplat.common.util;

import com.argusoft.medplat.config.Application;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An util class to generate mapper
 * @author hshah
 * @since 31/08/2020 4:30
 */
public class MapperGenerator {

    private MapperGenerator() {
            
    }

    /**
     * Generated mapping from given from class to given to class
     * @param fromClass A value of from class
     * @param toClass A value of to class
     * @param notFoundList A not found list
     * @return A mapped string
     */
    public static String getMapping(final Class<?> fromClass, final Class<?> toClass, final List<String> notFoundList) {
        String fromName = fromClass.getSimpleName();
        String toName = toClass.getSimpleName();
        final StringBuilder sb = new StringBuilder(
                "public static " + toName + " convert" + fromName + "To" + toName + " ( " + fromName + " from) { \n"
                + toName + " to=new " + toName + "();\n");
        ReflectionUtils.doWithFields(toClass,
                field -> {

                    try {

                        String set = new PropertyDescriptor(field.getName(), toClass).getWriteMethod().getName();
                        try {
                            String name = new PropertyDescriptor(field.getName(), fromClass).getReadMethod().getName();
                            sb.append("to.")
                            .append(set)
                            .append("(from.")
                            .append(name)
                            .append("());\n");

                        } catch (IntrospectionException ex) {

                            if (field.getType().equals(Boolean.class)) {
                                handleCatch(field,toClass,sb,set,notFoundList,ex);
                            } else {
                                sb.append("to.")
                                .append(set)
                                .append("(from.get")
                                .append("());\n");
                                notFoundList.add(field.getName());

                            }
                        }
                    } catch (IntrospectionException ex) {
                        Logger.getLogger(MapperGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                },
                field -> {
                    final int modifiers = field.getModifiers();
                    // no static fields please
                    return !Modifier.isStatic(modifiers);
                });
        sb.append("return to;\n}");
        return sb.toString();
    }

    /**
     * Handle catch details.
     * @param field Define field.
     * @param toClass To class.
     * @param sb String builder.
     * @param set Set details.
     * @param notFoundList List of not found details.
     * @param ex Exception.
     */
    private static void handleCatch(Field field, final Class<?> toClass, StringBuilder sb, String set,
                                    final List<String> notFoundList, IntrospectionException ex){
        try {
            String name = field.getName();
            String first = "" + name.charAt(0);
            name = name.replaceFirst(first, first.toUpperCase());
            Method method = toClass.getMethod("get" + name);
            sb.append("to.")
                    .append(set)
                    .append("(from.")
                    .append(method.getName())
                    .append("());\n");

        } catch (NoSuchMethodException ex1) {
            notFoundList.add(field.getName());

        } catch (SecurityException ex1) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
