package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
//import com.qualcomm.robotcore.hardware.CRServo;

import java.util.ArrayList;

@Autonomous(name = "with brakes ")
public class brake extends LinearOpMode {

    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontRight;
    private DcMotor motorFrontLeft;


    private DcMotorEx slide1;
    private DcMotorEx slide2;
    private Servo claw;
    private Servo rotation;

    private DigitalChannel slideLimitSwitch;


    int encoderTicks;
    int target_Position;
    double slidePower = 0.53;
    double speed;
    int bl;
    int br;
    int fl;
    int fr;


    @Override
    public void runOpMode() {
        motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");
        motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
        motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
        motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");

        claw = hardwareMap.get(Servo.class, "claw");
        rotation = hardwareMap.get(Servo.class, "rotation");
        slide1 = hardwareMap.get(DcMotorEx.class, "slide1");
        slide2 = hardwareMap.get(DcMotorEx.class, "slide2");

        slideLimitSwitch = hardwareMap.get(DigitalChannel.class, "slideLimitSwitch");


        // Put initialization blocks here.
        speed = 0.6;
        double slideMotorVelocity = 2800;


        //slideMotor.setDirection(DcMotorEx.Direction.REVERSE);
        //armMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slide1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slide2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);


        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();


        if (opModeIsActive()) {

            moveForward2(2300);
            sleep(2500);
            Strafe_Left(1000);



            while (opModeIsActive()) {
                if (speed <= 0){

                    motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                }
                // Put loop blocks here.
                bl = motorBackLeft.getCurrentPosition();
                br = motorBackRight.getCurrentPosition();
                fl = motorFrontLeft.getCurrentPosition();
                fr = motorFrontRight.getCurrentPosition();

                telemetry.addData("Current Encoder Ticks of Back Left", motorBackLeft.getCurrentPosition());
                telemetry.addData("Current Encoder Ticks of Front Right", motorFrontRight.getCurrentPosition());

                telemetry.addData("Front Left Speed: ", motorFrontLeft.getPower());
                telemetry.addData("Back Left Speed", motorBackLeft.getPower());
                telemetry.addData("Front Right Speed", motorFrontRight.getPower());
                //telemetry.addData("Arm Ticks: ", armMotor.getCurrentPosition());
                telemetry.update();
            }
        }
    }

    /**
     * Describe this function...
     */


    private void moveForward2(int encoder_Ticks) {

        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);


        encoderTicks = encoder_Ticks;

        bl = motorBackLeft.getCurrentPosition() + encoderTicks;
        br = motorBackRight.getCurrentPosition() + encoderTicks;
        fl = motorFrontLeft.getCurrentPosition() + encoderTicks;
        fr = motorFrontRight.getCurrentPosition() + encoderTicks;

        telemetry.addData(" Target Position of Front Left", fl);

        motorFrontLeft.setTargetPosition(fl);
        motorFrontRight.setTargetPosition(fr);
        motorBackLeft.setTargetPosition(bl);
        motorBackRight.setTargetPosition(br);

        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed);

        while (motorFrontLeft.isBusy() || motorBackLeft.isBusy() || motorFrontRight.isBusy() || motorBackRight.isBusy()){
            telemetry.addData("hhh", motorBackLeft.getCurrentPosition());
        }
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sleep(1000);
    }

    private void Strafe_Right(int encoder_Ticks) {


        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        encoderTicks = encoder_Ticks;

        bl = motorBackLeft.getCurrentPosition() + encoderTicks;
        br = motorBackRight.getCurrentPosition() + encoderTicks;
        fl = motorFrontLeft.getCurrentPosition() + encoderTicks;
        fr = motorFrontRight.getCurrentPosition() + encoderTicks;

        telemetry.addData(" Target Position of Front Left", fl);

        motorFrontLeft.setTargetPosition(fl);
        motorFrontRight.setTargetPosition(fr);
        motorBackLeft.setTargetPosition(bl);
        motorBackRight.setTargetPosition(br);

        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed);

        telemetry.addData("BL TICKS: ", motorBackLeft.getCurrentPosition());
        telemetry.addData("FR TICKS: ", motorFrontRight.getCurrentPosition());
        telemetry.update();

        sleep(1000);
    }

    private void Strafe_Left(int encoder_Ticks) {
        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);

        encoderTicks = encoder_Ticks;

        bl = motorBackLeft.getCurrentPosition() + encoderTicks;
        br = motorBackRight.getCurrentPosition() + encoderTicks;
        fl = motorFrontLeft.getCurrentPosition() + encoderTicks;
        fr = motorFrontRight.getCurrentPosition() + encoderTicks;

        telemetry.addData(" Target Position of Front Left", fl);

        motorFrontLeft.setTargetPosition(fl);
        motorFrontRight.setTargetPosition(fr);
        motorBackLeft.setTargetPosition(bl);
        motorBackRight.setTargetPosition(br);

        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed);

        sleep(1000);
    }


    private void turnLeft(int encoder_Ticks) {
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        encoderTicks = encoder_Ticks;

        bl = motorBackLeft.getCurrentPosition() + encoderTicks;
        br = motorBackRight.getCurrentPosition() + encoderTicks;
        fl = motorFrontLeft.getCurrentPosition() + encoderTicks;
        fr = motorFrontRight.getCurrentPosition() + encoderTicks;

        telemetry.addData(" Target Position of Front Left", fl);

        motorFrontLeft.setTargetPosition(fl);
        motorFrontRight.setTargetPosition(fr);
        motorBackLeft.setTargetPosition(bl);
        motorBackRight.setTargetPosition(br);

        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed);

        sleep(1000);
    }

}

