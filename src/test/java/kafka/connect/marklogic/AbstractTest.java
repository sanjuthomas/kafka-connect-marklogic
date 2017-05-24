package kafka.connect.marklogic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import kafka.connect.marklogic.sink.MarkLogicSinkConfig;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Sanju Thomas
 *
 */
public abstract class AbstractTest {
    
    protected static final ObjectMapper MAPPER = new ObjectMapper();
    protected final Map<String, String> conf = new HashMap<>();

    public void setup(){

        conf.put(MarkLogicSinkConfig.CONNECTION_URL, "http://localhost:8000/v1/documents");
        conf.put(MarkLogicSinkConfig.CONNECTION_USER, "admin");
        conf.put(MarkLogicSinkConfig.CONNECTION_PASSWORD, "admin");
        conf.put(MarkLogicSinkConfig.BATCH_SIZE, "100");
        conf.put(MarkLogicSinkConfig.WRITER_IMPL, MarkLogicDefaultWriter.class.getCanonicalName());
        conf.put(MarkLogicSinkConfig.RETRY_BACKOFF_MS, "100");
        conf.put(MarkLogicSinkConfig.MAX_RETRIES, "10");
        conf.put("topics", "trades");
    }
    


    protected HttpResponse get(String url) throws ClientProtocolException, IOException, URISyntaxException {

        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "admin"));
        final HttpClientContext localContext = HttpClientContext.create();
        localContext.setCredentialsProvider(credentialsProvider);
        return httpClient.execute(createGet(url), localContext);
    }
    
    protected HttpResponse delete(String url) throws ClientProtocolException, IOException, URISyntaxException{
        
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "admin"));
        final HttpClientContext localContext = HttpClientContext.create();
        localContext.setCredentialsProvider(credentialsProvider);
        return httpClient.execute(createDelete(url), localContext);
    }

    private HttpRequestBase createGet(final String uri) throws URISyntaxException {

        final URIBuilder uriBuilder = getURIBuilder();
        uriBuilder.addParameter("uri", uri);
        return new HttpGet(uriBuilder.build());
    }
    
    private HttpRequestBase createDelete(final String uri) throws URISyntaxException {

        final URIBuilder uriBuilder = getURIBuilder();
        uriBuilder.addParameter("uri", uri);
        return new HttpDelete(uriBuilder.build());
    }

    private URIBuilder getURIBuilder() {

        final URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(8000)
                .setPath("/v1/documents");
        return builder;
    }

}
