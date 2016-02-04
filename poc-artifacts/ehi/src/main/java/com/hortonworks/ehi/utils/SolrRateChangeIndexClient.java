package com.hortonworks.ehi.utils;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.elasticsearch.client.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hortonworks.ehi.domain.RateChangeEvent;

public class SolrRateChangeIndexClient implements RateChangeIndex {
	private static Client client = null;
	String url = new String();
	SolrServer server = null;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Logger logger = Logger
			.getLogger(SolrRateChangeIndexClient.class);

	public SolrRateChangeIndexClient() {
		String url = ConfigurationClient.getInstance().getProperty("solr.url");
		server = new HttpSolrServer(url);
		(new Thread(new CommitThread(server))).start();
	}

	public void index(RateChangeEvent rateChange)
			throws JsonProcessingException {
		try {
			UpdateResponse response = server.addBean(rateChange);
			logger.debug("Indexed document with id: " + rateChange.getId()
					+ " status: " + response.getStatus());
		} catch (IOException e) {
			logger.error("Could not index document: " + rateChange.getId()
					+ " " + e.getMessage());
			e.printStackTrace();
		} catch (SolrServerException e) {
			logger.error("Could not index document: " + rateChange.getId()
					+ " " + e.getMessage());
			e.printStackTrace();
		}
	}

	class CommitThread implements Runnable {
		SolrServer server;

		public CommitThread(SolrServer server) {
			this.server = server;
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(15000);
					server.commit();
					logger.info("Committing Index");
				} catch (InterruptedException e) {
					logger.error("Interrupted: " + e.getMessage());
					e.printStackTrace();
				} catch (SolrServerException e) {
					logger.error("Error committing: " + e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("Error committing: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
