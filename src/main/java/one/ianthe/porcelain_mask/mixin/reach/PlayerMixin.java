package one.ianthe.porcelain_mask.mixin.reach;

import one.ianthe.porcelain_mask.item.IReachProvider;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity{
	@Shadow public abstract boolean isCreative();
	
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level){
		super(entityType, level);
	}
	
	@ModifyConstant(
		method = "attack",
		constant = @Constant(doubleValue = 9.0, ordinal = 0)
	)
	private double rangeMixin(double r){
		ItemStack stackInHand = getMainHandItem();
		
		if(stackInHand.getItem() instanceof IReachProvider reachProvider){
			return Mth.square(reachProvider.getReach(isCreative()));
		}
		return r;
	}
}
