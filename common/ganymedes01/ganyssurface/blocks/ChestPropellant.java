package ganymedes01.ganyssurface.blocks;

import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.core.utils.Utils;
import ganymedes01.ganyssurface.lib.GUIsID;
import ganymedes01.ganyssurface.lib.Strings;
import ganymedes01.ganyssurface.tileentities.TileEntityChestPropellant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Gany's Surface
 * 
 * @author ganymedes01
 * 
 */

public class ChestPropellant extends BlockContainer {

	public static final int MAX_PILE_SIZE = 17;

	@SideOnly(Side.CLIENT)
	private Icon blockTop, blockSide;

	public ChestPropellant(int id) {
		super(id, Material.rock);
		setHardness(1.0F);
		setLightOpacity(0);
		setCreativeTab(GanysSurface.surfaceTab);
		setUnlocalizedName(Utils.getUnlocalizedName(Strings.CHEST_PROPELLANT_NAME));
		setTextureName(Utils.getBlockTexture(Strings.CHEST_PROPELLANT_NAME, false));
		setBlockBounds(0.07F, 0.0F, 0.07F, 0.93F, 1.0F, 0.93F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		for (int i = 1; i < MAX_PILE_SIZE; i++) {
			TileEntity tile = world.getBlockTileEntity(x, y - i, z);
			if (tile instanceof IInventory) {
				if (!(tile instanceof TileEntityChestPropellant))
					if (world.getBlockId(x, y - i, z) < Block.blocksList.length) {
						Block block = Block.blocksList[world.getBlockId(x, y - i, z)];
						if (block.blockID == Block.chest.blockID || block.blockID == Block.chestTrapped.blockID) {
							player.openGui(GanysSurface.instance, GUIsID.VANILLA_CHEST, world, x, y - i, z);
							return true;
						} else if (block.blockID == Block.furnaceIdle.blockID || block.blockID == Block.furnaceBurning.blockID) {
							player.openGui(GanysSurface.instance, GUIsID.VANILLA_FURNACE, world, x, y - i, z);
							return true;
						} else if (block.blockID == Block.brewingStand.blockID) {
							player.openGui(GanysSurface.instance, GUIsID.VANILLA_BREW_STAND, world, x, y - i, z);
							return true;
						} else if (block.blockID == Block.hopperBlock.blockID) {
							player.openGui(GanysSurface.instance, GUIsID.VANILLA_HOPPER, world, x, y - i, z);
							return true;
						} else if (block.blockID == Block.dispenser.blockID) {
							player.openGui(GanysSurface.instance, GUIsID.VANILLA_DISPENSER, world, x, y - i, z);
							return true;
						} else if (block.blockID == Block.dropper.blockID) {
							player.openGui(GanysSurface.instance, GUIsID.VANILLA_DROPPER, world, x, y - i, z);
							return true;
						} else
							return block.onBlockActivated(world, x, y - i, z, player, side, hitX, hitY, hitZ);
					}
			} else
				return false;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityChestPropellant();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return side == 0 || side == 1 ? blockTop : blockSide;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister reg) {
		blockSide = reg.registerIcon(Utils.getBlockTexture(Strings.CHEST_PROPELLANT_NAME, true) + "side");
		blockTop = reg.registerIcon(Utils.getBlockTexture(Strings.CHEST_PROPELLANT_NAME, true) + "top");
	}
}