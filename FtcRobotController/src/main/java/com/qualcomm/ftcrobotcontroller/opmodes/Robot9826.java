package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Thomas on 1/31/2016.
 *
 * Robot9826
 *
 * This is a class file to hold all of our preset values and math to be used
 *      across multiple autonomous and teleop functions.
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

    final double wheelDistance = 16.625;
    final double pivotSlideFactor = 1.9;
    final double wheelDiameter = 5.07;
    final int tickPerRev = 1120;

    public enum DriveDirection {forward, reverse, left, right}
    public enum ArmDirection {up, down, stop}

    double ledPower = 1;
    public enum LEDPower {up, down, stop}
    LEDPower ledPowerDir = LEDPower.down;

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


        //Arm motor, reversed so 1 is up, -1 is down
        motorArm = hardwareMap.dcMotor.get("motorArm");
        motorArm.setDirection(DcMotor.Direction.REVERSE);

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

    /**Conveyor and Sweeper Direction*/
    public ConveyorDirection getConveyorDirection(){
        return conveyorDirection;
    }

    public SweeperDirection getSweeperDirection(){
        return sweeperDirection;
    }

    //<editor-fold desc="Drive Functions">
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
            case left: {
                drive(speed, -speed);
                break;
            }
            case right: {
                drive(-speed, speed);
                break;
            }
        }
    }

    public void drive(double rightSpeed, double leftSpeed){
        if (motorLeft != null) {
            motorLeft.setPower(Range.clip(leftSpeed, -1, 1));
        }
        if (motorRight != null) {
            motorRight.setPower(Range.clip(rightSpeed, -1, 1));
        }
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
                    motorArm.setPower(Range.clip(speed, -1, 1));
                    break;
                }
                case down:{
                    motorArm.setPower(Range.clip(-speed, -1, 1));
                    break;
                }
                case stop:{
                    motorArm.setPower(0);
                    break;
                }
            }
        }
    }
    //</editor-fold desc="Drive Functions">

    //<editor-fold desc="Bucket Toggles">
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
            case reverse: {
                sweeperServo.setPosition(sweeperBackward);
                break;
            }
            case stop:{
                sweeperServo.setPosition(sweeperStop);
                break;
            }
        }
    }
    //</editor-fold desc="Bucket Toggles">

    //<editor-fold desc="LED Functions">
    public void setLedPower(double power){
        led.setPower(Range.clip(power, -1, 1));
    }

    public void cycleLedPower(){
        if (ledPowerDir == LEDPower.down){
            setLedPower(ledPower -= .01);

            if(ledPower <= -.9){
                ledPowerDir = LEDPower.up;
            }
        }else if(ledPowerDir == LEDPower.up){
            setLedPower(ledPower += .01);

            if(ledPower >= .9){
                ledPowerDir = LEDPower.down;
            }
        }
    }
    ///</editor-fold desc="LED Functions">

    //<editor-fold desc="Encoder Math">
    public int inchesToTicks(double inches) {
        return (int) (inches / ((wheelDiameter * Math.PI) / tickPerRev));
    }

    public int degreesToTicks(int degrees){
        return inchesToTicks((((wheelDistance + pivotSlideFactor) * Math.PI) / 360) * degrees);
    }

    public double ticksToInches(int ticks) {
        return (ticks * ((wheelDiameter * Math.PI) / tickPerRev));
    }
    //</editor-fold desc="Encoder Math">

    //<editor-fold desc="Encoder Functions">
    public void reset_drive_encoders() {
        reset_left_drive_encoder();
        reset_right_drive_encoder();
    }

    public void reset_arm_encoder(){
        if (motorArm != null) {
            motorArm.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
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

    public void run_using_arm_drive_encoder() {
        if (motorArm != null) {
            motorArm.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }

    boolean have_drive_encoders_reached(int count){
        boolean l_return = false;
        if (has_left_drive_encoder_reached(count) &&
                has_right_drive_encoder_reached(count)) {
            l_return = true;
        }
        return l_return;
    }

    boolean have_drive_encoders_reset(){
        boolean l_return = false;
        if (has_left_drive_encoder_reset() &&
                has_right_drive_encoder_reset()) {
            l_return = true;
        }
        return l_return;
    }

    boolean have_drive_encoders_reached(int leftCount, int rightCount){
        boolean l_return = false;
        if (has_left_drive_encoder_reached(leftCount) &&
                has_right_drive_encoder_reached(rightCount)) {
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
            }
        }
        return l_return;
    }

    /**Indicate whether the right drive motor's encoder has reached a value*/
    boolean has_right_drive_encoder_reached(int count){
        boolean l_return = false;
        if (motorRight != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs(motorRight.getCurrentPosition()) >= count) {
                l_return = true;
            }
        }
        return l_return;
    }

    boolean has_left_drive_encoder_reset() {
        boolean l_return = false;
        if (motorLeft != null) {
            if (Math.abs(motorLeft.getCurrentPosition()) == 0) {
                l_return = true;
            }
        }
        return l_return;
    }

    /**Indicate whether the right drive motor's encoder has reset*/
    boolean has_right_drive_encoder_reset(){
        boolean l_return = false;
        if (motorRight != null) {
            if (Math.abs(motorRight.getCurrentPosition()) == 0) {
                l_return = true;
            }
        }
        return l_return;
    }

    boolean has_arm_drive_encoder_reached(int count){
        boolean l_return = false;
        if (motorArm != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs(motorArm.getCurrentPosition()) >= count) {
                l_return = true;
            }
        }
        return l_return;
    }

    boolean has_arm_drive_encoder_reset(){
        boolean l_return = false;
        if (motorArm != null) {
            if (Math.abs(motorArm.getCurrentPosition()) == 0) {
                l_return = true;
            }
        }
        return l_return;
    }

    public void run_without_encoders(){
        run_without_right_drive_encoder();
        run_without_arm_drive_encoder();
        run_without_left_drive_encoder();
    }

    public void run_without_left_drive_encoder () {
        if (motorLeft != null)
        {
            if (motorLeft.getMode() ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                motorLeft.setMode
                        ( DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    }

    public void run_without_right_drive_encoder () {
        if (motorRight != null)
        {
            if (motorRight.getMode() ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                motorRight.setMode
                        (DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    }

    public void run_without_arm_drive_encoder () {
        if (motorArm != null)
        {
            if (motorArm.getMode() ==
                    DcMotorController.RunMode.RESET_ENCODERS)
            {
                motorArm.setMode
                        (DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    }
    //</editor-fold desc="Encoder Functions">

    boolean hasWaited(long lastStateChange, double seconds){
        return System.currentTimeMillis() > (lastStateChange + (seconds * 1000));
    }

    void updateTelemetry(){
        telemetry.addData("R01", "Left Drive: " + motorLeft.getPower() + ", " + motorLeft.getCurrentPosition() + ", " + ticksToInches(motorLeft.getCurrentPosition()));
        telemetry.addData("R02", "Right Drive: " + motorRight.getPower() + ", " + motorRight.getCurrentPosition() + ", " + ticksToInches(motorRight.getCurrentPosition()));
        telemetry.addData("R03", "Arm Drive: " + motorArm.getPower() + ", " + motorArm.getCurrentPosition());
        telemetry.addData("R04", "LED: " + led.getPower());
    }
}
