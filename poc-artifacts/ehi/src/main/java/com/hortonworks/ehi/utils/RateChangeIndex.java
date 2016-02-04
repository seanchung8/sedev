package com.hortonworks.ehi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hortonworks.ehi.domain.RateChangeEvent;

public interface RateChangeIndex {
	public void index(RateChangeEvent rateChange)
			throws JsonProcessingException;
}
