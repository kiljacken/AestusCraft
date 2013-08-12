/**
 * AestusCraft
 * 
 * CreativeTab.java
 *
 * @author Kiljacken
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
package dk.kiljacken.aestuscraft.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
    private ItemStack m_IconItemStack;

    public CreativeTab(String label)
    {
        super(label);
    }

    /**
     * Sets the item stack used as the tab icon
     * 
     * @param iconItemStack
     *            ItemStack to use as icon
     */
    public void setIconItemStack(ItemStack iconItemStack)
    {
        m_IconItemStack = iconItemStack;
    }

    @Override
    public ItemStack getIconItemStack()
    {
        return m_IconItemStack;
    }
}
