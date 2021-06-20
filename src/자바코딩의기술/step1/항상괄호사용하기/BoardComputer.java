package 자바코딩의기술.step1.항상괄호사용하기;

import java.util.Objects;
import 자바코딩의기술.step1.Switch.User;

public class BoardComputer {
    CruiseControl cruiseControl;

    void authorize(User user) {
        Objects.requireNonNull(user);
        if (user.isUnknown())
            cruiseControl.logUnauthorizedAccessAttempt();
        if (user.isAstronaut())
            cruiseControl.grantAccess(user);
        if (user.isCommander())
            cruiseControl.grantAccess(user);
        cruiseControl.grantAdminAccess(user);
    }

    void authorize_improving(User user) {
        Objects.requireNonNull(user);
        if (user.isUnknown()) {
            cruiseControl.logUnauthorizedAccessAttempt();
        }
        if (user.isAstronaut()) {
            cruiseControl.grantAccess(user);
        }
        if (user.isCommander()) {
            cruiseControl.grantAccess(user);
        }
        cruiseControl.grantAdminAccess(user); // SECURITY THREAT
    }
}
