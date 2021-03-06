/*
 * This file ("ModUtil.java") is part of the Actually Additions Mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense/
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2016 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.util;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModUtil{

    public static final String VERSION = "@VERSION@"; //build.gradle

    public static final String MOD_ID = ActuallyAdditionsAPI.MOD_ID;
    public static final String NAME = "Actually Additions";

    public static final Logger LOGGER = LogManager.getLogger(NAME);
}
