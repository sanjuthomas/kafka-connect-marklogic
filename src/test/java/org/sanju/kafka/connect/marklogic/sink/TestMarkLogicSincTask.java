package org.sanju.kafka.connect.marklogic.sink;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;
import org.sanju.kafka.connect.marklogic.AbstractTest;
import org.sanju.kafka.connect.marklogic.beans.Account;
import org.sanju.kafka.connect.marklogic.beans.Client;
import org.sanju.kafka.connect.marklogic.beans.QuoteRequest;


/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestMarkLogicSincTask extends AbstractTest{
	
	private MarkLogicSinkTask markLogicSinkTask;
	
	@Before
	public void setup(){
	    super.setup();
		markLogicSinkTask = new MarkLogicSinkTask();
		markLogicSinkTask.start(super.conf);
	}
	
	
	@Test
	public void shouldPut(){
		
		List<SinkRecord> documents = new ArrayList<SinkRecord>();
		final Account account = new Account("A1");
		final Client client = new Client("C1", account);
		final QuoteRequest quoteRequest = new QuoteRequest("Q1", "APPL", 100, client, new Date());
	
		documents.add(new SinkRecord("trades", 1, null, null, null, new BeanMap(quoteRequest), 0));
		markLogicSinkTask.put(documents);
	}
}
