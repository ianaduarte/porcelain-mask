package one.ianthe.porcelain_mask.mixin.reach;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import one.ianthe.porcelain_mask.item.IReachProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Item.class)
public class ItemMixin{
	@ModifyConstant(
		method = "getPlayerPOVHitResult",
		constant = @Constant(doubleValue = 5.0)
	)
	private static double getExtReach(double original, Level level, Player player, ClipContext.Fluid fluidMode){
		if(player.getMainHandItem().getItem() instanceof IReachProvider reachProvider){
			return reachProvider.getReach(true);
		}
		return original;
	}
}