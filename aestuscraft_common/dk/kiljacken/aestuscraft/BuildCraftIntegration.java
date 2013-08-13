package dk.kiljacken.aestuscraft;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.Configuration;
import dk.kiljacken.aestuscraft.api.info.BlockInfo;
import dk.kiljacken.aestuscraft.api.info.TileInfo;
import dk.kiljacken.aestuscraft.buildcraft.BlockFrictionHeater;
import dk.kiljacken.aestuscraft.buildcraft.TileFrictionHeater;
import dk.kiljacken.aestuscraft.core.Registry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BuildCraftIntegration {
    public Block blockFrictionHeater;

    public void config(Configuration config)
    {
        try
        {
            config.load();

            // IDs
            BlockInfo.FRICTION_HEATER_ID = config.getBlock(BlockInfo.FRICTION_HEATER_NAME, BlockInfo.FRICTION_HEATER_ID).getInt();

            // Balance
            TileFrictionHeater.HEAT_PER_MJ = (float) config.get("balance", "frictionHeater.heatPerMJ", TileFrictionHeater.HEAT_PER_MJ).getDouble(
                    TileFrictionHeater.HEAT_PER_MJ);
            TileFrictionHeater.HEAT_TRANSFER_RATE = (float) config.get("balance", "frictionHeater.heatTransferRate", TileFrictionHeater.HEAT_TRANSFER_RATE)
                    .getDouble(TileFrictionHeater.HEAT_TRANSFER_RATE);
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

    public void preInit()
    {
        blockFrictionHeater = Registry.registerBlock(new BlockFrictionHeater(BlockInfo.FRICTION_HEATER_ID), BlockInfo.FRICTION_HEATER_NAME);
        GameRegistry.registerTileEntity(TileFrictionHeater.class, TileInfo.FRICTION_HEATER_NAME);
    }

    public void init()
    {
        Item woodenGear = null;
        Item stoneGear = null;

        try
        {
            woodenGear = (Item) Class.forName("buildcraft.BuildCraftCore").getField("woodenGearItem").get(null);
            stoneGear = (Item) Class.forName("buildcraft.BuildCraftCore").getField("stoneGearItem").get(null);
        }
        catch (Exception ex)
        {
            AestusCraft.log.severe("Unable to find Buildcraft items");
        }

        GameRegistry.addShapedRecipe(new ItemStack(blockFrictionHeater), "scs", "i i", "WwW", 's', Block.stone, 'c', AestusCraft.content.blockConductor, 'i',
                stoneGear, 'W', Block.cloth, 'w', woodenGear);
    }

    public void postInit()
    {

    }

    public boolean shouldLoad()
    {
        return Loader.isModLoaded("BuildCraft|Energy");
    }
}
