package one.ianthe.porcelain_mask.mixin.reach;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;

import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import one.ianthe.porcelain_mask.item.IReachProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
public class GameRendererMixin{
	@Shadow @Final Minecraft minecraft;
	
	@Unique
	private ItemStack getItemInMainHand(){
		LocalPlayer player = minecraft.player;
		return player == null? ItemStack.EMPTY : minecraft.player.getMainHandItem();
	}
	
	@ModifyConstant(
		method = "pick",
		require = 1, allow = 1,
		constant = @Constant(doubleValue = 6.0)
	)
	private double getCreativeAttackRange(double originalValue){
		if(getItemInMainHand().getItem() instanceof IReachProvider reachProvider){
			return reachProvider.getAttackReach(true);
		}
		return originalValue;
	}
	
	@ModifyConstant(
		method = "pick",
		require = 1, allow = 1,
		constant = @Constant(doubleValue = 3.0)
	)
	private double getAttackRange(double originalValue){
		if(getItemInMainHand().getItem() instanceof IReachProvider reachProvider){
			return reachProvider.getAttackReach(false);
		}
		return originalValue;
	}
	
	@ModifyConstant(
		method = "pick",
		require = 1, allow = 1,
		constant = @Constant(doubleValue = 9.0)
	)
	private double getSqrAttackRange(double originalValue){
		if(getItemInMainHand().getItem() instanceof IReachProvider reachProvider){
			return Mth.square(reachProvider.getAttackReach(minecraft.gameMode != null && minecraft.gameMode.hasFarPickRange()));
		}
		return originalValue;
	}
}
