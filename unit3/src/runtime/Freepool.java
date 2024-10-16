package runtime;

import mqtt.MQTTclient;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Freepool {
    private int _freepools;
    private Queue<String> _freepoolsQueue;
    private Queue<String> _bufferQueue;
    private MQTTclient _mqttClient;

    public Freepool(int freepools, MQTTclient mqttClient) {
        _freepools = freepools;
        _freepoolsQueue = new LinkedBlockingDeque<String>();
        _bufferQueue = new LinkedBlockingDeque<String>();
        _mqttClient = mqttClient;

        for (int i = 0; i < freepools; i++) {
            _freepoolsQueue.add("*"); // add token
        }
    }

    public void sendData(String data, String topic) {
        if (_freepools > 0) {
            String token = _freepoolsQueue.poll();
            assert token != null;
            _mqttClient.sendMessage(topic, token);
            _freepools--;
            _mqttClient.sendMessage(topic, data);
        } else {
            _bufferQueue.add(data);
        }
    }

    public void sendFreepool(String topic) {
        if (_freepools > 0) {
            String token = _freepoolsQueue.poll();
            assert token != null;
            _mqttClient.sendMessage(topic, token);
            _freepools--;
        }
    }

    public void receiveFreepool(String topic) {
        _freepools++;
        _freepoolsQueue.add("*");
        if (!_bufferQueue.isEmpty()) {
            String data = _bufferQueue.poll();
            sendData(data, topic);
        }
    }

    public int getFreepools() {
        return _freepools;
    }
}
