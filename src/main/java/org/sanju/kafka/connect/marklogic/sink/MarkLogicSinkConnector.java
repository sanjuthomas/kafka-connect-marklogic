package org.sanju.kafka.connect.marklogic.sink;

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
	
	public static final String MarkLogicSinkConnectorVersion = "1.0";

	@Override
	public ConfigDef config() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends Task> taskClass() {
		return MarkLogicSinkTask.class;
	}

	@Override
	public List<Map<String, String>> taskConfigs(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String version() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
