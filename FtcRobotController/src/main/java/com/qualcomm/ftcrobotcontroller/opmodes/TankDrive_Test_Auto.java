package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by thomas on 1/6/16.
 */
public class TankDrive_Test_Auto extends OpMode {
    Robot9826 robot;
    private int state;
    private long lastStateChange;


    public TankDrive_Test_Auto(){
    }

    @Override
    public void init(){
        robot = new Robot9826(super.hardwareMap, super.telemetry);
        robot.init();

        state = 0;
    }

    @Override
    public void start(){
        super.start();
        lastStateChange = System.currentTimeMillis();
    }

    @Override
    public void loop(){
        switch(state){
            case 0:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.forward);
                if (robot.have_drive_encoders_reached(robot.inchesToTicks(10))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            case 1:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                    lastStateChange = System.currentTimeMillis();
                }

                break;
            }

            case 2:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.left);
                if (robot.have_drive_encoders_reached(robot.degreesToTicks(90))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            case 3:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                    lastStateChange = System.currentTimeMillis();
                }

                break;
            }

            case 4:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.left);
                if (robot.have_drive_encoders_reached(robot.degreesToTicks(90))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            case 5:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                    lastStateChange = System.currentTimeMillis();
                }
                break;
            }

            case 6:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.left);
                if (robot.have_drive_encoders_reached(robot.degreesToTicks(90))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
             }

            case 7:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                    lastStateChange = System.currentTimeMillis();
                }
                break;
            }

            case 8:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.left);
                if (robot.have_drive_encoders_reached(robot.degreesToTicks(90))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            case 9:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                    lastStateChange = System.currentTimeMillis();
                }
                break;
            }

            case 10:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.forward);
                if (robot.have_drive_encoders_reached(robot.inchesToTicks(10))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;

            }
        }
        telemetry.addData("A00", "State: " + String.format("%d", state));
        robot.updateTelemetry();

    }
    

    @Override
    public void stop(){
        super.stop();
    }
}
