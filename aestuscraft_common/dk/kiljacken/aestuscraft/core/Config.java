/**
 * AestusCraft
 * 
 * Config.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core;

import java.io.File;

import net.minecraftforge.common.Configuration;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.core.blocks.BlockInfo;
import dk.kiljacken.aestuscraft.core.items.ItemInfo;
import dk.kiljacken.aestuscraft.core.tiles.TileFrictionHeater;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatedFlooring;
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class Config {
    public Config(File file) {
        Configuration configuration = new Configuration(file);

        try {
            configuration.load();

            // Block Ids
            BlockInfo.BLOCK_HEAT_CONDUCTOR_ID = configuration.getBlock(BlockInfo.BLOCK_HEAT_CONDUCTOR_NAME, BlockInfo.BLOCK_HEAT_CONDUCTOR_ID).getInt();

            BlockInfo.BLOCK_FUEL_BURNER_ID = configuration.getBlock(BlockInfo.BLOCK_FUEL_BURNER_NAME, BlockInfo.BLOCK_FUEL_BURNER_ID).getInt();
            BlockInfo.BLOCK_FRICTION_HEATER_ID = configuration.getBlock(BlockInfo.BLOCK_FRICTION_HEATER_NAME, BlockInfo.BLOCK_FRICTION_HEATER_ID).getInt();

            BlockInfo.BLOCK_INSULATED_FURNACE_ID = configuration.getBlock(BlockInfo.BLOCK_INSULATED_FURNACE_NAME, BlockInfo.BLOCK_INSULATED_FURNACE_ID).getInt();
            BlockInfo.BLOCK_HEATED_FLOORING_ID = configuration.getBlock(BlockInfo.BLOCK_HEATED_FLOORING_NAME, BlockInfo.BLOCK_HEATED_FLOORING_ID).getInt();

            // Item Ids
            ItemInfo.NETWORK_DEBUGGER_ID = configuration.getItem(ItemInfo.NETWORK_DEBUGGER_NAME, ItemInfo.NETWORK_DEBUGGER_ID).getInt() - 256;

            // Balancing values
            {
                // Fuel burner
                TileFuelBurner.HEAT_PER_FUEL = (float) configuration.get("balance", "fuelBurner.heatPerFuel", TileFuelBurner.HEAT_PER_FUEL).getDouble(TileFuelBurner.HEAT_PER_FUEL);
                TileFuelBurner.HEAT_TRANSFER_RATE = (float) configuration.get("balance", "fuelBurner.heatTransferRate", TileFuelBurner.HEAT_TRANSFER_RATE).getDouble(TileFuelBurner.HEAT_TRANSFER_RATE);

                // Insulated furnace
                TileInsulatedFurnace.HEAT_PER_BURN_TICK = (float) configuration.get("balance", "insulatedFurnace.heatPerBurnTick", TileInsulatedFurnace.HEAT_PER_BURN_TICK).getDouble(TileInsulatedFurnace.HEAT_PER_BURN_TICK);

                // Friction heater
                TileFrictionHeater.HEAT_PER_MJ = (float) configuration.get("balance", "frictionHeater.heatPerMJ", TileFrictionHeater.HEAT_PER_MJ).getDouble(TileFrictionHeater.HEAT_PER_MJ);
                TileFrictionHeater.HEAT_TRANSFER_RATE = (float) configuration.get("balance", "frictionHeater.heatTransferRate", TileFrictionHeater.HEAT_TRANSFER_RATE).getDouble(TileFrictionHeater.HEAT_TRANSFER_RATE);

                // Heated flooring
                TileHeatedFlooring.HEAT_PER_REMOVED_SNOW = (float) configuration.get("balance", "heatedFlooring.heatPerRemovedSnow", TileHeatedFlooring.HEAT_PER_REMOVED_SNOW).getDouble(TileHeatedFlooring.HEAT_PER_REMOVED_SNOW);
            }
        } catch (Exception e) {
            AestusCraft.log.severe("Exception while loading configuration");
        } finally {
            configuration.save();
        }
    }
}
