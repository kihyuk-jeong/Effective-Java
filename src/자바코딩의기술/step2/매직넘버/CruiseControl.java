package 자바코딩의기술.step2.매직넘버;

/**
 * 개발자는 코드에서 옵션 집합을 표현할 때 종종 숫자 집합을 사용한다. 특별한 맥락 없이 이 숫자를 '매직 넘버', 즉 표면상 의미가 없는 수자이지만 프로그램의 동작을 제어한다.
 * 매직 넘버가 있으면 코드를 이해하기 어려워지고 Side effect 발생 확률도 높아진다.
 */

public class CruiseControl {

    static final int STOP_PRESET = 0;
    static final int PLANETARY_SPEED_PRESET = 1;
    static final int CRUISE_SPEED_PRESET = 2;

    static final double CRUISE_SPEED_KMH = 16944;
    static final double PLANETARY_SPEED_KMH = 7667;
    static final double STOP_SPEED_KMH = 0;

    private double targetSpeedKmh;

    /**
     * 매직 넘버를 제거하자.
     */
    void setPreset_improving(int speedPreset) {
        if (speedPreset == CRUISE_SPEED_PRESET) {
            setTargetSpeedKmh(CRUISE_SPEED_KMH);
        } else if (speedPreset == PLANETARY_SPEED_PRESET) {
            setTargetSpeedKmh(PLANETARY_SPEED_KMH);
        } else if (speedPreset == STOP_PRESET) {
            setTargetSpeedKmh(STOP_SPEED_KMH);
        }
    }

    void setPreset(int speedPreset) {
        if (speedPreset == 2) {
            setTargetSpeedKmh(16944);
        } else if (speedPreset == 1) {
            setTargetSpeedKmh(7667);
        } else if (speedPreset == 0) {
            setTargetSpeedKmh(0);
        }
    }

    void setTargetSpeedKmh(double speed) {
        targetSpeedKmh = speed;
    }
}

//class Main {
//    static void usage() {
//        CruiseControl cruiseControl = null;
//        cruiseControl.setPreset(1);
//        cruiseControl.setPreset(2);
//        cruiseControl.setPreset(-1);
//    }
//}

class Main {
    static final int STOP_PRESET = 0;
    static final int PLANETARY_SPEED_PRESET = 1;
    static final int CRUISE_SPEED_PRESET = 2;

    static void usage() {
        CruiseControl cruiseControl = null;
        cruiseControl.setPreset(PLANETARY_SPEED_PRESET);
        cruiseControl.setPreset(CRUISE_SPEED_PRESET);
        cruiseControl.setPreset(-1);
    }
}