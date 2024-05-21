package componentes;

import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MySimpleLogger;

import java.sql.Timestamp;

public class SmartCar_TrafficNotifier extends MyMqttClient{

    private static final String Type = "TRAFFIC";

    public SmartCar_TrafficNotifier(String clientId, SmartCar smartcar, String MQTTBrokerURL) {
        super(clientId, smartcar, MQTTBrokerURL);
    }

    public void traffic(String smartCarID, String notificationType, RoadPlace place) {

        String myTopic =  "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + place.getRoad() + "/traffic";

        MqttTopic topic = myClient.getTopic(myTopic);


        // publish traffic
        // TIP: habrá que adaptar este mensaje si queremos conectarlo al servicio de tráfico SmartTraffic PTPaterna,
        //      para que siga la estructura allí propuesta (ver documento Seminario 3)
        JSONObject M4 = new JSONObject();
        JSONObject msg = new JSONObject();
        String timestamp = Long.toString(System.currentTimeMillis());
        try {
            M4.put("id", "MSG_" + timestamp);
            M4.put("type", Type);
            M4.put("timestamp", timestamp);

            msg.put("action", notificationType);
            msg.put("road", "R1");
            msg.put("road-segment", place.getRoad());
            msg.put("vehicle-id", smartCarID);
            msg.put("position", place.getKm());
            msg.put("role", "");

            M4.put("msg", msg);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        int pubQoS = 0;
        MqttMessage message = new MqttMessage(M4.toString().getBytes());
        message.setQos(pubQoS);
        message.setRetained(false);

        // Publish the message
        MySimpleLogger.trace(this.clientId, "Publishing to topic " + topic + " qos " + pubQoS);
        MqttDeliveryToken token = null;
        try {
            // publish message to broker
            token = topic.publish(message);
            MySimpleLogger.trace(this.clientId, M4.toString());
            // Wait until the message has been delivered to the broker
            token.waitForCompletion();
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
