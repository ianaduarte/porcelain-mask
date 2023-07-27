package one.ianthe.porcelain_mask.mixin.reach;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import one.ianthe.porcelain_mask.item.IReachProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin{
	@Shadow @Final protected ServerPlayer player;
	
	@Redirect(
		method = "handleBlockBreakAction",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D",
			opcode = Opcodes.GETSTATIC
		)
	)
	private double getReach(){
		if(player.getMainHandItem().getItem() instanceof IReachProvider reachProvider){
			return Mth.square(reachProvider.getReach(player.isCreative()));
		}
		return ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE;
	}
}
