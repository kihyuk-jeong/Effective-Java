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

/**
 * 상수 : CAPITAL_SNAKE_CASE
 * 메소드 : is, has, save , get , set 등의 동사로 시작
 * 변수 : 항상 소문자로 시작
 */

//class Rover_improving {
//    static final double WALKING_SPEED = 3;
//
//    final String serialNumber;
//    double milesPerHour;
//
//    Rover(String serialNumber) {
//        this.serialNumber = serialNumber;
//    }
//
//    void drive() {
//        milesPerHour = WALKING_SPEED;
//    }
//
//    void stop() {
//        milesPerHour = 0;
//    }
//}
