package org.sanju.kafka.connect.marklogic.sink;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestMarkLogicWriter {
	
	private final Map<String, String> conf = new HashMap<>();
	private MarkLogicWriter markLogicWriter;

	@Before
	public void setup() throws MalformedURLException{
		
		conf.put(MarkLogicSinkConfig.CONNECTION_URL, "http://localhost:8000/v1/documents");
		conf.put(MarkLogicSinkConfig.CONNECTION_USER, "admin");
		conf.put(MarkLogicSinkConfig.CONNECTION_PASSWORD, "admin");
		
		markLogicWriter = new MarkLogicWriter(conf);
	}
	
	@Test
	public void shouldWrite(){
		
		List<SinkRecord> documents = new ArrayList<SinkRecord>();
		documents.add(new SinkRecord("topic", 1, null, null, null, new Document("John", 1), 0));
		documents.add(new SinkRecord("topic", 1, null, null, null, new Document("Doe", 2), 0));
		
		markLogicWriter.write(documents);
	}
	
	class Document{
		
		private String name;
		private int id;
		
		Document(String name, int id){
			this.name = name;
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
	}
}

