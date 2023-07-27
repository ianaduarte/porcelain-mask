package one.ianthe.porcelain_mask.mixin.reach;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import one.ianthe.porcelain_mask.item.IReachProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin{
	@Shadow @Final private Minecraft minecraft;
	
	@Inject(
		method = "getPickRange",
		at = @At("HEAD"),
		cancellable = true
	)
	private void getReach(CallbackInfoReturnable<Float> cir){
		LocalPlayer player = minecraft.player;
		if(player != null && player.getMainHandItem().getItem() instanceof IReachProvider reachProvider){
			cir.setReturnValue((float) reachProvider.getReach(player.isCreative()));
			cir.cancel();
		}
	}
}
