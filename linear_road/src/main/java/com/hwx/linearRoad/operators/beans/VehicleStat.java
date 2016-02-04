package com.hwx.linearRoad.operators.beans;

import com.hwx.linearRoad.operators.PositionReportOp.PositionReportRec;
import com.hwx.linearRoad.operators.PositionReportOp.SegStatKey;
import com.hwx.linearRoad.operators.PositionReportOp.SegmentStat;

public class VehicleStat {

	private int Time = -1;
	private int VID = -1;
	private int Spd = -1;
	private int XWay = -1;
	private int Dir = -1;
	private int Seg = -1;
	private int Lane = -1;
	private int Pos = -1;
	private long Tin;
	
	private int runningTotal = 0;
	private int currentToll = 0;
	
	private int timesStopped = 0;
	
	//callable by extennal classes - used by the historic query bolt when interacting with the VehicleDAO
	public VehicleStat(int VID){
		this.VID = VID;
	}
	
	public VehicleStat(PositionReportRec rec){
		updateVehicleStat(rec);
	}
	
	//called everytime a car is found to be in a stopped state
	public void incrementStopped(SegmentStat seg){
		timesStopped++;
		//when a car is in the same spot for 4 total position reports (the orignal plus the next 3), it's considered a stopped car in that segment
		if(timesStopped==3){
			seg.carStopped(this);
		}
	}
	
	//called when a car starts moving after being in a stopped state
	public void resetStopped(SegmentStat seg){
		if(timesStopped>=3){
			seg.carMovingAgain(this);
		}
		timesStopped=0;
	}
	
	public int getTimesStopped(){
		return this.timesStopped;
	}
	
	/*
	 * 
	 */
	public void updateVehicleStat(PositionReportRec rec){
		this.Time = rec.getTime();
		this.VID = rec.getVID();
		this.Spd = rec.getSpd();
		this.XWay = rec.getXWay();
		this.Dir = rec.getDir();
		this.Seg = rec.getSeg();
		this.Lane = rec.getLane();
		this.Pos = rec.getPos();
		this.Tin = rec.getTin();
	}
	
	
	public int getTime() {
		return Time;
	}

	public void setTime(int time) {
		this.Time = time;
	}

	public int getVID() {
		return VID;
	}

	public void setVID(int vID) {
		this.VID = vID;
	}

	public int getXWay() {
		return XWay;
	}

	public void setXWay(int xWay) {
		this.XWay = xWay;
	}

	public int getDir() {
		return Dir;
	}

	public void setDir(int dir) {
		this.Dir = dir;
	}

	public int getSeg() {
		return Seg;
	}

	public void setSeg(int seg) {
		this.Seg = seg;
	}

	public int getLane() {
		return Lane;
	}

	public void setLane(int lane) {
		this.Lane = lane;
	}

	public int getPos() {
		return Pos;
	}

	public void setPos(int pos) {
		this.Pos = pos;
	}
	
	public int getSpd(){
		return this.Spd;
	}
	
	public void setSpd(int Spd){
		this.Spd = Spd;
	}
	
	public long getTin(){
		return this.Tin;
	}
	
	public void setTin(long Tin){
		this.Tin = Tin;
	}
	
	public int getCurrentToll(){
		return this.currentToll;
	}
	
	public void setCurrentToll(int currentToll){
		this.currentToll = currentToll;
	}
	
	public int getRunningTotal(){
		return this.runningTotal; 
	}
	
	public void setRunningTotal(int runningTotal){
		this.runningTotal = runningTotal;
	}

}
