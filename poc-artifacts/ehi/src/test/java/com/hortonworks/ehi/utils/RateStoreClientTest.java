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

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;

import com.hortonworks.ehi.domain.RateStoreResult;

public class RateStoreClientTest extends TestCase {
	RateStoreClient wrapper;

	protected void setUp() throws Exception {
		wrapper = RateStoreClient.getInstance();
	}

	public void testGet() {
		try {
			Object result = wrapper.get("test");

			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetRatesForKey() {
		try {
			RateStoreResult result = wrapper
					.getRatesForKey("hertzMismatched HourABI*ABIC96");
			Cell cell = result.getResult().getColumnLatestCell(
					"rate".getBytes(), "CCAR".getBytes());
			if (cell != null) {
				String rate = new String(CellUtil.cloneValue(cell));
				System.out.println(rate);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testPut() {
		try {
			wrapper.put("key", "CCAR", "130.55");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void tearDown() throws Exception {
	}
}