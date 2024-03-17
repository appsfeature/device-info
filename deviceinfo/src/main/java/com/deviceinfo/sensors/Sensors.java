package com.deviceinfo.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.deviceinfo.util.DITimeLogger;

import java.util.ArrayList;
import java.util.List;

public class Sensors {

    private final SensorManager sensorManager;

    public Sensors(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public List<Sensor> getInfo() {
        long startTime = DITimeLogger.getStartTime();
        List<Sensor> sensors = getAllSensors();
        DITimeLogger.timeLogging("Sensors", startTime);
        return sensors;
    }

    /**
     * Gets all sensors.
     */
    public List<Sensor> getAllSensors() {
        try {
            return this.sensorManager.getSensorList(Sensor.TYPE_ALL);
        }catch (NullPointerException e){
            return new ArrayList<>();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
}
