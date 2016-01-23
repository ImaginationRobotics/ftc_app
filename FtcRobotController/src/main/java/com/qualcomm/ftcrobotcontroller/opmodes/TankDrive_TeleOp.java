package com.qualcomm.ftcrobotcontroller.opmodes;

/**TankDrive_TeleOp
 *
 * Code to drive two motors in tank drive, two hinge servos,
 *      one conveyor servo, one sweeper servo,
 *      and one worm driven arm.
 */

public class TankDrive_TeleOp extends TankDrive_Hardware{
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
        super.init();
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop(){
        super.loop();
        //Set motor power
        set_drive_power(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        //Arm Motor using Right Bumper and Trigger
        if(gamepad1.right_bumper){
            motorArm.setPower(.5);
        }else if(gamepad1.right_trigger > .2){
            motorArm.setPower(-.5);
        }else{
            motorArm.setPower(0);
        }

        //Conveyor Right
        if(gamepad1.dpad_right){
            if(System.currentTimeMillis() > lastPressRight+delay) {
                if (conveyorSwitch == 0){
                    conveyorServo.setPosition(conveyorRight);
                    conveyorSwitch = 1;
                }else if (conveyorSwitch == 1) {
                    conveyorServo.setPosition(conveyorStop);
                    conveyorSwitch = 0;
                }else if (conveyorSwitch == 2){
                    conveyorServo.setPosition(conveyorRight);
                    conveyorSwitch = 1;
                }

                lastPressRight = System.currentTimeMillis();
            }
        }

        //Conveyor Left
        if(gamepad1.dpad_left){
            if(System.currentTimeMillis() > lastPressLeft+delay) {
                if (conveyorSwitch == 0){
                    conveyorServo.setPosition(conveyorLeft);
                    conveyorSwitch = 2;
                }else if (conveyorSwitch == 2) {
                    conveyorServo.setPosition(conveyorStop);
                    conveyorSwitch = 0;
                }else if (conveyorSwitch == 1){
                    conveyorServo.setPosition(conveyorLeft);
                    conveyorSwitch = 2;
                }

                lastPressLeft = System.currentTimeMillis();
            }
        }

        //Door Right
        if(gamepad1.y){
            if(System.currentTimeMillis() > lastPressY+delay){
                if(doorRight.getPosition() == doorRightClose) {
                    doorRight.setPosition(doorRightOpen);
                }else if(doorRight.getPosition() == doorRightOpen){
                    doorRight.setPosition(doorRightClose);
                }

                lastPressY = System.currentTimeMillis();
            }
        }

        //Door Left
        if(gamepad1.x){
            if(System.currentTimeMillis() > lastPressX+delay){
                if(doorLeft.getPosition() == doorLeftClose) {
                    doorLeft.setPosition(doorLeftOpen);
                }else if(doorLeft.getPosition() == doorLeftOpen){
                    doorLeft.setPosition(doorLeftClose);
                }

                lastPressX = System.currentTimeMillis();
            }
        }

        //Sweeper Right
        if(gamepad1.a){
            if(System.currentTimeMillis() > lastPressA+delay) {
                if (sweeperSwitch != 1) {
                    sweeper.setPosition(sweeperForward);
                    sweeperSwitch = 1;
                } else if (sweeperSwitch == 1) {
                    sweeper.setPosition(sweeperStop);
                    sweeperSwitch = 0;
                }

                lastPressA = System.currentTimeMillis();
            }
        }

        //Sweeper Left
        if(gamepad1.b){
            if(System.currentTimeMillis() > lastPressB+delay) {
                if (sweeperSwitch != 2) {
                    sweeper.setPosition(sweeperBackward);
                    sweeperSwitch = 2;
                } else if (sweeperSwitch == 2) {
                    sweeper.setPosition(sweeperStop);
                    sweeperSwitch = 0;
                }

                lastPressB = System.currentTimeMillis();
            }
        }

        //Motors conjoined drive
        if(gamepad1.dpad_down){
            motorRight.setPower(-.01);
            motorLeft.setPower(-.01);
        }else if(gamepad1.dpad_up){
            motorRight.setPower(.8);
            motorLeft.setPower(.8);
        }

        //Telemetry
        telemetry.addData("00", "Right stick: " + String.format("%.2f", gamepad1.right_stick_y));
        telemetry.addData("01", "Left stick: " + String.format("%.2f", gamepad1.left_stick_y));
        telemetry.addData("02", "LED Power: " + String.format("%.7f", ledPower));
    }

    @Override
    public void stop(){
        super.stop();
    }


}
