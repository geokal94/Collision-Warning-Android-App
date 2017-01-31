package connectdb;

import javafx.scene.control.TextField;

import javax.management.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Crash {
    int id;
    String mobile;
    float longitude;
    float latitude;
    String sensortype1;
    float sensor1valuex;
    float sensor1valuey;
    float sensor1valuez;
    String sensortype2;
    float sensor2valuex;
    float sensor2valuey;
    float sensor2valuez;
    Date sampletime;
    boolean confirmed;

    public Crash(ResultSet rs) {
        try {
            id = rs.getInt("id");
            mobile = rs.getString("mobile");
            longitude = rs.getFloat("longitude");
            latitude = rs.getFloat("latitude");
            sensortype1 = rs.getString("sensortype1");
            sensor1valuex = rs.getFloat("sensor1valuex");
            sensor1valuey = rs.getFloat("sensor1valuey");
            sensor1valuez = rs.getFloat("sensor1valuez");
            sensortype2 = rs.getString("sensortype2");
            sensor2valuex = rs.getFloat("sensor2valuex");
            sensor2valuey = rs.getFloat("sensor2valuey");
            sensor2valuez = rs.getFloat("sensor2valuez");
            sampletime = rs.getDate("sampletime");
            confirmed = rs.getBoolean("confirmed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getSensortype1() {
        return sensortype1;
    }

    public void setSensortype1(String sensortype1) {
        this.sensortype1 = sensortype1;
    }

    public float getSensor1valuex() {
        return sensor1valuex;
    }

    public void setSensor1valuex(float sensor1valuex) {
        this.sensor1valuex = sensor1valuex;
    }

    public float getSensor1valuey() {
        return sensor1valuey;
    }

    public void setSensor1valuey(float sensor1valuey) {
        this.sensor1valuey = sensor1valuey;
    }

    public float getSensor1valuez() {
        return sensor1valuez;
    }

    public void setSensor1valuez(float sensor1valuez) {
        this.sensor1valuez = sensor1valuez;
    }

    public String getSensortype2() {
        return sensortype2;
    }

    public void setSensortype2(String sensortype2) {
        this.sensortype2 = sensortype2;
    }

    public float getSensor2valuex() {
        return sensor2valuex;
    }

    public void setSensor2valuex(float sensor2valuex) {
        this.sensor2valuex = sensor2valuex;
    }

    public float getSensor2valuey() {
        return sensor2valuey;
    }

    public void setSensor2valuey(float sensor2valuey) {
        this.sensor2valuey = sensor2valuey;
    }

    public float getSensor2valuez() {
        return sensor2valuez;
    }

    public void setSensor2valuez(float sensor2valuez) {
        this.sensor2valuez = sensor2valuez;
    }

    public Date getSampletime() {
        return sampletime;
    }

    public void setSampletime(Date sampletime) {
        this.sampletime = sampletime;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public static ArrayList<Crash> load(TextField textfieldID, TextField textfieldMobile, TextField textfieldLongitude, TextField textfieldLatitude,
                                        TextField textfieldSensor1Type, TextField textfieldSensor2Type, TextField textfieldDateTime, TextField textfieldX1Value, TextField textfieldY1Value,
                                        TextField textfieldZ1Value, TextField textfieldX2Value, TextField textfieldY2Value,
                                        TextField textfieldZ2Value, TextField textfieldConfirmed) {

        String query = "SELECT * FROM crash";
        ArrayList<String> conditions = new ArrayList<String>();

        String valueID = textfieldID.getText();
        if (valueID != null) {
            valueID = valueID.trim();

            if (!valueID.equals("")) {
                conditions.add("ID = '" + valueID + "'");
            }
        }

        String valueMobile = textfieldMobile.getText();
        if (valueMobile != null) {
            valueMobile = valueMobile.trim();

            if (!valueMobile.equals("")) {
                conditions.add("mobile = '" + valueMobile + "'");
            }
        }

        String valueLongitude = textfieldLongitude.getText();
        if (valueLongitude != null) {
            valueLongitude = valueLongitude.trim();

            if (!valueLongitude.equals("")) {
                if (valueLongitude.contains("-")) {
                    String[] range = valueLongitude.split("-");
                    String from = range[0];
                    String to = range[1];
                    conditions.add("longitude >= " + from + " and longitude <= " + to);
                } else {
                    conditions.add("longitude = " + valueLongitude);
                }
            }
        }

        String valueLatitude = textfieldLatitude.getText();
        if (valueLatitude != null) {
            valueLatitude = valueLatitude.trim();

            if (!valueLatitude.equals("")) {
                if (valueLatitude.contains("-")) {
                    String[] range = valueLatitude.split("-");
                    String from = range[0];
                    String to = range[1];
                    conditions.add("latitude >= " + from + " and latitude <= " + to);
                } else {
                    conditions.add("latitude = " + valueLatitude);
                }
            }
        }

        String valueSensor1Type = textfieldSensor1Type.getText();
        if (valueSensor1Type != null) {
            valueSensor1Type = valueSensor1Type.trim();

            if (!valueSensor1Type.equals("")) {
                conditions.add("sensortype1 = '" + valueSensor1Type + "'");
            }
        }


        String valueSensor2Type = textfieldSensor2Type.getText();
        if (valueSensor2Type != null) {
            valueSensor2Type = valueSensor2Type.trim();

            if (!valueSensor2Type.equals("")) {
                conditions.add("sensortype2 = '" + valueSensor2Type + "'");
            }
        }

        String valueDateTime = textfieldDateTime.getText();
        if (valueDateTime != null) {
            valueDateTime = valueDateTime.trim();

            if (!valueDateTime.equals("")) {
                if (valueDateTime.contains("-")) {
                    String[] range = valueDateTime.split("to");
                    String from = range[0].trim();
                    String to = range[1].trim();
                    conditions.add("sampletime >= '" + from + "' and sampletime <= '" + to + "'");
                } else {
                    conditions.add("sampletime = '" + valueDateTime + "'");
                }
            }
        }

        String valueX1Value = textfieldX1Value.getText();
        if (valueX1Value != null) {
            valueX1Value = valueX1Value.trim();

            if (!valueX1Value.equals("")) {
                conditions.add("sensor1valuex = '" + valueX1Value + "'");
            }
        }

        String valueY1Value = textfieldY1Value.getText();
        if (valueY1Value != null) {
            valueY1Value = valueY1Value.trim();

            if (!valueY1Value.equals("")) {
                conditions.add("sensor1valuey = '" + valueY1Value + "'");
            }
        }

        String valueZ1Value = textfieldZ1Value.getText();
        if (valueZ1Value != null) {
            valueZ1Value = valueZ1Value.trim();

            if (!valueZ1Value.equals("")) {
                conditions.add("sensor1valuez = '" + valueZ1Value + "'");
            }
        }

        String valueX2Value = textfieldX2Value.getText();
        if (valueX2Value != null) {
            valueX2Value = valueX2Value.trim();

            if (!valueX2Value.equals("")) {
                conditions.add("sensor2valuex = '" + valueX2Value + "'");
            }
        }

        String valueY2Value = textfieldY2Value.getText();
        if (valueY2Value != null) {
            valueY2Value = valueY2Value.trim();

            if (!valueY2Value.equals("")) {
                conditions.add("sensor2valuey = '" + valueY2Value + "'");
            }
        }

        String valueZ2Value = textfieldZ2Value.getText();
        if (valueZ2Value != null) {
            valueZ2Value = valueZ2Value.trim();

            if (!valueZ2Value.equals("")) {
                conditions.add("sensor2valuez = '" + valueZ2Value + "'");
            }
        }

        String valueConfirmed = textfieldConfirmed.getText();
        if (valueConfirmed != null) {
            valueConfirmed = valueConfirmed.trim();

            if (!valueConfirmed.equals("")) {
                conditions.add("confirmed = '" + valueConfirmed + "'");
            }
        }




        for (int i = 0; i < conditions.size(); i++) {
            if (i == 0) {
                query = query + " where " + conditions.get(i);
            } else {
                query = query + " and (" + conditions.get(i) + ")";
            }
        }

        System.out.println("Query is : " + query);

        ResultSet rs = QueryDb.SelectQuery(query);

        ArrayList<Crash> itemList = new ArrayList<Crash>();

        if (rs != null) {
            try {
                while (rs.next()) {
                    Crash line = new Crash(rs);
                    itemList.add(line);
                }
            } catch (SQLException ex) {
            } finally {
                QueryDb.CloseResources(rs);
            }
        }
        return itemList;
    }


    public static void insert(String mobile, float latitude, float longitude, float proximity, float s1x, float s1y, float s1z, String sampletime, boolean confirmation) {
        int c;
        if (confirmation) {
            c = 1;
        } else {
            c = 0;
        }
        String query = "INSERT INTO `projectdb`.`crash`" +
                "(`mobile`," +
                "`longitude`," +
                "`latitude`," +
                "`sensortype1`," +
                "`sensor1valuex`," +
                "`sensor1valuey`," +
                "`sensor1valuez`," +
                "`sensortype2`," +
                "`sensor2valuex`," +
                "`sensor2valuey`," +
                "`sensor2valuez`," +
                "`sampletime`," +
                "`confirmed`)" +
                "VALUES" +
                "(" +
                "'vmobile' ," +
                "vlongitude ," +
                "vlatitude ," +
                "'vsensortype1' ," +
                "vsensor1valuex ," +
                "vsensor1valuey ," +
                "vsensor1valuez ," +
                "'vsensortype2' ," +
                "vsensor2valuex ," +
                "vsensor2valuey ," +
                "vsensor2valuez ," +
                "'vsampletime' ," +
                "vconfirmed " +
                ");";
        query = query.replace("vmobile", mobile)
                .replace("vlongitude", String.valueOf(longitude))
                .replace("vlatitude", String.valueOf(latitude))
                .replace("vsensortype1", "proximity")
                .replace("vsensor1valuex", String.valueOf(proximity))
                .replace("vsensor1valuey", "0")
                .replace("vsensor1valuez", "0")
                .replace("vsensortype2", "accelerometer")
                .replace("vsensor2valuex", String.valueOf(s1x))
                .replace("vsensor2valuey", String.valueOf(s1y))
                .replace("vsensor2valuez", String.valueOf(s1z))
                .replace("vsampletime", sampletime)
                .replace("vconfirmed", String.valueOf(c));

        System.out.println("Query is: " + query);

        QueryDb.DoQuery(query);
    }

    public static Date getLastDate(String mobile) {
        String query = "select sampletime from crash where mobile='" + mobile + "' order by sampletime desc limit 1";
        ResultSet results = QueryDb.SelectQuery(query);

        try {
            if (results.next()) {
                Timestamp sampletime = results.getTimestamp("sampletime");
                Date date = new Date(sampletime.getTime());
                return date;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getLastId(String mobile) {
        String query = "select id from crash where mobile='" + mobile + "' order by sampletime desc limit 1";
        ResultSet results = QueryDb.SelectQuery(query);

        try {
            if (results.next()) {
                int x = results.getInt("id");
                return x;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Crash{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", sensortype1='" + sensortype1 + '\'' +
                ", sensor1valuex=" + sensor1valuex +
                ", sensor1valuey=" + sensor1valuey +
                ", sensor1valuez=" + sensor1valuez +
                ", sensortype2='" + sensortype2 + '\'' +
                ", sensor2valuex=" + sensor2valuex +
                ", sensor2valuey=" + sensor2valuey +
                ", sensor2valuez=" + sensor2valuez +
                ", sampletime=" + sampletime +
                ", confirmed=" + confirmed +
                '}';
    }

    public static void update(int id) {
        String query = "update crash set confirmed=1 where id='" + id + "'";
        QueryDb.DoQuery(query);
    }

}
