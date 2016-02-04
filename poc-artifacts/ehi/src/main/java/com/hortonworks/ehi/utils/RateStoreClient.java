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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.log4j.Logger;

import com.google.protobuf.ServiceException;
import com.hortonworks.ehi.domain.RateStoreResult;

/**
 * Class used to interact with HBase to retrieve rates based on composite result
 * keys
 * 
 * @author paul
 * 
 */
public class RateStoreClient {
	private static RateStoreClient instance = null;
	private HConnection connection;
	private HTableInterface table = null;
	private static Logger logger = Logger.getLogger(RateStoreClient.class);

	protected RateStoreClient() {
		try {
			Configuration config = HBaseConfiguration.create();
			config.set("hbase.zookeeper.quorum", ConfigurationClient
					.getInstance().getProperty("hbase.zk.quorum"));
			config.set("zookeeper.znode.parent", ConfigurationClient
					.getInstance().getProperty("hbase.zk.znode.parent"));
			HBaseAdmin.checkHBaseAvailable(config);
			// Create a connection to the cluster.
			connection = HConnectionManager.createConnection(config);
			table = connection.getTable("rates");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public Object get(String key) throws IOException {
		return table.get(new Get(key.getBytes()));
	}

	public void put(String key, String qual, String val) throws IOException {
		Put put = new Put(key.getBytes());
		put.add("rate".getBytes(), qual.getBytes(), val.getBytes());
		table.put(put);
	}

	public RateStoreResult getRatesForKey(String key) throws IOException {
		if (table != null) {
			Result result = table.get(new Get(key.getBytes()));
			RateStoreResult rateResult = new RateStoreResult();
			rateResult.setResult(result);
			return rateResult;
		}
		return new RateStoreResult();
	}

	public static RateStoreClient getInstance() {
		return RateStoreClientHolder.INSTANCE;
	}

	private static class RateStoreClientHolder {
		public static final RateStoreClient INSTANCE = new RateStoreClient();
	}
}
