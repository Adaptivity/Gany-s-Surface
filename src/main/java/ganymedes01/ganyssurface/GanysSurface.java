package ganymedes01.ganyssurface;

import ganymedes01.ganyssurface.blocks.ModBlocks;
import ganymedes01.ganyssurface.configuration.ConfigurationHandler;
import ganymedes01.ganyssurface.core.handlers.FuelHandler;
import ganymedes01.ganyssurface.core.handlers.InterModComms;
import ganymedes01.ganyssurface.core.handlers.OpenContainerHandler;
import ganymedes01.ganyssurface.core.handlers.PoopHandler;
import ganymedes01.ganyssurface.core.handlers.RenderCapeHandler;
import ganymedes01.ganyssurface.core.handlers.VersionCheckTickHandler;
import ganymedes01.ganyssurface.core.proxy.CommonProxy;
import ganymedes01.ganyssurface.core.utils.VersionHelper;
import ganymedes01.ganyssurface.creativetab.CreativeTabSurface;
import ganymedes01.ganyssurface.integration.ModIntegrator;
import ganymedes01.ganyssurface.items.ModItems;
import ganymedes01.ganyssurface.lib.Reference;
import ganymedes01.ganyssurface.network.PacketHandler;
import ganymedes01.ganyssurface.recipes.ModRecipes;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Gany's Surface
 * 
 * @author ganymedes01
 * 
 */

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER, dependencies = Reference.DEPENDENCIES)
@NetworkMod(channels = { Reference.CHANNEL_NAME }, clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class)
public class GanysSurface {

	@Instance(Reference.MOD_ID)
	public static GanysSurface instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static CreativeTabs surfaceTab = new CreativeTabSurface();
	public static boolean mobsShouldPoop = true;
	public static boolean activateChocolate = true;
	public static boolean shouldDoVersionCheck = true;
	public static boolean forceAllContainersOpen = false;
	public static boolean enableMarket = true;
	public static boolean enableWoodenArmour = true;
	public static boolean enableCamilaSeedsToDropFromGrass = true;
	public static boolean enableNormalWitchSpawn = true;
	public static int maxLevelOMCWorks = 15;
	public static int inkHarvesterMaxStrike = 5;
	public static int poopingChance = 15000;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModIntegrator.preInit();

		ConfigurationHandler.init(new File(event.getModConfigurationDirectory().getAbsolutePath() + File.separator + Reference.MASTER + File.separator + Reference.MOD_ID + ".cfg"));

		if (shouldDoVersionCheck) {
			VersionHelper.execute();
			TickRegistry.registerTickHandler(new VersionCheckTickHandler(), Side.CLIENT);
		}

		proxy.registerEntities();

		if (enableNormalWitchSpawn) {
			ArrayList<BiomeGenBase> biomes = new ArrayList<BiomeGenBase>();
			biomes.add(BiomeGenBase.beach);
			biomes.add(BiomeGenBase.desert);
			biomes.add(BiomeGenBase.desertHills);
			biomes.add(BiomeGenBase.forest);
			biomes.add(BiomeGenBase.forestHills);
			biomes.add(BiomeGenBase.extremeHills);
			biomes.add(BiomeGenBase.extremeHillsEdge);
			biomes.add(BiomeGenBase.jungle);
			biomes.add(BiomeGenBase.jungleHills);
			biomes.add(BiomeGenBase.mushroomIsland);
			biomes.add(BiomeGenBase.mushroomIslandShore);
			biomes.add(BiomeGenBase.ocean);
			biomes.add(BiomeGenBase.frozenOcean);
			biomes.add(BiomeGenBase.plains);
			biomes.add(BiomeGenBase.river);
			biomes.add(BiomeGenBase.icePlains);
			biomes.add(BiomeGenBase.iceMountains);
			biomes.add(BiomeGenBase.taiga);
			biomes.add(BiomeGenBase.taigaHills);

			EntityRegistry.addSpawn(EntityWitch.class, 5, 1, 1, EnumCreatureType.monster, biomes.toArray(new BiomeGenBase[0]));
			EntityRegistry.addSpawn(EntityWitch.class, 10, 1, 1, EnumCreatureType.monster, BiomeGenBase.swampland);
		}

		ModBlocks.init();
		ModItems.init();
		ModRecipes.init();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		GameRegistry.registerFuelHandler(new FuelHandler());

		if (mobsShouldPoop)
			MinecraftForge.EVENT_BUS.register(new PoopHandler());
		MinecraftForge.EVENT_BUS.register(new OpenContainerHandler());

		proxy.registerTileEntities();
		proxy.registerRenderers();

		if (!Loader.isModLoaded("mobsplice"))
			if (event.getSide() == Side.CLIENT) {
				RenderCapeHandler.getUsernames();
				MinecraftForge.EVENT_BUS.register(new RenderCapeHandler());
			}

		ModIntegrator.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ModIntegrator.postInit();
	}

	@EventHandler
	public void processIMCRequests(IMCEvent event) {
		InterModComms.processIMC(event);
	}
}