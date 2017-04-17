package org.sanju.kafka.connect.marklogic.sink;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;
import org.sanju.kafka.connect.marklogic.Account;
import org.sanju.kafka.connect.marklogic.Client;
import org.sanju.kafka.connect.marklogic.QuoteRequest;


/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestMarkLogicSincTask {
	
	private MarkLogicSinkTask markLogicSinkTask;
	private final Map<String, String> conf = new HashMap<>();
	
	@Before
	public void setup(){
		
		conf.put(MarkLogicSinkConfig.CONNECTION_URL, "http://localhost:8000/v1/documents");
		conf.put(MarkLogicSinkConfig.CONNECTION_USER, "admin");
		conf.put(MarkLogicSinkConfig.CONNECTION_PASSWORD, "admin");
		conf.put(MarkLogicSinkConfig.BATCH_SIZE, "100");
	    conf.put(MarkLogicSinkConfig.RETRY_BACKOFF_MS, "100");
		markLogicSinkTask = new MarkLogicSinkTask();
		markLogicSinkTask.start(conf);
	}
	
	
	@Test
	public void shouldPut(){
		
		List<SinkRecord> documents = new ArrayList<SinkRecord>();
		final Account account = new Account("A1");
		final Client client = new Client("C1", account);
		final QuoteRequest quoteRequest = new QuoteRequest("Q1", "APPL", 100, client, new Date());
	
		documents.add(new SinkRecord("topic", 1, null, null, null, new BeanMap(quoteRequest), 0));
		markLogicSinkTask.put(documents);
	}
}
