package org.sanju.kafka.connect.marklogic;

import java.util.Collection;

import java.util.Map;

/**
 * @author Sanju Thomas
 * 
 * 
 */
import org.apache.kafka.connect.sink.SinkRecord;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicDefaultWriter extends MarkLogicWriter{

    public MarkLogicDefaultWriter(final Map<String, String> config) {
        super(config);
    }

    @Override
    public void write(final Collection<SinkRecord> recrods) {
        recrods.forEach(record -> {
            process(createPutRequest(record.value(), record.topic()));
        });
    }

}
