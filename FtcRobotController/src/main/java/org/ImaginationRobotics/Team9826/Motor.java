package org.imaginationrobotics.team9826;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ifs on 11/16/15.
 */
public class Motor {
    private String motorName;
    private float maxRPM;
    private float gearRatio;
    private float encoderResolution;
    private long lastEncoderTickTimeStamp;
    private long lastTicksValue;
    private int averagingSize = 10;
    private List<Float> rpmValues;

    public Motor(String motorName, float maxRPM, float encoderResolution, float gearRatio){
        this.motorName = motorName;
        this.maxRPM = maxRPM;
        this.gearRatio = gearRatio;
        this.encoderResolution = encoderResolution;
        rpmValues = new ArrayList<Float>();
    }

    public float getRPM(long ticks){
        long currentTickTimeStamp = System.nanoTime();
        float rpm = (((ticks-lastTicksValue)*(60000000000f/(currentTickTimeStamp-lastEncoderTickTimeStamp)) / encoderResolution) /gearRatio);
        this.lastEncoderTickTimeStamp = currentTickTimeStamp;
        this.lastTicksValue = ticks;
        return rpm;
    }

    public float getPower(long ticks){
        return getRPM(ticks) / this.maxRPM;
    }

}
