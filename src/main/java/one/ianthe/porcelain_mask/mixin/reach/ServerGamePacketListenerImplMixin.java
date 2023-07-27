package one.ianthe.porcelain_mask.mixin.reach;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import one.ianthe.porcelain_mask.item.IReachProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin{
	@Shadow @Final public static double MAX_INTERACTION_DISTANCE;
	@Shadow public ServerPlayer player;
	
	@Redirect(
		method = "handleInteract",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D",
			opcode = Opcodes.GETSTATIC
		)
	)
	private double getSqrAttackReach(){
		if(player.getMainHandItem().getItem() instanceof IReachProvider reachProvider){
			return Mth.square(reachProvider.getAttackReach(player.isCreative()));
		}
		return MAX_INTERACTION_DISTANCE;
	}
	@Redirect(
		method = "handleUseItemOn",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D",
			opcode = Opcodes.GETSTATIC
		)
	)
	private double getSqrReach(){
		if(player.getMainHandItem().getItem() instanceof IReachProvider reachProvider){
			return Mth.square(reachProvider.getReach(player.isCreative()));
		}
		return MAX_INTERACTION_DISTANCE;
	}
	
	@ModifyConstant(
		method = "handleUseItemOn",
		constant = @Constant(doubleValue = 64.0)
	)
	private double getSqrReach(double originalValue){
		ItemStack stackInHand = player.getMainHandItem();
		if(stackInHand.getItem() instanceof IReachProvider reachProvider){
			return Mth.square(reachProvider.getReach(player.isCreative()));
		}
		return originalValue;
	}
}
