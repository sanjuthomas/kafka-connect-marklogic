package org.sanju.kafka.connect.marklogic.sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MarkLogicSinkConnector extends SinkConnector{
	
	public static final String MARKLOGIC_CONNECTOR_VERSION = "1.0";
	
	private Map<String, String> config;

	@Override
	public ConfigDef config() {
		
		return MarkLogicSinkConfig.CONFIG_DEF;
	}

	@Override
	public void start(Map<String, String> arg0) {
		config = arg0;
	}

	@Override
	public void stop() {
		
	}

	@Override
	public Class<? extends Task> taskClass() {
		
		return MarkLogicSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int taskCunt) {
	
	    final List<Map<String, String>> configs = new ArrayList<>(taskCunt);
	    for (int i = 0; i < taskCunt; ++i) {
	      configs.add(config);
	    }
	    return configs;
	}

	@Override
	public String version() {
		
		return MARKLOGIC_CONNECTOR_VERSION;
	}
	

}
