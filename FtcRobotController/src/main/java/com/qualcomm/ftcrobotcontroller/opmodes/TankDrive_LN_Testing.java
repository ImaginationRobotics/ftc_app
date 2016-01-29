package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas on 1/28/2016.
 *
 * This class was created to use as a testing ground, rather than screwing
 * with the actual autonomous
 */
public class TankDrive_LN_Testing extends LinearOpMode {
    double doorRightClose = 0;
    double doorRightOpen = .8;

    double doorLeftClose = 1;
    double doorLeftOpen = .2;

    int conveyorSwitch = 0; //0, 1, 2
    double conveyorStop = .48;
    double conveyorRight = 1;
    double conveyorLeft = 0;

    final double sweeperStop = .495;
    final double sweeperForward = 1;
    final double sweeperBackward = 0;
    final int sweeperSwitch = 0; //0, 1, 2

    final double wheelDistance = 15.375;
    final double wheelDiameter = 4.7;//14.8125 / PI
    final double PI = 3.14159265359;
    final int tickPerRev = 1120;

    enum turnDirection {right, left}

    enum driveDirection {forward, reverse}

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
    public void runOpMode() throws InterruptedException {
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
        sweeper = hardwareMap.servo.get("sweeper");

        //Led power
        led = hardwareMap.dcMotor.get("led");
        led.setPower(1);

        //Set the conveyor position
        conveyorServo.setPosition(conveyorStop);

        //Set the door positions
        doorRight.setPosition(doorRightClose);
        doorLeft.setPosition(doorLeftClose);

        //Set the sweeper to stop
        sweeper.setPosition(sweeperStop);

        waitForStart();
        telemetry.addData("00", "Start");
        //sleep(5000);

        led.setPower(-1);
        telemetry.addData("00", "Step 1");
        driveInches(10, .3f, driveDirection.forward);
        sleep(1000);
        driveInches(10, .3f, driveDirection.forward);
        sleep(1000);
        driveInches(10, .3f, driveDirection.forward);
        sleep(1000);
        driveInches(10, .3f, driveDirection.forward);
        sleep(1000);
        driveInches(10, .3f, driveDirection.forward);
        sleep(1000);
        driveInches(10, .3f, driveDirection.forward);
        sleep(1000);
    }

    //<editor-fold desc="Autonomous Functions">
    void set_drive_power(double p_left_power, double p_right_power) {
        if (motorLeft != null) {
            motorLeft.setPower(p_left_power);
        }
        if (motorRight != null) {
            motorRight.setPower(p_right_power);
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
        if (has_left_drive_encoder_reached(p_left_count) ||
                has_right_drive_encoder_reached(p_right_count)) {
            l_return = true;
        }
        return l_return;
    }

    boolean has_left_drive_encoder_reached(int p_count) {
        boolean l_return = false;
        if (motorLeft != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs(motorLeft.getCurrentPosition()) >= p_count) {
                l_return = true;
            }
        }
        return l_return;
    }

    /**
     * Indicate whether the right drive motor's encoder has reached a value.
     */
    boolean has_right_drive_encoder_reached(int p_count){
        boolean l_return = false;
        if (motorRight != null) {
            // TODO Implement stall code using these variables.
            if (Math.abs(motorRight.getCurrentPosition()) >= p_count) {
                l_return = true;
            }
        }
        return l_return;
    }
    //</editor-fold>

    int getTicks(double inches) {
        return (int) (inches / ((wheelDiameter * PI) / tickPerRev));
    }

    double getInches(int ticks) {
        return (ticks * ((wheelDiameter * PI) / tickPerRev));
    }

    void turnPivotDegrees(int Degrees, double speed, turnDirection direction) {
        run_using_encoders();

        double Distance = ((wheelDistance * PI) / 360) * Degrees;

        switch (direction) {
            case right: {
                while (!have_drive_encoders_reached(getTicks(Distance), getTicks(Distance))) {
                    set_drive_power(speed, -speed);
                }
            }

            case left: {
                while (!have_drive_encoders_reached(getTicks(Distance), getTicks(Distance))) {
                    set_drive_power(-speed, speed);
                }
            }
            telemetry.addData("01", "Left Drive: " + motorLeft.getPower() + ", " + motorLeft.getCurrentPosition() + ", " + getInches(motorLeft.getCurrentPosition()));
            telemetry.addData("02", "Right Drive: " + motorRight.getPower() + ", " + motorRight.getCurrentPosition() + ", " + getInches(motorRight.getCurrentPosition()));
        }

        stopDrive();
        telemetry.addData("01", "Left Drive: 0, " + motorLeft.getCurrentPosition() + ", " + getInches(motorLeft.getCurrentPosition()));
        telemetry.addData("02", "Right Drive: 0, " + motorRight.getCurrentPosition() + ", " + getInches(motorRight.getCurrentPosition()));
        reset_drive_encoders();
    }

    void driveInches(int inches, double speed, driveDirection direction) {
        run_using_encoders();

        while (!has_left_drive_encoder_reached(getTicks(inches))) {
            switch (direction) {
                case forward: {
                    set_drive_power(-speed, -speed);
                }
                case reverse: {
                    set_drive_power(speed, speed);
                }
            }
            telemetry.addData("01", "Left Drive: " + motorLeft.getPower() + ", " + motorLeft.getCurrentPosition() + ", " + getInches(motorLeft.getCurrentPosition()));
            telemetry.addData("02", "Right Drive: " + motorRight.getPower() + ", " + motorRight.getCurrentPosition() + ", " + getInches(motorRight.getCurrentPosition()));
        }

        stopDrive();
        telemetry.addData("01", "Left Drive: 0, " + motorLeft.getCurrentPosition() + ", " + getInches(motorLeft.getCurrentPosition()));
        telemetry.addData("02", "Right Drive: 0, " + motorRight.getCurrentPosition() + ", " + getInches(motorRight.getCurrentPosition()));
        reset_drive_encoders();
    }
}
