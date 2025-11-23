package com.example.floatingdoors.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin {

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void floatingdoors$keepDoorWhenSupportIsGone(BlockState state,
                                                         LevelReader level,
                                                         BlockPos pos,
                                                         CallbackInfoReturnable<Boolean> cir) {
        // Only change behavior for the LOWER half of the door.
        // The UPPER half still uses vanilla logic so it breaks
        // if the bottom half is missing.
        if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            cir.setReturnValue(true);
            // setReturnValue already cancels the rest of the method.
        }
    }
}
