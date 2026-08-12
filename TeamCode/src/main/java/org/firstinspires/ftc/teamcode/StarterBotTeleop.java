package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * Skeleton TeleOp for the 2026-27 REV DUO FTC Starter Bot.
 *
 * Drivetrain: tank / skid-steer with two motors (HD Hex + UltraPlanetary).
 *   - gamepad1 left stick  -> left side
 *   - gamepad1 right stick -> right side
 *
 * Intake: single Core Hex Motor driving the flap wheels.
 *   - gamepad1 cross (bottom face button on the REV-31-2983) toggles the intake on/off.
 *
 * Configure these names on the Robot Controller (Control Hub):
 *   "left_drive", "right_drive", "intake"
 */
@TeleOp(name = "Starter Bot: Tank Teleop", group = "Starter Bot")
public class StarterBotTeleop extends OpMode {

    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private DcMotor intake;
    private Servo servo;

    private static final double INTAKE_POWER = 1.0;

    private boolean intakeOn = false;
    private boolean lastCrossButton = false;

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position

    // Define class members

    double  position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
    boolean rampUp = true;

    @Override
    public void init() {
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        servo = hardwareMap.get(Servo.class, "servo");

        // One side is reversed so that pushing both sticks forward drives the robot forward.
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData(">", "Initialized. Press START.");
    }

    @Override
    public void loop() {
        // READ INPUTS.
        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;
        boolean servoLeft = gamepad1.dpad_left;
        boolean servoRigh = gamepad1.dpad_right;
        // Toggle the intake on the rising edge of the cross button.
        if (gamepad1.cross && !lastCrossButton) {
            intakeOn = !intakeOn;
        }


        // NEXT STATE
        lastCrossButton = gamepad1.cross;

        // slew the servo, according to the rampUp (direction) variable.
        if (servoLeft) {
            // Keep stepping up until we hit the max value
            if (position < MAX_POS ) {
                position += INCREMENT ;

            }
        }

        if (servoRigh) {
            // Keep stepping up until we hit the max value
            if (position > MIN_POS ) {
                position -= INCREMENT ;

            }
        }


        // WRITE OUTPUTS
        leftDrive.setPower(left);
        rightDrive.setPower(right);
        intake.setPower(intakeOn ? INTAKE_POWER : 0.0);
        servo.setPosition(position);


        // TELEMETRY
        telemetry.addData("Drive", "L %.2f | R %.2f", left, right);
        telemetry.addData("Intake", intakeOn ? "ON" : "OFF");

        servo.getPosition();
        telemetry.addData("posicao", position);
        telemetry.update();

    }

    @Override
    public void stop() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        intake.setPower(0);
    }
}
