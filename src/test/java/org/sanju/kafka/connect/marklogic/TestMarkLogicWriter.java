package org.sanju.kafka.connect.marklogic;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;
import org.sanju.kafka.connect.marklogic.sink.MarkLogicSinkConfig;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestMarkLogicWriter {
	
	private final Map<String, String> conf = new HashMap<>();
	private MarkLogicWriter markLogicWriter;

	@Before
	public void setup() throws MalformedURLException{
		
		conf.put(MarkLogicSinkConfig.CONNECTION_URL, "http://localhost:8000/v1/documents");
		conf.put(MarkLogicSinkConfig.CONNECTION_USER, "admin");
		conf.put(MarkLogicSinkConfig.CONNECTION_PASSWORD, "admin");
		markLogicWriter = new MarkLogicWriter(conf);
	}
	
	@Test
	public void shouldWrite(){
		
		List<SinkRecord> documents = new ArrayList<SinkRecord>();
		final Account account = new Account("A1");
		final Client client = new Client("C1", account);
		final QuoteRequest quoteRequest = new QuoteRequest("Q1", "APPL", 100, client);
		
		documents.add(new SinkRecord("topic", 1, null, null, null, new BeanMap(quoteRequest), 0));
		
		markLogicWriter.write(documents);
	}
}

