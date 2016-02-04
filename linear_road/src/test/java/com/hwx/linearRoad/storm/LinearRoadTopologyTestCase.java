package com.hwx.linearRoad.storm;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

import com.hwx.linearRoad.AbstractTestCase;
import com.hwx.linearRoad.operators.OperatorTestCase;

public class LinearRoadTopologyTestCase extends AbstractTestCase{

	private static final Logger logger = Logger.getLogger(LinearRoadTopologyTestCase.class);
	
	//tested and working
	@Ignore
	public void testProducer() throws Exception{
		String configPath = getResourcePath(OperatorTestCase.class)+"/../config.properties";
		String sourceFile = getResourcePath(OperatorTestCase.class)+"/../operators/cardatapoints.csv";
		
		TextFileProducer producer = new TextFileProducer(new String[]{configPath, sourceFile});
		producer.run();
	}
	
	
	@Test
	public void testLinearRoadTopology() throws Exception{
		String configPath = getResourcePath(OperatorTestCase.class)+"/../config.properties";
		LinearRoadTopology topo = new LinearRoadTopology(configPath);
		
		String sourceFile = getResourcePath(OperatorTestCase.class)+"/../operators/1.xway.final.dat";
		Runnable testProducer = new TextFileProducer(new String[]{configPath, sourceFile});
//		Runnable testProducer = getSimpleProducer(sourceFile);
		runTopology("LinearRoadTest", topo.compose(), testProducer);
	}
			
	
	/**
	 * Starts the given topology, then runs a TextFileProducer with the given producer args in another thread.
	 * When the producer finishes, the topology is killed.
	 * 
	 * @param testName
	 * @param topology
	 * @param producerArgs
	 */
	private void runTopology(String testName, TopologyBuilder topology, Runnable producer){
		//instantiate a local cluster and kick off the submitted topology
		LocalCluster cluster = new LocalCluster();
		Config conf = new Config();
//		conf.setDebug(true);
		cluster.submitTopology(testName, conf, topology.createTopology());
		
		//give it 15 seconds for the topology to spin up
		goSleep(15000);
		System.out.println("************STARTING PRODUCER************");
		
		//start the producer on a new thread and let it start putting messages on the queue
		Thread t = new Thread(producer);	
		t.start();
		
		//keep checking to see if the producer finished writing all of it's messages
		while(t.isAlive()){
			try {
				t.join(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("************PRODUCER FINISHED************");
		//wait 15 seconds for the topology to finish and terminate the cluster
		goSleep(15000);
		cluster.shutdown();
	}
	
	
	private void goSleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	

}
