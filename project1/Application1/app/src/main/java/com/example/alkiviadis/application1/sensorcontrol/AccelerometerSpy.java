package com.example.alkiviadis.application1.sensorcontrol;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.PlaybackParams;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alkiviadis.application1.mainactivitylisteners.ManualAutomaticListener;
import com.example.alkiviadis.application1.mainactivitylisteners.OnlineOfflineListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import state.CurrentState;

public class AccelerometerSpy implements SensorEventListener, MqttCallback {
    private TextView textView;
    private Activity activity;
    private CurrentState state;
    private Toast toast;
    private Toast toastMqttConfirmed;
    private Toast toastMqttUnconfirmed;
    private ConnectivityManager cm;

    //for audio message
    private final int duration = 3; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private final double freqOfTone = 440; // hz

    private final byte generatedSnd[] = new byte[2 * numSamples];

    private ChoiceMaker maker = new ChoiceMaker();

    private Date lastDate = new Date();

    private AudioTrack audioTrack = null;
    private AudioTrack audioTrackMqtt = null;

    public AccelerometerSpy(TextView textView, Activity activity, CurrentState state, ConnectivityManager cm) {
        this.textView = textView;
        this.activity = activity;
        this.state = state;
        this.toast = Toast.makeText(activity, "WATCH OUT!", Toast.LENGTH_LONG);
        this.toastMqttConfirmed = Toast.makeText(activity, "WATCH OUT! MQQT confirmed", Toast.LENGTH_LONG);
        this.toastMqttUnconfirmed = Toast.makeText(activity, "WATCH OUT! MQTT unconfirmed", Toast.LENGTH_LONG);
        this.cm = cm;

        genTone();
        genToneMqtt();
    }

    private void genToneMqtt() {
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }

        audioTrackMqtt = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length, AudioTrack.MODE_STATIC);
        audioTrackMqtt.write(generatedSnd, 0, generatedSnd.length);
        audioTrackMqtt.setLoopPoints(0, generatedSnd.length/4, -1);
    }

    void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length, AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.setLoopPoints(0, generatedSnd.length/4, -1);
    }


    private void publish(String content) {
        String id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        String topic = id;
        String clientId = id + "Publisher";
        String broker = "tcp://" + state.getServer_ip() + ":1883";

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float [] accevalues = event.values;
        float [] proxValues = ProximitySpy.values;

        String s1 = "Proximity sensor value: "  + proxValues[0];
        String s2 = "Accelerometer sensor value: \n"  + accevalues[0] + "\n" + accevalues[1] + "\n" + accevalues[2];
        String s3;
        if (GpsSpy.latitude != null && GpsSpy.longitude != null) {
            s3 = "Gps " + GpsSpy.latitude + " " + GpsSpy.longitude;
        } else {
            s3 = null;
        }

        String msg = s1 + "\n" + s2 + "\n" + s3;

        textView.setText(msg); // update GUI

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean internet_available = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (ManualAutomaticListener.manual == false) {
            if (internet_available) {
                OnlineOfflineListener.online = true;
                OnlineOfflineListener.updategui(OnlineOfflineListener.button);
            } else {
                OnlineOfflineListener.online = false;
                OnlineOfflineListener.updategui(OnlineOfflineListener.button);
            }
        }

        boolean useMqtt = maker.useMqtt(ManualAutomaticListener.manual, OnlineOfflineListener.online, internet_available );


        if (useMqtt == true) {
            if (GpsSpy.longitude != null && GpsSpy.latitude != null && proxValues != null && accevalues != null) {
                if (new Date().getTime() - lastDate.getTime() < 250) {
                    return;
                }

                lastDate = new Date();

                String id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

                // id:2204694f62075e09,long:42,lat:38,prox:5,s1x:0,s2y:0,s1z:0,date:2010-10-10 00-00-00.000
                StringBuffer buf = new StringBuffer();

                String now = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss.SSS", Locale.ENGLISH).format(new Date());

                buf.append("id:");
                buf.append(id);
                buf.append(",");

                buf.append("long:");
                buf.append(GpsSpy.longitude);
                buf.append(",");

                buf.append("lat:");
                buf.append(GpsSpy.latitude);
                buf.append(",");

                buf.append("prox:");
                buf.append(proxValues[0]);
                buf.append(",");

                buf.append("s1x:");
                buf.append(accevalues[0]);
                buf.append(",");

                buf.append("s1y:");
                buf.append(accevalues[1]);
                buf.append(",");

                buf.append("s1z:");
                buf.append(accevalues[2]);
                buf.append(",");

                buf.append("date:");
                buf.append(now);

                msg = buf.toString();

                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        String msg = params[0];
                        publish(msg);
                        return null;
                    }
                }.execute(msg);
            }
        }


        if (useMqtt == false) {
            //Conflict Condition
            if (proxValues != null) {
                if (proxValues[0] <= state.getSensor1_thres() &&
                        Math.abs(accevalues[0]) >= state.getSensor2_thres_x() &&
                        Math.abs(accevalues[1]) >= state.getSensor2_thres_y() &&
                        Math.abs(accevalues[2]) >= state.getSensor2_thres_z()) {
                    toast.show();

                    //Loop audio until danger of conflict is gone
                    if (!(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
                        audioTrack.setLoopPoints(0, generatedSnd.length / 4, -1);
                        audioTrack.play();
                    }
                    Log.i("*******AT*******", "state = " + audioTrack.getState() + " " + audioTrack.getPlayState());
                    //danger of conflict is gone
                } else {
                    toast.cancel();

                    if ((audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
                        audioTrack.stop();
                    }
                }
            } else {
                toast.cancel();

                if ((audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
                    audioTrack.stop();
                }
            }
        }

        if (audioTrackMqtt != null) {
            if ((audioTrackMqtt.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
                int x = audioTrackMqtt.getPlaybackHeadPosition();
                if (x > numSamples) {
                    audioTrackMqtt.stop();
                }
            }
        }
    }

    public void stopAudio() {
        if ((audioTrackMqtt.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
            audioTrackMqtt.stop();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String id = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

        String m = new String(mqttMessage.getPayload());
        System.out.println("messageArrived: " + m);


        if (m.equals(id)) {
            toastMqttConfirmed.cancel();
            toastMqttUnconfirmed.show();

            //Loop audio until danger of conflict is gone
            if (!(audioTrackMqtt.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
                audioTrackMqtt.play();
            }
        }
        if (m.equals("collision")) {
            toastMqttUnconfirmed.cancel();
            toastMqttConfirmed.show();

            //Loop audio until danger of conflict is gone
            if (!(audioTrackMqtt.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
                audioTrackMqtt.play();
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
