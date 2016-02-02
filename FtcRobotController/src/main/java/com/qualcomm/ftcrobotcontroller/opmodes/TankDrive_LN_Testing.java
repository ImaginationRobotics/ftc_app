package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Thomas on 1/28/2016.
 *
 * This class was created to use as a testing ground, rather than screwing
 * with the actual autonomous
 */
public class TankDrive_LN_Testing extends LinearOpMode {
    Robot9826 robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot9826(super.hardwareMap, super.telemetry);
        robot.init();

        waitForStart();
        telemetry.addData("00", "I");
        sleep(2000);

        robot.setLedPower(-1);
        telemetry.addData("00", "HATE");
        drive(10, .5f, Robot9826.DriveDirection.forward);
        robot.stopDrive();
        sleep(2000);

        robot.setLedPower(1);
        telemetry.addData("00", "THIS");
        drive(10, .5f, Robot9826.DriveDirection.reverse);
        robot.stopDrive();
        sleep(2000);

        robot.setLedPower(1);
        telemetry.addData("00", "DANG");
        pivot(180, .5f, Robot9826.TurnDirection.left);
        robot.stopDrive();
        sleep(2000);

        robot.setLedPower(1);
        telemetry.addData("00", "PROGRAM");
        pivot(180, .5f, Robot9826.TurnDirection.right);
        robot.stopDrive();

    }

    private void waitForEncoderToReset() throws InterruptedException {
        while(robot.motorLeft.getCurrentPosition() != 0 &&
                robot.motorRight.getCurrentPosition() != 0){
            waitForNextHardwareCycle();
        }
    }

    public synchronized void drive(int inches, double speed, Robot9826.DriveDirection direction) throws InterruptedException{
        robot.reset_drive_encoders();
        waitForEncoderToReset();
        switch(direction){
            case forward: {
                robot.motorLeft.setTargetPosition(robot.inchesToTicks(inches));
                robot. motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                robot.drive(speed, speed);
                break;
            }
            case reverse: {
                robot.motorLeft.setTargetPosition(-robot.inchesToTicks(inches));
                robot.motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                robot.drive(-speed, -speed);
                break;
            }
        }
    }

    public synchronized void pivot(int degrees, double speed, Robot9826.TurnDirection direction) throws InterruptedException{
        robot.reset_drive_encoders();
        waitForEncoderToReset();
        switch(direction){
            case right: {
                robot.motorLeft.setTargetPosition(robot.degreesToTicks(degrees));
                robot.motorRight.setTargetPosition(-robot.degreesToTicks(degrees));
                robot.motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                robot.motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                robot.drive(-speed, speed);
                break;
            }
            case left: {
                robot.motorLeft.setTargetPosition(-robot.degreesToTicks(degrees));
                robot.motorRight.setTargetPosition(robot.degreesToTicks(degrees));
                robot.motorLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                robot.motorRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                robot.drive(speed, -speed);
                break;
            }
        }
    }
}
