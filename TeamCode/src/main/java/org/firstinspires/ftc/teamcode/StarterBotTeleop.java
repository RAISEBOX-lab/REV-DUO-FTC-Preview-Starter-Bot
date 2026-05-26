package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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

    private static final double INTAKE_POWER = 1.0;

    private boolean intakeOn = false;
    private boolean lastCrossButton = false;

    @Override
    public void init() {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        intake = hardwareMap.get(DcMotor.class, "intake");

        // One side is reversed so that pushing both sticks forward drives the robot forward.
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData(">", "Initialized. Press START.");
    }

    @Override
    public void loop() {
        // Tank drive: sticks read negative when pushed forward, so negate.
        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;

        leftDrive.setPower(left);
        rightDrive.setPower(right);

        // Toggle the intake on the rising edge of the cross button.
        if (gamepad1.cross && !lastCrossButton) {
            intakeOn = !intakeOn;
        }
        lastCrossButton = gamepad1.cross;

        intake.setPower(intakeOn ? INTAKE_POWER : 0.0);

        telemetry.addData("Drive", "L %.2f | R %.2f", left, right);
        telemetry.addData("Intake", intakeOn ? "ON" : "OFF");
        telemetry.update();
    }

    @Override
    public void stop() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        intake.setPower(0);
    }
}
