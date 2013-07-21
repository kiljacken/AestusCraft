/**
 * AestusCraft
 * 
 * NBTUtil.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.library.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.library.ReflectionUtil;

public class NBTUtil {
    /**
     * Read values in object with {@link NBTValue} annotation from NBT
     * 
     * @param object The object to read
     * @param tagCompound The tag to read from
     */
    public static void readAnnotatedFromNBT(Object object, NBTTagCompound tagCompound) {
        Class<?> clazz = object.getClass();

        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(NBTValue.class)) {
                    NBTValue annotation = field.getAnnotation(NBTValue.class);
                    INBTHandler nbtHandler = ReflectionUtil.instanciateOrCrash(annotation.handler());

                    Object value = nbtHandler.readFromTag(tagCompound.getTag(annotation.name()));
                    try {
                        ReflectionUtil.setPrivateValue(clazz, object, value, field);
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                        AestusCraft.log.info("Exception while setting field from NBT value");
                        throw new RuntimeException(e);
                    }
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    /**
     * Write values in object with {@link NBTValue} annotation to NBT
     * 
     * @param object The object to write
     * @param tagCompound The tag to write to
     */
    public static void writeAnnotatedToNBT(Object object, NBTTagCompound tagCompound) {
        Class<?> clazz = object.getClass();

        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(NBTValue.class)) {
                    NBTValue annotation = field.getAnnotation(NBTValue.class);
                    INBTHandler nbtHandler = ReflectionUtil.instanciateOrCrash(annotation.handler());

                    Object value;
                    try {
                        value = ReflectionUtil.getPrivateValue(clazz, object, field);
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                        AestusCraft.log.info("Exception while setting field from NBT value");
                        throw new RuntimeException(e);
                    }

                    tagCompound.setTag(annotation.name(), nbtHandler.writeToTag(annotation.name(), value));
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
    }

    /**
     * Annotation marking a field for storage in NBT data
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NBTValue {
        String name();

        Class<? extends INBTHandler> handler() default NativesNBTHandler.class;
    }
    
    /**
     * Reads a compound tag from a stream
     * 
     * @param stream The stream to read from
     * @return The tag read
     * @throws IOException
     */
    public static NBTTagCompound readCompoundFromStream(DataInputStream stream) throws IOException {
        short length = stream.readShort();

        if (length < 0) {
            return null;
        } else {
            byte[] data = new byte[length];
            stream.readFully(data);

            return CompressedStreamTools.decompress(data);
        }
    }

    /**
     * Writes a compound tag to a stream
     * 
     * @param stream The stream to write to
     * @param nbt The tag to write
     * @throws IOException
     */
    public static void writeCompoundToStream(DataOutputStream stream, NBTTagCompound tag) throws IOException {
        if (tag == null) {
            stream.writeShort(-1);
        } else {
            byte[] data = CompressedStreamTools.compress(tag);

            stream.writeShort((short) data.length);
            stream.write(data);
        }
    }
}
