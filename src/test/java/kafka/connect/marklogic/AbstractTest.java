package kafka.connect.marklogic;

import java.util.HashMap;
import java.util.Map;

import kafka.connect.marklogic.beans.QuoteRequest;
import kafka.connect.marklogic.sink.MarkLogicSinkConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.DigestAuthContext;
import com.marklogic.client.document.DocumentPage;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.JacksonHandle;

/**
 * 
 * @author Sanju Thomas
 *
 */
public abstract class AbstractTest {
    
    protected static final ObjectMapper MAPPER = new ObjectMapper();
    protected final Map<String, String> conf = new HashMap<>();
    protected JSONDocumentManager manager;
    protected DatabaseClient client;

    public void setup(){

        conf.put(MarkLogicSinkConfig.CONNECTION_HOST, "localhost");
        conf.put(MarkLogicSinkConfig.CONNECTION_PORT, "8000");
        conf.put(MarkLogicSinkConfig.CONNECTION_USER, "admin");
        conf.put(MarkLogicSinkConfig.CONNECTION_PASSWORD, "admin");
        conf.put(MarkLogicSinkConfig.BATCH_SIZE, "100");
        conf.put(MarkLogicSinkConfig.WRITER_IMPL, MarkLogicWriter.class.getCanonicalName());
        conf.put(MarkLogicSinkConfig.RETRY_BACKOFF_MS, "100");
        conf.put(MarkLogicSinkConfig.MAX_RETRIES, "10");
        conf.put("topics", "trades");
        
        client = DatabaseClientFactory.newClient("localhost", 8000, new DigestAuthContext("admin", "admin"));
        manager = client.newJSONDocumentManager();
    }

    public QuoteRequest find(String url){
        
        final DocumentPage documentPage = manager.read(url);
        if(documentPage.hasNext()){
            JacksonHandle handle = new JacksonHandle();
            handle = documentPage.nextContent(handle);
            return MAPPER.convertValue(handle.get(), QuoteRequest.class);
        }
        return null;
    }
    
    public void delete(String url){
        manager.delete(url);
    }
   
}
