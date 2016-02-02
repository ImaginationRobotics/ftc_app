package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Thomas on 1/19/2016.
 *
 * This holds all the variables and settings for loop style programs.
 */
public class TankDrive_Hardware extends OpMode {
    double doorRightClose = 0;
    double doorRightOpen = .8;

    double doorLeftClose = 1;
    double doorLeftOpen = .2;

    int conveyorSwitch = 0; //0, 1, 2
    double conveyorStop = .48;
    double conveyorRight = 1;
    double conveyorLeft = 0;

    double sweeperStop = .495;
    double sweeperForward = 1;
    double sweeperBackward = 0;
    int sweeperSwitch = 0; //0, 1, 2

    double inchesPerTick = 0.01237; //13.62574 in / 1120 ticks
    double wheelDistance = 15.5;
    double wheelDiameter = 13.62574;
    final double PI = 3.14159265359;

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
        sweeper = hardwareMap.servo.get("sweeperServo");

        //Led power
        led = hardwareMap.dcMotor.get("led");
        led.setPower(1);

        //Set the conveyor position
        conveyorServo.setPosition(conveyorStop);

        //Set the door positions
        doorRight.setPosition(doorRightClose);
        doorLeft.setPosition(doorLeftClose);

        //Set the sweeperServo to stop
        sweeper.setPosition(sweeperStop);
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop(){
        try {
            //LED pulse
            if (ledSwitch == true) {
                ledPower -= .01;
                led.setPower(ledPower);

                if (ledPower < -.99) {
                    ledSwitch = false;
                }
            } else if (ledSwitch == false) {
                ledPower += .01;
                led.setPower(ledPower);

                if (ledPower > .99) {
                    ledSwitch = true;
                }
            }
        }catch (Exception ex){
            telemetry.addData("EX", ex.getMessage());
        }
    }

    @Override
    public void stop(){
        super.stop();
    }

    //<editor-fold desc="Autonomous Functions">
    void set_drive_power (double p_left_power, double p_right_power)
    {
        if (motorLeft != null)
        {
            motorLeft.setPower (p_left_power);
        }
        if (motorRight != null)
        {
            motorRight.setPower (p_right_power);
        }

    } // set_drive_power

    void stopDrive ()

    {
        if (motorLeft != null)
        {
            motorLeft.setPower (0);
        }
        if (motorRight != null)
        {
            motorRight.setPower (0);
        }

    } // set_drive_power

    public void reset_drive_encoders ()

    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_left_drive_encoder ();
        reset_right_drive_encoder ();

    } // reset_drive_encoders

    public void reset_left_drive_encoder ()

    {
        if (motorLeft != null)
        {
            motorLeft.setMode
                    ( DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // reset_right_drive_encoder
    //
    /**
     * Reset the right drive wheel encoder.
     */
    public void reset_right_drive_encoder ()

    {
        if (motorRight != null)
        {
            motorRight.setMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_right_drive_encoder

    public void run_using_encoders ()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_using_left_drive_encoder();
        run_using_right_drive_encoder();

    } // run_using_encoders

    public void run_using_left_drive_encoder ()

    {
        if (motorLeft != null)
        {
            motorLeft.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_right_drive_encoder
    //
    /**
     * Set the right drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_using_right_drive_encoder ()

    {
        if (motorRight != null)
        {
            motorRight.setMode
                    ( DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_right_drive_encoder

    boolean have_drive_encoders_reached
            ( int p_left_count
                    , int p_right_count
            )

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached the specified values?
        //
        if (has_left_drive_encoder_reached (p_left_count) &&
                has_right_drive_encoder_reached (p_right_count))
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_encoders_reached

    boolean has_left_drive_encoder_reached (int p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (motorLeft != null)
        {
            //
            // Has the encoder reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (motorLeft.getCurrentPosition ()) >= p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reached
    //
    /**
     * Indicate whether the right drive motor's encoder has reached a value.
     */
    boolean has_right_drive_encoder_reached (int p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (motorRight != null)
        {
            //
            // Have the encoders reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (motorRight.getCurrentPosition ()) >= p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reached
    //</editor-fold>

    int getTicks(double inches){
        return (int)(inches / inchesPerTick);
    }

    double getInches(int ticks){
        return (ticks * inchesPerTick);
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
        return dScale ;
    }

//    double pivotDegrees (int Degrees){
//        if(Degrees > 0){
//            double Distance = ((2* wheelDistance * PI)/360)*Degrees;
//
//            while(nMotorEncoder[LeftMotor]<(1440*(Distance/(wheelDiameter *PI)))){
//            }
//            return Distance;
//        }
//
//        else{
//            double Distance = ((2* wheelDistance * PI)/360)*-Degrees;
//
//            while(nMotorEncoder[RightMotor]<(1440*(Distance/(wheelDiameter *PI)))){
//            }
//
//            return Distance;
//        }
//
//    }
}
