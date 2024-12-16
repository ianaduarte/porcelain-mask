package dev.ianaduarte.porcelain_mask.util;

import dev.ianaduarte.porcelain_mask.model.pose.PosingData;

public interface ExtModelWrapper {
	void setPoses(PosingData poses);
	PosingData getPoses();
}
