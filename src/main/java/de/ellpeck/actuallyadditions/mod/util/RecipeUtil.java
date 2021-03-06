/*
 * This file ("RecipeUtil.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.util;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CrusherRecipe;
import de.ellpeck.actuallyadditions.api.recipe.LensNoneRecipe;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class RecipeUtil{

    public static LensNoneRecipe lastReconstructorRecipe(){
        List<LensNoneRecipe> list = ActuallyAdditionsAPI.reconstructorLensNoneRecipes;
        return list.get(list.size()-1);
    }

    public static CrusherRecipe lastCrusherRecipe(){
        List<CrusherRecipe> list = ActuallyAdditionsAPI.crusherRecipes;
        return list.get(list.size()-1);
    }

    public static IRecipe lastIRecipe(){
        List list = CraftingManager.getInstance().getRecipeList();
        Object recipe = list.get(list.size()-1);
        return recipe instanceof IRecipe ? (IRecipe)recipe : null;
    }
}
