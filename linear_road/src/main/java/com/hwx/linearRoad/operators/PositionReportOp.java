package com.hwx.linearRoad.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.log4j.Logger;

import com.hwx.linearRoad.hbase.VehicleDAO;
import com.hwx.linearRoad.operators.beans.AccidentNotification;
import com.hwx.linearRoad.operators.beans.Notification;
import com.hwx.linearRoad.operators.beans.TollNotification;
import com.hwx.linearRoad.operators.beans.VehicleStat;

public class PositionReportOp {
	
	private static final Logger logger = Logger.getLogger(PositionReportOp.class);
	
	private final int minSecCutoff = 60;

	
	private final VehicleDAO vehicleDAO;
	
	private Map<Integer, VehicleStat> vehicleCache = null;
	private Map<SegStatKey, SegmentStat> segmentCache = null;
	
	private int currentMinute = 1;
	
	private TollNotification tollNotification = null;
	private AccidentNotification accidentNotification = null;
	

	public PositionReportOp(Connection conn) {
		this.vehicleDAO = new VehicleDAO(conn);
		vehicleCache = new HashMap<Integer, VehicleStat>();
		segmentCache = new HashMap<SegStatKey, SegmentStat>();
	}
	
	
	
	public void process(PositionReportRec rec){
		
		//reset the Notificaitons
		this.tollNotification = null;
		this.accidentNotification = null;
		
		//see if we hit the minute marker
		if(rec.Time%minSecCutoff==0 && rec.Time > 0 && currentMinute==(int)rec.Time/minSecCutoff){
//			logger.info(String.format("ROLLING THE BUCKETS ", rec.Time));
			//update current minute e.g. Time 0-60 = 1, 61-120 = 2, etc...
			currentMinute = (int)rec.Time/minSecCutoff+1;
			
			//roll the segment stats buckets
			for(SegmentStat stat : segmentCache.values()){
				stat.rollSegmentStat();
			}
		}
		
		//The vehicle and segment status objects from the cache
		VehicleStat vehicleStat;
		SegmentStat segmentStat;
		
		//Check the vehicleCache
		if(!vehicleCache.containsKey(rec.VID)){
			//if the vehicle isn't in the cache, try to pull it from HBase - this vehicle may have driven on a different XWay...
			vehicleStat = new VehicleStat(rec);
			vehicleDAO.getVehicleStat(vehicleStat);
			//if we didn't find it in HBase, update this one with the record info
//			if(vehicleStat.getVID()==-1)
				vehicleStat.updateVehicleStat(rec);
			//add it to the vehicleCache
			vehicleCache.put(rec.VID, vehicleStat);
			
			//find or create a new SegmentStat
			segmentStat = fetchSegmentStat(rec);
			
			//add the current toll to the vehicleStat
			segmentStat.carEnteringSegment(vehicleStat);
			
		}else{
//			Notification notification = null;
			
			
			//The vehicle was found in the cache
			vehicleStat = vehicleCache.get(rec.VID);
			
			//compare the incoming PositionReportRec against the vehicleStat object from the cache
			ComparisonResult res = compareVehicleStatus(rec, vehicleStat);
			
			SegmentStat currentSeg = fetchSegmentStat(rec);
			SegmentStat previousSeg = segmentCache.get(new SegStatKey(vehicleStat.getXWay(), vehicleStat.getDir(), vehicleStat.getSeg()));
			if(previousSeg == null){
				logger.warn(String.format("Something not right - previous Segment was not foud for SegStatKey %d,%d,%d in cache %s, thread: %s", vehicleStat.getXWay(), vehicleStat.getDir(), vehicleStat.getSeg(), this, Thread.currentThread().getId()));
				previousSeg = new SegmentStat(new SegStatKey(vehicleStat.getXWay(), vehicleStat.getDir(), vehicleStat.getSeg()));
				segmentCache.put(previousSeg.getKey(), previousSeg);
			}
			
			/*
			 * NOTE: 
			 * I think it's possible to immediately update the vehicleStat object with the current info from the PositionReportRec - I don't think there are further comparisons between the state of these two objects
			 * We can then use teh vehicleStat object for the remaineder of this method
			 */
			vehicleStat.updateVehicleStat(rec);
			
			
			//evaluate position, then movement - rec and vehicle stat
			switch(res.posState){
			case entering:
				currentSeg.carEnteringSegment(vehicleStat);
				
				if(res.movState==MovementState.notPreviouslyStopped){
					//not previously stopped
					//do nothing - most common scenario so top of If tree
				}else if(res.movState==MovementState.isStopped){
					//is stopped
					vehicleStat.incrementStopped(currentSeg);
				}else{
					//was stopped
					vehicleStat.resetStopped(currentSeg);
				}
				
				break;
			
			case exiting:
				currentSeg.carExitingXWay(vehicleStat);
				//Stopped scenarios don't apply - one a vehicle appears in lane 4, no more reports for this vehicle in this segment will appear.
				
				if(vehicleCache.containsKey(vehicleStat.getVID()))
					vehicleCache.remove(vehicleStat.getVID());
				break;
				
			case sameSegment:
				currentSeg.carStillInSegment(vehicleStat);
				
				if(res.movState==MovementState.notPreviouslyStopped){
					//not previously stopped
					//do nothing - most common scenario so top of If tree
				}else if(res.movState==MovementState.isStopped){
					//is stopped
					vehicleStat.incrementStopped(currentSeg);
				}else{
					//was stopped
					vehicleStat.resetStopped(currentSeg);
				}
				
				//update the vehicleStat object in the cache
				vehicleStat.updateVehicleStat(rec);
				break;
				
			case changedSegment:
				previousSeg.carLeavingSegment(vehicleStat);
				currentSeg.carEnteringSegment(vehicleStat);
				
				if(res.movState==MovementState.notPreviouslyStopped){
					//not previously stopped
					//do nothing - most common scenario so top of If tree
				}else if(res.movState==MovementState.isStopped){
					//Not a possible combination
					logger.warn(String.format("IMPOSSIBLE SCENARIO - Vehicle %d found to be in a stopped state AND changed segments @ %d", rec.VID, rec.Time));
				}else{
					//was stopped
					vehicleStat.resetStopped(previousSeg);
				}
				
				//update the vehicleStat object in the cache
				vehicleStat.updateVehicleStat(rec);
				break;
				
			case changedAndExiting:
				
				if(res.movState==MovementState.notPreviouslyStopped){
					//not previously stopped
					//do nothing - most common scenario so top of If tree
				}else if(res.movState==MovementState.isStopped){
					//Not a possible combination
					logger.warn(String.format("IMPOSSIBLE SCENARIO - Vehicle %d found to be in a stopped state AND changed segments @ %d", rec.VID, rec.Time));
				}else{
					//was stopped
					vehicleStat.resetStopped(previousSeg);
				}
				
				//remvoe the car from the previous segment
				previousSeg.carLeavingSegment(vehicleStat);	

				if(vehicleCache.containsKey(rec.VID))
					vehicleCache.remove(rec.VID);
				break;
			
			}
//			return notification;
			
		}
		
	}
	
	
	/*
	 * This method comapres the current Position Record to the previous VehicleStatus and generates a comparison result
	 */
	private ComparisonResult compareVehicleStatus(PositionReportRec rec, VehicleStat stat){
		
		ComparisonResult res = new ComparisonResult();
		
		//assess it's movement state
		if(rec.Pos==stat.getPos() && rec.Lane==stat.getLane() && rec.Dir==stat.getDir() && rec.XWay==stat.getXWay()){
			res.movState = MovementState.isStopped;
		}else if(stat.getTimesStopped()>0){
			res.movState = MovementState.wasStopped;
		}else{
			res.movState = MovementState.notPreviouslyStopped;
		}
		
		//assess it's position state
		
		if(rec.Lane==0){
			res.posState = PositionState.entering;
		}else if(rec.Lane==4){
			if(rec.Seg==stat.getSeg()){
				res.posState = PositionState.exiting;
			}else{
				res.posState = PositionState.changedAndExiting;
			}	
		}else if(rec.Seg==stat.getSeg()){
			res.posState = PositionState.sameSegment;
		}else{
			res.posState = PositionState.changedSegment;
		}
		
		return res;
	}
	
	
	private SegmentStat fetchSegmentStat(PositionReportRec rec){
		/*
		 * get the segmentStat where the vehicle is entering
		 * 	if there is none, create a new one and add it to the cache
		 * 	update the segment stat with the car that is entering which will also set the toll
		 */
		if(!segmentCache.containsKey(rec.getSegStatKey())){
			segmentCache.put(rec.getSegStatKey(), new SegmentStat(rec.getSegStatKey()));
//			logger.info(String.format("adding SegStatKey %d,%d,%d to the cache %s, thread %s", rec.getSegStatKey().Xway, rec.getSegStatKey().Dir, rec.getSegStatKey().Seg, this, Thread.currentThread().getId()));
		}
		return segmentCache.get(rec.getSegStatKey());
	}
	
	
	TollNotification getTollNotification(){
		return this.tollNotification;
	}
	
	AccidentNotification getAccidentNotification(){
		return this.accidentNotification;
	}
	
	
	enum PositionState{entering, exiting, sameSegment, changedSegment, changedAndExiting}
	enum MovementState{isStopped, wasStopped, notPreviouslyStopped}
	class ComparisonResult{
		private PositionState posState;
		private MovementState movState;
	}
	
	
	
	/**
	 * 
	 * @author rtempleton
	 *
	 */
	public class PositionReportRec{
		
		//Type = 0, Time, VID, Spd, XWay, Lane, Dir, Seg, Pos, Tin
		int Time;
		int VID;
		int Spd;
		int XWay;
		int Lane;
		int Dir;
		int Seg;
		int Pos;
		long Tin;
		
		public PositionReportRec(int Time, int VID, int Spd, int Xway, int Lane, int Dir, int Seg, int Pos, long Tin){
			this.Time = Time;
			this.VID = VID;
			this.Spd = Spd;
			this.XWay = Xway;
			this.Lane = Lane;
			this.Dir = Dir;
			this.Seg = Seg;
			this.Pos = Pos;
			this.Tin = Tin;
		}

		
		private SegStatKey getSegStatKey(){
			return new SegStatKey(XWay, Dir, Seg);
		}


		public int getTime() {
			return Time;
		}


		public void setTime(int time) {
			Time = time;
		}


		public int getVID() {
			return VID;
		}


		public void setVID(int vID) {
			VID = vID;
		}


		public int getSpd() {
			return Spd;
		}


		public void setSpd(int spd) {
			Spd = spd;
		}


		public int getXWay() {
			return XWay;
		}


		public void setXWay(int xWay) {
			XWay = xWay;
		}


		public int getLane() {
			return Lane;
		}


		public void setLane(int lane) {
			Lane = lane;
		}


		public int getDir() {
			return Dir;
		}


		public void setDir(int dir) {
			Dir = dir;
		}


		public int getSeg() {
			return Seg;
		}


		public void setSeg(int seg) {
			Seg = seg;
		}


		public int getPos() {
			return Pos;
		}


		public void setPos(int pos) {
			Pos = pos;
		}


		public long getTin() {
			return Tin;
		}


		public void setTin(long tin) {
			Tin = tin;
		}
		
	}
	
	
	
	/**
	 * Object used to store the stats on the given Segment. This object is keyed by the XWay, Direction and Segment
	 * which is obtainable by calling the getSegStatKey method on the PositionReportRec object
	 * 
	 * @author rtempleton
	 *
	 */
	public class SegmentStat{
		
		private final SegStatKey key;
		private final ArrayList<Integer> currentCarsList = new ArrayList<Integer>();
		private final ArrayList<Integer> stoppedCars = new ArrayList<Integer>();
		private final ArrayList<SegStatKey> upstreamAccidentSegments = new ArrayList<SegStatKey>();
		private boolean inAccidentState = false;
		
		private int distinctCars = 0;
		private int historyCars = 0;
		
		private int cumulativeCarCount = 0;
		private int cumulativeSpeed = 0;
		
		private int carCount1 = 0;
		private int sumSpeed1 = 0;
		
		private int carCount2 = 0;
		private int sumSpeed2 = 0;
		
		private int carCount3 = 0;
		private int sumSpeed3 = 0;
		
		private int carCount4 = 0;
		private int sumSpeed4 = 0;
		
		private int carCount5 = 0;
		private int sumSpeed5 = 0;
		
		public SegmentStat(SegStatKey key){
			this.key = key;
		}
		
		/**
		 * Resets the stats for this Segment
		 */
		private void rollSegmentStat(){
			
			historyCars = distinctCars;
			
			carCount5 = carCount4;
			sumSpeed5 = sumSpeed4;
			
			carCount4 = carCount3;
			sumSpeed4 = sumSpeed3;
			
			carCount3 = carCount2;
			sumSpeed3 = sumSpeed2;
			
			carCount2 = carCount1;
			sumSpeed2 = sumSpeed1;
			
			carCount1 = cumulativeCarCount;
			sumSpeed1 = cumulativeSpeed;
			
			distinctCars = 0;
			cumulativeCarCount = 0;
			cumulativeSpeed = 0;

		}
		
		/**
		 * increments the car list and counters
		 * returns the toll for this segment
		 */
		private void carEnteringSegment(VehicleStat vehicle){
			//only return a toll the First time a car enters a segment. Covers the event a vehicle is in lane 0 two consectutive reports.
			if(!currentCarsList.contains(vehicle.getVID())){
				getToll(vehicle);
				distinctCars++;
//				logger.info(String.format("Entering Segment %d - Toll Calculation: Vehicle %d, Toll %f", vehicle.Seg, vehicle.VID, vehicle.currentToll));
				currentCarsList.add(vehicle.getVID());
			}
			carStillInSegment(vehicle);
		}
		
		/**
		 * Accumulates the speed and vehicle counters
		 * @param rec PositionReportRec
		 */
		private void carStillInSegment(VehicleStat vehicle){
			cumulativeCarCount++;
			cumulativeSpeed += vehicle.getSpd();
		}
		
		/**
		 * applies the toll from previous segment to the running total
		 * decrements the vehicle from this segments car list
		 * @param VehicleStat
		 */
		private void carLeavingSegment(VehicleStat stat){
			stat.setRunningTotal(stat.getRunningTotal()+stat.getCurrentToll());
			if(currentCarsList.contains(stat.getVID()))
				currentCarsList.remove(currentCarsList.indexOf(stat.getVID()));
//			logger.info(String.format("Vehicle %d is leaving segment %d, toll %f was applied", stat.VID, (stat.Dir==0)?stat.Seg-1:stat.Seg+1, stat.currentToll));
			vehicleDAO.putVehicleStat(stat);
		}
		
		
		/**
		 * Called when car exits a segment - lane == 4
		 * @param rec
		 */
		private void carExitingXWay(VehicleStat vehicle){
			if (currentCarsList.contains(vehicle.getVID())){
				currentCarsList.remove(currentCarsList.indexOf(vehicle.getVID()));
			}
		}
		
		
		private void getToll(VehicleStat vehicle){
			//get the toll no matter what
			calculateToll(vehicle);
			
			tollNotification = new TollNotification(vehicle, this);
			
			if(inAccidentState){
				//pass this details of this segment
				accidentNotification = new AccidentNotification(vehicle, this);
			}else if(upstreamAccidentSegments.size()>0){
				//pass the details of the segment where the accident is
				accidentNotification = new AccidentNotification(vehicle,segmentCache.get(upstreamAccidentSegments.get(0)));
			}
			
			
			

			
		}
		
		
		private void calculateToll(VehicleStat vehicle){
//			if there were less than 50 cars in the sement from the last minute or the LAV was over 40, there's no toll
			logger.info((String.format("###Segment %d, Time %d: Generating toll for vehicle %d, CarCount: %d  Lav: %d", vehicle.getSeg(), vehicle.getTime(), vehicle.getVID(), historyCars, getLAV())));
			logger.info((String.format("###CarCount1: %d SumSpeed1: %d CarCount2: %d SumSpeed2: %d CarCount3: %d SumSpeed3: %d CarCount4: %d SumSpeed4: %d CarCount5: %d SumSpeed5: %d", carCount1, sumSpeed1, carCount2, sumSpeed2, carCount3, sumSpeed3, carCount4, sumSpeed4, carCount5, sumSpeed5)));
			
			if(historyCars <= 50 || getLAV()>=40){
//				logger.info(String.format("No congestion => Car count: %d, LAV: %f", distinctCarCount1, getLAV()));
				vehicle.setCurrentToll(0);
			}else {
				vehicle.setCurrentToll( (int)Math.round((2 * Math.pow((carCount1-50), 2))) );
			}
		}
		
		public int getLAV(){
			//no divide by 0 exceptions
			if (carCount5+carCount4+carCount3+carCount2+carCount1 == 0)
				return 0;
			return Math.round((sumSpeed5+sumSpeed4+sumSpeed3+sumSpeed2+sumSpeed1)/(carCount5+carCount4+carCount3+carCount2+carCount1));
		}
		
		public SegStatKey getKey(){
			return this.key;
		}
		
		/*
		 * Called when a car is found to be stopped in a given segment
		 */
		public void carStopped(VehicleStat vehicle){
			if(!stoppedCars.contains(vehicle.getVID())){
				stoppedCars.add(vehicle.getVID());
				//if this segment isn't isn't in an accident state, evaluate it
				if(!inAccidentState){
					evaluateAccidentState();
					//if it's now in an accient state, clear the tolls from the cars currenlty in this segment and notify the upstream segments so their tolls can be negated
					if(inAccidentState){
						notifyUpStreamStremSegmentsAccident(vehicle);
					}
				}
			}
		}
		
		/*
		 * Called when a car starts moving after being in a stopped state
		 * remove it from the list an reevaluate this segments accident state
		 */
		public void carMovingAgain(VehicleStat vehicle){
			if(stoppedCars.contains(vehicle.getVID()))
				stoppedCars.remove(stoppedCars.indexOf(vehicle.getVID()));
			if(inAccidentState){
				evaluateAccidentState();
				if(!inAccidentState){
					notifyUpStreamStremSegmentsClear(vehicle);
				}
			}
		}
		
		
		private void evaluateAccidentState(){
			//compare all the cars in the stoppedCars list to see if there is at least two cars in the same lane and positon, then there is an accident
			if(stoppedCars.size()>1){
				VehicleStat veh1, veh2;
				//compare each stopped car to every other stopped car
				for (int i = 0;i<stoppedCars.size()-1;i++){
					veh1 = vehicleCache.get(stoppedCars.get(i));
					if(veh1==null)
						continue;
					for (int j = i+1;j<stoppedCars.size();j++){
						veh2 = vehicleCache.get(stoppedCars.get(j));
						if(veh2==null)
							continue;
						if(veh1.getLane() == veh2.getLane() && veh1.getPos() == veh2.getPos()){
							inAccidentState=true;
							return;
						}
					}
				}
			}
			//if we looped through the list and no cars are in an accident state, clear the flag
			inAccidentState=false;
		}
		

		
		//if there's an accident in this segment, add this segment to the accident list of the 4 upstream segments
		private void notifyUpStreamStremSegmentsAccident(VehicleStat vehicle){
			//lane 0 == East, lane 1 == West
			SegStatKey segKey;
			int seg = vehicle.getSeg();
			
			for(int cntr = 0;cntr<4 && seg>=0;cntr++){
				//if direction is eastbound decrement the segment value otherwise increment it
				seg = (vehicle.getDir()==0) ? --seg : ++seg;
				segKey = new SegStatKey(vehicle.getXWay(), 0, seg);
				SegmentStat stat = segmentCache.get(segKey);
				if(stat!=null)
					stat.incrementUpstreamAccidents(this);
			}
		}
		
		
		/*
		 * if an accident clears, remove this segments key from the accident list for the 4 upstream segments
		 */
		private void notifyUpStreamStremSegmentsClear(VehicleStat vehicle){
			//lane 0 == East, lane 1 == West
			SegStatKey segKey;
			int seg = vehicle.getSeg();
			
			for(int cntr = 0;cntr<4 && seg>=0;cntr++){
				//if direction is eastbound decrement the segment value otherwise increment it
				seg = (vehicle.getDir()==0) ? --seg : ++seg;
				segKey = new SegStatKey(vehicle.getXWay(), 0, seg);
				SegmentStat stat = segmentCache.get(segKey);
				if(stat!=null)
					stat.decrementUpsrteamAccidents(this);
			}
			

		}
		
		/*
		 * called when an upstream accident occurs, add that segmentStatKey to the list of upstream accidents
		 */
		private void incrementUpstreamAccidents(SegmentStat stat){
			if(!upstreamAccidentSegments.contains(stat.getKey()))
				upstreamAccidentSegments.add(stat.getKey());
		}
		
		/*
		 * called when an upstream accident state clears, remove that segmentStatKey from the list of upstream accidents
		 */
		private void decrementUpsrteamAccidents(SegmentStat stat){
			if (upstreamAccidentSegments.contains(stat.key))
				upstreamAccidentSegments.remove(upstreamAccidentSegments.indexOf(stat.key));
		}
		
		
	}
	
	
	/**
	 * 
	 * @author rtempleton
	 *
	 */
	public class SegStatKey{
		
		private final int Xway;
		private final int Dir;
		private final int Seg;
		
		public SegStatKey(int Xway, int Dir, int Seg){
			this.Xway = Xway;
			this.Dir = Dir;
			this.Seg = Seg;
		}
		
		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof SegStatKey)) return false;
	        SegStatKey key = (SegStatKey) o;
	        return Xway == key.Xway && Dir == key.Dir && Seg == key.Seg;
	    }
		
		@Override
	    public int hashCode() {
	        return Xway*3 + Dir*5 + (Seg*7);
	    }

		public int getXway() {
			return Xway;
		}

		public int getDir() {
			return Dir;
		}

		public int getSeg() {
			return Seg;
		}
		
		
		
	}
	

}
