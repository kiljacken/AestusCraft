/**
 * AestusCraft
 * 
 * MultipleBlockConsumers.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import net.minecraft.block.material.Material;
import dk.kiljacken.aestuscraft.core.blocks.subblocks.SubBlockInsulatedFurnace;

public class MultipleBlockConsumers extends MultipleBlock {
    public static final int META_INSULATED_FURNACE = 0;

    public MultipleBlockConsumers(int id) {
        super(id, Material.rock, new SubBlockInsulatedFurnace());
    }
}
