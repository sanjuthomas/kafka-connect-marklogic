package org.sanju.kafka.connect.marklogic.sink;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSinkConfig extends AbstractConfig {

	public static final String CONNECTION_URL = "ml.connection.url";
	private static final String CONNECTION_URL_DOC = "ml application server connection URL";
	
	public static final String ENDPOINT = "ml.endpoint";
	private static final String ENDPOINT_DOC = "ml rest endpoint";

	public static final String CONNECTION_USER = "ml.connection.user";
	private static final String CONNECTION_USER_DOC = "ml connection user.";

	public static final String CONNECTION_PASSWORD = "ml.connection.password";
	private static final String CONNECTION_PASSWORD_DOC = "ml connection password";

	public static final String BATCH_SIZE = "ml.batch.size";
	private static final String BATCH_SIZE_DOC = "ml batch size";
	
	public static ConfigDef CONFIG_DEF;

	public MarkLogicSinkConfig(ConfigDef definition, Map<?, ?> originals) {
		super(definition, originals);
		definition
				.define(CONNECTION_URL, Type.STRING, Importance.HIGH,
						CONNECTION_URL_DOC)
				.define(ENDPOINT, Type.STRING, Importance.HIGH,
						ENDPOINT_DOC)
				.define(CONNECTION_USER, Type.STRING, Importance.HIGH,
						CONNECTION_USER_DOC)
				.define(CONNECTION_PASSWORD, Type.STRING, Importance.LOW,
						CONNECTION_PASSWORD_DOC)
				.define(BATCH_SIZE, Type.STRING, Importance.LOW, BATCH_SIZE_DOC);
		CONFIG_DEF =  definition;
	}

}
