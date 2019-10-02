package com.sanjuthomas.marklogic.writer;

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
