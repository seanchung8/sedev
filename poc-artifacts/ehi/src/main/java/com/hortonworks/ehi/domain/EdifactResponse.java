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
import java.util.HashMap;

import backtype.storm.tuple.Tuple;

public class EdifactResponse implements Serializable {
	private Long transactionId;
	private String companyName;
	private HashMap<String, String[]> prices = new HashMap<String, String[]>();

	public EdifactResponse() {
	}

	public EdifactResponse(Tuple input) {
		setTransactionId(Long.parseLong(input.getString(0).split(",")[3]
				.split("/")[4].split("-")[1].split("__")[0]));
		setCompanyName(input.getString(0).split(",")[3].split("/")[4]
				.split("-")[1].split("__")[1]);
		String[] responsePieces = input.getString(0).split(",")[3].split("/")[11]
				.split("'");
		String carType = null;
		for (String value : responsePieces) {
			if (value.contains("PRD"))
				carType = value.split(":")[1];
			if (value.contains("TFF")) {
				String dailyRate = value.split(":")[1];
				String weeklyRate = value.split(":")[7].split("\\+")[0];
				prices.put(carType, new String[] { dailyRate, weeklyRate });
			}
		}
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public HashMap<String, String[]> getPrices() {
		return prices;
	}

	public void setPrices(HashMap<String, String[]> prices) {
		this.prices = prices;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
