package ganymedes01.ganyssurface.blocks;

import ganymedes01.ganyssurface.ModItems;
import ganymedes01.ganyssurface.core.utils.Utils;
import ganymedes01.ganyssurface.lib.RenderIDs;

import java.util.Random;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNewDoor extends BlockDoor {

	public static final String[] names = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "dark_oak" };

	private final int meta;

	public BlockNewDoor(int meta) {
		super(Material.wood);
		String name = names[meta];
		this.meta = meta;

		disableStats();
		setHardness(3.0F);
		setStepSound(soundTypeWood);
		setBlockTextureName("door_" + name);
		setBlockName(Utils.getUnlocalizedName("door" + name.substring(0, 1).toUpperCase() + name.substring(1)));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z) {
		return getItemDoor();
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return (meta & 8) != 0 ? null : getItemDoor();
	}

	@Override
	public int getRenderType() {
		return RenderIDs.DOOR;
	}

	private Item getItemDoor() {
		return ModItems.doors[meta - 1];
	}
}