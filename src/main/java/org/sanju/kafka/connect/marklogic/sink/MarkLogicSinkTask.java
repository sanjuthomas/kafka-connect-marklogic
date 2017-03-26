package org.sanju.kafka.connect.marklogic.sink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSinkTask extends SinkTask {

	private static final Logger logger = LoggerFactory.getLogger(MarkLogicSinkTask.class);
	private int batchSize;
	private Writer writer;

	@Override
	public void put(final Collection<SinkRecord> records) {

		final int partitionSize = records.size() / batchSize;
		final List<List<SinkRecord>> recordsPartitions = Lists.partition(new ArrayList<>(records), 
				partitionSize == 0 ? 1 : partitionSize);
		recordsPartitions.forEach(partitions ->{
			writer.write(partitions);
		});
	}

	@Override
	public void start(Map<String, String> config) {
		writer = new MarkLogicWriter(config);
		batchSize = Integer.valueOf(config.get(MarkLogicSinkConfig.BATCH_SIZE));
	}

	@Override
	public void stop() {

		logger.info("stop called");
	}
	
	public void flush(Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
		
		logger.info("flush called");
	}

	public String version() {

		return MarkLogicSinkConnector.MARKLOGIC_CONNECTOR_VERSION;
	}

	
}
