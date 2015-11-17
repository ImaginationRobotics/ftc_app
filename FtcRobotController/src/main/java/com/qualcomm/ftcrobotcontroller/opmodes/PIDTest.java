package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.imaginationrobotics.team9826.Motor;
import org.imaginationrobotics.team9826.PIDController;

/**
 * Created by Thomas on 10/31/2015.
 *
 * Update Nov 7
 *  Changed
 */

public class PIDTest extends OpMode{
    PIDController pid = new PIDController(.25f, .25f, .25f, 0, 100f, -100f);
    Motor motor = new Motor("Motor 1", 150f, 1440f, 1f);
    float power = 1f;

    //Motors and servos
    DcMotor motorRight;

    /**
     * Constructors
     */
    public PIDTest(){

    }

    @Override
    public void init(){
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setPower(0);
    }

    @Override
    public void loop(){
        motorRight.setPower(power);
        telemetry.addData("rpm", String.valueOf(motor.getRPM(motorRight.getCurrentPosition())));
    }

    @Override
    public void stop(){
        motorRight.setPower(0);
    }

}
