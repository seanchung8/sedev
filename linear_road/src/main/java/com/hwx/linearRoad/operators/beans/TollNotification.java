package com.hwx.linearRoad.operators.beans;

import java.io.Serializable;

import com.hwx.linearRoad.operators.PositionReportOp.SegmentStat;

public class TollNotification implements Notification, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int Type = 0;
	private final int VID;
	private final int Seg;
	private final int Time;
	private final float Emit;
	private final int Spd;
	private final int Toll;
	
//	public TollNotification(int VID, int Seg, int Time, double Emit, int Spd, int Toll){
//		this.VID = VID;
//		this.Seg = Seg;
//		this.Time = Time;
//		this.Emit = Emit;
//		this.Spd = Spd;
//		this.Toll = Toll;
//	}
	
	public TollNotification(VehicleStat vehicle, SegmentStat segment){
		this.VID = vehicle.getVID();
		this.Seg = vehicle.getSeg();
		this.Time = vehicle.getTime();
		this.Emit = ((System.currentTimeMillis() - vehicle.getTin())/1000f)+Time;
		this.Spd = segment.getLAV();
		this.Toll = vehicle.getCurrentToll();
	}

	public String asString(){
		//0, t.getVID(), t.getTime(), t.getEmit(), t.getSpd(), t.getToll()
		return String.format("0,%d,%d,%4.10f,%d,%d", VID, Time, Emit, Spd, Toll);
	}
	
	public int getType() {
		return Type;
	}

	public int getVID() {
		return VID;
	}

	public int getSeg() {
		return Seg;
	}

	public int getTime() {
		return Time;
	}

	public float getEmit() {
		return Emit;
	}

	public int getSpd() {
		return Spd;
	}

	public int getToll() {
		return Toll;
	}


}
