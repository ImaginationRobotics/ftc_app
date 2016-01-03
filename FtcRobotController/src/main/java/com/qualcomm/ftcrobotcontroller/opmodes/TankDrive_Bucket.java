package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**TankDrive_Bucket
 *
 * Code to drive two motors in tank drive, two hinge servos,
 *      one conveyor servo, and one worm driven arm.
 */

public class TankDrive_Bucket extends OpMode{
    //Servo position values
    double doorRightClose = 0;
    double doorRightOpen = .6;

    double doorLeftClose = 1;
    double doorLeftOpen = .4;

    double conveyorStop = .48;
    double conveyorRight = 1;
    double conveyorLeft = 0;

    //Motors and servos
    DcMotor motorRight;
    DcMotor motorLeft;
//    DcMotor motorArm;
    Servo doorRight;
    Servo doorLeft;
    Servo conveyorServo;

    /** Constructors*/
    public TankDrive_Bucket(){
    }

    @Override
    public void init(){
        //Drive motors
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

//        motorArm = hardwareMap.dcMotor.get("motorArm");

        //Bucket motors and servos
        doorRight = hardwareMap.servo.get("doorRight");
        doorLeft = hardwareMap.servo.get("doorLeft");
        conveyorServo = hardwareMap.servo.get("conveyor");

        //Set the conveyor position
        conveyorServo.setPosition(conveyorStop);

        //Set the door positions
        doorRight.setPosition(doorRightClose);
        doorLeft.setPosition(doorLeftClose);
    }

    @Override
    public void loop(){
        float leftDrive = gamepad1.left_stick_y;
        float rightDrive = gamepad1.right_stick_y;

        rightDrive = Range.clip(rightDrive, -1, 1);
        leftDrive = Range.clip(leftDrive, -1, 1);

        rightDrive = (float)scaleInput(rightDrive);
        leftDrive = (float)scaleInput(leftDrive);

        //Set motor power
        motorRight.setPower(rightDrive);
        motorLeft.setPower(leftDrive);

//        //Arm Motor using Right Bumper and Trigger
//        if(gamepad1.right_bumper){
//            motorArm.setPower(.2);
//        }else if(gamepad1.right_trigger > .2){
//            motorArm.setPower(-.2);
//        }else{
//            motorArm.setPower(0);
//        }

        //Door Right open and Conveyor Right
        if(gamepad1.dpad_right){
            if(doorRight.getPosition() == doorRightOpen){
                doorRight.setPosition(doorRightClose);
                conveyorServo.setPosition(conveyorStop);

            }else if(doorRight.getPosition() == doorRightClose){
                doorRight.setPosition(doorRightOpen);
                conveyorServo.setPosition(conveyorRight);
            }
        }

        //Door Left open and Conveyor Left
        if(gamepad1.dpad_left){
            if(doorLeft.getPosition() == doorLeftOpen){
                doorLeft.setPosition(doorLeftClose);
                conveyorServo.setPosition(conveyorStop);
            }else if(doorLeft.getPosition() == doorLeftClose){
                doorLeft.setPosition(doorLeftOpen);
                conveyorServo.setPosition(conveyorLeft);
            }
        }

        //Telemetry
        telemetry.addData("Right tgt pwr", "Right pwr: " + String.format("%.2f", rightDrive));
        telemetry.addData("Left tgt pwr", "Left pwr: " + String.format("%.2f", leftDrive));
        telemetry.addData("Right stick", "Right stick: " + String.format("%.2f", gamepad1.right_stick_y));
        telemetry.addData("Left stick", "Left stick: " + String.format("%.2f", gamepad1.left_stick_y));
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
