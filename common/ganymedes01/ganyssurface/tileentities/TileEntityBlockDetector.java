package ganymedes01.ganyssurface.tileentities;

import ganymedes01.ganyssurface.blocks.BlockDetector;
import ganymedes01.ganyssurface.core.utils.Utils;
import ganymedes01.ganyssurface.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBlockDetector extends TileEntity implements ISidedInventory {

	protected ItemStack[] inventory = new ItemStack[1];
	protected int coolDown = 50;

	public void updateRedstoneSignalStatus(boolean flag) {
		blockType = getBlockType();
		if (blockType != null && blockType instanceof BlockDetector)
			((BlockDetector) blockType).updateBlockStatus(worldObj, xCoord, yCoord, zCoord, flag);
	}

	@Override
	public void updateEntity() {
		if (worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != 15)
			if (inventory[0] != null)
				if (inventory[0].getItem() instanceof ItemSeeds) {
					coolDown--;
					if (coolDown <= 0) {
						updateRedstoneSignalStatus(checkNearbyBlocks());
						coolDown = 50;
					}
				}
	}

	protected boolean checkSeeds() {
		if (inventory[0].getItem() instanceof ItemSeeds) {
			int cropID = ((ItemSeeds) inventory[0].getItem()).getPlantID(worldObj, xCoord, yCoord, zCoord);
			if (worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == cropID || worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == cropID || worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == cropID || worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == cropID ||
			worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == cropID) {
				coolDown = 0;
				if (worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord) >= 7 || worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord) >= 7 || worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord) >= 7 || worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1) >= 7 ||
				worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1) >= 7)
					return true;
			}
		}
		return false;
	}

	public boolean checkNearbyBlocks() {
		if (inventory[0] == null)
			return false;

		if (inventory[0].getItem() instanceof ItemSeeds) {
			int cropID = ((ItemSeeds) inventory[0].getItem()).getPlantID(worldObj, xCoord, yCoord, zCoord);
			if (worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == cropID || worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == cropID || worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == cropID || worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == cropID ||
			worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == cropID) {
				coolDown = 0;
				if (worldObj.getBlockMetadata(xCoord + 1, yCoord, zCoord) >= 7 || worldObj.getBlockMetadata(xCoord - 1, yCoord, zCoord) >= 7 || worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord) >= 7 || worldObj.getBlockMetadata(xCoord, yCoord, zCoord + 1) >= 7 ||
				worldObj.getBlockMetadata(xCoord, yCoord, zCoord - 1) >= 7)
					return true;
			}
		}

		if (inventory[0].getItem() instanceof ItemReed) {
			if (checkIdPicked(xCoord + 1, yCoord, zCoord) || checkIdPicked(xCoord - 1, yCoord, zCoord) || checkIdPicked(xCoord, yCoord + 1, zCoord) || checkIdPicked(xCoord, yCoord - 1, zCoord) || checkIdPicked(xCoord, yCoord, zCoord + 1) || checkIdPicked(xCoord, yCoord, zCoord - 1))
				return true;

		} else if (inventory[0].getItem() instanceof ItemBucket) {
			if (inventory[0].getItem().itemID == Item.bucketLava.itemID) {
				if (worldObj.getBlockMaterial(xCoord + 1, yCoord, zCoord) == Material.lava || worldObj.getBlockMaterial(xCoord - 1, yCoord, zCoord) == Material.lava || worldObj.getBlockMaterial(xCoord, yCoord, zCoord + 1) == Material.lava ||
				worldObj.getBlockMaterial(xCoord, yCoord, zCoord - 1) == Material.lava || worldObj.getBlockMaterial(xCoord, yCoord + 1, zCoord) == Material.lava || worldObj.getBlockMaterial(xCoord, yCoord - 1, zCoord) == Material.lava)
					return true;

			} else if (inventory[0].getItem().itemID == Item.bucketWater.itemID)
				if (worldObj.getBlockMaterial(xCoord + 1, yCoord, zCoord) == Material.water || worldObj.getBlockMaterial(xCoord - 1, yCoord, zCoord) == Material.water || worldObj.getBlockMaterial(xCoord, yCoord, zCoord + 1) == Material.water ||
				worldObj.getBlockMaterial(xCoord, yCoord, zCoord - 1) == Material.water || worldObj.getBlockMaterial(xCoord, yCoord + 1, zCoord) == Material.water || worldObj.getBlockMaterial(xCoord, yCoord - 1, zCoord) == Material.water)
					return true;

		} else if (worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == inventory[0].itemID || worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == inventory[0].itemID || worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == inventory[0].itemID ||
		worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == inventory[0].itemID || worldObj.getBlockId(xCoord, yCoord + 1, zCoord) == inventory[0].itemID || worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == inventory[0].itemID)
			return true;

		return false;
	}

	protected boolean checkIdPicked(int x, int y, int z) {
		if (!worldObj.isAirBlock(x, y, z))
			if (Block.blocksList[worldObj.getBlockId(x, y, z)].idPicked(worldObj, x, y, z) == inventory[0].itemID)
				return true;
		return false;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
			if (stack.stackSize <= amount)
				setInventorySlotContents(slot, null);
			else {
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0)
					setInventorySlotContents(slot, null);
			}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
			setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit())
			stack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String getInvName() {
		return Utils.getConainerName(Strings.BLOCK_DETECTOR_NAME);
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		updateRedstoneSignalStatus(checkNearbyBlocks());
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		NBTTagList tagList = data.getTagList("Items");
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound tagCompound = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tagCompound.getByte("Slot");
			if (slot >= 0 && slot < inventory.length)
				inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i)
			if (inventory[i] != null) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(tagCompound);
				tagList.appendTag(tagCompound);
			}
		data.setTag("Items", tagList);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}
}