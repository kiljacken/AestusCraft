package dk.kiljacken.aestuscraft.block;

import cpw.mods.fml.common.registry.GameRegistry;
import dk.kiljacken.aestuscraft.lib.BlockIds;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ModBlocks {
    public static Block insulatedFurnace;
    
    public static void init() {
        LogHelper.info("Initializing blocks");
        insulatedFurnace = new BlockInsulatedFurnace(BlockIds.INSULATED_FURNACE);
        
        GameRegistry.registerBlock(insulatedFurnace, StringResources.INSULATED_FURNACE_NAME);
        
        initBlockRecipes();
    }

    private static void initBlockRecipes() {
        LogHelper.info("Initializing block recipes");
        GameRegistry.addRecipe(new ItemStack(insulatedFurnace), new Object[]{"www", "wfw", "www", Character.valueOf('w'), Block.cloth, Character.valueOf('f'), Block.furnaceIdle});
    }
}
