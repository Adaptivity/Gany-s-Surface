package ganymedes01.ganyssurface.items;

import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.core.utils.Utils;
import ganymedes01.ganyssurface.lib.Strings;
import net.minecraft.item.Item;

public class TeaLeaves extends Item {

	public TeaLeaves(int id) {
		super(id);
		setCreativeTab(GanysSurface.surfaceTab);
		setTextureName(Utils.getItemTexture(Strings.TEA_LEAVES_NAME));
		setUnlocalizedName(Utils.getUnlocalizedName(Strings.TEA_LEAVES_NAME));
	}
}
