/*
 * This file ("TheMiscItems.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items.metalists;

import net.minecraft.item.EnumRarity;

public enum TheMiscItems{

    PAPER_CONE("PaperCone", EnumRarity.COMMON),
    MASHED_FOOD("MashedFood", EnumRarity.UNCOMMON),
    KNIFE_BLADE("KnifeBlade", EnumRarity.COMMON),
    KNIFE_HANDLE("KnifeHandle", EnumRarity.COMMON),
    DOUGH("Dough", EnumRarity.COMMON),
    QUARTZ("BlackQuartz", EnumRarity.EPIC),
    RING("Ring", EnumRarity.UNCOMMON),
    COIL("Coil", EnumRarity.COMMON),
    COIL_ADVANCED("CoilAdvanced", EnumRarity.UNCOMMON),
    RICE_DOUGH("RiceDough", EnumRarity.UNCOMMON),
    TINY_COAL("TinyCoal", EnumRarity.COMMON),
    TINY_CHAR("TinyCharcoal", EnumRarity.COMMON),
    RICE_SLIME("RiceSlime", EnumRarity.UNCOMMON),
    CANOLA("Canola", EnumRarity.UNCOMMON),
    CUP("Cup", EnumRarity.UNCOMMON),
    BAT_WING("BatWing", EnumRarity.RARE),
    DRILL_CORE("DrillCore", EnumRarity.UNCOMMON),
    BLACK_DYE("BlackDye", EnumRarity.EPIC),
    LENS("Lens", EnumRarity.UNCOMMON),
    ENDER_STAR("EnderStar", EnumRarity.EPIC);

    public final String name;
    public final EnumRarity rarity;

    TheMiscItems(String name, EnumRarity rarity){
        this.name = name;
        this.rarity = rarity;
    }
}