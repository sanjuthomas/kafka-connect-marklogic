package kafka.connect.marklogic.sink;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import kafka.connect.marklogic.AbstractTest;
import kafka.connect.marklogic.beans.Account;
import kafka.connect.marklogic.beans.Client;
import kafka.connect.marklogic.beans.QuoteRequest;

import org.apache.http.client.ClientProtocolException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;


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
	public void shouldPut() throws ClientProtocolException, IOException, URISyntaxException{
		
		List<SinkRecord> documents = new ArrayList<SinkRecord>();
		final Account account = new Account("A1");
		final Client client = new Client("C1", account);
		final QuoteRequest quoteRequest = new QuoteRequest("Q1", "APPL", 100, client, new Date());
	
		documents.add(new SinkRecord("trades", 1, null, null, null,  MAPPER.convertValue(quoteRequest, Map.class), 0));
		markLogicSinkTask.put(documents);
		
		QuoteRequest qr = super.find("/C1/A1/Q1.json");
        assertEquals("APPL", qr.getSymbol());
        super.delete("/C1/A1/Q1.json");
	}
}
