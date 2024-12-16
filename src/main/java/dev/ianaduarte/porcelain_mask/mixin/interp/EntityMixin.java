package dev.ianaduarte.porcelain_mask.mixin.interp;

import dev.ianaduarte.porcelain_mask.util.ExtEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class EntityMixin implements ExtEntity {
	@Unique float[] animData;
	
	@Override
	public void initializeData(int size) {
		this.animData = new float[size];
	}
	
	@Override
	public void setData(float[] data) {
		this.animData = data;
	}
	@Override
	public float[] getData() {
		return this.animData;
	}
}
