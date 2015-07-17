package ellpeck.actuallyadditions.inventory;

import ellpeck.actuallyadditions.blocks.InitBlocks;
import ellpeck.actuallyadditions.inventory.slot.SlotOutput;
import ellpeck.actuallyadditions.tile.TileEntityBase;
import ellpeck.actuallyadditions.tile.TileEntityFermentingBarrel;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

@InventoryContainer
public class ContainerFermentingBarrel extends Container{

    private TileEntityFermentingBarrel barrel;

    public ContainerFermentingBarrel(InventoryPlayer inventory, TileEntityBase tile){
        this.barrel = (TileEntityFermentingBarrel)tile;

        this.addSlotToContainer(new Slot(this.barrel, 0, 42, 74));
        this.addSlotToContainer(new SlotOutput(this.barrel, 1, 42, 43));
        this.addSlotToContainer(new Slot(this.barrel, 2, 118, 74));
        this.addSlotToContainer(new SlotOutput(this.barrel, 3, 118, 43));

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 9; j++){
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 97 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++){
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 155));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player){
        return this.barrel.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        final int inventoryStart = 3;
        final int inventoryEnd = inventoryStart+26;
        final int hotbarStart = inventoryEnd+1;
        final int hotbarEnd = hotbarStart+8;

        Slot theSlot = (Slot)this.inventorySlots.get(slot);

        if (theSlot != null && theSlot.getHasStack()){
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            //Other Slots in Inventory excluded
            if(slot >= inventoryStart){
                //Shift from Inventory
                if(FluidContainerRegistry.getContainerCapacity(new FluidStack(InitBlocks.fluidOil, 1), newStack) > 0){
                    if(!this.mergeItemStack(newStack, 3, 4, false)) return null;
                }
                else if(FluidContainerRegistry.containsFluid(newStack, new FluidStack(InitBlocks.fluidCanolaOil, 1))){
                    if(!this.mergeItemStack(newStack, 1, 2, false)) return null;
                }
                //

                else if(slot >= inventoryStart && slot <= inventoryEnd){
                    if(!this.mergeItemStack(newStack, hotbarStart, hotbarEnd+1, false)) return null;
                }
                else if(slot >= inventoryEnd+1 && slot < hotbarEnd+1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd+1, false)) return null;
            }
            else if(!this.mergeItemStack(newStack, inventoryStart, hotbarEnd+1, false)) return null;

            if (newStack.stackSize == 0) theSlot.putStack(null);
            else theSlot.onSlotChanged();

            if (newStack.stackSize == currentStack.stackSize) return null;
            theSlot.onPickupFromSlot(player, newStack);

            return currentStack;
        }
        return null;
    }
}