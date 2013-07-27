/**
 * AestusCraft
 * 
 * MetadataItemBlock.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core.items.blocks;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public abstract class MetadataItemBlock extends ItemBlock {
    private String[] m_Names;

    public MetadataItemBlock(int id, String[] names) {
        super(id);

        setMaxDamage(0);
        setHasSubtypes(true);

        m_Names = names;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int i = MathHelper.clamp_int(itemStack.getItemDamage(), 0, m_Names.length - 1);

        return "tile." + m_Names[i];
    }
}
