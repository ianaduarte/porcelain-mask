package one.ianthe.porcelain_mask.mixin.sweeping;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import one.ianthe.porcelain_mask.item.ISweeping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity{
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level){
		super(entityType, level);
	}
	
	@ModifyVariable(
		method = "attack",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
			shift = At.Shift.AFTER
		),
		ordinal = 3
	)
	private boolean porcelain_mask$sweepMixin(boolean value){
		if(getMainHandItem().getItem() instanceof ISweeping){
			return true;
		}
		return value;
	}
}
