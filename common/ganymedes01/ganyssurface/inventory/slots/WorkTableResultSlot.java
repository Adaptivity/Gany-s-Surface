package ganymedes01.ganyssurface.inventory.slots;

import ganymedes01.ganyssurface.tileentities.TileEntityWorkTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

/**
 * Gany's Surface
 * 
 * @author ganymedes01
 * 
 */

public class WorkTableResultSlot extends SlotCrafting {

	private TileEntityWorkTable workTable;
	private IInventory matrix;

	public WorkTableResultSlot(TileEntityWorkTable tile, EntityPlayer player, IInventory matrix, IInventory result, int slotIndex, int y, int z) {
		super(player, matrix, result, slotIndex, y, z);
		workTable = tile;
		this.matrix = matrix;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
		super.onPickupFromSlot(player, stack);
		for (int i = 0; i < matrix.getSizeInventory(); i++)
			workTable.setInventorySlotContents(i, matrix.getStackInSlot(i));
	}
}