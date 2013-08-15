package dk.kiljacken.aestuscraft.core;

import net.minecraftforge.common.Configuration;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.api.info.BlockInfo;
import dk.kiljacken.aestuscraft.api.info.ItemInfo;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatedFlooring;
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class Config {
    public static void initialize(Configuration config)
    {
        try
        {
            config.load();

            // Block Ids
            BlockInfo.HEAT_CONDUCTOR_ID = config.getBlock(BlockInfo.HEAT_CONDUCTOR_NAME, BlockInfo.HEAT_CONDUCTOR_ID).getInt();
            BlockInfo.FUEL_BURNER_ID = config.getBlock(BlockInfo.FUEL_BURNER_NAME, BlockInfo.FUEL_BURNER_ID).getInt();
            BlockInfo.INSULATED_FURNACE_ID = config.getBlock(BlockInfo.INSULATED_FURNACE_NAME, BlockInfo.INSULATED_FURNACE_ID).getInt();
            BlockInfo.HEATED_FLOORING_ID = config.getBlock(BlockInfo.HEATED_FLOORING_NAME, BlockInfo.HEATED_FLOORING_ID).getInt();

            // Item Ids
            ItemInfo.NETWORK_DEBUGGER_ID = config.getItem(ItemInfo.NETWORK_DEBUGGER_NAME, ItemInfo.NETWORK_DEBUGGER_ID).getInt() - 256;

            // Balancing values
            {
                // Fuel burner
                TileFuelBurner.HEAT_PER_FUEL = (float) config.get("balance", "fuelBurner.heatPerFuel", TileFuelBurner.HEAT_PER_FUEL).getDouble(
                        TileFuelBurner.HEAT_PER_FUEL);
                TileFuelBurner.HEAT_TRANSFER_RATE = (float) config.get("balance", "fuelBurner.heatTransferRate", TileFuelBurner.HEAT_TRANSFER_RATE).getDouble(
                        TileFuelBurner.HEAT_TRANSFER_RATE);

                // Insulated furnace
                TileInsulatedFurnace.HEAT_PER_BURN_TICK = (float) config.get("balance", "insulatedFurnace.heatPerBurnTick",
                        TileInsulatedFurnace.HEAT_PER_BURN_TICK).getDouble(TileInsulatedFurnace.HEAT_PER_BURN_TICK);

                // Heated flooring
                TileHeatedFlooring.HEAT_PER_TICK = (float) config.get("balance", "heatedFlooring.heatPerTick", TileHeatedFlooring.HEAT_PER_TICK).getDouble(
                        TileHeatedFlooring.HEAT_PER_TICK);
            }
        }
        catch (Exception e)
        {
            AestusCraft.log.severe("Exception while loading configuration");
        }
        finally
        {
            config.save();
        }
    }
}
