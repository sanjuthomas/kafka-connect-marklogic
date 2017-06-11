package kafka.connect.marklogic;

import java.util.Collection;

import org.apache.kafka.connect.sink.SinkRecord;

/**
 * 
 * @author Sanju Thomas
 *
 */
public interface Writer {
	
    /**
     * @param recrods
     */
	void write(final Collection<SinkRecord> records);
}
