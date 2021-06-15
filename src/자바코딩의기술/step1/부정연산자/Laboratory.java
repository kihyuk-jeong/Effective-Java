package 자바코딩의기술.step1.부정연산자;

/**
 *  부정연산자 '!' 사용을 지양하라
 */

public class Laboratory {

    Microscope microscope;

    Result analyze(Sample sample) {
        if(microscope.isInorganic(sample)) {
            return Result.INORGANIC;
        }else {
            return analyzeOrganic(sample);
        }
    }

    private Result analyzeOrganic(Sample sample) {
        if(!microscope.isHumanoid(sample)) {
            return Result.ALIEN;
        }else {
            return Result.HUMANOID;
        }
    }


}
