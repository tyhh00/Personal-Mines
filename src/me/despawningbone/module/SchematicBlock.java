package me.despawningbone.module;

public class SchematicBlock {
	int xCenterOffset, yCenterOffset, zCenterOffset;
	int materialID;
	int data;
	
	
	SchematicBlock(int xCenterOffset, int yCenterOffset, int zCenterOffset, int materialID, int data) {
		this.xCenterOffset = xCenterOffset;
		this.yCenterOffset = yCenterOffset;
		this.zCenterOffset = zCenterOffset;
		this.materialID = materialID;
		this.data = data;
		
	}
	
	public int getXCenterOffset() {
		return xCenterOffset;
	}
	
	public int getYCenterOffset() {
		return yCenterOffset;
	}
	
	public int getZCenterOffset() {
		return zCenterOffset;
	}
	
	public int getData() {
		return data;
	}
	
	public int getMaterialID() {
		return materialID;
	}
	
}
