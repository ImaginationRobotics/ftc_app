package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Thomas on 1/23/2016.
 *
 * Main autonomous function used in linear style, rather than a loop style.
 */
public class TankDrive_Auto_Ln extends LinearOpMode {
    Robot9826 robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot9826(super.hardwareMap, super.telemetry);
        robot.init();

        /**ROBOT START*/
        waitForStart();
        telemetry.addData("00", "Start");
        sleep(1000);

        robot.led.setPower(-1);
        telemetry.addData("00", "Step 1");
        driveInches(40, .3f, Robot9826.DriveDirection.forward);
        waitForNextHardwareCycle();
        sleep(1000);

        robot.setLedPower(1);
        telemetry.addData("A00", "Step 2");
        pivotDegrees(90, .3f, Robot9826.TurnDirection.left);
        robot.setLedPower(-1);
        telemetry.addData("A00", "Step 3");
        waitForNextHardwareCycle();
        sleep(1000);

        robot.led.setPower(-1);
        telemetry.addData("A00", "Step 4");
        driveInches(15, .3f, Robot9826.DriveDirection.forward);
        waitForNextHardwareCycle();
        sleep(1000);

        robot.led.setPower(1);
        telemetry.addData("A00", "Step 5");
        pivotDegrees(45, .3f, Robot9826.TurnDirection.left);
        waitForNextHardwareCycle();
        sleep(1000);

        robot.led.setPower(-1);
        telemetry.addData("A00", "Step 6");
        driveInches(12, .3f, Robot9826.DriveDirection.forward);
    }


    void resetEncoders() throws InterruptedException{
        robot.reset_drive_encoders();
        while (!robot.have_drive_encoders_reached(0, 0)) {
            waitOneFullHardwareCycle();
        }
        robot.run_using_encoders();
    }

    void pivotDegrees(int degrees, double speed, Robot9826.TurnDirection direction) throws InterruptedException {
        resetEncoders();
        int distance = robot.degreesToTicks(degrees);
        telemetry.addData("A01", "Distance: " + distance);
        telemetry.addData("A02", "Ticks: " + robot.inchesToTicks(distance));

        switch (direction) {
            case right: {
                telemetry.addData("A03", "Turning Right:" + degrees);
                while (!robot.have_drive_encoders_reached(robot.inchesToTicks(distance), robot.inchesToTicks(distance))) {
                    robot.drive(-speed, speed);
                    waitOneFullHardwareCycle();
                }
                break;
            }

            case left: {
                telemetry.addData("A03", "Turning Left:" + degrees);
                while (!robot.have_drive_encoders_reached(robot.inchesToTicks(distance), robot.inchesToTicks(distance))) {
                    robot.drive(speed, -speed);
                    waitOneFullHardwareCycle();
                }
                break;
            }
        }

        robot.stopDrive();
    }

    void driveInches(int inches, double speed, Robot9826.DriveDirection direction) throws InterruptedException {
        resetEncoders();

        while (!robot.has_left_drive_encoder_reached(robot.inchesToTicks(inches))) {
            robot.drive(speed, direction);
            waitOneFullHardwareCycle();
        }

        robot.stopDrive();
    }
}
