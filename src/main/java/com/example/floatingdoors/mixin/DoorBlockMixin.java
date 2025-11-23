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

    @Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
    private void floatingdoors$allowFloatingLowerHalf(BlockState state,
                                                      LevelReader level,
                                                      BlockPos pos,
                                                      CallbackInfoReturnable<Boolean> cir) {
        // If vanilla already says it survives, don't touch it.
        if (Boolean.TRUE.equals(cir.getReturnValue())) {
            return;
        }

        // Only care about real door blocks with the HALF property.
        if (!state.hasProperty(DoorBlock.HALF)) {
            return;
        }

        // Only modify the LOWER half.
        if (state.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
            return;
        }

        BlockPos abovePos = pos.above();
        BlockPos belowPos = pos.below();

        BlockState aboveState = level.getBlockState(abovePos);
        BlockState belowState = level.getBlockState(belowPos);

        // Is there still a proper upper half?
        boolean hasTopHalf =
                aboveState.getBlock() instanceof DoorBlock
                        && aboveState.hasProperty(DoorBlock.HALF)
                        && aboveState.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER;

        // Did we lose the block below (support)?
        boolean lostSupport = belowState.isAir();

        // Only "rescue" the door when the *support block* is gone,
        // but the door itself (upper half) is still intact.
        if (hasTopHalf && lostSupport) {
            cir.setReturnValue(true);
        }
    }
}
