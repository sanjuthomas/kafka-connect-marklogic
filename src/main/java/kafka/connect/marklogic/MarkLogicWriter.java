package kafka.connect.marklogic;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import kafka.connect.marklogic.sink.MarkLogicSinkConfig;

import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.DigestAuthContext;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.JacksonHandle;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicWriter implements Writer{
    
    private static final Logger logger = LoggerFactory.getLogger(MarkLogicWriter.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String URL = "url";
    private final JSONDocumentManager manager;
    protected final DatabaseClient client;
    
    public MarkLogicWriter(final Map<String, String> config){
        client = DatabaseClientFactory.newClient(config.get(MarkLogicSinkConfig.CONNECTION_HOST), 
                        Integer.valueOf(config.get(MarkLogicSinkConfig.CONNECTION_PORT)),
                        new DigestAuthContext(config.get(MarkLogicSinkConfig.CONNECTION_USER), 
                        config.get(MarkLogicSinkConfig.CONNECTION_PASSWORD)));
        manager = client.newJSONDocumentManager();
    }
    
    public void write(final Collection<SinkRecord> recrods){
        
        recrods.forEach(r -> {
            logger.debug("received value {}, and collection {}", r.value(), r.topic());
            final Map<?, ?> v = new LinkedHashMap<>((Map<?,?>) r.value());
            try {
                manager.write(url(v), handle(v));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        });
    }
    
    protected String url(final Map<?, ?> valueMap ){
        return valueMap.get(URL) == null ? UUID.randomUUID().toString() : valueMap.remove(URL).toString();
    }
    
    protected JacksonHandle handle(final Map<?, ?> valueMap ){
        return new JacksonHandle(MAPPER.convertValue(valueMap, JsonNode.class));
    }
}
