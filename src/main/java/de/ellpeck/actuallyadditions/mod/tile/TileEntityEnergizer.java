/*
 * This file ("TileEntityEnergizer.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityEnergizer extends TileEntityInventoryBase implements IEnergyReceiver, IEnergySaver{

    public EnergyStorage storage = new EnergyStorage(500000);
    private int lastEnergy;

    public TileEntityEnergizer(){
        super(2, "energizer");
    }

    @Override
    public void writeSyncableNBT(NBTTagCompound compound, boolean sync){
        this.storage.writeToNBT(compound);
        super.writeSyncableNBT(compound, sync);
    }

    @Override
    public void readSyncableNBT(NBTTagCompound compound, boolean sync){
        this.storage.readFromNBT(compound);
        super.readSyncableNBT(compound, sync);
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!this.worldObj.isRemote){
            if(this.slots[0] != null && this.slots[0].getItem() instanceof IEnergyContainerItem && this.slots[1] == null){
                if(this.storage.getEnergyStored() > 0){
                    int received = ((IEnergyContainerItem)this.slots[0].getItem()).receiveEnergy(this.slots[0], this.storage.getEnergyStored(), false);
                    this.storage.extractEnergy(received, false);
                }

                if(((IEnergyContainerItem)this.slots[0].getItem()).getEnergyStored(this.slots[0]) >= ((IEnergyContainerItem)this.slots[0].getItem()).getMaxEnergyStored(this.slots[0])){
                    this.slots[1] = this.slots[0].copy();
                    this.slots[0].stackSize--;
                    if(this.slots[0].stackSize <= 0){
                        this.slots[0] = null;
                    }
                }
            }

            if(this.lastEnergy != this.storage.getEnergyStored() && this.sendUpdateWithInterval()){
                this.lastEnergy = this.storage.getEnergyStored();
            }
        }
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack){
        return i == 0 && stack.getItem() instanceof IEnergyContainerItem;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side){
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side){
        return slot == 1;
    }

    @SideOnly(Side.CLIENT)
    public int getEnergyScaled(int i){
        return this.storage.getEnergyStored()*i/this.storage.getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate){
        return this.storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing from){
        return this.storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from){
        return this.storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from){
        return true;
    }

    @Override
    public int getEnergy(){
        return this.storage.getEnergyStored();
    }

    @Override
    public void setEnergy(int energy){
        this.storage.setEnergyStored(energy);
    }
}
