/**
 * AestusCraft
 * 
 * ReflectionUtil.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.library;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {
    /**
     * Gets the value from a private field of an object
     * 
     * @param clazz
     *            Class of the object
     * @param instance
     *            Instance to get the value from
     * @param fieldName
     *            Name of the field
     * @return The value
     */
    public static <T> T getPrivateValue(Class<?> clazz, Object instance, String fieldName) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException
    {
        Field field = clazz.getDeclaredField(fieldName);
        return getPrivateValue(clazz, instance, field);
    }

    /**
     * Gets the value from a private field of an object
     * 
     * @param clazz
     *            Class of the object
     * @param instance
     *            Instance to get the value from
     * @param field
     *            The field
     * @return The value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPrivateValue(Class<?> clazz, Object instance, Field field) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException
    {
        field.setAccessible(true);
        return (T) field.get(instance);
    }

    /**
     * Sets the value of a private field of an object
     * 
     * @param clazz
     *            Class of the object
     * @param instance
     *            Instance to set the value on
     * @param value
     *            The value to set the field to
     * @param fieldName
     *            Name of the field
     */
    public static void setPrivateValue(Class<?> clazz, Object instance, Object value, String fieldName) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException
    {
        Field field = clazz.getDeclaredField(fieldName);
        setPrivateValue(clazz, instance, value, field);
    }

    /**
     * Sets the value of a private field of an object
     * 
     * @param clazz
     *            Class of the object
     * @param instance
     *            Instance to set the value on
     * @param value
     *            The value to set the field to
     * @param field
     *            The field
     */
    public static void setPrivateValue(Class<?> clazz, Object instance, Object value, Field field) throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException
    {
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * Instanciates an object with supplied arguments. Throw a RuntimeException
     * on failure
     * 
     * @param clazz
     *            Class of the object
     * @param args
     *            List of arguments
     * @return The instanciated object
     */
    @SuppressWarnings("unchecked")
    public static <T> T instanciateOrCrash(Class<? extends T> clazz, Object... args)
    {
        try
        {
            Class<?>[] argClasses = new Class[args.length];

            for (int i = 0; i < args.length; i++)
            {
                argClasses[i] = args[i].getClass();
            }

            Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructor(argClasses);

            return constructor.newInstance(args);
        }
        catch (InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException e)
        {
            throw new RuntimeException("Exception while initializing registered object", e);
        }
    }
}
