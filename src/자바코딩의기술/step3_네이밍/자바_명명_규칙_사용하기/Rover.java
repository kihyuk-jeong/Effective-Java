package 자바코딩의기술.step3_네이밍.자바_명명_규칙_사용하기;

public class Rover {
    static final double WalkingSpeed = 3;

    final String SerialNumber;
    double MilesPerHour;

    Rover(String NewSerialNumber) {
        SerialNumber = NewSerialNumber;
    }

    void Drive() {
        MilesPerHour = WalkingSpeed;
    }

    void stop() {
        MilesPerHour = 0;
    }

}
