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
 * An util class to generate field name constant
 * @author hshah
 * @since 31/08/2020 4:30
 */
public class FieldNameConstantGenerator {

    private static final String FROM_STR="(from.";

    private FieldNameConstantGenerator() {
            
    }

    /**
     * Converts given string to upper case
     * @param s A string to convert
     * @return A converted string
     */
    public static String convert(String s) {
        char[] toCharArray = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        char old = 'Z';
        for (Character c : toCharArray) {
            if (Character.isUpperCase(c) && !Character.isUpperCase(old)) {
                sb.append('_');
            }
            old = c;
            sb.append(c);
        }
        return sb.toString().toUpperCase();
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
                "public static " + toName + " convert" + fromName + "To" + toName
                + " ( " + fromName + " from) { \n"
                + toName + " to=new " + toName + "();\n");
        ReflectionUtils.doWithFields(toClass,
                field -> {

                    try {

                        String set = new PropertyDescriptor(field.getName(), toClass)
                        .getWriteMethod().getName();
                        try {
                            String name = new PropertyDescriptor(field.getName(), fromClass)
                            .getReadMethod().getName();
                            sb.append("to.")
                            .append(set)
                            .append(FROM_STR)
                            .append(name)
                            .append("());\n");

                        } catch (IntrospectionException ex) {

                            if (field.getType().equals(Boolean.class)) {
                                handleCatch(field,fromClass,sb,set,notFoundList,ex);
                            } else {
                                sb.append("// to.")
                                .append(set)
                                .append(FROM_STR)
                                .append(");\n");
                                notFoundList.add(field.getName());

                            }
                        }
                    } catch (IntrospectionException ex) {
                        Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
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
     * handle catch.
     * @param field Filed details.
     * @param fromClass From class.
     * @param sb String builder.
     * @param set Set.
     * @param notFoundList List of not found details.
     * @param ex Define exception.
     */
    private static void handleCatch(Field field,final Class<?> fromClass,StringBuilder sb,String set,
                             final List<String> notFoundList,IntrospectionException ex){
        try {
            String name = field.getName();
            String first = "" + name.charAt(0);
            name = name.replaceFirst(first, first.toUpperCase());
            Method method = fromClass.getMethod("get" + name);
            sb.append("to.")
                    .append(set)
                    .append(FROM_STR)
                    .append(method.getName())
                    .append("());\n");

        } catch (NoSuchMethodException ex1) {
            sb.append("// to.")
                    .append(set)
                    .append(FROM_STR)
                    .append(");\n");
            notFoundList.add(field.getName());

        } catch (SecurityException ex1) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
