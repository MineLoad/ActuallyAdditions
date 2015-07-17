package ellpeck.actuallyadditions.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ellpeck.actuallyadditions.blocks.InitBlocks;
import ellpeck.actuallyadditions.config.values.ConfigIntValues;
import ellpeck.actuallyadditions.items.InitItems;
import ellpeck.actuallyadditions.items.metalists.TheMiscItems;
import ellpeck.actuallyadditions.network.sync.IPacketSyncerToClient;
import ellpeck.actuallyadditions.network.sync.PacketSyncerToClient;
import ellpeck.actuallyadditions.util.WorldUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityCanolaPress extends TileEntityInventoryBase implements IEnergyReceiver, IFluidHandler, IPacketSyncerToClient{

    public EnergyStorage storage = new EnergyStorage(40000);
    private int lastEnergyStored;

    public FluidTank tank = new FluidTank(2*FluidContainerRegistry.BUCKET_VOLUME);
    private int lastTankAmount;

    public static int energyUsedPerTick = ConfigIntValues.PRESS_ENERGY_USED.getValue();
    public int mbProducedPerCanola = ConfigIntValues.PRESS_MB_PRODUCED.getValue();

    public int maxTimeProcessing = ConfigIntValues.PRESS_PROCESSING_TIME.getValue();
    public int currentProcessTime;
    private int lastProcessTime;

    public TileEntityCanolaPress(){
        super(3, "canolaPress");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateEntity(){
        if(!worldObj.isRemote){
            if(this.isCanola(0) && this.mbProducedPerCanola <= this.tank.getCapacity()-this.tank.getFluidAmount()){
                if(this.storage.getEnergyStored() >= energyUsedPerTick){
                    this.currentProcessTime++;
                    this.storage.extractEnergy(energyUsedPerTick, false);
                    if(this.currentProcessTime >= this.maxTimeProcessing){
                        this.currentProcessTime = 0;

                        this.slots[0].stackSize--;
                        if(this.slots[0].stackSize == 0) this.slots[0] = null;

                        this.tank.fill(new FluidStack(InitBlocks.fluidCanolaOil, mbProducedPerCanola), true);
                        this.markDirty();
                    }
                }
            }
            else this.currentProcessTime = 0;

            WorldUtil.fillBucket(tank, slots, 1, 2);

            if(this.tank.getFluidAmount() > 0){
                WorldUtil.pushFluid(worldObj, xCoord, yCoord, zCoord, ForgeDirection.DOWN, this.tank);
                if(!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)){
                    WorldUtil.pushFluid(worldObj, xCoord, yCoord, zCoord, ForgeDirection.NORTH, this.tank);
                    WorldUtil.pushFluid(worldObj, xCoord, yCoord, zCoord, ForgeDirection.EAST, this.tank);
                    WorldUtil.pushFluid(worldObj, xCoord, yCoord, zCoord, ForgeDirection.SOUTH, this.tank);
                    WorldUtil.pushFluid(worldObj, xCoord, yCoord, zCoord, ForgeDirection.WEST, this.tank);
                }
            }

            if(this.storage.getEnergyStored() != this.lastEnergyStored || this.tank.getFluidAmount() != this.lastTankAmount | this.currentProcessTime != this.lastProcessTime){
                this.lastEnergyStored = this.storage.getEnergyStored();
                this.lastProcessTime = this.currentProcessTime;
                this.lastTankAmount = this.tank.getFluidAmount();
                this.sendUpdate();
            }
        }
    }

    public boolean isCanola(int slot){
        return this.slots[slot] != null && this.slots[slot].getItem() == InitItems.itemMisc && this.slots[slot].getItemDamage() == TheMiscItems.CANOLA.ordinal();
    }

    @SideOnly(Side.CLIENT)
    public int getTankScaled(int i){
        return this.tank.getFluidAmount() * i / this.tank.getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public int getProcessScaled(int i){
        return this.currentProcessTime * i / this.maxTimeProcessing;
    }

    @SideOnly(Side.CLIENT)
    public int getEnergyScaled(int i){
        return this.storage.getEnergyStored() * i / this.storage.getMaxEnergyStored();
    }

    @Override
    public void writeToNBT(NBTTagCompound compound){
        compound.setInteger("ProcessTime", this.currentProcessTime);
        this.storage.writeToNBT(compound);
        this.tank.writeToNBT(compound);
        super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        this.currentProcessTime = compound.getInteger("ProcessTime");
        this.storage.readFromNBT(compound);
        this.tank.readFromNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack){
        return (i == 0 && stack.getItem() == InitItems.itemMisc && stack.getItemDamage() == TheMiscItems.CANOLA.ordinal()) || (i == 1 && stack.getItem() == Items.bucket);
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side){
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side){
        return slot == 2 && FluidContainerRegistry.containsFluid(this.slots[0], new FluidStack(InitBlocks.fluidCanolaOil, FluidContainerRegistry.BUCKET_VOLUME));
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate){
        return this.storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from){
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from){
        return this.storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from){
        return true;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill){
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain){
        if(resource.getFluid() == InitBlocks.fluidCanolaOil) return this.tank.drain(resource.amount, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain){
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid){
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid){
        return from != ForgeDirection.UP;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from){
        return new FluidTankInfo[]{this.tank.getInfo()};
    }

    @Override
    public int[] getValues(){
        return new int[]{this.currentProcessTime, this.tank.getFluidAmount(), this.tank.getFluid() == null ? -1 : this.tank.getFluid().getFluidID(), this.storage.getEnergyStored()};
    }

    @Override
    public void setValues(int[] values){
        this.currentProcessTime = values[0];
        if(values[2] != -1){
            this.tank.setFluid(new FluidStack(FluidRegistry.getFluid(values[2]), values[1]));
        }
        else this.tank.setFluid(null);
        this.storage.setEnergyStored(values[3]);
    }

    @Override
    public void sendUpdate(){
        PacketSyncerToClient.sendPacket(this);
    }
}