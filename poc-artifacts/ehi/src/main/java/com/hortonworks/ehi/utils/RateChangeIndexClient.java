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

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hortonworks.ehi.domain.RateChangeEvent;

public class RateChangeIndexClient {
	private static RateChangeIndex instance = null;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Logger logger = Logger
			.getLogger(RateChangeIndexClient.class);

	public static RateChangeIndex getESInstance() {
		return RateChangeIndexClientHolder.ES_INSTANCE;
	}

	public static RateChangeIndex getSolrInstance() {
		return RateChangeIndexClientHolder.SOLR_INSTANCE;
	}

	private static class RateChangeIndexClientHolder {
		public static final RateChangeIndex ES_INSTANCE = new ElasticsearchRateChangeIndexClient();
		public static final RateChangeIndex SOLR_INSTANCE = new SolrRateChangeIndexClient();
	}
}