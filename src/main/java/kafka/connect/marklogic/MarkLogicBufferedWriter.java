package kafka.connect.marklogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import kafka.connect.marklogic.sink.MarkLogicSinkConfig;

import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.datamovement.DataMovementManager;
import com.marklogic.client.datamovement.WriteBatcher;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicBufferedWriter extends MarkLogicWriter implements Writer{
	
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicBufferedWriter.class);
	private final int batchSize;
	private final BufferedRecords bufferedRecords;
	private final DataMovementManager manager;
	
	public MarkLogicBufferedWriter(final Map<String, String> config){ 
	    super(config);
	    bufferedRecords = new BufferedRecords();
	    manager = super.client.newDataMovementManager();
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
	}

    @Override
    public void write(final Collection<SinkRecord> recrods) {
       recrods.forEach(r -> bufferedRecords.buffer(r));
       flush();
    }
    

    private void flush() {
        
        final WriteBatcher batcher = manager.newWriteBatcher();
        
        batcher.withBatchSize(batchSize).withThreadCount(8).onBatchFailure((b, t) -> {
            logger.error("batch write failed {}", t);
            throw new RetriableException(t.getMessage());
        });
        
        manager.startJob(batcher);
        this.bufferedRecords.forEach(r -> {
            final Map<?, ?> v = new LinkedHashMap<>((Map<?,?>) r.value());
            batcher.add(super.url(v), super.handle(v));
        });
        
        batcher.flushAndWait();
        manager.stopJob(batcher);
        bufferedRecords.clear();
    }
    
}
