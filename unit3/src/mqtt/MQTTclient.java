package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import runtime.Scheduler;

public class MQTTclient implements MqttCallback {

	private Scheduler scheduler;
	private MqttClient client;
	private String receivedMessage;

	public MQTTclient(String broker, String myAddress, boolean conf, Scheduler s) {
		scheduler = s;
		MemoryPersistence pers = new MemoryPersistence();
		try {
			client = new MqttClient(broker, myAddress, pers);
			MqttConnectOptions opts = new MqttConnectOptions();
			opts.setCleanSession(true);
			client.connect(opts);
			assert client.isConnected() : "Failed to connect to broker";  // Ensure connection is successful
			client.setCallback(this);
			scheduler.addToQueueLast("MQTTReady ");
		}
		catch (MqttException e) {
			System.err.println("MQTT Exception: " + e);
			scheduler.addToQueueLast("MQTTError");
		}
	}

	// Method to send a message to a specified topic
	public void sendMessage(String topic, String content) {
		assert client.isConnected() : "Client is not connected. Cannot send message.";
		try {
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(2);  // Setting QoS level to 2 for reliable delivery
			client.publish(topic, message);
			System.out.println("Message sent to topic: " + topic);
			scheduler.addToQueueLast("MQTTMessageSent");
		} catch (MqttException e) {
			System.err.println("Failed to send message: " + e.getMessage());
			scheduler.addToQueueLast("MQTTMessageSendError");
		}
	}

	// Method to subscribe to a specific topic
	public void subscribe(String topic) {
		assert client.isConnected() : "Client is not connected. Cannot subscribe to topic.";
		try {
			client.subscribe(topic, 2);  // Setting QoS level to 2 for reliable subscription
			System.out.println("Subscribed to topic: " + topic);
			scheduler.addToQueueLast("MQTTSubscribed");
		} catch (MqttException e) {
			System.err.println("Failed to subscribe to topic: " + e.getMessage());
			scheduler.addToQueueLast("MQTTSubscribeError");
		}
	}

	// Method to disconnect from the MQTT broker
	public void disconnect() {
		try {
			if (client.isConnected()) {
				client.disconnect();
				System.out.println("Disconnected from the broker.");
				scheduler.addToQueueLast("MQTTDisconnected");
			}
		} catch (MqttException e) {
			System.err.println("Failed to disconnect: " + e.getMessage());
			scheduler.addToQueueLast("MQTTDisconnectError");
		}
	}

	// Method to check if the client is connected to the MQTT broker
	public boolean isConnected() {
		return client.isConnected();
	}

	public String getLastMessage() {
		return receivedMessage;
	}

	// Method triggered when the connection to the broker is lost
	public void connectionLost(Throwable e) {
		System.err.println("Connection lost: " + e.getMessage());
		scheduler.addToQueueLast("MQTTConnectionLost");
	}

	// Method triggered when a message delivery is complete
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("Message delivery complete. Message ID: " + token.getMessageId());
		scheduler.addToQueueLast("MQTTDeliveryComplete");
	}

	// Method triggered when a message is received on a subscribed topic
	public void messageArrived(String topic, MqttMessage mess) {
		receivedMessage = new String(mess.getPayload());
		System.out.println("Message received on topic: " + topic + " Message: " + receivedMessage);

		// Notify scheduler about the message arrival
		scheduler.addToQueueLast("MQTTMessageReceived");
	}
}
