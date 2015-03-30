package ellpeck.actuallyadditions.tile;

import cpw.mods.fml.common.registry.GameRegistry;
import ellpeck.actuallyadditions.util.ModUtil;
import ellpeck.actuallyadditions.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityBase extends TileEntity{

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet){
        super.onDataPacket(net, packet);
        this.readFromNBT(packet.func_148857_g());
    }

    public static void init(){
        Util.logInfo("Registering TileEntities...");
        GameRegistry.registerTileEntity(TileEntityCompost.class, ModUtil.MOD_ID_LOWER + ":tileEntityCompost");
        GameRegistry.registerTileEntity(TileEntityFeeder.class, ModUtil.MOD_ID_LOWER + ":tileEntityFeeder");
        GameRegistry.registerTileEntity(TileEntityGiantChest.class, ModUtil.MOD_ID_LOWER + ":tileEntityGiantChest");
        GameRegistry.registerTileEntity(TileEntityGrinder.class, ModUtil.MOD_ID_LOWER + ":tileEntityGrinder");
        GameRegistry.registerTileEntity(TileEntityFurnaceDouble.class, ModUtil.MOD_ID_LOWER + ":tileEntityFurnaceDouble");
        GameRegistry.registerTileEntity(TileEntityInputter.class, ModUtil.MOD_ID_LOWER + ":tileEntityInputter");
        GameRegistry.registerTileEntity(TileEntityFishingNet.class, ModUtil.MOD_ID_LOWER + ":tileEntityFishingNet");
        GameRegistry.registerTileEntity(TileEntityFurnaceSolar.class, ModUtil.MOD_ID_LOWER + ":tileEntityFurnaceSolar");
    }

    @Override
    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z){
        return newBlock == null || newBlock instanceof BlockAir;
    }
}