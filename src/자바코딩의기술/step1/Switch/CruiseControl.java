package 자바코딩의기술.step1.Switch;

public interface CruiseControl {

    public void grantAccess(User u);

    public void grantAdminAccess(User u);

    public void logUnauthorizedAccessAttempt();
}