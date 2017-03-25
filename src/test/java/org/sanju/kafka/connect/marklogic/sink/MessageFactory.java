package org.sanju.kafka.connect.marklogic.sink;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class MessageFactory {

	public static void main(String[] args) {

		String topicName = "documents";
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 3);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",
				"org.sanju.kafka.connect.marklogic.sink.PayloadSerializer");

		Producer<String, Object> producer = new KafkaProducer<String, Object>(props);

		for (int i = 1; i < 10000; i++)
			producer.send(new ProducerRecord<String, Object>(topicName,
					new Document(UUID.randomUUID().toString(), i)));
		producer.flush();
		producer.close();
		System.out.println("Message sent successfully");

	}

	static class Document{

		private String name;
		private int id;

		Document(String name, int id) {
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
