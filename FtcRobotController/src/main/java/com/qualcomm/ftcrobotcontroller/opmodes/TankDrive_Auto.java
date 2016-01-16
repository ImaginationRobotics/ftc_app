package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by thomas on 1/6/16.
 */
public class TankDrive_Auto extends PushBotTelemetry{ //Servo position values
    double doorRightClose = 0;

    double doorLeftClose = 1;

    double conveyorStop = .48;

    double sweeperStop = .495;

    double ledPower = 1;
    boolean ledSwitch = true;

    //Motors and servos
    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor motorArm;
    DcMotor led;
    Servo doorRight;
    Servo doorLeft;
    Servo conveyorServo;
    Servo sweeper;

    @Override
    public void init(){
        //Drive motors
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        //Arm motor, reversed so 1 is up, -1 is down
        motorArm = hardwareMap.dcMotor.get("motorArm");
        motorArm.setDirection(DcMotor.Direction.REVERSE);

        //Bucket motors and servos
        doorRight = hardwareMap.servo.get("doorRight");
        doorLeft = hardwareMap.servo.get("doorLeft");
        conveyorServo = hardwareMap.servo.get("conveyor");
        sweeper = hardwareMap.servo.get("sweeper");

        //Led power
        led = hardwareMap.dcMotor.get("led");

        //Set the conveyor position
        conveyorServo.setPosition(conveyorStop);

        //Set the door positions
        doorRight.setPosition(doorRightClose);
        doorLeft.setPosition(doorLeftClose);

        //Set the sweeper to stop
        sweeper.setPosition(sweeperStop);
    }
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

                set_drive_power(1.0f, 1.0f);
                if (have_drive_encoders_reached (2880, 2880))
                {
                    reset_drive_encoders();

                    set_drive_power (0.0f, 0.0f);

                    state++;
                }
            }

            case 1:{
                run_using_encoders();

                set_drive_power(-1.0f, -1.0f);
                if (have_drive_encoders_reached (-2880, -2880))
                {
                    reset_drive_encoders();

                    set_drive_power (0.0f, 0.0f);

                    state++;
                }
            }
        }

        //LED pulse
        if(ledSwitch == true){
            ledPower -= .01;
            led.setPower(ledPower);

            if(ledPower < -.99){
                ledSwitch = false;
            }
        }else if(ledSwitch == false){
            ledPower += .01;
            led.setPower(ledPower);

            if(ledPower > .99){
                ledSwitch = true;
            }
        }
    }

    private int state = 0;
}
