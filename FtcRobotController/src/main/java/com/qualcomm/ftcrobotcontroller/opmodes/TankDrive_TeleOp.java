package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**TankDrive_TeleOp
 *
 * Code to drive two motors in tank drive, two hinge servos,
 *      one conveyor servo, one sweeperServo servo,
 *      one worm driven arm, and an LED setup.
 */

public class TankDrive_TeleOp extends OpMode{
    Robot9826 robot;
    long lastPressA  = 0;
    long lastPressB  = 0;
    long lastPressRight  = 0;
    long lastPressLeft  = 0;
    long lastPressX = 0;
    long lastPressY = 0;
    long delay = 250;

    /** Constructors*/
    public TankDrive_TeleOp(){

    }

    @Override
    public void init(){
        robot = new Robot9826(super.hardwareMap, super.telemetry);
        robot.init();
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop(){
        try {
            /**Set motor power*/
            robot.drive(-gamepad1.right_stick_y, -gamepad1.left_stick_y);

            //Arm Motor using Right Bumper and Trigger
            if (gamepad1.right_bumper) {
                robot.driveArm(.6, Robot9826.ArmDirection.up);
            } else if (gamepad1.right_trigger > .2) {
                robot.driveArm(.5, Robot9826.ArmDirection.down);
            } else {
                robot.driveArm(0, Robot9826.ArmDirection.stop);
            }

            /**Conveyor Right*/
            if (gamepad1.dpad_right) {
                if (System.currentTimeMillis() > lastPressRight + delay) {
                    if (robot.getConveyorDirection() == Robot9826.ConveyorDirection.stop || robot.getConveyorDirection() == Robot9826.ConveyorDirection.left) {
                        robot.conveyorToggle(Robot9826.ConveyorDirection.right);
                    } else if (robot.getConveyorDirection() == Robot9826.ConveyorDirection.right) {
                        robot.conveyorToggle(Robot9826.ConveyorDirection.stop);
                    }

                    lastPressRight = System.currentTimeMillis();
                }
            }

            /**Conveyor Left*/
            if (gamepad1.dpad_left) {
                if (System.currentTimeMillis() > lastPressLeft + delay) {
                    if (robot.getConveyorDirection() == Robot9826.ConveyorDirection.stop || robot.getConveyorDirection() == Robot9826.ConveyorDirection.right) {
                        robot.conveyorToggle(Robot9826.ConveyorDirection.left);
                    } else if (robot.getConveyorDirection() == Robot9826.ConveyorDirection.left) {
                        robot.conveyorToggle(Robot9826.ConveyorDirection.stop);
                    }

                    lastPressLeft = System.currentTimeMillis();
                }
            }

            /**Door Right*/
            if (gamepad1.y) {
                if (System.currentTimeMillis() > lastPressY + delay) {
                    robot.toggleRightDoor();

                    lastPressY = System.currentTimeMillis();
                }
            }

            /**Door Left*/
            if (gamepad1.x) {
                if (System.currentTimeMillis() > lastPressX + delay) {
                    robot.toggleLeftDoor();

                    lastPressX = System.currentTimeMillis();
                }
            }

            /**Sweeper Forward*/
            if (gamepad1.a) {
                if (System.currentTimeMillis() > lastPressA + delay) {
                    if (robot.getSweeperDirection() == Robot9826.SweeperDirection.stop || robot.getSweeperDirection() == Robot9826.SweeperDirection.reverse) {
                        robot.sweeperToggle(Robot9826.SweeperDirection.forward);
                    } else if (robot.getSweeperDirection() == Robot9826.SweeperDirection.forward) {
                        robot.sweeperToggle(Robot9826.SweeperDirection.stop);
                    }

                    lastPressA = System.currentTimeMillis();
                }
            }

            /**Sweeper Reverse*/
            if (gamepad1.b) {
                if (System.currentTimeMillis() > lastPressB + delay) {
                    if (robot.getSweeperDirection() == Robot9826.SweeperDirection.stop || robot.getSweeperDirection() == Robot9826.SweeperDirection.forward) {
                        robot.sweeperToggle(Robot9826.SweeperDirection.reverse);
                    } else if (robot.getSweeperDirection() == Robot9826.SweeperDirection.reverse) {
                        robot.sweeperToggle(Robot9826.SweeperDirection.stop);
                    }

                    lastPressB = System.currentTimeMillis();
                }
            }

            /**Motors conjoined drive*/
            if (gamepad1.dpad_down) {
                robot.drive(.01, Robot9826.DriveDirection.reverse);
            } else if (gamepad1.dpad_up) {
                robot.drive(.9, Robot9826.DriveDirection.forward);
            }

            /**LED Power Cycle*/
            robot.cycleLedPower();

            /**Telemetry*/
            telemetry.addData("00", "Right stick: " + String.format("%.2f", gamepad1.right_stick_y));
            telemetry.addData("01", "Left stick: " + String.format("%.2f", gamepad1.left_stick_y));
            telemetry.addData("02", "Right power: " + String.format("%.2f", robot.motorRight.getPower()));
            telemetry.addData("03", "Left power: " + String.format("%.2f", robot.motorLeft.getPower()));
            telemetry.addData("04", "Arm Power: " + String.format("%.2f", robot.motorArm.getPower()));
        }catch (Exception ex){
            telemetry.addData("EX", ex.getMessage());
        }
    }

    @Override
    public void stop(){
        super.stop();
    }


}
