package utils;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.*;

public class MQTT {

	private IMqttClient client;
	private String topic;

	public MQTT(String url, String topic, IMqttMessageListener callback) {
		this.topic = topic;
		System.out.println("Creating MQTT connection: " + url + " " + topic);
		try {
			String clientId = UUID.randomUUID().toString();
			IMqttClient client = new MqttClient(url, clientId);
			
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			client.connect(options);
			
			this.client = client;
			client.subscribe(this.topic, callback);
		} catch (Exception e) {
			System.out.println("Error creating MQTT connection: " + e.toString());
		}
	}

	public void send(String msg) {
		if (!client.isConnected()) {
	        return;
	    }

	    try {
		    byte[] payload = String.format("%s", msg).getBytes();
		    MqttMessage mqttMsg = new MqttMessage(payload);
		    mqttMsg.setQos(2);
		    mqttMsg.setRetained(false);
			client.publish(this.topic, mqttMsg);
			System.out.println("Sent: " + new String(payload));
			
			
			
		} catch (Exception e) {}
	}
	
	/* Former format of messages in HomeIn platform:
		{
		  "id": "AAA", // unique ID - I think it is an identifier of the message itself, no ???
		  "timestamp": 876545678000, // un entier en millisecondes l'epoch unix de l'�mission
		  "type": "Mashup",
		  "source": "BBB", // le composant qui est l�emetteur du message
		  "data": {
		     "mashup": "LampeSalon.SetColor(red); MaTV.SetState(on)"
		  }
		}
 	*/
	public String buildCmd(String id, String type, String source, String cmd) {
		return "{ 'id': '" + id + "', 'timestamp': " + (System.currentTimeMillis() / 1000L) +
			   ", 'type': '" + type + "', 'source': '" + source + "', 'data': { 'mashup': '" + cmd + "' } }";
	}
}
