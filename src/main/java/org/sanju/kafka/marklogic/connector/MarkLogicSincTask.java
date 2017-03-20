package org.sanju.kafka.marklogic.connector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSincTask extends SinkTask {

	private static final Logger logger = LoggerFactory.getLogger(MarkLogicSincTask.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ContentType DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_JSON;

	private int batchSize = 100;
	private String connectionUrl = "http://localhost:8000";
	private String endpoint = "/v1/documents";
	private String user = "admin";
	private String password = "admin";

	@Override
	public void put(final Collection<SinkRecord> arg0) {

		final List<SinkRecord> records = new ArrayList<>(arg0);
		final int partitionSize = records.size() / batchSize;
		final List<List<SinkRecord>> recordsPartitions = Lists.partition(records, partitionSize);

		recordsPartitions.forEach(recordPartition -> {
			// write into ML
			});
	}

	@Override
	public void start(Map<String, String> config) {
		try {
			batchSize = Integer.valueOf(config.get(MarkLogicSinkConfig.BATCH_SIZE));
			connectionUrl = config.get(MarkLogicSinkConfig.CONNECTION_URL);
			endpoint = config.get(MarkLogicSinkConfig.ENDPOINT);
			user = config.get(MarkLogicSinkConfig.CONNECTION_USER);
			password = config.get(MarkLogicSinkConfig.CONNECTION_PASSWORD);
		} catch (Exception e) {
			// batch size is defaulted to 100
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	public String version() {

		return MarkLogicSinkConnector.MarkLogicSinkConnectorVersion;
	}

	/**
	 * @return
	 */
	private URIBuilder getURIBuilder() {

		final URIBuilder builder = new URIBuilder();
		final String scheme = connectionUrl.split(":")[0];
		final String host = connectionUrl.split(":")[1];
		builder.setScheme(scheme).setHost(host).setPath(endpoint);

		return builder;
	}

	/**
	 * 
	 * @param payload
	 * @return
	 */
	private HttpPost createPostRequest(final List<Struct> values) {

		final URIBuilder uriBuilder = getURIBuilder();
		try {
			final String jsonString = MAPPER.writeValueAsString(values);
			HttpPost request = new HttpPost(uriBuilder.build());
			final StringEntity params = new StringEntity(jsonString, "UTF-8");
			params.setContentType(DEFAULT_CONTENT_TYPE.toString());
			request.setEntity(params);
			return request;
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	private HttpResponse process(final HttpRequestBase request) throws ClientProtocolException, IOException {

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
		final HttpClientContext localContext = HttpClientContext.create();
		localContext.setCredentialsProvider(credentialsProvider);
		return httpClient.execute(request, localContext);
	}
}
