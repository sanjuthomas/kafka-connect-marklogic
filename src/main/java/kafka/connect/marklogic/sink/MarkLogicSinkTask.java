package kafka.connect.marklogic.sink;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;

import kafka.connect.marklogic.MarkLogicWriter;
import kafka.connect.marklogic.Writer;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSinkTask extends SinkTask {

    private static final Logger logger = LoggerFactory.getLogger(MarkLogicSinkTask.class);
    private int timeout;
    private Writer writer;
    private Map<String, String> config;
    private int maxRetires;
    private int remainingRetries;

    @Override
    public void put(final Collection<SinkRecord> records) {

        if (records.isEmpty()) {
            logger.debug("Empty record collection to process");
            return;
        }

        final SinkRecord first = records.iterator().next();
        final int recordsCount = records.size();
        logger.debug("Received {} records. kafka coordinates from record: Topic - {}, Partition - {}, Offset - {}",
                        recordsCount, first.topic(), first.kafkaPartition(), first.kafkaOffset());

        try {
            writer.write(records);
        } catch (final RetriableException e) {
            if (maxRetires > 0 && remainingRetries == 0) {
                throw new ConnectException("Retries exhausted, ending the task. Manual restart is required.");
            }else{
                logger.warn("Setting the task timeout to {} ms upon RetriableException", timeout);
                initWriter(config);
                context.timeout(timeout);
                remainingRetries--;
                throw e;
            }
        }
        this.remainingRetries = maxRetires;
    }

    @Override
    public void start(final Map<String, String> config) {

        logger.info("start called!");
        this.config = config;
        this.timeout = Integer.valueOf(config.get(MarkLogicSinkConfig.RETRY_BACKOFF_MS));
        this.maxRetires = Integer.valueOf(config.get(MarkLogicSinkConfig.MAX_RETRIES));
        this.remainingRetries = maxRetires;
        initWriter(config);
    }
    
    private void initWriter(final Map<String, String> config){
        
        logger.info("initWriter called!");
        final String writerClazz = config.get(MarkLogicSinkConfig.WRITER_IMPL);
        logger.info("MarkLogic writer class {}", writerClazz);
        if(null == writerClazz || writerClazz.trim().isEmpty()){
            this.writer = new MarkLogicWriter(config);
        }else{
            try {
                final Constructor<?> c = Class.forName(writerClazz).getConstructor(Map.class);
                this.writer = (Writer) c.newInstance(config);
            } catch (Exception e) {
                logger.error("ml.writer.impl value is invalid {}", writerClazz);
                throw new ConnectException(e);
            }
        }
    }

    @Override
    public void stop() {

        logger.info("stop called!");
    }

    @Override
    public void flush(final Map<TopicPartition, OffsetAndMetadata> currentOffsets) {

        currentOffsets.forEach((k, v) -> logger.debug("Flush - Topic {}, Partition {}, Offset {}, Metadata {}",
                k.topic(), k.partition(), v.offset(), v.metadata()));
    }

    public String version() {

        return MarkLogicSinkConnector.MARKLOGIC_CONNECTOR_VERSION;
    }

}
