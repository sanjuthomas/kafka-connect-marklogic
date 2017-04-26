package org.sanju.kafka.connect.marklogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.sanju.kafka.connect.marklogic.sink.MarkLogicSinkConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicBufferedWriter extends MarkLogicWriter{
	
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicBufferedWriter.class);
	private final int batchSize;
	private final BufferedRecords bufferedRecords = new BufferedRecords();
	
	public MarkLogicBufferedWriter(final Map<String, String> config){ 
	    super(config);
	    batchSize = Integer.valueOf(config.get(MarkLogicSinkConfig.BATCH_SIZE));
	    
	}
	
	/**
	 * 
	 * Buffer the Records until the batch size reached.
	 *
	 */
	class BufferedRecords extends ArrayList<SinkRecord>{
	    
        private static final long serialVersionUID = 1L;
        
        void buffer(final SinkRecord r){
	        add(r);
	        if(batchSize <= size()){
	            logger.debug("buffer size is {}", batchSize);
	            flush();
	            logger.debug("flushed the buffer");
	        }
	    }

        /**
         * As of today, there is no batch support.
         * This implementation stream the data into MarkLogic.
         * Batch support will be added for ML9 using DMSDK.
         */
        void flush() {
            forEach(record -> {
                process(createPutRequest(record.value(), record.topic()));
            });
            clear();
        }
	}

    @Override
    public void write(final Collection<SinkRecord> recrods) {
       recrods.forEach(r -> bufferedRecords.buffer(r));
       bufferedRecords.flush();
    }
    
}
