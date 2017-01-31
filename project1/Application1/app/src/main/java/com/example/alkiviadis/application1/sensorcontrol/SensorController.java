package com.example.alkiviadis.application1.sensorcontrol;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.widget.TextView;

import com.example.alkiviadis.application1.R;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import state.CurrentState;

//starts and stops monitoring of sensors
public class SensorController {
    private Activity activity;
    private SensorManager sMgr;
    private Sensor proximitySensor;
    private Sensor accelerometerSensor;

    private ProximitySpy proxSpy;
    private AccelerometerSpy accSpy;
    private GpsSpy gpsSpy;

    private CurrentState state;

    private ConnectivityManager cm;

    private MqttClient sampleClient = null;

    public void init(Activity activity, ConnectivityManager connectivityManager) {
        this.activity=activity;
        this.cm = connectivityManager;
        sMgr = (SensorManager)activity.getSystemService(activity.SENSOR_SERVICE);

        proximitySensor = sMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        accelerometerSensor = sMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start(CurrentState state) {
        this.state = state;

        proxSpy = new ProximitySpy(state, cm);

        TextView tv = (TextView)activity.findViewById(R.id.textviewStatistics);
        accSpy = new AccelerometerSpy(tv, activity, state, cm);

        sMgr.registerListener(proxSpy, proximitySensor, state.getMilliseconds());
        sMgr.registerListener(accSpy, accelerometerSensor, state.getMilliseconds());

        subscribe();

    }

    public void stop() {
        if (proxSpy != null) {
            sMgr.unregisterListener(proxSpy);
            proxSpy = null;
        }

        if (accSpy != null) {
            sMgr.unregisterListener(accSpy);
            accSpy.stopAudio();
            accSpy = null;
        }


        unsubscribe();
    }

    public void subscribe() {
        String id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        String topic = "MQTT";
        String clientId = id + "Subscriber";
        String broker = "tcp://" + state.getServer_ip() + ":1883";


        MemoryPersistence persistence = new MemoryPersistence();

        try {
            System.out.println("Subscribing	to topics " + topic);

            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            sampleClient.setCallback(accSpy);

            System.out.println("Connecting	to	broker:	" + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");


            sampleClient.subscribe(topic, 2);
        } catch (MqttException me) {
            me.printStackTrace();
        }

    }

    public void unsubscribe() {
        if (sampleClient != null) {
            try {
                sampleClient.disconnect();
                System.out.println("unsuscbrive");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }


}