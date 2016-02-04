package com.hwx.linearRoad.storm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.log4j.Logger;

import com.hwx.linearRoad.utils.StormUtils;

public class TextFileProducer implements Runnable{

	private Producer<String, String> producer;
	private static final Logger logger = Logger.getLogger(TextFileProducer.class);
	
	private final String sourceFile;
	private final String topic;
	private Properties props;
	
	public static void main(String[] args) throws Exception{
		TextFileProducer prod = new TextFileProducer(args);
		prod.run();
	}

	
	public TextFileProducer(String[] args) throws Exception{
		if(args.length<2){
			usage();
		}
		
		props = new Properties();
		try {
			props.load(new FileInputStream(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		topic = StormUtils.getExpectedProperty(props, "kafka.topic");
		sourceFile = args[1];
		ProducerConfig foo = new ProducerConfig(props);
		producer = new Producer<>(foo);
	}
	
//	public TextFileProducer(Map<String, String> argsMap) {
//		Properties props = new Properties();
//		props.put("metadata.broker.list", argsMap.get("metadata.broker.list"));
//		props.put("serializer.class", argsMap.get("serializer.class"));
//		props.put("request.required.acks", argsMap.get("request.required.acks"));
//		ProducerConfig config = new ProducerConfig(props);
//		producer = new Producer<String, String>(config);
//		
//		this.sourceFile = argsMap.get("sourceFile");
//		this.topic = argsMap.get("topic");
//	}
	

	public void run() {
		int seconds = 0;
		int cntr = 0;
		long foo = System.currentTimeMillis();
		try(BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
	    		
		    for(String line; (line = br.readLine()) != null; ) {
		    	
//		    	String[] tmp = line.split(",");
		    	//if the current line (time) is greater than the number of seconds, sleep to increment the seconds, then
//		    	if(Integer.parseInt(tmp[1])>seconds){
//		    		logger.info(String.format("Wrote %d records.", cntr));
//		    		//reset the records counter 
//		    		cntr = 0;
//		    		int sleepTime = (int) Math.round(Math.random()*10)+5;
//		    		Thread.sleep(sleepTime*1000);
//		    		logger.info(String.format("Slept %d seconds.", sleepTime));
//		    		seconds += sleepTime;
//		    	}
		    	KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, line);
		        producer.send(data);
		        
		        if(++cntr%1000==0){
		    		logger.info(String.format("Wrote %d records.", cntr));
		    		logger.info(String.format("in %f seconds", (System.currentTimeMillis()-foo)/1000f));
		    		foo = System.currentTimeMillis();
		        }

		    }
		    br.close();
		    logger.info(String.format("Finished!! Wrote %d total records.", cntr));
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void usage(){
		logger.error("TestFileProducer requires two arguments to run:");
		logger.error("\t Path to a configuration file for Kafka");
		logger.error("\t Path to a source file to be written to Kafka");
		logger.error("\t (optional) the max number of records to be loaded");
		System.exit(-1);
	}

}
