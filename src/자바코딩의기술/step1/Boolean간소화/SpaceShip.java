package 자바코딩의기술.step1.Boolean간소화;

/**
 * 기존에 복잡했던 'willCrewSurvice()' 메소드의 조건식을 간단하게 변경하기
 * 더 낮은 수준의 메서드를 호출하는 것이 이상적이다.
 * 한 메서드 안에서는 추상화 수준이 비슷하도록 명령문을 합쳐야 한다.
 */

class SpaceShip {

    Crew crew;
    FuelTank fuelTank;
    Hull hull;
    Navigator navigator;
    OxygenTank oxygenTank;

    boolean willCrewSurvive() {
        return hull.holes == 0 &&
            fuelTank.fuel >= navigator.requiredFuelToEarth() &&
            oxygenTank.lastsFor(crew.size) > navigator.timeToEarth();
    }


    // 하위 수준의 메서드를 호출하는 방식으로 변경
    boolean willCrewSurvive_improving() {
        boolean hasEnoughResources = hasEnoughFuel() && hasEnoughOxygen();
        return hull.isIntact() && hasEnoughResources;
    }

    private boolean hasEnoughOxygen() {
        return oxygenTank.lastsFor(crew.size) > navigator.timeToEarth();
    }

    private boolean hasEnoughFuel() {
        return fuelTank.fuel >= navigator.requiredFuelToEarth();
    }


}