package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by thomas on 1/6/16.
 */
public class TankDrive_Auto extends OpMode { //Servo position values
    double doorRightClose = 0;
    double doorLeftClose = 1;
    double conveyorStop = .48;
    double sweeperStop = .495;
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

    int state = 0;

    public TankDrive_Auto(){
    }

    @Override
    public void init(){
        //Drive motors
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

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

        //Set the conveyor position
        conveyorServo.setPosition(conveyorStop);

        //Set the door positions
        doorRight.setPosition(doorRightClose);
        doorLeft.setPosition(doorLeftClose);

        //Set the sweeper to stop
        sweeper.setPosition(sweeperStop);
    }


    @Override public void start(){
        super.start();
        reset_drive_encoders();
    }

    @Override public void loop(){
        switch(state){
            case 0:{
                reset_drive_encoders();
                state++;
                break;
            }

            case 1:{
                run_using_encoders();
                set_drive_power(1.0f, 1.0f);
                if (have_drive_encoders_reached (2880, 2880))
                {
                    reset_drive_encoders();
                    set_drive_power (0.0f, 0.0f);

                    state++;
                }
                break;
            }

            case 2:{
                run_using_encoders();

                set_drive_power(-1.0f, -1.0f);
                if (have_drive_encoders_reached (-2880, -2880))
                {
                    reset_drive_encoders();
                    set_drive_power (0.0f, 0.0f);
                    state++;
                }
                break;
            }
        }

        //LED pulse
        if(ledSwitch == true){
            ledPower -= .01;
            led.setPower(ledPower);

            if(ledPower < -.99){
                ledSwitch = false;
            }
        }else if(ledSwitch == false){
            ledPower += .01;
            led.setPower(ledPower);

            if(ledPower > .99){
                ledSwitch = true;
            }
        }

        telemetry.addData ("State", "State: " + state);
    }


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
            ( double p_left_count
                    , double p_right_count
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

    boolean has_left_drive_encoder_reached (double p_count)

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
            if (Math.abs (motorLeft.getCurrentPosition ()) > p_count)
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
    boolean has_right_drive_encoder_reached (double p_count)

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
            if (Math.abs (motorRight.getCurrentPosition ()) > p_count)
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
}
