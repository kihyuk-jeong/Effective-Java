package 자바코딩의기술.step1.코드대칭이루기;

import 자바코딩의기술.step1.Switch.User;

public interface CruiseControl {

    public void grantAccess(User u);

    public void grantAdminAccess(User u);

    public void logUnauthorizedAccessAttempt();
}