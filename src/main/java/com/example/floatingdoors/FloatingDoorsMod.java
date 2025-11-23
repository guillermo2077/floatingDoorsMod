package com.example.floatingdoors;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;

@Mod(FloatingDoorsMod.MOD_ID)
public class FloatingDoorsMod {
    public static final String MOD_ID = "floatingdoors";
    private static final Logger LOGGER = LogUtils.getLogger();

    public FloatingDoorsMod() {
        LOGGER.info("Floating Doors Mod initialized");
    }
}
