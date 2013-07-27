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
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.blocks.tiles.TileInsulatedFurnace;
import dk.kiljacken.aestuscraft.core.items.ItemInfo;

public class Config {
    public Config(File file) {
        Configuration configuration = new Configuration(file);

        try {
            configuration.load();

            // Block Ids
            BlockInfo.BLOCK_PRODUCERS_ID = configuration.getBlock(BlockInfo.BLOCK_PRODUCERS_NAME, BlockInfo.BLOCK_PRODUCERS_ID).getInt();
            BlockInfo.BLOCK_CONSUMERS_ID = configuration.getBlock(BlockInfo.BLOCK_CONSUMERS_NAME, BlockInfo.BLOCK_CONSUMERS_ID).getInt();
            BlockInfo.BLOCK_HEAT_CONDUCTOR_ID = configuration.getBlock(BlockInfo.BLOCK_HEAT_CONDUCTOR_NAME, BlockInfo.BLOCK_HEAT_CONDUCTOR_ID).getInt();

            // Item Ids
            ItemInfo.NETWORK_DEBUGGER_ID = configuration.getItem(ItemInfo.NETWORK_DEBUGGER_NAME, ItemInfo.NETWORK_DEBUGGER_ID).getInt();

            // Balancing values
            {
                // Fuel burner
                TileFuelBurner.HEAT_PER_FUEL = (float) configuration.get("balance", "fuelBurner.heatPerFuel", TileFuelBurner.HEAT_PER_FUEL).getDouble(TileFuelBurner.HEAT_PER_FUEL);
                TileFuelBurner.HEAT_TRANSFER_RATE = (float) configuration.get("balance", "fuelBurner.heatTransferRate", TileFuelBurner.HEAT_TRANSFER_RATE).getDouble(TileFuelBurner.HEAT_TRANSFER_RATE);

                // Insulated furnace
                TileInsulatedFurnace.HEAT_PER_BURN_TICK = (float) configuration.get("balance", "insulatedFurnace.heatPerBurnTick", TileInsulatedFurnace.HEAT_PER_BURN_TICK).getDouble(TileInsulatedFurnace.HEAT_PER_BURN_TICK);
            }
        } catch (Exception e) {
            AestusCraft.log.severe("Exception while loading configuration");
        } finally {
            configuration.save();
        }
    }
}
