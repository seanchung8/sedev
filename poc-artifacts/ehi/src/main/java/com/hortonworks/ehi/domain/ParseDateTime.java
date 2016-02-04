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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseDateTime {
	private int minutes;
	private int hours;
	private int years;
	private int days;
	private int months;
	private Date dateTime;
	
	public ParseDateTime() {
		
	}
	
	public ParseDateTime(String dateTime) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("yyMMdd HHmm");
		
		this.minutes = Integer.parseInt(new SimpleDateFormat("mm").format((Date)formatter.parse(dateTime)));
		this.hours = Integer.parseInt(new SimpleDateFormat("HH").format((Date)formatter.parse(dateTime)));
		this.years = Integer.parseInt(new SimpleDateFormat("yyyy").format((Date)formatter.parse(dateTime)));
		this.months = Integer.parseInt(new SimpleDateFormat("MM").format((Date)formatter.parse(dateTime)));
		this.days = Integer.parseInt(new SimpleDateFormat("dd").format((Date)formatter.parse(dateTime)));
		this.dateTime = new SimpleDateFormat("yyMMdd HHmm").parse(dateTime);
	}
	
	public int getMinutes() {
		return this.minutes;
	}
	
	public int getHours() {
		return this.hours;
	}
	
	public int getYears() {
		return this.years;
	}
	
	public int getMonths() {
		return this.months;
	}
	
	public int getDays() {
		return this.days;
	}
	
	public String getTime() {
		return Integer.toString(this.hours) + ":" + Integer.toString(this.minutes);
	}
	
	public Date getDate() {
		return this.dateTime;
	}
}
