package org.sanju.kafka.marklogic.connector;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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

	private static final Logger logger = LoggerFactory
			.getLogger(MarkLogicSincTask.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ContentType DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_JSON;

	private int batchSize = 100;
	private String connectionUrl = "http://localhost:8000";
	private String endpoint = "/v1/documents";

	@Override
	public void put(final Collection<SinkRecord> arg0) {

		final List<SinkRecord> records = new ArrayList<>(arg0);
		final int size = records.size() / batchSize;
		final List<List<SinkRecord>> recordsPartitions = Lists.partition(
				records, size);

		recordsPartitions.forEach(recordPartition -> {
			// write into ML
			});
	}

	@Override
	public void start(Map<String, String> config) {
		try {
			batchSize = Integer.valueOf(config
					.get(MarkLogicSinkConfig.BATCH_SIZE));
			connectionUrl = config.get(MarkLogicSinkConfig.CONNECTION_URL);
			endpoint = config.get(MarkLogicSinkConfig.ENDPOINT);
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
	private HttpPost createPostRequest(final Object payload) {

		final URIBuilder uriBuilder = getURIBuilder();
		try {
			final String jsonString = MAPPER.writeValueAsString(payload);
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
}
