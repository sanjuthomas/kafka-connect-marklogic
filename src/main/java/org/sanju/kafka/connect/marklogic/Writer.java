package org.sanju.kafka.connect.marklogic;

import java.util.List;

import org.apache.kafka.connect.sink.SinkRecord;

/**
 * 
 * @author Sanju Thomas
 *
 */
public interface Writer {
	
	void write(final List<SinkRecord> records);
}
