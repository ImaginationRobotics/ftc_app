package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Thomas on 10/31/2015.
 *
 * Update Nov 7
 *  Changed
 */

public class TankDrive_Bucket extends OpMode{
    //Static Ranges for servos
    final static double BUCKET_LIFT_LEFT_MIN_RANGE = 1;
    final static double BUCKET_LIFT_LEFT_MAX_RANGE = 0.05;
    final static double BUCKET_LIFT_RIGHT_MIN_RANGE = 0;
    final static double BUCKET_LIFT_RIGHT_MAX_RANGE = 1;

    //Servo positions
    double bucketLiftRightPos;
    double bucketLiftLeftPos;

    //Servo change amount
    double bucketLiftDelta = 0.01; //Amount to change the servos

    //Motors and servos
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    Servo bucketLiftRight;
    Servo bucketLiftLeft;

    /**
     * Constructors
     */
    public TankDrive_Bucket(){

    }

    @Override
    public void init(){
        motorFR = hardwareMap.dcMotor.get("motorRight");
        motorBR = hardwareMap.dcMotor.get("motorBR");

        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.REVERSE);

        bucketLiftRight = hardwareMap.servo.get("servo_1");
        bucketLiftLeft = hardwareMap.servo.get("servo_2");

        bucketLiftRightPos = 0.5;
        bucketLiftLeftPos = 0.5;
    }

    @Override
    public void loop(){
        float leftDrive = gamepad1.left_stick_y;
        float rightDrive = gamepad1.right_stick_y;

        rightDrive = Range.clip(rightDrive, -1, 1);
        leftDrive = Range.clip(leftDrive, -1, 1);

        rightDrive = (float)scaleInput(rightDrive);
        leftDrive = (float)scaleInput(leftDrive);

        motorFR.setPower(rightDrive);
        motorBR.setPower(rightDrive);
        motorFL.setPower(leftDrive);
        motorBL.setPower(leftDrive);

        if(gamepad1.right_bumper){
            bucketLiftRightPos -= bucketLiftDelta;
            bucketLiftLeftPos += bucketLiftDelta;
        }

        if(gamepad1.right_trigger > 0.1){
            bucketLiftRightPos += bucketLiftDelta;
            bucketLiftLeftPos -= bucketLiftDelta;
        }

        bucketLiftRightPos = Range.clip(bucketLiftRightPos, BUCKET_LIFT_RIGHT_MIN_RANGE, BUCKET_LIFT_RIGHT_MAX_RANGE);
        bucketLiftLeftPos = Range.clip(bucketLiftLeftPos, BUCKET_LIFT_LEFT_MAX_RANGE, BUCKET_LIFT_LEFT_MIN_RANGE);

        bucketLiftRight.setPosition(bucketLiftRightPos);
        bucketLiftLeft.setPosition(bucketLiftLeftPos);

        telemetry.addData("Right tgt pwr", "Right pwr: " + String.format("%.2f", rightDrive));
        telemetry.addData("Left tgt pwr", "Left pwr: " + String.format("%.2f", leftDrive));
        telemetry.addData("Right stick", "Right stick: " + String.format("%.2f", gamepad1.right_stick_y));
        telemetry.addData("Left stick", "Left stick: " + String.format("%.2f", gamepad1.left_stick_y));
        telemetry.addData("Right servo", "Right servo: " + String.format("%.2f", bucketLiftRightPos));
        telemetry.addData("Left servo", "Left servo: " + String.format("%.2f", bucketLiftLeftPos));
    }

    @Override
    public void stop(){

    }

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
