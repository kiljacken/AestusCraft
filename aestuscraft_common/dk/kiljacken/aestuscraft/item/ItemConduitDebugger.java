/**
 * AestusCraft
 * 
 * ItemConduitDebugger.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dk.kiljacken.aestuscraft.AestusCraft;
import dk.kiljacken.aestuscraft.api.heat.IHeatConduit;
import dk.kiljacken.aestuscraft.lib.StringResources;
import dk.kiljacken.aestuscraft.util.LogHelper;

public class ItemConduitDebugger extends Item {
    public ItemConduitDebugger(int id) {
        super(id);
        setUnlocalizedName(StringResources.ITEM_CONDUIT_DEBUGGER_NAME);
        setMaxStackSize(1);

        setCreativeTab(AestusCraft.creativeTab);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
        if (world.blockHasTileEntity(x, y, z)) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile instanceof IHeatConduit) {
                IHeatConduit conduit = (IHeatConduit) tile;

                int numConduits = conduit.getNetwork().getHeatConduits().size();
                int numConsumers = conduit.getNetwork().getHeatConsumers().size();
                int numProducers = conduit.getNetwork().getHeatProducers().size();

                Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();

                LogHelper.info("Heat Network Debug (" + effectiveSide.name() + ") - Cd: " + numConduits + ", Cn: " + numConsumers + ", Pd: " + numProducers);

                return true;
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon("aec:conduit_debugger");
    }
}
