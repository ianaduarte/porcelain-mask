package one.ianthe.porcelain_mask.model;

public enum HoldingContext{
	MAINHAND_RIGHT,
	MAINHAND_LEFT,
	OFFHAND_RIGHT,
	OFFHAND_LEFT;
	
	public boolean isOffhand(){
		return this == OFFHAND_RIGHT || this == OFFHAND_LEFT;
	}
	
	public HoldingContext opposite(){
		return switch(this){
			case MAINHAND_RIGHT -> MAINHAND_LEFT;
			case MAINHAND_LEFT -> MAINHAND_RIGHT;
			case OFFHAND_RIGHT -> OFFHAND_LEFT;
			case OFFHAND_LEFT -> OFFHAND_RIGHT;
		};
	}
	public static HoldingContext get(boolean leftHand, boolean offHand){
		if(leftHand){
			return offHand? OFFHAND_LEFT : MAINHAND_LEFT;
		}
		return offHand? OFFHAND_RIGHT : MAINHAND_RIGHT;
	}
}
