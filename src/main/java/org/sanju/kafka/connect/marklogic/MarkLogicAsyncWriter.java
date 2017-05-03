package org.sanju.kafka.connect.marklogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicAsyncWriter extends MarkLogicWriter {

    private static final Logger logger = LoggerFactory.getLogger(MarkLogicAsyncWriter.class);

    public MarkLogicAsyncWriter(final Map<String, String> config) {
        super(config);
    }

    @Override
    public void write(final Collection<SinkRecord> rs) {

        final ExecutorService fjPool = new ForkJoinPool(8);
        final List<Callable<HttpResponse>> rps = new ArrayList<>();
        try {
            rs.forEach(r -> {
            	final Callable<HttpResponse> c = () -> process(createPutRequest(r.value(), r.topic()));
                rps.add(c);
            });
            final List<Future<HttpResponse>> results = fjPool.invokeAll(rps);
            results.forEach(r -> {
                try {
                    r.get();
                } catch (Exception e) {
                    throw new RetriableException(e);
                }
            });
        } catch (Exception e) {
            logger.error("Exception occurred during the write, marking the entire collection of records with size {} to retry, exception was {}",
                    rs.size(), e);
            throw new RetriableException(e);
        }finally{
            fjPool.shutdown();
        }
    }

}
