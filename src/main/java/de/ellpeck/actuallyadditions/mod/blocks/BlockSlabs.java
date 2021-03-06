/*
 * This file ("BlockSlabs.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.blocks;

import de.ellpeck.actuallyadditions.mod.blocks.base.BlockBase;
import de.ellpeck.actuallyadditions.mod.blocks.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSlabs extends BlockBase{

    private static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    private static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    private static final PropertyInteger META = PropertyInteger.create("meta", 0, 1);
    private Block fullBlock;
    private int meta;

    public BlockSlabs(String name, Block fullBlock){
        this(name, fullBlock, 0);
    }

    public BlockSlabs(String name, Block fullBlock, int meta){
        super(fullBlock.getMaterial(fullBlock.getDefaultState()), name);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.fullBlock = fullBlock;
        this.meta = meta;
    }

    /*@Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List list, Entity entity){
        this.setBlockBoundsBasedOnState(world, pos);
        super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos){
        int meta = PosUtil.getMetadata(pos, world);
        float minY = meta == 1 ? 0.5F : 0.0F;
        float maxY = meta == 1 ? 1.0F : 0.5F;
        this.setBlockBounds(0.0F, minY, 0F, 1.0F, maxY, 1.0F);
    }

    @Override
    public void setBlockBoundsForItemRender(){
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }*/

    @Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        if(facing.ordinal() == 1){
            return this.getStateFromMeta(meta);
        }
        if(facing.ordinal() == 0 || hitY >= 0.5F){
            return this.getStateFromMeta(meta+1);
        }
        return this.getStateFromMeta(meta);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
        return state.getValue(META) == 1 ? AABB_TOP_HALF : AABB_BOTTOM_HALF;
    }

    @Override
    protected ItemBlockBase getItemBlock(){
        return new TheItemBlock(this);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.COMMON;
    }

    @Override
    protected PropertyInteger getMetaProperty(){
        return META;
    }

    public static class TheItemBlock extends ItemBlockBase{

        public TheItemBlock(Block block){
            super(block);
            this.setHasSubtypes(false);
            this.setMaxDamage(0);
        }

        @Override
        public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
            if(stack.stackSize != 0 && playerIn.canPlayerEdit(pos.offset(facing), facing, stack)){
                IBlockState state = worldIn.getBlockState(pos);

                if(state.getBlock() == this.block){
                    BlockSlabs theBlock = (BlockSlabs)this.block;
                    if((facing == EnumFacing.UP && state.getValue(META) == 0 || facing == EnumFacing.DOWN && state.getValue(META) == 1)){
                        IBlockState newState = theBlock.fullBlock.getStateFromMeta(theBlock.meta);
                        AxisAlignedBB bound = newState.getCollisionBoundingBox(worldIn, pos);

                        if(bound != Block.NULL_AABB && worldIn.checkNoEntityCollision(bound.offset(pos)) && worldIn.setBlockState(pos, newState, 11)){
                            SoundType soundtype = theBlock.fullBlock.getSoundType();
                            worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume()+1.0F)/2.0F, soundtype.getPitch()*0.8F);
                            --stack.stackSize;
                        }

                        return EnumActionResult.SUCCESS;
                    }
                }

                return this.tryPlace(playerIn, stack, worldIn, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            }
            else{
                return EnumActionResult.FAIL;
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack){
            IBlockState state = worldIn.getBlockState(pos);

            if(state.getBlock() == this.block){
                if((side == EnumFacing.UP && state.getValue(META) == 0 || side == EnumFacing.DOWN && state.getValue(META) == 1)){
                    return true;
                }
            }

            return worldIn.getBlockState(pos.offset(side)).getBlock() == this.block || super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
        }

        private boolean tryPlace(EntityPlayer player, ItemStack stack, World worldIn, BlockPos pos){
            IBlockState iblockstate = worldIn.getBlockState(pos);

            if(iblockstate.getBlock() == this.block){
                BlockSlabs theBlock = (BlockSlabs)this.block;
                IBlockState newState = theBlock.fullBlock.getStateFromMeta(theBlock.meta);
                AxisAlignedBB bound = newState.getCollisionBoundingBox(worldIn, pos);

                if(bound != Block.NULL_AABB && worldIn.checkNoEntityCollision(bound.offset(pos)) && worldIn.setBlockState(pos, newState, 11)){
                    SoundType soundtype = theBlock.fullBlock.getSoundType();
                    worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume()+1.0F)/2.0F, soundtype.getPitch()*0.8F);
                    --stack.stackSize;
                }

                return true;
            }

            return false;
        }

        @Override
        public String getUnlocalizedName(ItemStack stack){
            return this.getUnlocalizedName();
        }
    }
}
