package gui;

import connectdb.Crash;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import state.CurrentState;

import java.util.ArrayList;

public class Controller {
    public static CurrentState state = new CurrentState();

    public TextField idTopic1;
    public TextField idTopic2;
    public TextField idTopic3;

    public TextField idTH1;
    public TextField idTH2x;
    public TextField idTH2y;
    public TextField idTH2z;

    public TextField textfieldID;
    public TextField textfieldMobile;
    public TextField textfieldLongitude;
    public TextField textfieldLatitude;

    public TextField textfieldSensorType1;
    public TextField textfieldSensorType2;
    public TextField textfieldSensor1Valuex;
    public TextField textfieldSensor1Valuey;
    public TextField textfieldSensor1Valuez;
    public TextField textfieldSensor2Valuex;
    public TextField textfieldSensor2Valuey;
    public TextField textfieldSensor2Valuez;
    public TextField textfieldSampleTime;

    public TextField textfieldConfirmed;

    public TextArea textArea;
    public TextArea textArea2;

    @FXML
    public void initialize() {
        idTopic1.setText(state.getTopic1());
        idTopic2.setText(state.getTopic2());
        idTopic3.setText(state.getTopic3());

        idTH1.setText(state.getSensor1_thres() + "");
        idTH2x.setText(state.getSensor2_thres_x() + "");
        idTH2y.setText(state.getSensor2_thres_y() + "");
        idTH2z.setText(state.getSensor2_thres_z() + "");
    }


    public void handleSubmitSearchAction(ActionEvent actionEvent) {
        ArrayList<Crash> data = Crash.load(
                textfieldID, textfieldMobile, textfieldLongitude, textfieldLongitude,
                textfieldSensorType1, textfieldSensorType2, textfieldSampleTime,
                textfieldSensor1Valuex, textfieldSensor1Valuey, textfieldSensor1Valuez,
                textfieldSensor2Valuex, textfieldSensor2Valuey, textfieldSensor2Valuez,
                textfieldConfirmed);

        String representation1 = "";
        String representation2 = "";
        for (Crash line : data) {
            if (line.isConfirmed()) {
                representation2 = representation2 + String.format("%4d %10s %10s \n", line.getId(), line.getLatitude(), line.getLongitude());
            } else {
                representation1 = representation1 + String.format("%4d %10s %10s \n", line.getId(), line.getLatitude(), line.getLongitude());
            }
        }

        textArea.setText(representation1);
        textArea2.setText(representation2);
    }

    public void handleSubmitApplySettingChangesAction(ActionEvent actionEvent) {
        String th1 = idTH1.getText();
        String th2x = idTH2x.getText();
        String th2y = idTH2y.getText();
        String th2z = idTH2z.getText();

        try {
            state.setSensor1_thres(Float.parseFloat(th1));
            state.setSensor2_thres_x(Float.parseFloat(th2x));
            state.setSensor2_thres_y(Float.parseFloat(th2y));
            state.setSensor2_thres_z(Float.parseFloat(th2z));
        } catch(Exception e) {

        }

    }

    public void handleSubmitApplyMqttSettingChangesAction(ActionEvent actionEvent) {
        state.setTopic1(idTopic1.getText());
        state.setTopic2(idTopic2.getText());
        state.setTopic3(idTopic3.getText());

        try {
            state.setSensor1_thres(Float.parseFloat(idTH1.getText()));
        } catch (Exception ex) {

        }
    }


    public void clear(ActionEvent actionEvent) {

    }

    public void handleSubmitRefreshAction(ActionEvent actionEvent) {
    }

    public void handleSubmitClearAction(ActionEvent actionEvent) {
        textfieldID.setText("");
        textfieldMobile.setText("");
        textfieldLongitude.setText("");
        textfieldLongitude.setText("");
        textfieldSensorType1.setText("");
        textfieldSensorType2.setText("");
        textfieldSampleTime.setText("");
        textfieldSensor1Valuex.setText("");
        textfieldSensor1Valuey.setText("");
        textfieldSensor1Valuez.setText("");
        textfieldSensor2Valuex.setText("");
        textfieldSensor2Valuey.setText("");
        textfieldSensor2Valuez.setText("");
        textfieldConfirmed.setText("");
    }
}
