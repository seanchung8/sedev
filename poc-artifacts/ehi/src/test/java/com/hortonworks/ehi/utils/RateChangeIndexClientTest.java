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

import java.util.Date;

import junit.framework.TestCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hortonworks.ehi.domain.RateChangeEvent;

public class RateChangeIndexClientTest extends TestCase {
	RateChangeIndex client = RateChangeIndexClient.getSolrInstance();

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testIndex() {
		try {
			RateChangeEvent event = new RateChangeEvent("3d 13hr 30min",
					"MDH*MDHC96", "1312030230", new Date(),
					Double.parseDouble("936.97"), Double.parseDouble("692.41"),
					"FFAR", "ehi", "Mismatched Hour", "ORD");
			client.index(event);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
