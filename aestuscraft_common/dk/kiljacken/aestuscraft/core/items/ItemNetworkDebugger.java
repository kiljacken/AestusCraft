/**
 * AestusCraft
 * 
 * ItemNetworkDebugger.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import dk.kiljacken.aestuscraft.api.heat.IHeatConductor;
import dk.kiljacken.aestuscraft.api.heat.IHeatContainer;
import dk.kiljacken.aestuscraft.api.heat.IHeatNetwork;

public class ItemNetworkDebugger extends BaseItem {
    public ItemNetworkDebugger(int id) {
        super(id);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister) {
        itemIcon = par1IconRegister.registerIcon("aestuscraft:network_debugger");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Definitely not a tazer");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile != null && tile instanceof IHeatConductor) {
                IHeatConductor conductor = (IHeatConductor) tile;
                IHeatNetwork network = conductor.getNetwork();

                String text = String.format("CO: %d - PD: %d - CD: %d", network.getConnectedConsumers().size(), network.getConnectedProducers().size(), network.getConnectedConductors().size());
                player.sendChatToPlayer(ChatMessageComponent.func_111066_d(text));

                return true;
            }

            if (tile != null && tile instanceof IHeatContainer) {
                IHeatContainer container = (IHeatContainer) tile;

                player.sendChatToPlayer(ChatMessageComponent.func_111066_d("Heat Level: " + container.getHeatLevel() + "/" + container.getMaxHeatLevel()));

                return true;
            }
        }

        return false;
    }

}
