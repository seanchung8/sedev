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
package com.hortonworks.ehi.topologies;

import javax.jms.Session;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.contrib.jms.JmsProvider;
import backtype.storm.contrib.jms.JmsTupleProducer;
import backtype.storm.contrib.jms.spout.JmsSpout;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

import com.hortonworks.ehi.bolts.EdifactGroupingBolt;
import com.hortonworks.ehi.bolts.RateComparerBolt;
import com.hortonworks.ehi.spouts.JsonTupleProducer;
import com.hortonworks.ehi.spouts.SpringJmsProvider;
import com.hortonworks.ehi.utils.ConfigurationClient;

public class EdifactProcessorTopology {
	public static final String JMS_QUEUE_SPOUT = "stream_data_spout";
	public static final String EDIFACT_GROUPING_BOLT = "edifact_grouping_bolt";
	public static final String RATE_COMPARE_BOLT = "rate_compare_bolt";

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		JmsProvider jmsQueueProvider = new SpringJmsProvider(
				"jms-activemq.xml", "jmsConnectionFactory", "notificationQueue");
		// JMS Producer
		JmsTupleProducer producer = new JsonTupleProducer();
		// JMS Queue Spout
		JmsSpout queueSpout = new JmsSpout();
		queueSpout.setJmsProvider(jmsQueueProvider);
		queueSpout.setJmsTupleProducer(producer);
		queueSpout.setJmsAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		queueSpout.setDistributed(true); // allow multiple instances

		TopologyBuilder builder = new TopologyBuilder();

		// Define a spout with 5 parallel instances
		builder.setSpout(JMS_QUEUE_SPOUT, queueSpout, 5);
		// Attach the grouping bolt to the spout
		builder.setBolt(EDIFACT_GROUPING_BOLT, new EdifactGroupingBolt())
				.shuffleGrouping(JMS_QUEUE_SPOUT);
		// Attach the rate comparer to the grouping bolt
		builder.setBolt(RATE_COMPARE_BOLT, new RateComparerBolt())
				.shuffleGrouping(EDIFACT_GROUPING_BOLT);

		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(3);

		String stormMode = System.getProperty("deployTo");
		if (stormMode != null && stormMode.equals("local")) {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("edifact-event-processor", conf,
					builder.createTopology());
		} else
			StormSubmitter.submitTopology(args[0], conf,
					builder.createTopology());
	}
}