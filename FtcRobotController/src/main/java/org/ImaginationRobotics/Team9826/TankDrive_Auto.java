package org.ImaginationRobotics.Team9826;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by thomas on 1/6/16
 *
 * TankDrive_Auto
 *
 * An autonomous for the Red side of the field.
 */
public class TankDrive_Auto extends OpMode {
    Robot9826 robot;
    private int state;
    private long lastStateChange;


    public TankDrive_Auto(){
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
            /**Robot Start*/
            case 0:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                }

                break;
            }

            /**Drive 1*/
            case 1:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.forward);
                if (robot.have_drive_encoders_reached(robot.inchesToTicks(42))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            /**Wait*/
            case 2:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                }

                break;
            }

            /**Turn 1*/
            case 3:{
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

            /**Wait*/
            case 4:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                }
                break;
            }

            /**Drive 2*/
            case 5:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.forward);
                if (robot.have_drive_encoders_reached(robot.inchesToTicks(37))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            /**Wait*/
            case 6:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                }
                break;
            }

            /**Turn 2*/
            case 7:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.left);
                if (robot.have_drive_encoders_reached(robot.degreesToTicks(45))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            /**Wait*/
            case 8:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                }
                break;
            }

            /**Drive 3*/
            case 9:{
                robot.run_using_encoders();
                robot.drive(0.5, Robot9826.DriveDirection.forward);
                if (robot.have_drive_encoders_reached(robot.inchesToTicks(20))) {
                    robot.stopDrive();
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            /**Wait*/
            case 10:{
                robot.setLedPower(-1);
                robot.reset_drive_encoders();
                if(robot.have_drive_encoders_reset() && robot.hasWaited(lastStateChange, 2)){
                    state++;
                }
                break;
            }

            /**Reset Arm Encoder*/
            case 11:{
                robot.setLedPower(-1);
                robot.reset_arm_encoder();
                if(robot.has_arm_drive_encoder_reset()){
                    state++;
                }
                break;
            }

            /**Move Arm Down*/
            case 12:{
                robot.run_using_arm_drive_encoder();
                robot.driveArm(0.5, Robot9826.ArmDirection.down);
                if (robot.has_arm_drive_encoder_reached(1700)) {
                    robot.driveArm(0, Robot9826.ArmDirection.stop);
                    lastStateChange = System.currentTimeMillis();
                    state++;
                }
                robot.setLedPower(1);
                break;
            }

            /**Drive 4 CLIMB RAMP*/
            case 13:{
                robot.run_using_encoders();
                robot.drive(0.9, Robot9826.DriveDirection.forward);
                if (robot.have_drive_encoders_reached(robot.inchesToTicks(20))) {
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
