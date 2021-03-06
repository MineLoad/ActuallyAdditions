/*
 * This file ("LivingDropEvent.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.event;

import de.ellpeck.actuallyadditions.mod.config.values.ConfigBoolValues;
import de.ellpeck.actuallyadditions.mod.items.InitItems;
import de.ellpeck.actuallyadditions.mod.items.metalists.TheMiscItems;
import de.ellpeck.actuallyadditions.mod.util.Util;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingDropEvent{

    @SubscribeEvent
    public void onEntityDropEvent(LivingDropsEvent event){
        if(event.getSource().getEntity() instanceof EntityPlayer){
            //Drop Solidified XP
            if(event.getEntityLiving() instanceof EntityCreature){
                if(Util.RANDOM.nextInt(15) <= 0){
                    event.getEntityLiving().entityDropItem(new ItemStack(InitItems.itemSolidifiedExperience, Util.RANDOM.nextInt(2)+1), 0);
                }
            }

            //Drop Cobwebs from Spiders
            if(ConfigBoolValues.DO_SPIDER_DROPS.isEnabled() && event.getEntityLiving() instanceof EntitySpider){
                if(Util.RANDOM.nextInt(80) <= 0){
                    event.getEntityLiving().entityDropItem(new ItemStack(Blocks.WEB, Util.RANDOM.nextInt(2)+1), 0);
                }
            }

            //Drop Wings from Bats
            if(ConfigBoolValues.DO_BAT_DROPS.isEnabled() && event.getEntityLiving() instanceof EntityBat){
                if(Util.RANDOM.nextInt(30) <= 0){
                    event.getEntityLiving().entityDropItem(new ItemStack(InitItems.itemMisc, Util.RANDOM.nextInt(2)+1, TheMiscItems.BAT_WING.ordinal()), 0);
                }
            }
        }
    }
}
