/**
 * AestusCraft
 * 
 * MultipleBlockProducers.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.blocks;

import net.minecraft.block.material.Material;
import dk.kiljacken.aestuscraft.core.blocks.subblocks.SubBlockFrictionHeater;
import dk.kiljacken.aestuscraft.core.blocks.subblocks.SubBlockFuelBurner;

public class MultipleBlockProducers extends MultipleBlock {
    public static final int META_FUEL_BURNER = 0;
    public static final int META_FRICTION_HEATER = 1;
    
    public MultipleBlockProducers(int id) {
        super(id, Material.rock, new SubBlockFuelBurner(), new SubBlockFrictionHeater());
    }
}
