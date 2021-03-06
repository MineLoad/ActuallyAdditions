/*
 * This file ("LensColor.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items.lens;


import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.internal.IAtomicReconstructor;
import de.ellpeck.actuallyadditions.api.lens.Lens;
import de.ellpeck.actuallyadditions.api.recipe.IColorLensChanger;
import de.ellpeck.actuallyadditions.mod.blocks.InitBlocks;
import de.ellpeck.actuallyadditions.mod.util.PosUtil;
import de.ellpeck.actuallyadditions.mod.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Map;

public class LensColor extends Lens{

    public static final int ENERGY_USE = 200;

    //Thanks to xdjackiexd for this, as I couldn't be bothered
    private static final float[][] POSSIBLE_COLORS = {
            {158F, 43F, 39F}, //Red
            {234F, 126F, 53F}, //Orange
            {194F, 181F, 28F}, //Yellow
            {57F, 186F, 46F}, //Lime Green
            {54F, 75F, 24F}, //Green
            {99F, 135F, 210F}, //Light Blue
            {38F, 113F, 145F}, //Cyan
            {37F, 49F, 147F}, //Blue
            {126F, 52F, 191F}, //Purple
            {190F, 73F, 201F}, //Magenta
            {217F, 129F, 153F}, //Pink
            {86F, 51F, 28F}, //Brown
    };

    @SuppressWarnings("unchecked")
    @Override
    public boolean invoke(IBlockState hitState, BlockPos hitBlock, IAtomicReconstructor tile){
        if(hitBlock != null){
            if(tile.getEnergy() >= ENERGY_USE){
                int meta = PosUtil.getMetadata(hitBlock, tile.getWorldObject());
                ItemStack returnStack = this.tryConvert(new ItemStack(PosUtil.getBlock(hitBlock, tile.getWorldObject()), 1, meta));
                if(returnStack != null && returnStack.getItem() instanceof ItemBlock){
                    PosUtil.setBlock(hitBlock, tile.getWorldObject(), Block.getBlockFromItem(returnStack.getItem()), returnStack.getItemDamage(), 2);

                    tile.extractEnergy(ENERGY_USE);
                }
            }

            ArrayList<EntityItem> items = (ArrayList<EntityItem>)tile.getWorldObject().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(hitBlock.getX(), hitBlock.getY(), hitBlock.getZ(), hitBlock.getX()+1, hitBlock.getY()+1, hitBlock.getZ()+1));
            for(EntityItem item : items){
                if(!item.isDead && item.getEntityItem() != null && tile.getEnergy() >= ENERGY_USE){
                    ItemStack newStack = this.tryConvert(item.getEntityItem());
                    if(newStack != null){
                        item.setDead();

                        EntityItem newItem = new EntityItem(tile.getWorldObject(), item.posX, item.posY, item.posZ, newStack);
                        tile.getWorldObject().spawnEntityInWorld(newItem);

                        tile.extractEnergy(ENERGY_USE);
                    }
                }
            }
        }
        return false;
    }

    private ItemStack tryConvert(ItemStack stack){
        if(stack != null){
            Item item = stack.getItem();
            if(item != null){
                for(Map.Entry<Item, IColorLensChanger> changer : ActuallyAdditionsAPI.reconstructorLensColorChangers.entrySet()){
                    if(item == changer.getKey()){
                        return changer.getValue().modifyItem(stack);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public float[] getColor(){
        float[] colors = POSSIBLE_COLORS[Util.RANDOM.nextInt(POSSIBLE_COLORS.length)];
        return new float[]{colors[0]/255F, colors[1]/255F, colors[2]/255F};
    }

    @Override
    public int getDistance(){
        return 10;
    }
}