package org.sanju.kafka.connect.marklogic;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.sanju.kafka.connect.marklogic.sink.MarkLogicSinkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicWriter implements Writer{
	
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicWriter.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ContentType DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_JSON;

	private final String connectionUrl;
	private final String user;
	private final String password;
	private final CloseableHttpClient httpClient;
	private final HttpClientContext localContext;
	private final RequestConfig requestConfig;
	
	public MarkLogicWriter(final Map<String, String> config){
		
		connectionUrl = config.get(MarkLogicSinkConfig.CONNECTION_URL);
		user = config.get(MarkLogicSinkConfig.CONNECTION_USER);
		password = config.get(MarkLogicSinkConfig.CONNECTION_PASSWORD);
		
		requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5 * 1000).build();
		localContext = HttpClientContext.create();
		httpClient = HttpClientBuilder.create().build();
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
        localContext.setCredentialsProvider(credentialsProvider);
        localContext.setRequestConfig(requestConfig);
    
	}
	
	/**
	 * change the implementation to batch using DMSDK when ML 9 is available, until then writing one by one
	 */
	@Override
	public void write(final List<SinkRecord> records) {
	
		records.forEach(record -> {
			final HttpPut post = createPutRequest(record.value(), record.topic());
			process(post);
		});
	}
	
	/**
	 * @return
	 * @throws MalformedURLException 
	 */
	private URIBuilder getURIBuilder(final String urlString, final String collection) throws MalformedURLException {

		logger.debug("received url {}, and collection {}", urlString, collection);
		final URIBuilder builder = new URIBuilder();
		final URL url = new URL(connectionUrl);
		builder.setScheme(url.getProtocol()).setHost(url.getAuthority()).setPath(url.getPath());
		builder.addParameter("uri", urlString);
		builder.addParameter("collection", collection);
		return builder;
	}

	/**
	 * 
	 * @param payload
	 * @return
	 */
	private HttpPut createPutRequest(final Object value, final String collection) {
	
		try {
			logger.debug("received value {}, and collection {}", value, collection);
			final URIBuilder uriBuilder = getURIBuilder(((Map<?,?>)value).get("url").toString(), collection);
			final String jsonString = MAPPER.writeValueAsString(value);
			HttpPut request = new HttpPut(uriBuilder.build());
			final StringEntity params = new StringEntity(jsonString, "UTF-8");
			params.setContentType(DEFAULT_CONTENT_TYPE.toString());
			request.setEntity(params);
			return request;
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
			throw new RetriableException(e);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
			throw new RetriableException(e);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			throw new RetriableException(e);
		} catch(Exception e){
		    logger.error(e.getMessage(), e);
		    throw new RetriableException(e);
		}
	}
	
	private HttpResponse process(final HttpRequestBase request) {

		try {
			return httpClient.execute(request, localContext);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RetriableException(e);
		}
	}

}
