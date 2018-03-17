package kafka.connect.marklogic.sink;

import java.util.Map;

import kafka.connect.marklogic.MarkLogicBufferedWriter;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSinkConfig extends AbstractConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(MarkLogicSinkConfig.class);
	
	public static final String CONNECTION_HOST = "ml.connection.host";
	private static final String CONNECTION_HOST_DOC = "ml application server hostname";
	
	public static final String CONNECTION_PORT = "ml.connection.port";
    private static final String CONNECTION_PORT_DOC = "ml application server port";
	
	public static final String CONNECTION_USER = "ml.connection.user";
	private static final String CONNECTION_USER_DOC = "ml connection user.";

	public static final String CONNECTION_PASSWORD = "ml.connection.password";
	private static final String CONNECTION_PASSWORD_DOC = "ml connection password";

	public static final String BATCH_SIZE = "ml.batch.size";
	private static final int BATCH_SIZE_DEFAULT = 1000;
	private static final String BATCH_SIZE_DOC = "ml batch size";
	
	public static final String WRITER_IMPL = "ml.writer.impl";
	private static final String WRITER_IMPL_DEFAULT = MarkLogicBufferedWriter.class.getCanonicalName();
	private static final String WRITER_IMPL_DOC = "ml writer implementation class name";
	
	public static final String MAX_RETRIES = "max.retries";
    private static final int MAX_RETRIES_DEFAULT = 100;
    private static final String MAX_RETRIES_DOC =  "The maximum number of times to retry on errors/exception before failing the task.";
		
	public static final String RETRY_BACKOFF_MS = "retry.backoff.ms";
    private static final int RETRY_BACKOFF_MS_DEFAULT = 10000;
	private static final String RETRY_BACKOFF_MS_DOC = "The time in milliseconds to wait following an error/exception before a retry attempt is made.";
	
	public static ConfigDef CONFIG_DEF = new ConfigDef()
			.define(CONNECTION_HOST, Type.STRING, Importance.HIGH, CONNECTION_HOST_DOC)
			.define(CONNECTION_PORT, Type.INT, Importance.HIGH, CONNECTION_PORT_DOC)
			.define(CONNECTION_USER, Type.STRING, Importance.HIGH, CONNECTION_USER_DOC)
			.define(CONNECTION_PASSWORD, Type.STRING, Importance.LOW, CONNECTION_PASSWORD_DOC)
			.define(BATCH_SIZE, Type.INT, BATCH_SIZE_DEFAULT, Importance.MEDIUM, BATCH_SIZE_DOC)
			.define(MAX_RETRIES, Type.INT, MAX_RETRIES_DEFAULT, Importance.MEDIUM, MAX_RETRIES_DOC)
			.define(RETRY_BACKOFF_MS, Type.INT, RETRY_BACKOFF_MS_DEFAULT, Importance.MEDIUM, RETRY_BACKOFF_MS_DOC)
			.define(WRITER_IMPL, Type.STRING, WRITER_IMPL_DEFAULT, Importance.MEDIUM, WRITER_IMPL_DOC);

	public MarkLogicSinkConfig(final Map<?, ?> originals) {
		
		super(CONFIG_DEF, originals, false);
		logger.info("Original Configs {}", originals);
	}

}
