package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by thomas on 1/6/16.
 */
public class TankDrive_Auto extends TankDrive_Hardware {
    private int state = 0;
    private long lastTime;


    public TankDrive_Auto(){
    }

    @Override
    public void init(){
        super.init();
        run_using_encoders();
        reset_drive_encoders();
        state = 0;
        led.setPower(1);
    }

    @Override
    public void start(){
        super.start();
        lastTime = System.nanoTime();
    }

    @Override
    public void loop(){

        switch(state){
            case 0:{
                if(System.nanoTime() >= lastTime + 5000){
                    reset_drive_encoders();
                    state++;
                }
                led.setPower(-1);

                break;
            }

            case 1:{
                set_drive_power(0.5f, 0.5f);
                if (has_right_drive_encoder_reached(getTicks(40))) {
                    stopDrive();
                    lastTime = System.nanoTime();
                    state++;
                }
                led.setPower(1);
                break;
            }

            case 2:{
                if(System.nanoTime() >= lastTime + 5000){
                    reset_drive_encoders();
                    state++;
                }
                led.setPower(-1);
                break;
            }

            case 3:{
                set_drive_power(-0.3f, 0.3f);
                if (have_drive_encoders_reached (-getTicks(48.69/4), getTicks(48.69/4))){
                    stopDrive();
                    this.resetStartTime();
                     state++;
                }
                led.setPower(1);
                break;
            }

            case 4:{
                if(System.nanoTime() >= lastTime + 5000){
                    reset_drive_encoders();
                    state++;
                }
                led.setPower(-1);
                break;
            }

            case 5:{
                set_drive_power(0.5f, 0.5f);
                if(has_right_drive_encoder_reached(getTicks(5))){
                    stopDrive();
                    state++;
                }
                led.setPower(1);
                break;
            }
        }

        //super.loop();

        telemetry.addData("00", "State: " + state);
        telemetry.addData("01", "Left Drive: " + motorLeft.getPower() + ", " + motorLeft.getCurrentPosition() + ", " + getInches(motorLeft.getCurrentPosition()));
        telemetry.addData("02", "Right Drive: " + motorRight.getPower() + ", " + motorRight.getCurrentPosition() + ", " + getInches(motorRight.getCurrentPosition()));
        telemetry.addData("03", "Arm: " + motorArm.getPower() + ", " + motorArm.getCurrentPosition());
        telemetry.addData("04", "LED: " + led.getPower());
    }


    @Override
    public void stop(){
        super.stop();
    }
}
