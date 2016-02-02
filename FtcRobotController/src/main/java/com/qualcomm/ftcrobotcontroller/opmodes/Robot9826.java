package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

import org.imaginationrobotics.team9826.EncoderMotorTask;

/**
 * Created by Thomas on 1/31/2016.
 */
public class Robot9826 {
    public Telemetry telemetry;
    HardwareMap hardwareMap;

    double doorRightClose = 0;
    double doorRightOpen = .8;

    double doorLeftClose = 1;
    double doorLeftOpen = .2;

    double conveyorStop = .48;
    double conveyorRight = 1;
    double conveyorLeft = 0;
    public enum ConveyorDirection {left, right, stop}
    ConveyorDirection conveyorDirection;

    final double sweeperStop = .495;
    final double sweeperForward = 1;
    final double sweeperBackward = 0;
    public enum SweeperDirection {forward, reverse, stop}
    SweeperDirection sweeperDirection;

    final double wheelDistance = 15.375;
    final double wheelDiameter = 4.7;//14.8125 / PI
    final double PI = 3.14159265359;
    final int tickPerRev = 1120;

    public enum DriveDirection {forward, reverse}
    public enum TurnDirection {left, right}
    public enum ArmDirection {up, down, stop}

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
    Servo sweeperServo;

    public Robot9826(HardwareMap hardwareMap, Telemetry telemetry){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void init(){
        //Drive motors
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        //Arm motor, reversed so 1 is up, -1 is down
        motorArm = hardwareMap.dcMotor.get("motorArm");
        motorArm.setDirection(DcMotor.Direction.REVERSE);
        motorArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        //Bucket motors and servos
        doorRight = hardwareMap.servo.get("doorRight");
        doorLeft = hardwareMap.servo.get("doorLeft");
        conveyorServo = hardwareMap.servo.get("conveyor");
        sweeperServo = hardwareMap.servo.get("sweeper");

        //Led power
        led = hardwareMap.dcMotor.get("led");
        led.setPower(1);

        //Set the conveyor position
        conveyorServo.setPosition(conveyorStop);
        conveyorDirection = ConveyorDirection.stop;

        //Set the door positions
        doorRight.setPosition(doorRightClose);
        doorLeft.setPosition(doorLeftClose);

        //Set the sweeperServo to stop
        sweeperServo.setPosition(sweeperStop);
        sweeperDirection = SweeperDirection.stop;
    }

    public ConveyorDirection getConveyorDirection(){
        return conveyorDirection;
    }

    public SweeperDirection getSweeperDirection(){
        return sweeperDirection;
    }

    public void drive(double speed, DriveDirection direction){
        switch(direction){
            case forward: {
                drive(speed, speed);
                break;
            }
            case reverse: {
                drive(-speed, -speed);
                break;
            }
        }
    }

//    public void drive(int inches, double speed, DriveDirection direction){
//        switch(direction){
//            case forward: {
//                motorLeft.setTargetPosition(inchesToTicks(inches));
//                motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
//                drive(speed, speed);
//                break;
//            }
//            case reverse: {
//                motorLeft.setTargetPosition(-inchesToTicks(inches));
//                motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
//                drive(-speed, -speed);
//                break;
//            }
//        }
//    }
//
//    public void pivot(int degrees, double speed, TurnDirection direction){
//        switch(direction){
//            case right: {
//                motorLeft.setTargetPosition(degreesToTicks(degrees));
//                motorRight.setTargetPosition(-degreesToTicks(degrees));
//                motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
//                motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
//                drive(-speed, speed);
//                break;
//            }
//            case left: {
//                motorLeft.setTargetPosition(-degreesToTicks(degrees));
//                motorRight.setTargetPosition(degreesToTicks(degrees));
//                motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
//                motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
//                drive(speed, -speed);
//                break;
//            }
//        }
//    }

    public synchronized void drive(double rightSpeed, double leftSpeed){
        if (motorLeft != null) {
            motorLeft.setPower(leftSpeed);
        }
        if (motorRight != null) {
            motorRight.setPower(rightSpeed);
        }
        telemetry.addData("R01", "Left Drive: " + motorLeft.getPower() + ", " + motorLeft.getCurrentPosition() + ", " + ticksToInches(motorLeft.getCurrentPosition()));
        telemetry.addData("R02", "Right Drive: " + motorRight.getPower() + ", " + motorRight.getCurrentPosition() + ", " + ticksToInches(motorRight.getCurrentPosition()));
    }

    void stopDrive() {
        if (motorLeft != null) {
            motorLeft.setPower(0);
        }
        if (motorRight != null) {
            motorRight.setPower(0);
        }

    }

    public void driveArm(double speed, ArmDirection direction){
        if (motorArm != null){
            switch(direction){
                case up: {
                    motorArm.setPower(speed);
                    break;
                }
                case down:{
                    motorArm.setPower(-speed);
                    break;
                }
                case stop:{
                    motorArm.setPower(0);
                    break;
                }
            }
        }
    }

    public void toggleLeftDoor(){
        if(doorLeft != null) {
            if (doorLeft.getPosition() == doorLeftClose) {
                doorLeft.setPosition(doorLeftOpen);
            } else if (doorLeft.getPosition() == doorLeftOpen) {
                doorLeft.setPosition(doorLeftClose);
            }
        }
    }

    public void toggleRightDoor(){
        if(doorRight != null) {
            if (doorRight.getPosition() == doorRightClose) {
                doorRight.setPosition(doorRightOpen);
            } else if (doorRight.getPosition() == doorRightOpen) {
                doorRight.setPosition(doorRightClose);
            }
        }
    }

    public void conveyorToggle(ConveyorDirection conveyorDirection){
        this.conveyorDirection = conveyorDirection;
        switch(conveyorDirection){
            case left:{
                conveyorServo.setPosition(conveyorLeft);
                break;
            }
            case right:{
                conveyorServo.setPosition(conveyorRight);
                break;
            }
            case stop:{
                conveyorServo.setPosition(conveyorStop);
                break;
            }
        }
    }

    public void sweeperToggle(SweeperDirection sweeperDirection){
        this.sweeperDirection = sweeperDirection;
        switch(sweeperDirection){
            case forward:{
                sweeperServo.setPosition(sweeperForward);
                break;
            }
            case reverse:{
                sweeperServo.setPosition(sweeperBackward);
                break;
            }
            case stop:{
                sweeperServo.setPosition(sweeperStop);
                break;
            }
        }
    }

    public void setLedPower(double power){
        led.setPower(power);
    }

    public int inchesToTicks(int inches) {
        return (int) (inches / ((wheelDiameter * PI) / tickPerRev));
    }

    public int degreesToTicks(int degrees){
        return (int)((wheelDistance * PI) / 360) * degrees;
    }

    public double ticksToInches(int ticks) {
        return (ticks * ((wheelDiameter * PI) / tickPerRev));
    }

    public void reset_drive_encoders() {
        reset_left_drive_encoder();
        reset_right_drive_encoder();
    }

    public void reset_left_drive_encoder() {
        if (motorLeft != null) {
            motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }

    public void reset_right_drive_encoder() {
        if (motorRight != null) {
            motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }

    public void run_using_encoders() {
        run_using_left_drive_encoder();
        run_using_right_drive_encoder();
    }

    public void run_using_left_drive_encoder() {
        if (motorLeft != null) {
            motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }

    public void run_using_right_drive_encoder() {
        if (motorRight != null) {
            motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }

    }

    boolean have_drive_encoders_reached(int p_left_count, int p_right_count){
        boolean l_return = false;
        if (has_left_drive_encoder_reached(p_left_count) &&
                has_right_drive_encoder_reached(p_right_count)) {
            l_return = true;
        }
        return l_return;
    }

    boolean has_left_drive_encoder_reached(int count) {
        boolean l_return = false;
        if (motorLeft != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs(motorLeft.getCurrentPosition()) >= count) {
                l_return = true;
                telemetry.addData("R03", "Left Encoder has reached " + count);
            }
        }
        telemetry.addData("R03", "Left Encoder: " + count + " - " + motorLeft.getCurrentPosition() + " = " + (count - motorLeft.getCurrentPosition() + " (" + l_return + ")"));
        return l_return;
    }

    /**
     * Indicate whether the right drive motor's encoder has reached a value.
     */
    boolean has_right_drive_encoder_reached(int count){
        boolean l_return = false;
        if (motorRight != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs(motorRight.getCurrentPosition()) >= count) {
                l_return = true;
                telemetry.addData("R04", "Right Encoder has reached " + count);
            }
        }
        telemetry.addData("R04", "Right Encoder: " + count + " - " + motorRight.getCurrentPosition() + " = " + (count - motorRight.getCurrentPosition() + " (" + l_return + ")"));
        return l_return;
    }

}
