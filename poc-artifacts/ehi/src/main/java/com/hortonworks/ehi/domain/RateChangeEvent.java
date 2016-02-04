/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hortonworks.ehi.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;

import org.apache.solr.client.solrj.beans.Field;
import org.joda.time.DateTime;

public class RateChangeEvent {
	@Field
	String id;
	@Field
	private String lor;
	@Field
	private String rentalLocation;
	@Field
	private String arrivalDate;
	@Field
	private Date timestamp;
	@Field
	private double totalRate;
	@Field
	private double prevTotalRate;
	@Field
	private double netRateChange;
	@Field
	private String carClass;
	@Field
	private String companyName;
	@Field
	private String rateCode;
	@Field
	private String airportCode;
	private static HashMap<String, String> airportGeoJson = new HashMap<String, String>();

	{
		airportGeoJson.put("ABI", "US-TX");
		airportGeoJson.put("ACT", "US-TX");
		airportGeoJson.put("AMA", "US-TX");
		airportGeoJson.put("AUS", "US-TX");
		airportGeoJson.put("BLV", "US-MO");
		airportGeoJson.put("BPT", "US-TX");
		airportGeoJson.put("BRO", "US-TX");
		airportGeoJson.put("CLL", "US-TX");
		airportGeoJson.put("COU", "US-MO");
		airportGeoJson.put("CRP", "US-TX");
		airportGeoJson.put("DFW", "US-TX");
		airportGeoJson.put("ELP", "US-TX");
		airportGeoJson.put("GGG", "US-TX");
		airportGeoJson.put("GRB", "US-WI");
		airportGeoJson.put("HRL", "US-TX");
		airportGeoJson.put("LBB", "US-TX");
		airportGeoJson.put("LRD", "US-TX");
		airportGeoJson.put("MAF", "US-TX");
		airportGeoJson.put("MCI", "US-MO");
		airportGeoJson.put("MFE", "US-TX");
		airportGeoJson.put("MKE", "US-WI");
		airportGeoJson.put("MSN", "US-WI");
		airportGeoJson.put("ORD", "US-IL");
		airportGeoJson.put("SAT", "US-TX");
		airportGeoJson.put("SJT", "US-TX");
		airportGeoJson.put("SPS", "US-TX");
		airportGeoJson.put("STL", "US-MO");
		airportGeoJson.put("TBN", "US-MO");
		airportGeoJson.put("TYR", "US-TX");
		airportGeoJson.put("YFC", "US-NB");
		airportGeoJson.put("YSJ", "US-NB");
	}

	public RateChangeEvent(String lor, String rentalLocation,
			String arrivalDate, Date timestamp, double totalRate,
			double prevTotalRate, String carClass, String companyName,
			String rateCode, String airportCode) {
		super();
		this.lor = lor;
		this.rentalLocation = rentalLocation;
		this.arrivalDate = arrivalDate;
		this.timestamp = timestamp;
		this.totalRate = totalRate;
		this.prevTotalRate = prevTotalRate;
		this.carClass = carClass;
		this.companyName = companyName;
		this.rateCode = rateCode;
		this.airportCode = airportCode.substring(0, 3);
		this.netRateChange = totalRate - prevTotalRate;
		id = genKey(companyName + rentalLocation + carClass + rateCode
				+ airportCode + new DateTime());
	}

	public void prepForIndexing() {
		this.rateCode = getRateCode().replace(" ", "");
	}

	public String getLor() {
		return lor;
	}

	public void setLor(String lor) {
		this.lor = lor;
	}

	public String getRentalLocation() {
		return rentalLocation;
	}

	public void setRentalLocation(String rentalLocation) {
		this.rentalLocation = rentalLocation;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getTotalRate() {
		return totalRate;
	}

	public void setTotalRate(double totalRate) {
		this.totalRate = totalRate;
	}

	public double getPrevTotalRate() {
		return prevTotalRate;
	}

	public void setPrevTotalRate(double prevTotalRate) {
		this.prevTotalRate = prevTotalRate;
	}

	public String getCarClass() {
		return carClass;
	}

	public void setCarClass(String carClass) {
		this.carClass = carClass;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRateCode() {
		return rateCode;
	}

	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getAirportLocation() {
		return airportGeoJson.get(this.airportCode);
	}

	public double getNetRateChange() {
		return netRateChange;
	}

	public String getId() {
		return id;
	}

	private String genKey(String input) {
		StringBuffer hashSb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			byte[] bytes = md.digest();
			for (int i = 0; i < bytes.length; i++) {
				hashSb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashSb.toString();
	}
}