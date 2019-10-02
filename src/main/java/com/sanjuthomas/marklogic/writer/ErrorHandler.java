package com.sanjuthomas.marklogic.writer;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.common.annotation.InterfaceStability.Evolving;
import com.sanjuthomas.marklogic.config.WriterConfig.ErrorHandlerConfig;

/**
 * 
 * @author Sanju Thomas
 * @since 1.0.0
 *
 */
@Evolving
public class ErrorHandler {

  private ErrorHandlerConfig config;
  private AtomicInteger counter = new AtomicInteger(0);

  public ErrorHandler(final ErrorHandlerConfig config) {
    this.config = config;
  }

  public Result handle(Throwable exception) {
    if (counter.incrementAndGet() <= config.maxRetryCount()) {
      return new Result(true, retryBackOffSeconds(), counter.get());
    }
    return new Result(false, 0, counter.get());
  }

  private long retryBackOffSeconds() {
    if (config.exponentialBackoff()) {
      return counter.longValue() * config.retryBackoffSeconds();
    }
    return config.retryBackoffSeconds();
  }

  public void reset() {
    counter.set(0);
  }

  class Result {

    private boolean retry;
    private long secondsToSleep;
    private int attempt;

    Result(final boolean retry, final long secondsToSleep, final int attempt) {
      this.retry = retry;
      this.secondsToSleep = secondsToSleep;
      this.attempt = attempt;
    }

    public boolean retry() {
      return retry;
    }

    public long secondsToSleep() {
      return secondsToSleep;
    }

    public int attempt() {
      return attempt;
    }
  }
}