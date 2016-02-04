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
package com.hortonworks.ehi.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.hortonworks.ehi.domain.RateChangeEvent;

public class PrismClient {
	private static PrismClient instance = null;
	private static Logger logger = Logger.getLogger(PrismClient.class);

	private PrismClient() {

	}

	public void insertRateChange(RateChangeEvent rateChange) {
		insertRateChange(rateChange.getLor(), rateChange.getRentalLocation(),
				rateChange.getArrivalDate(), rateChange.getTimestamp(),
				rateChange.getTotalRate(), rateChange.getTotalRate(),
				rateChange.getCarClass(), rateChange.getCompanyName(),
				rateChange.getRateCode());
	}

	public void insertRateChange(String lor, String rentalLocation,
			String arrivalDate, Date timestamp, double totalRate,
			double prevTotalRate, String carClass, String companyName,
			String rateCode) {
		CallableStatement proc;

		try {
			proc = MySQLConnection().prepareCall(
					"{call InsertRates(?,?,?,?,?,?,?,?,?)}");
			proc.setString(1, lor);
			proc.setString(2, rentalLocation);
			proc.setString(3, arrivalDate);
			proc.setString(4, timestamp.toString());
			proc.setDouble(5, totalRate);
			proc.setDouble(6, prevTotalRate);
			proc.setString(7, carClass);
			proc.setString(8, companyName);
			proc.setString(9, rateCode);
			proc.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public Connection MySQLConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}

		String jdbcUrl = "jdbc:mysql://"
				+ ConfigurationClient.getInstance().getProperty("mysql.host")
				+ "/rates";
		return DriverManager.getConnection(jdbcUrl, ConfigurationClient
				.getInstance().getProperty("mysql.user"), ConfigurationClient
				.getInstance().getProperty("mysql.pass"));
	}

	public static PrismClient getInstance() {
		return PrismClientHolder.INSTANCE;
	}
	
	private static class PrismClientHolder {
		public static final PrismClient INSTANCE = new PrismClient();
	}
}
