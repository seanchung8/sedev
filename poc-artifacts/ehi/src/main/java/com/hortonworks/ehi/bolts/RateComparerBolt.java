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

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import com.hortonworks.ehi.domain.EdifactRequest;
import com.hortonworks.ehi.domain.EdifactResponse;
import com.hortonworks.ehi.domain.ParseDateTime;
import com.hortonworks.ehi.domain.RateChangeEvent;
import com.hortonworks.ehi.domain.RateStoreResult;
import com.hortonworks.ehi.utils.EdifactFilterClient;
import com.hortonworks.ehi.utils.PrismClient;
import com.hortonworks.ehi.utils.RateChangeIndexClient;
import com.hortonworks.ehi.utils.RateStoreClient;

/**
 * Compare rates for every request/response combination
 * 
 * @author paul
 * 
 */
public class RateComparerBolt implements IRichBolt {
	private OutputCollector collector;
	private static Logger logger = Logger.getLogger(RateComparerBolt.class);

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		EdifactRequest request = (EdifactRequest) input
				.getValueByField("request");
		EdifactResponse response = (EdifactResponse) input
				.getValueByField("response");

		// Construct our row key and lookup rates for each car type
		String rowStoreKey = response.getCompanyName() + request.getRateType()
				+ request.getAirportCode();

		try {
			RateStoreResult result = RateStoreClient.getInstance()
					.getRatesForKey(rowStoreKey);
			// Filter events by location and rate type
			if (!((EdifactFilterClient.getInstance().filterByCode(new String[] {
					"BRO*BROC96", "AUS*AUSC96" }, request.getAirportCode())) && (EdifactFilterClient
					.getInstance().filterByCode(
					new String[] { "Mismatched Hour" }, request.getRateType())))) {

				for (String carType : response.getPrices().keySet()) {
					if (result.getResult() != null
							&& result.getResult().containsNonEmptyColumn(
									"rate".getBytes(), "CCAR".getBytes()) == false) {
						RateStoreClient.getInstance().put(rowStoreKey, carType,
								response.getPrices().get(carType)[0]);
					} else {
						Cell cell = result.getResult().getColumnLatestCell(
								"rate".getBytes(), carType.getBytes());
						if (cell != null) {
							String rate = new String(CellUtil.cloneValue(cell));
							String responseRate = response.getPrices().get(
									carType)[0];
							double storedValue = Double.parseDouble(rate);
							double responseValue = Double
									.parseDouble(responseRate);
							if (storedValue != responseValue) {
								logger.debug("The rates are not the same - "
										+ storedValue + " vs " + responseValue
										+ " RowKey: " + rowStoreKey);
								RateStoreClient.getInstance().put(rowStoreKey,
										carType, responseRate);

								// figure out the length of rental here
								try {
									Period p = new Period(new DateTime(
											new ParseDateTime(request
													.getPickupDate()
													+ " "
													+ request.getPickupTime())
													.getDate()), new DateTime(
											new ParseDateTime(request
													.getReturnDate()
													+ " "
													+ request.getReturnTime())
													.getDate()));

									// write newly changed value into MySQL
									// table rates, and Elasticsearch/Solr
									RateChangeEvent rateChange = new RateChangeEvent(
											p.getDays() + "d " + p.getHours()
													+ "hr " + p.getMinutes()
													+ "min",
											request.getAirportCode(),
											request.getPickupDate()
													+ request.getPickupTime(),
											new Date(), responseValue,
											storedValue, carType,
											response.getCompanyName(),
											request.getRateType(),
											request.getAirportCode());
									PrismClient.getInstance().insertRateChange(
											rateChange);
									RateChangeIndexClient.getESInstance()
											.index(rateChange);
									RateChangeIndexClient.getSolrInstance()
											.index(rateChange);
								} catch (Exception e) {
									logger.error(e.getMessage(), e);
								}
							}
						}
					}
				}
			} else {
				logger.debug("Filtered out record with airport: "
						+ request.getAirportCode() + "and RateType: "
						+ request.getRateType());
			}
			collector.ack(input);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
