package com.hortonworks.target.dse.ingest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.NoOp;
import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.property.AppProps;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

import com.ifesdjeen.cascading.cassandra.CassandraTap;
import com.ifesdjeen.cascading.cassandra.cql3.CassandraCQL3Scheme;

public class SampleFlow {
	public static void main(String[] args) {
		if (args != null && args.length == 1) {
			// Setup
			String destinationHdfsPath = args[0];
			Properties properties = new Properties();
			AppProps.setApplicationJarClass(properties, SampleFlow.class);
			HadoopFlowConnector flowConnector = new HadoopFlowConnector(
					properties);

			// Tap Definition - Where we're going to get and put data
			Tap destination = new Hfs(new TextDelimited(false, "\t"),
					destinationHdfsPath);

			Pipe noOp = new Pipe("main");
			noOp = new Each(noOp, new NoOp(), Fields.ALL);

			// Configure the Environment
			Map<String, Object> config = new HashMap<String, Object>();
			config.put("db.host", "127.0.0.1");
			config.put("db.port", "9160");

			config.put("db.keyspace", "esv");
			config.put("db.columnFamily", "users");

			config.put("db.inputPartitioner",
					"org.apache.cassandra.dht.Murmur3Partitioner");
			config.put("db.outputPartitioner",
					"org.apache.cassandra.dht.Murmur3Partitioner");

			// Put mappings of types, specifying which source field has which
			// type
			Map<String, String> types = new HashMap<String, String>();
			types.put("name", "UTF8Type");
			types.put("language", "UTF8Type");
			types.put("schmotes", "Int32Type");
			types.put("votes", "Int32Type");

			config.put("types", types);

			// Configure input columns in an order they should appear in client
			// code
			config.put("mappings.source",
					Arrays.asList("language", "schmotes", "votes"));
			config.put("mappings.rowKey", "name");

			CassandraCQL3Scheme scheme = new CassandraCQL3Scheme(config);
			CassandraTap cassandraTap = new CassandraTap(scheme);
			FlowDef flowDef = FlowDef.flowDef().setName("dse-ingest")
					.addSource(noOp, cassandraTap)
					.addTailSink(noOp, destination);

			// Write a DOT file and run the flow
			Flow wcFlow = flowConnector.connect(flowDef);
			wcFlow.writeDOT("dot/wc.dot");
			wcFlow.complete();
		} else
			printUsage();
	}

	public static void printUsage() {
		System.err
				.println("I need 1 argument, and that is the path in HDFS you want to use to store the dse data");
	}
}
