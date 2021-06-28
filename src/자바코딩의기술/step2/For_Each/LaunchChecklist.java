package 자바코딩의기술.step2.For_Each;

import java.util.Arrays;
import java.util.List;

class LaunchChecklist {

    List<String> checks = Arrays.asList("Cabin Pressure",
        "Communication",
        "Engine");

    Status prepareForTakeoff(Commander commander) {
        for (int i = 0; i < checks.size(); i++) {
            boolean shouldAbortTakeoff = commander.isFailing(checks.get(i));
            if (shouldAbortTakeoff) {
                return Status.ABORT_TAKE_OFF;
            }
        }
        return Status.READY_FOR_TAKE_OFF;
    }
}

/**
 * for 루프를 인덱스 변수인 i로 checks하며 순회하는 전통(고전)적인 방식 보다는 for-each를 사용하자.
 * 해당 예제 코드의 경우 리스트 내 다음 원소에 접근할 때가 아니면 i(인덱스)를 사용하지 않는다.
 * 즉, 사용하지 않는 i 변수를 굳이 할당할 필요가 없다.
  그렇다면, 전통적인 방법은 언제 사용될까???
   -> 정답은...  '거의 없다!'
   순회하면서 인덱스를 추가적으로 사용해야 할 경우가 아니라면 for-each로 처리하자.
 */

class LaunchChecklist_improving {

    List<String> checks = Arrays.asList("Cabin Pressure",
        "Communication",
        "Engine");

    Status prepareForTakeoff(Commander commander) {
        for (String check : checks) {
            boolean shouldAbortTakeoff = commander.isFailing(check);
            if (shouldAbortTakeoff) {
                return Status.ABORT_TAKE_OFF;
            }
        }
        return Status.READY_FOR_TAKE_OFF;
    }
}
