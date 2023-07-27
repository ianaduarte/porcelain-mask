package one.ianthe.porcelain_mask.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

public interface IContextualModelProvider{
	@Nullable
	ModelResourceLocation getModel(ItemDisplayContext context);
}