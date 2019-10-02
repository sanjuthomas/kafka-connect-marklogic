package com.sanjuthomas.marklogic.writer;

import java.util.List;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sanjuthomas.marklogic.bean.SinkDocument;

/**
 * 
 * Construct micro batches as per the configured size.
 * 
 * @author Sanju Thomas
 *
 */
public class Partitioner {

  private final int bulkMutateRowsMaxSize;

  Partitioner(final int bulkMutateRowsMaxSize) {
    this.bulkMutateRowsMaxSize = bulkMutateRowsMaxSize;
  }

  public List<List<SinkDocument>> partitions(final List<SinkDocument> rows) {
    Preconditions.checkArgument(!(rows == null || rows.size() == 0),
        "argument rows can't be null or empty");
    return Lists.partition(rows, partitionsCount(rows.size()));
  }

  @VisibleForTesting
  int partitionsCount(final int totalRows) {
   if (totalRows > bulkMutateRowsMaxSize) {
      return bulkMutateRowsMaxSize;
    }
    return totalRows;
  }

}