package threads;

import connectdb.Crash;
import gui.Controller;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 1 subscribe
// 2 publish
// 3 compute
// 4 insert

public class ComputationalServiceRunnable implements Runnable, MqttCallback {
    private MqttClient sampleClient = null;
    String topic1 = "2204694f62075e08";
    String topic2 = "6f51888c3715cd1d";

    // next sample data:
    String id = null;
    float latitude =0;
    float longitude =0;
    float proximity =0;
    float s1x =0;
    float s1y =0;
    float s1z = 0;
    String sampletime_s;
    Date sampletime;
    boolean confirmation;

    public void subscribe() {

        String broker = "tcp://localhost:1883";
        String clientId = "JavaSampleSubscriber";

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            System.out.println("Subscribing	to topics " + topic1 + " " + topic2);

            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            sampleClient.setCallback(this);

            System.out.println("Connecting	to	broker:	" + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");


            sampleClient.subscribe(topic1, 2);
            sampleClient.subscribe(topic2, 2);
        } catch (MqttException me) {
            me.printStackTrace();
        }

    }

    public void unsubscribe() {
        if (sampleClient != null) {
            try {
                sampleClient.disconnect();
                System.out.println("unsubscribe");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void publish(String content) {
        String topic = "MQTT";
        String broker = "tcp://localhost:1883";
        String clientId = "JavaSamplePublisher";

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);

            System.out.println("Publishing	message:	" + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(2);
            sampleClient.publish(topic, message);

            System.out.println("Message	published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    // id:2204694f62075e08,long:32,lat:28,prox:5,s1x:0,s2y:0,s1z:0,date:2010-10-10 00-00-00.000

    private boolean compute(String msg) {
        String [] pairs = msg.split(",");
        int total_pairs = pairs.length;

        for (int i=0;i<total_pairs;i++) {
            String pair = pairs[i];
            String[] values = pair.split(":");
            String key = values[0];
            String value = values[1];

            if (key.equals("id")) {
                id = value;
            }else if (key.equals("long")) {
				longitude = Float.parseFloat(value);
			}else if (key.equals("lat")) {
				latitude = Float.parseFloat(value);
            }else if (key.equals("prox")) {
				proximity = Float.parseFloat(value);
			}else if (key.equals("s1x")) {
				s1x = Float.parseFloat(value);
			}else if (key.equals("s1y")) {
				s1y = Float.parseFloat(value);
			}else if (key.equals("s1z")) {
				s1z = Float.parseFloat(value);
			} else if (key.equals("date")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS", Locale.ENGLISH);
                try {
                    sampletime = df.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                    sampletime = new Date();
                }
                sampletime_s = value;
            }
        }

        confirmation = false;

        if (proximity <= Controller.state.getSensor1_thres() && s1x >= Controller.state.getSensor2_thres_x() && s1y >= Controller.state.getSensor2_thres_y() && s1z >= Controller.state.getSensor2_thres_z()) {
            return true;
        } else {
            return false;
        }
    }

    public void run() {
        subscribe();
    }

    // -------------------------------------------------------------------------------
    public void connectionLost(Throwable throwable) {

    }

    // Minima: id:2204694f62075e08,lng:10,lat:10,
    public void messageArrived(String mobile_id, MqttMessage mqttMessage) throws Exception {
        String mine = mobile_id;
        String other;

        if (mine.equals(topic1)) {
            other = topic2;
        } else {
            other = topic1;
        }

        String m = new String(mqttMessage.getPayload());
        System.out.println("messageArrived: " + m);

        if (compute(m) == true) {
            Date myDate = sampletime;
            Date otherDate = Crash.getLastDate(other);

            if (myDate != null && otherDate != null) {
                long x = myDate.getTime() - otherDate.getTime();
                System.out.println("dt=" +x);
                if (x < 1000) {
                    confirmation = true;
                }
            }

            Crash.insert(mobile_id, latitude, longitude, proximity, s1x, s1y, s1z, sampletime_s, confirmation);

            if (confirmation == true) {
                int id =  Crash.getLastId(other);
                Crash.update(id);
                publish("collision");
            } else {
                publish(mobile_id);
            }
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}
