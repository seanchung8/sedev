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
package com.hortonworks.ehi.bolts;

import java.text.ParseException;
import java.util.Map;

import org.apache.log4j.Logger;

import storm.trident.util.LRUMap;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.hortonworks.ehi.domain.EdifactRateTypeRules;
import com.hortonworks.ehi.domain.EdifactRequest;
import com.hortonworks.ehi.domain.EdifactResponse;
import com.hortonworks.ehi.utils.EdifactFilterClient;

/**
 * Group Requests by type and then emit the matched request/responses.
 * 
 * @author paul
 * 
 */
public class EdifactGroupingBolt implements IRichBolt {
	private static final long serialVersionUID = 6711927509800236001L;
	OutputCollector collector;
	private LRUMap<Long, EdifactRequest> lruCache = new LRUMap<Long, EdifactRequest>(
			100);
	private static Logger logger = Logger.getLogger(EdifactGroupingBolt.class);

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String messageType = input.getString(0).split(",")[2];

		if (messageType.contains("Request")) {
			EdifactRequest request = new EdifactRequest(input);

			// If we don't have this request AirportCode in the master filter
			// list process it and add to LRU cache
			if (!EdifactFilterClient.getInstance().filterByCode(
					new String[] { "GG" }, request.getAirportCode())) {
				// Set the rate type and then add it to a LRUCache
				request.setRateType(groupRequestByRateType(request));
				lruCache.put(request.getTransactionId(), request);
				collector.ack(input);
			} else
				logger.debug("Filtered out request");

		} else {
			EdifactResponse response = new EdifactResponse(input);
			EdifactRequest request = lruCache.get(response.getTransactionId());
			if (request != null)
				collector.emit(input, new Values(request, response));
			collector.ack(input);
		}
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("request", "response"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	private String groupRequestByRateType(EdifactRequest request) {
		String[] rateTypes = { "Mismatched Hour", "Off Hour", "Web PM",
				"Web AM", "GDS / TA", "Sunday PM" };
		String rateType = "Unknown";
		try {
			EdifactRateTypeRules rateTypeRule = new EdifactRateTypeRules(
					request.getPickupDate() + " " + request.getPickupTime(),
					request.getReturnDate() + " " + request.getReturnTime());
			rateType = rateTypeRule.evaluateRateType();
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return rateType;
	}
}