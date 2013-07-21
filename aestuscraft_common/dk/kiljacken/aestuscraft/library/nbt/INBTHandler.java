/**
 * AestusCraft
 * 
 * INBTHandler.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.library.nbt;

import net.minecraft.nbt.NBTBase;

public interface INBTHandler {
    /**
     * Reads a value from the provided tag
     * 
     * @param tag The tag to read from
     * @return The valuer read from the tag
     */
    public <T> T readFromTag(NBTBase tag);

    /**
     * Writes a value to a compound tag
     * 
     * @param value The value to write to a tag
     * @return A tag containing the value
     */
    public NBTBase writeToTag(String name, Object value);
}
