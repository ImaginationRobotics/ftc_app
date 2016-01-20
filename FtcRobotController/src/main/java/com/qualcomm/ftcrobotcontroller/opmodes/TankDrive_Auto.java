package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by thomas on 1/6/16.
 */
public class TankDrive_Auto extends TankDrive_Hardware {
    long startTime;
    private int state = 0;


    public TankDrive_Auto(){
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void start(){
        super.start();
        reset_drive_encoders();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void loop(){

        switch(state){
            case 0:{
                reset_drive_encoders();
                if(System.currentTimeMillis() > startTime+10000){
                    state++;
                }
                break;
            }

            case 1:{
                run_using_encoders();

                set_drive_power(0.5f, 0.5f);
                if (have_drive_encoders_reached (getTicks(10), getTicks(10)))
                {
//                    reset_drive_encoders();
                    stopDrive();

                    state++;
                }

                break;
            }

//            case 2:{
//                run_using_encoders();
//
//                set_drive_power(-0.5f, -0.5f);
//                if (have_drive_encoders_reached (-getTicks(10), -getTicks(10)))
//                {
//                    reset_drive_encoders();
//                    stopDrive();
//                    state++;
//                }
//
//                break;
//            }
        }

        super.loop();

        telemetry.addData("00", "State: " + state);
        telemetry.addData("01", "Left Drive: " + motorLeft.getPower() + ", " + motorLeft.getCurrentPosition ());
        telemetry.addData("02", "Right Drive: " + motorRight.getPower() + ", " + motorRight.getCurrentPosition());
        telemetry.addData("03", "Arm: " + motorArm.getPower() + ", " + motorArm.getCurrentPosition());
        telemetry.addData("04", "LED: " + led.getPower());
    }


    @Override
    public void stop(){
        super.stop();
    }
}
