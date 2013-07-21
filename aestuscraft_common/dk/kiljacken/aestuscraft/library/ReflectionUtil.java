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
     * @param clazz Class of the object
     * @param instance Instance to get the value from
     * @param fieldName Name of the field
     * @return The value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPrivateValue(Class<?> clazz, Object instance, String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        return (T) field.get(instance);
    }

    /**
     * Instanciates an object with supplied arguments. Throw a RuntimeException
     * on failure
     * 
     * @param clazz Class of the object
     * @param args List of arguments
     * @return The instanciated object
     */
    @SuppressWarnings("unchecked")
    public static <T> T instanciateOrCrash(Class<? extends T> clazz, Object... args) {
        try {
            Class<?>[] argClasses = new Class[args.length];

            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }

            Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructor(argClasses);

            return constructor.newInstance(args);
        } catch (InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException("Exception while initializing registered object", e);
        }
    }
}
