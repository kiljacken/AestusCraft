package dk.kiljacken.aestuscraft.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class StreamUtil {
    /**
     * Reads a compressed NBTTagCompound from the InputStream
     */
    public static NBTTagCompound readNBTTagCompound(DataInputStream input) throws IOException {
        short length = input.readShort();

        if (length < 0) {
            return null;
        } else {
            byte[] abyte = new byte[length];
            input.readFully(abyte);
            return CompressedStreamTools.decompress(abyte);
        }
    }

    /**
     * Writes a compressed NBTTagCompound to the OutputStream
     */
    public static void writeNBTTagCompound(NBTTagCompound nbtTagCompound, DataOutputStream output) throws IOException {
        if (nbtTagCompound == null) {
            output.writeShort(-1);
        } else {
            byte[] abyte = CompressedStreamTools.compress(nbtTagCompound);
            output.writeShort((short) abyte.length);
            output.write(abyte);
        }
    }
}
