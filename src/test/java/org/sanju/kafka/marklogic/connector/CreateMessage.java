package org.sanju.kafka.marklogic.connector;
import java.util.Properties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class CreateMessage {
	
	public static void main(String[] args) {
		
	      String topicName = "documents";
	      Properties props = new Properties();
	      props.put("bootstrap.servers", "localhost:32779");
	      props.put("acks", "all");
	      props.put("retries", 3);
	      props.put("batch.size", 16384);
	      props.put("linger.ms", 1);
	      props.put("buffer.memory", 33554432);
	      props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	      
	      Producer<String, String> producer = new KafkaProducer<String, String>(props);
	            
	      for(int i = 0; i < 1000; i++)
	         producer.send(new ProducerRecord<String, String>(topicName, 
	            Integer.toString(i), Integer.toString(i)));
	            producer.flush();
	            producer.close();
	            System.out.println("Message sent successfully");
	               
	}

}
