package com.hortonworks.ehi.utils;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hortonworks.ehi.domain.RateChangeEvent;

public class ElasticsearchRateChangeIndexClient implements RateChangeIndex {
	private static RateChangeIndexClient instance = null;
	private static Client client = null;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Logger logger = Logger
			.getLogger(RateChangeIndexClient.class);

	public ElasticsearchRateChangeIndexClient() {
		client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						ConfigurationClient.getInstance()
								.getProperty("es.host"), 9300));
	}

	public void index(RateChangeEvent rateChange)
			throws JsonProcessingException {
		rateChange.prepForIndexing();
		String json = mapper.writeValueAsString(rateChange);
		System.out.println(json);
		IndexResponse response = client.prepareIndex("ehi", "rateChange")
				.setSource(json).execute().actionGet();
		logger.debug("Indexed document with id: " + response.getId());
	}
}
