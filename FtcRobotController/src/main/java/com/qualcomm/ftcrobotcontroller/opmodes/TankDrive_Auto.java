package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by thomas on 1/6/16.
 */
public class TankDrive_Auto extends PushBotTelemetry{
    public TankDrive_Auto(){
    }

    @Override public void start(){
        super.start();
        reset_drive_encoders();
    }

    @Override public void loop(){
        switch(state){
            case 0:{
                reset_drive_encoders();
                state++;
            }

            case 1:{
                run_using_encoders();
            }
        }
    }

    private int state = 0;
}
