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

import org.apache.http.client.ClientProtocolException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestMarkLogicBufferedWriter extends AbstractTest{
    
    private Writer writer;
    
    @Before
    public void setup(){
        super.setup();
        writer = new MarkLogicBufferedWriter(super.conf);
    }

    @Test
    public void shouldWrite() throws ClientProtocolException, IOException, URISyntaxException{
        
        final List<SinkRecord> documents = new ArrayList<SinkRecord>();
        final QuoteRequest quoteRequest1 = new QuoteRequest("Q2", "IBM", 100, new Client("C2", new Account("A2")), new Date());
        final QuoteRequest quoteRequest2 = new QuoteRequest("Q3", "GS", 100, new Client("C3", new Account("A3")), new Date());
        
        documents.add(new SinkRecord("topic", 1, null, null, null, MAPPER.convertValue(quoteRequest1, Map.class), 0));
        documents.add(new SinkRecord("topic", 1, null, null, null, MAPPER.convertValue(quoteRequest2, Map.class), 0));
        writer.write(documents);
        
        QuoteRequest qr = super.find("/C2/A2/Q2.json");
        assertEquals("IBM", qr.getSymbol());
        qr = super.find("/C3/A3/Q3.json");
        assertEquals("GS", qr.getSymbol());
        super.delete("/C3/A3/Q3.json");
        super.delete("/C2/A2/Q2.json");
    }
    
}
