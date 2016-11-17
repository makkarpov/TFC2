package com.bioxx.tfc2;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.bioxx.jmapgen.IslandMap;
import com.bioxx.jmapgen.graph.Center;
import com.bioxx.tfc2.api.types.WoodType;
import com.bioxx.tfc2.blocks.*;
import com.bioxx.tfc2.core.*;
import com.bioxx.tfc2.world.WorldGen;
import org.apache.commons.lang3.text.WordUtils;

public class Core 
{
	public static PortalSchematic PortalSchematic;
	public static Block getGroundAboveSeaLevel(World world, BlockPos pos)
	{
		BlockPos blockpos1;

		for (blockpos1 = new BlockPos(pos.getX(), 63, pos.getZ()); !world.isAirBlock(blockpos1.up()); blockpos1 = blockpos1.up())
		{
			;
		}

		return world.getBlockState(blockpos1).getBlock();
	}

	public static int getHeight(World world, int worldX, int worldZ)
	{
		Chunk c = world.getChunkFromChunkCoords(worldX >> 4, worldZ >> 4); 
		return c.getHeightValue(worldX & 15, worldZ & 15);
	}

	/**
	 * Sets the block using setActualState for the given block. Helper method to reduce repeated code usage
	 * @return Returns if the block is successfully set
	 */
	public static boolean setBlock(World world, Block b, BlockPos bp)
	{
		return setBlock(world, b.getActualState(b.getDefaultState(), world, bp), bp, 3);
	}

	/**
	 * Sets the block using setActualState for the given block. Helper method to reduce repeated code usage
	 * @return Returns if the block is successfully set
	 */
	public static boolean setBlock(World world, IBlockState b, BlockPos bp)
	{
		return setBlock(world, b, bp, 3);
	}

	public static boolean setBlock(World world, IBlockState b, BlockPos bp, int flag)
	{
		return world.setBlockState(bp, b.getBlock().getActualState(b, world, bp), flag);
	}

	public static String translate(String s)
	{
		return I18n.format(s);
	}

	public static String textConvert(String s)
	{
		return WordUtils.capitalize(s, '_').replaceAll("_", " ");
	}

	public static String[] capitalizeStringArray(String[] array)
	{
		String[] outArray = new String[array.length];
		for(int i = 0; i < array.length; i++)
		{
			outArray[i] = WordUtils.capitalize(array[i]);
		}
		return outArray;
	}

	public static String getUnlocalized(String s)
	{
		return s.replaceAll("tile.", "");
	}

	/**
	 * Creates a new ResourceLocation from an input string. Shortens the code slightly.
	 */
	public static ResourceLocation CreateRes(String s)
	{
		return new ResourceLocation(s);
	}

	public static boolean isGrass(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Grass)
			return true;

		return false;
	}

	public static boolean isDirt(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Dirt)
			return true;

		return false;
	}

	public static boolean isSoil(IBlockState state)
	{
		return isGrass(state) || isDirt(state);
	}

	public static boolean isSand(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Sand)
			return true;

		return false;
	}

	public static boolean isStone(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Stone)
			return true;

		return false;
	}

	public static boolean isGravel(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Gravel)
			return true;

		return false;
	}

	public static boolean isTerrain(IBlockState state)
	{
		return isSoil(state) || isSand(state) || isStone(state) || isGravel(state);
	}

	public static boolean isNaturalLog(IBlockState state)
	{
		return state.getBlock() == TFCBlocks.LogNatural || state.getBlock() == TFCBlocks.LogNatural2 || state.getBlock() == TFCBlocks.LogNaturalPalm;
	}

	/**
	 * 
	 * @param w
	 * @param pos
	 * @return
	 */
	public static float getMoistureFromChunk(ChunkCache w, BlockPos pos)
	{
		Chunk c = w.worldObj.getChunkFromBlockCoords(pos);
		byte[] moistureArray = c.getBiomeArray();
		byte b = moistureArray[(pos.getZ() & 0xF) << 4 | (pos.getX() & 0xF)];
		int s = (b & 0xFF);
		return (float)s / 255F;
	}

	public static float getMoistureFromChunk(Chunk c, BlockPos pos)
	{
		byte[] moistureArray = c.getBiomeArray();
		byte b = moistureArray[(pos.getZ() & 0xF) << 4 | (pos.getX() & 0xF)];
		int s = (b & 0xFF);
		return (float)s / 255F;
	}

	public static void bindTexture(ResourceLocation texture)
	{
		net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}

	public static int getExtraEquipInventorySize() 
	{
		return 0;
	}

	public static InventoryPlayer getNewInventory(EntityPlayer player)
	{
		InventoryPlayer ip = player.inventory;
		NBTTagList nbt = new NBTTagList();
		nbt = player.inventory.writeToNBT(nbt);
		ip = new InventoryPlayerTFC(player);
		ip.readFromNBT(nbt);
		return ip;
	}

	public static boolean isPlayerInDebugMode(EntityPlayer player)
	{
		return true;
	}

	/**
	 * If this is modified in any way, setPlayerFoodStats should be called to save the data back 
	 * to players nbt or the changes will be lost.
	 * @return Returns the FoodStatsTFC object that is associated with this player. 
	 */
	public static FoodStatsTFC getPlayerFoodStats(EntityLivingBase player)
	{
		FoodStatsTFC foodstats = new FoodStatsTFC((EntityPlayer)player);
		foodstats.readNBT(player.getEntityData());
		return foodstats;
	}

	public static void setPlayerFoodStats(EntityPlayer player, FoodStatsTFC foodstats)
	{
		foodstats.writeNBT(player.getEntityData());
	}

	public static IBlockState getPlanks(WoodType w)
	{
		if(w.getMeta() >= 16)
			return TFCBlocks.Planks2.getDefaultState().withProperty(BlockPlanks2.META_PROPERTY, w);
		return TFCBlocks.Planks.getDefaultState().withProperty(BlockPlanks.META_PROPERTY, w);
	}

	public static IBlockState getNaturalLog(WoodType w)
	{
		if(w == WoodType.Palm)
			return TFCBlocks.LogNaturalPalm.getDefaultState();
		if(w.getMeta() >= 16)
			return TFCBlocks.LogNatural2.getDefaultState().withProperty(BlockLogNatural2.META_PROPERTY, w);
		return TFCBlocks.LogNatural.getDefaultState().withProperty(BlockLogNatural.META_PROPERTY, w);
	}

	public static IBlockState getLeaves(WoodType w)
	{
		if(w.getMeta() >= 16)
			return TFCBlocks.Leaves2.getDefaultState().withProperty(BlockLeaves2.META_PROPERTY, w);
		return TFCBlocks.Leaves.getDefaultState().withProperty(BlockLeaves.META_PROPERTY, w);
	}

	public static IslandMap getMapForWorld(World w, BlockPos pos)
	{
		if(w.provider.getDimension() == 2)
		{
			return WorldGen.getInstance().getIslandMap(pos.getX() >> 9, pos.getZ() >> 9);
		}
		return WorldGen.getInstance().getIslandMap(pos.getX() >> 12, pos.getZ() >> 12);
	}

	public static void dropItem(World world, BlockPos pos, ItemStack is)
	{
		dropItem(world, (double)pos.getX()+0.5, (double)pos.getY(), (double)pos.getZ()+0.5, is);
	}
	public static void dropItem(World world, double posX, double posY, double posZ, ItemStack is)
	{
		EntityItem ei = new EntityItem(world, posX, posY, posZ, is);
		ei.motionX = -0.07+world.rand.nextFloat() * 0.14;
		ei.motionY = 0.15;
		ei.motionZ = -0.07+world.rand.nextFloat() * 0.14;
		world.spawnEntityInWorld(ei);
	}

	public static void playSoundAtEntity(Entity e, SoundEvent name, float volume, float pitch)
	{
		e.worldObj.playSound(e.posX, e.posY, e.posZ, name, SoundCategory.BLOCKS, volume, pitch, false);
	}

	public static boolean isCenterInRect(Center c, int x, int z, int xRange, int zRange)
	{
		if(c.point.x >= x && c.point.x < x+xRange && c.point.y >= z && c.point.y < z+zRange)
			return true;
		return false;
	}

	public static PlayerSkillData getPlayerSkillData(EntityLivingBase entity)
	{
		PlayerSkillData data = new PlayerSkillData((EntityPlayer)entity);
		data.readNBT(entity.getEntityData());
		return data;
	}

	public static void setPlayerSkillData(EntityPlayer player, PlayerSkillData data)
	{
		data.writeNBT(player.getEntityData());
	}

	public static void inventoryUpdateFood(EntityPlayer player, ItemStack is) 
	{
		long expiration = 300000L;
		long time = Food.getDecayTimer(is)-player.worldObj.getWorldTime();
		if(time < 0)
		{
			int expiredAmt = (int)Math.min(1+(time / expiration)* (-1), is.stackSize);
			is.stackSize-=expiredAmt;
			Food.setDecayTimer(is, Food.getDecayTimer(is)+expiration*expiredAmt);
		}
	}
}
