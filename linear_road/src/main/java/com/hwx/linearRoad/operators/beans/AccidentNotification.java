package com.hwx.linearRoad.operators.beans;

import java.io.Serializable;

import com.hwx.linearRoad.operators.PositionReportOp.SegmentStat;

public class AccidentNotification implements Notification, Serializable {

	private static final long serialVersionUID = 1L;
	private final int Type = 1;
	private final int VID;
	private final int Time;
	private final float Emit;
	private final int Xway;
	private final int Seg;
	private final int Dir;
	
	
	
	public AccidentNotification(VehicleStat vehicle, SegmentStat segment){
		
		this.VID = vehicle.getVID();
		this.Time = vehicle.getTime();
		this.Emit = ((System.currentTimeMillis() - vehicle.getTin())/1000f)+Time;
		this.Xway = segment.getKey().getXway();
		this.Seg = segment.getKey().getSeg();
		this.Dir = segment.getKey().getDir();
		
	}


	//formats the values of this Notification in the order expected
	public String asString(){
		//1, a.getVID(), a.getTime(), a.getEmit(), a.getXway(), a.getSeg(), a.getDir()
		return(String.format("1,%d,%d,%4.10f,%d,%d,%d", VID, Time, Emit, Xway, Seg, Dir));
	}

	public int getType() {
		return Type;
	}



	public int getVID() {
		return VID;
	}



	public int getTime() {
		return Time;
	}



	public float getEmit() {
		return Emit;
	}



	public int getXway() {
		return Xway;
	}



	public int getSeg() {
		return Seg;
	}



	public int getDir() {
		return Dir;
	}
}
