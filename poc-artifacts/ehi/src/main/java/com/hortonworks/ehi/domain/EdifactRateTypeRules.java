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

import java.text.ParseException;
import java.util.Date;

public class EdifactRateTypeRules {
	
	ParseDateTime pickupDateTime;
	ParseDateTime returnDateTime;
	
	public EdifactRateTypeRules(String pickupTime, String returnTime) throws ParseException {
		this.pickupDateTime = new ParseDateTime(pickupTime);
		this.returnDateTime = new ParseDateTime(returnTime);
//		System.out.println("Pickup " + pickupTime);
//		System.out.println("Return " + returnTime);
	}
	
	public String evaluateRateType() {
		String rateType = "";
		
		if (offHours())
		{
			rateType = "Off Hours";
		}
		else if (sundayPm())
		{
			rateType = "Sunday PM";
		}
		else if (webPm())
		{
			rateType = "Web PM";
		}
		else if (webAm())
		{
			rateType = "Web AM";
		}
		else if (missmatchedHours())
		{
			rateType = "Mismatched Hour";
		}
		else
		{
			rateType = "Unknown";
		}
		
		return rateType;
	}
	
	public boolean missmatchedHours() {
		boolean missmatchedHour = false;
		
		if (pickupDateTime.getTime() != returnDateTime.getTime())
		{
			missmatchedHour = true;
		}
		
		return missmatchedHour;
	}
	public boolean offHours() {
		boolean offHours = false;
		
		if (pickupDateTime.getTime().equals(returnDateTime.getTime()) && pickupDateTime.getMinutes() != 0 )
		{
			offHours = true;
		}
		
		return offHours;
	}
	
	public boolean webPm() {
		boolean webPM = false;
		Date dt = new Date(pickupDateTime.getYears() - 1900, pickupDateTime.getMonths()-1, pickupDateTime.getDays(), 13, 59, 0);
		
		if (pickupDateTime.getTime().equals(returnDateTime.getTime()) && pickupDateTime.getMinutes() == 0 
				&& pickupDateTime.getDate().after(dt))
		{
			webPM = true;
		}
		
		return webPM;
	}
	
	public boolean webAm() {
		boolean webAM = false;
		Date dt = new Date(pickupDateTime.getYears() - 1900, pickupDateTime.getMonths()-1, pickupDateTime.getDays(), 14, 0, 0);
		
		if (pickupDateTime.getTime().equals(returnDateTime.getTime()) && pickupDateTime.getMinutes() == 0 
				&& pickupDateTime.getDate().before(dt))
		{
			webAM = true;
		}
		
		return webAM;
	}
	
	public boolean sundayPm() {
		boolean sundayPM = false;		
		Date dt = new Date(pickupDateTime.getYears() - 1900, pickupDateTime.getMonths()-1, pickupDateTime.getDays(), 13, 59, 0);
		
		if (pickupDateTime.getTime().equals(returnDateTime.getTime()) && pickupDateTime.getMinutes() == 0 
				&& pickupDateTime.getDate().after(dt) && pickupDateTime.getDate().getDay() == 0)
		{
			sundayPM = true;
		}
		
		return sundayPM;
	}
}