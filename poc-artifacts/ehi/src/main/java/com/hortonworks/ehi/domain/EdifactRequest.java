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

import java.io.Serializable;

import backtype.storm.tuple.Tuple;

public class EdifactRequest implements Serializable {
	private Long transactionId;
	private String airportCode;
	private String pickupDate;
	private String pickupTime;
	private String returnDate;
	private String returnTime;
	private String rateType;

	public EdifactRequest() {

	}

	public EdifactRequest(Tuple input) {
		String[] pieces = input.getString(0).split(",");
		String requestBody = pieces[3];
		String[] requestBodyPieces = requestBody.split("/");
		setTransactionId(Long.parseLong(requestBodyPieces[4].split("-")[1]));
		String[] requestDetails = requestBodyPieces[11].split("'")[5]
				.split("TVL\\+")[1].split(":");
		setPickupDate(requestDetails[0]);
		setPickupTime(requestDetails[1]);
		setReturnDate(requestDetails[2]);
		setReturnTime(requestDetails[3].split("\\+")[0]);
		setAirportCode(requestDetails[3].split("\\+")[1]);
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(String pickupDate) {
		this.pickupDate = pickupDate;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
}