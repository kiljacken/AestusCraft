package dk.kiljacken.aestuscraft.core;

import java.io.File;

import net.minecraftforge.common.Configuration;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.api.info.BlockInfo;
import dk.kiljacken.aestuscraft.api.info.ItemInfo;
import dk.kiljacken.aestuscraft.core.tiles.TileFrictionHeater;
import dk.kiljacken.aestuscraft.core.tiles.TileFuelBurner;
import dk.kiljacken.aestuscraft.core.tiles.TileHeatedFlooring;
import dk.kiljacken.aestuscraft.core.tiles.TileInsulatedFurnace;

public class Config {
    public Config(File file)
    {
        Configuration configuration = new Configuration(file);

        try
        {
            configuration.load();

            // Block Ids
            BlockInfo.HEAT_CONDUCTOR_ID = configuration.getBlock(BlockInfo.HEAT_CONDUCTOR_NAME, BlockInfo.HEAT_CONDUCTOR_ID).getInt();

            BlockInfo.FUEL_BURNER_ID = configuration.getBlock(BlockInfo.FUEL_BURNER_NAME, BlockInfo.FUEL_BURNER_ID).getInt();
            BlockInfo.FRICTION_HEATER_ID = configuration.getBlock(BlockInfo.FRICTION_HEATER_NAME, BlockInfo.FRICTION_HEATER_ID).getInt();

            BlockInfo.INSULATED_FURNACE_ID = configuration.getBlock(BlockInfo.INSULATED_FURNACE_NAME, BlockInfo.INSULATED_FURNACE_ID).getInt();
            BlockInfo.HEATED_FLOORING_ID = configuration.getBlock(BlockInfo.HEATED_FLOORING_NAME, BlockInfo.HEATED_FLOORING_ID).getInt();

            // Item Ids
            ItemInfo.NETWORK_DEBUGGER_ID = configuration.getItem(ItemInfo.NETWORK_DEBUGGER_NAME, ItemInfo.NETWORK_DEBUGGER_ID).getInt() - 256;

            // Balancing values
            {
                // Fuel burner
                TileFuelBurner.HEAT_PER_FUEL = (float) configuration.get("balance", "fuelBurner.heatPerFuel", TileFuelBurner.HEAT_PER_FUEL).getDouble(
                        TileFuelBurner.HEAT_PER_FUEL);
                TileFuelBurner.HEAT_TRANSFER_RATE = (float) configuration.get("balance", "fuelBurner.heatTransferRate", TileFuelBurner.HEAT_TRANSFER_RATE)
                        .getDouble(TileFuelBurner.HEAT_TRANSFER_RATE);

                // Insulated furnace
                TileInsulatedFurnace.HEAT_PER_BURN_TICK = (float) configuration.get("balance", "insulatedFurnace.heatPerBurnTick",
                        TileInsulatedFurnace.HEAT_PER_BURN_TICK).getDouble(TileInsulatedFurnace.HEAT_PER_BURN_TICK);

                // Friction heater
                TileFrictionHeater.HEAT_PER_MJ = (float) configuration.get("balance", "frictionHeater.heatPerMJ", TileFrictionHeater.HEAT_PER_MJ).getDouble(
                        TileFrictionHeater.HEAT_PER_MJ);
                TileFrictionHeater.HEAT_TRANSFER_RATE = (float) configuration.get("balance", "frictionHeater.heatTransferRate",
                        TileFrictionHeater.HEAT_TRANSFER_RATE).getDouble(TileFrictionHeater.HEAT_TRANSFER_RATE);

                // Heated flooring
                TileHeatedFlooring.HEAT_PER_TICK = (float) configuration.get("balance", "heatedFlooring.heatPerTick", TileHeatedFlooring.HEAT_PER_TICK)
                        .getDouble(TileHeatedFlooring.HEAT_PER_TICK);
            }
        }
        catch (Exception e)
        {
            AestusCraft.log.severe("Exception while loading configuration");
        }
        finally
        {
            configuration.save();
        }
    }
}
