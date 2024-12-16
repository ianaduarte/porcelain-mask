package dev.ianaduarte.porcelain_mask.mixin.interp;

import dev.ianaduarte.porcelain_mask.util.ExtRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class ExtRenderStateImpl implements ExtRenderState {
	@Unique private Entity owner;
	
	@Override
	public void setOwner(Entity owner) {
		this.owner = owner;
	}
	@Override
	public Entity getOwner() {
		return this.owner;
	}
}
