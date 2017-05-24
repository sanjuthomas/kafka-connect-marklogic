package kafka.connect.marklogic;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import kafka.connect.marklogic.MarkLogicDefaultWriter;
import kafka.connect.marklogic.Writer;
import kafka.connect.marklogic.beans.Account;
import kafka.connect.marklogic.beans.Client;
import kafka.connect.marklogic.beans.QuoteRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestMarkLogicDefaultWriter extends AbstractTest{
	
	private Writer writer;

	@Before
	public void setup(){
	    super.setup();
		writer = new MarkLogicDefaultWriter(super.conf);
	}
	
	@Test
	public void shouldWrite() throws ClientProtocolException, IOException, URISyntaxException{
		
		final List<SinkRecord> documents = new ArrayList<SinkRecord>();
		final Account account = new Account("A1");
		final Client client = new Client("C1", account);
		final QuoteRequest quoteRequest = new QuoteRequest("Q1", "APPL", 100, client, new Date());
		
		documents.add(new SinkRecord("topic", 1, null, null, null, MAPPER.convertValue(quoteRequest, Map.class), 0));
		writer.write(documents);
		
		final HttpResponse response = super.get("/C1/A1/Q1.json");
		final QuoteRequest qr = MAPPER.readValue(response.getEntity().getContent(), QuoteRequest.class);
		assertEquals("APPL", qr.getSymbol());
	}
}

