/*
 * This file ("GuiXPSolidifier.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.inventory.gui;

import de.ellpeck.actuallyadditions.mod.inventory.ContainerXPSolidifier;
import de.ellpeck.actuallyadditions.mod.network.PacketHandler;
import de.ellpeck.actuallyadditions.mod.network.gui.PacketGuiButton;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityBase;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityXPSolidifier;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import de.ellpeck.actuallyadditions.mod.util.StringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiXPSolidifier extends GuiContainer{

    private static final ResourceLocation resLoc = AssetUtil.getGuiLocation("guiXPSolidifier");
    private TileEntityXPSolidifier solidifier;
    private int x;
    private int y;
    private int z;
    private World world;

    public GuiXPSolidifier(InventoryPlayer inventory, TileEntityBase tile, int x, int y, int z, World world){
        super(new ContainerXPSolidifier(inventory, tile));
        this.solidifier = (TileEntityXPSolidifier)tile;
        this.xSize = 176;
        this.ySize = 93+86;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui(){
        super.initGui();

        GuiButton buttonOne = new GuiInputter.SmallerButton(0, this.guiLeft+62, this.guiTop+44, "1");
        GuiButton buttonFive = new GuiInputter.SmallerButton(1, this.guiLeft+80, this.guiTop+44, "5");
        GuiButton buttonTen = new GuiInputter.SmallerButton(2, this.guiLeft+99, this.guiTop+44, "10");
        GuiButton buttonTwenty = new GuiInputter.SmallerButton(3, this.guiLeft+62, this.guiTop+61, "20");
        GuiButton buttonThirty = new GuiInputter.SmallerButton(4, this.guiLeft+80, this.guiTop+61, "30");
        GuiButton buttonForty = new GuiInputter.SmallerButton(5, this.guiLeft+99, this.guiTop+61, "40");
        GuiButton buttonFifty = new GuiInputter.SmallerButton(6, this.guiLeft+62, this.guiTop+78, "50");
        GuiButton buttonSixtyFour = new GuiInputter.SmallerButton(7, this.guiLeft+80, this.guiTop+78, "64");
        GuiButton buttonAll = new GuiInputter.SmallerButton(8, this.guiLeft+99, this.guiTop+78, "All");

        this.buttonList.add(buttonOne);
        this.buttonList.add(buttonFive);
        this.buttonList.add(buttonTen);
        this.buttonList.add(buttonTwenty);
        this.buttonList.add(buttonThirty);
        this.buttonList.add(buttonForty);
        this.buttonList.add(buttonFifty);
        this.buttonList.add(buttonSixtyFour);
        this.buttonList.add(buttonAll);
    }

    @Override
    public void drawScreen(int x, int y, float f){
        super.drawScreen(x, y, f);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y){
        AssetUtil.displayNameString(this.fontRendererObj, this.xSize, -10, this.solidifier.getName());
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int x, int y){
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(AssetUtil.GUI_INVENTORY_LOCATION);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop+93, 0, 0, 176, 86);

        this.mc.getTextureManager().bindTexture(resLoc);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 93);

        this.drawCenteredString(this.fontRendererObj, Integer.toString(this.solidifier.amount), this.guiLeft+88, this.guiTop+30, StringUtil.DECIMAL_COLOR_WHITE);
    }

    @Override
    public void actionPerformed(GuiButton button){
        PacketHandler.theNetwork.sendToServer(new PacketGuiButton(this.x, this.y, this.z, this.world, button.id, Minecraft.getMinecraft().thePlayer));
        this.solidifier.onButtonPressed(button.id, Minecraft.getMinecraft().thePlayer);
    }
}