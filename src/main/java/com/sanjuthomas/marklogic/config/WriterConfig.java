package com.sanjuthomas.marklogic.config;

public class WriterConfig {
  
  
  
  public static class ErrorHandlerConfig {
    private int maxRetryCount;
    private int retryBackoffSeconds;
    private boolean exponentialBackoff;

    public ErrorHandlerConfig(final int maxRetryCount, final int retryBackoffSeconds,
        final boolean exponentialBackoff) {
      this.maxRetryCount = maxRetryCount;
      this.retryBackoffSeconds = retryBackoffSeconds;
      this.exponentialBackoff = exponentialBackoff;
    }

    public int maxRetryCount() {
      return maxRetryCount;
    }

    public int retryBackoffSeconds() {
      return retryBackoffSeconds;
    }

    public boolean exponentialBackoff() {
      return exponentialBackoff;
    }
  }

}
