package 자바코딩의기술.step1.코드대칭이루기;

import java.util.Objects;
import 자바코딩의기술.step1.Switch.User;

class BoardComputer {

    CruiseControl cruiseControl;

    void authorize(User user) {
        Objects.requireNonNull(user);
        if (user.isUnknown()) {
            cruiseControl.logUnauthorizedAccessAttempt();
        } else if (user.isAstronaut()) {
            cruiseControl.grantAccess(user);
        } else if (user.isCommander()) {
            cruiseControl.grantAccess(user);
            cruiseControl.grantAdminAccess(user);
        }
    }

    /**
     * 1. 권한을 부여하는 코드와 부여하지 않는 코드가 섞여있던 기존 코드의 비대칭성을 아래와 같이 개선한다.
     * 2. 'Objects.requireNonNull(object)' 는 매개변수 object가 null이라면 NPE를 발생시킨다.
     *  -> fast-fail (빠른 실패)
     */

    void authorize_improving(User user) {
        Objects.requireNonNull(user);
        if (user.isUnknown()) {
            cruiseControl.logUnauthorizedAccessAttempt();
            return;
        }

        if (user.isAstronaut()) {
            cruiseControl.grantAccess(user);
        }else if(user.isCommander()) {
            cruiseControl.grantAccess(user);
            cruiseControl.grantAdminAccess(user);
        }
    }
}