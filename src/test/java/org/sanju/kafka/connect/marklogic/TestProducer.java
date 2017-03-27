package org.sanju.kafka.connect.marklogic;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class TestProducer {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static void main(String[] args) {

		String topicName = "trades";
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
				"org.apache.kafka.connect.json.JsonSerializer");
		Producer<String, JsonNode> producer = new KafkaProducer<String, JsonNode>(props);
		for (int i = 0; i < 10000; i++){
			final Account account = new Account("A" + i);
			final Client client = new Client("C" + i, account);
			final QuoteRequest quoteRequest = new QuoteRequest("Q" + i, UUID.randomUUID().toString(), 
					ThreadLocalRandom.current().nextInt(1, 100 + 1), client);
			JsonNode jsonNode = MAPPER.valueToTree(quoteRequest);
			producer.send(new ProducerRecord<String, JsonNode>(topicName, jsonNode));
		}
		producer.flush();
		producer.close();
		System.out.println("Message sent successfully");

	}
}
