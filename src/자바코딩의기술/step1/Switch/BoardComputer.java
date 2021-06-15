package 자바코딩의기술.step1.Switch;

import java.util.Objects;

/**
1. Switch문은 항상 Case마다 break를 걸어줘야 한다.
2. 의도적으로 break를 누락했다면 반드시 주석을 남겨주는 습관을 갖도록 하자. (다른 개발자는 그것을 실수로 인식할수도 있다.)
 */


public class BoardComputer {

    CruiseControl cruiseControl;

    void authorize(User user) {
        Objects.requireNonNull(user); // 매개변수가 null이면 예외를 발생시킨다.
        switch (user.getRank()) {
            case UNKNOWN:
                cruiseControl.logUnauthorizedAccessAttempt();
            case ASTRONAUT:
                cruiseControl.grantAccess(user);
                break;
            case COMMANDER:
                cruiseControl.grantAccess(user);
                cruiseControl.grantAdminAccess(user);
                break;
        }
    }

    void authorize_improving(User user) {
        Objects.requireNonNull(user); // 매개변수가 null이면 예외를 발생시킨다.
        switch (user.getRank()) {
            case UNKNOWN:
                cruiseControl.logUnauthorizedAccessAttempt();
                break;
            case ASTRONAUT:
                cruiseControl.grantAccess(user);
                break;
            case COMMANDER:
                cruiseControl.grantAccess(user);
                cruiseControl.grantAdminAccess(user);
                break;
        }
    }
}