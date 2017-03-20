package org.sanju.kafka.marklogic.connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import com.google.common.collect.Lists;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSincTask extends SinkTask {

	private int batchSize = 100;

	@Override
	public void put(final Collection<SinkRecord> arg0) {
		
		final List<SinkRecord> records = new ArrayList<>(arg0);
		final int size = records.size() / batchSize;
		final List<List<SinkRecord>> recordsPartitions = Lists.partition(records, size);
		
		recordsPartitions.forEach(recordPartition -> {
			//write into ML
		});
	}

	@Override
	public void start(Map<String, String> config) {
		try {
			batchSize = Integer.valueOf(config.get(MarkLogicSinkConfig.BATCH_SIZE));

		} catch (Exception e) {
			//batch size is defaulted to 100
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	public String version() {

		return MarkLogicSinkConnector.MarkLogicSinkConnectorVersion;
	}

}
