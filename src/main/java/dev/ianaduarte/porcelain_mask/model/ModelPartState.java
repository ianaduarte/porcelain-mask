package dev.ianaduarte.porcelain_mask.model;

import net.minecraft.client.model.geom.ModelPart;

public class ModelPartState {
	private static final int X_POS_OFFSET = 0;
	private static final int Y_POS_OFFSET = 1;
	private static final int Z_POS_OFFSET = 2;
	private static final int X_ROT_OFFSET = 3;
	private static final int Y_ROT_OFFSET = 4;
	private static final int Z_ROT_OFFSET = 5;
	public static final int OFFSET = 6;
	
	public float xPos;
	public float yPos;
	public float zPos;
	public float xRot;
	public float yRot;
	public float zRot;
	
	public void fetch(float[] data, int offset) {
		this.xPos = data[offset + X_POS_OFFSET];
		this.yPos = data[offset + Y_POS_OFFSET];
		this.zPos = data[offset + Z_POS_OFFSET];
		
		this.xRot = data[offset + X_ROT_OFFSET];
		this.yRot = data[offset + Y_ROT_OFFSET];
		this.zRot = data[offset + Z_ROT_OFFSET];
	}
	public void fetch(ModelPart part) {
		this.xPos = part.x;
		this.yPos = part.y;
		this.zPos = part.z;
		this.xRot = part.xRot;
		this.yRot = part.yRot;
		this.zRot = part.zRot;
	}
	public void set(float[] data, int offset) {
		data[offset + X_POS_OFFSET] = this.xPos;
		data[offset + Y_POS_OFFSET] = this.yPos;
		data[offset + Z_POS_OFFSET] = this.zPos;
		
		data[offset + X_ROT_OFFSET] = this.xRot;
		data[offset + Y_ROT_OFFSET] = this.yRot;
		data[offset + Z_ROT_OFFSET] = this.zRot;
	}
}
