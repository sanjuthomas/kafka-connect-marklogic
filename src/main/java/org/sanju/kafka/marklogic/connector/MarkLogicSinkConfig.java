package org.sanju.kafka.marklogic.connector;

import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSinkConfig extends AbstractConfig{

	public MarkLogicSinkConfig(ConfigDef definition, Map<?, ?> originals) {
		super(definition, originals);
		// TODO Auto-generated constructor stub
	}

}
