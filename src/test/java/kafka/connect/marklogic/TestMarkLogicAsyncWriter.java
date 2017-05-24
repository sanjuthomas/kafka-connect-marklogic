package kafka.connect.marklogic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import kafka.connect.marklogic.beans.Account;
import kafka.connect.marklogic.beans.Client;
import kafka.connect.marklogic.beans.QuoteRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestMarkLogicAsyncWriter extends AbstractTest{
	
	private Writer writer;

	@Before
	public void setup(){
	    super.setup();
		writer = new MarkLogicAsyncWriter(super.conf);
	}
	
	@After
	public void tearDown(){
	    
	}
	
	@Test
	public void shouldWrite() throws ClientProtocolException, IOException, URISyntaxException{
        
        final List<SinkRecord> documents = new ArrayList<SinkRecord>();
        final QuoteRequest quoteRequest1 = new QuoteRequest("Q4", "IBM", 100, new Client("C4", new Account("A4")), new Date());
        final QuoteRequest quoteRequest2 = new QuoteRequest("Q5", "GS", 100, new Client("C5", new Account("A5")), new Date());
        
        documents.add(new SinkRecord("topic", 1, null, null, null, MAPPER.convertValue(quoteRequest1, Map.class), 0));
        documents.add(new SinkRecord("topic", 1, null, null, null, MAPPER.convertValue(quoteRequest2, Map.class), 0));
        writer.write(documents);
        
        HttpResponse response = super.get("/C4/A4/Q4.json");
        QuoteRequest qr = MAPPER.readValue(response.getEntity().getContent(), QuoteRequest.class);
        assertEquals("IBM", qr.getSymbol());
        response = super.get("/C5/A5/Q5.json");
        qr = MAPPER.readValue(response.getEntity().getContent(), QuoteRequest.class);
        assertEquals("GS", qr.getSymbol());
        super.delete("/C5/A5/Q5.json");
    }
}

