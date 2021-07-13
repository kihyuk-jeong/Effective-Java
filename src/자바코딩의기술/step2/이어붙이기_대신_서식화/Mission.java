package 자바코딩의기술.step2.이어붙이기_대신_서식화;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 한 눈에 봐도 가독성이 박살난 코드는 좋은 코드가 아니다. 코드 자체는 복잡하지 않지만 읽기가 어렵다.
 * 분명 짧은 코드임에도 불구하고 읽기가 어려운 코드라면 ?
 * 다시한번 생각해보자.
 */

class Mission {

    Logbook logbook;
    LocalDate start;

    void update(String author, String message) {
        LocalDate today = LocalDate.now();
        String month = String.valueOf(today.getMonthValue());
        String formattedMonth = month.length() < 2 ? "0" + month : month;
        String entry = author.toUpperCase() + ": [" + formattedMonth + "-" +
            today.getDayOfMonth() + "-" + today.getYear() + "](Day " +
            (ChronoUnit.DAYS.between(start, today) + 1) + ")> " +
            message + System.lineSeparator();
        logbook.write(entry);
    }
}

class Usage {
    static void main(String[] args) {
        new Mission().update("LInUS", "message");
    }
}

/**
 * format 메소드 사용 결과 매우 간결한 코드로!
 */

class Mission_improving {

    Logbook logbook;
    LocalDate start;

    void update(String author, String message) {
        final LocalDate today = LocalDate.now();
        String entry = String.format("%S: [%tm-%<te-%<tY](Day %d)> %s%n",
            author, today,
            ChronoUnit.DAYS.between(start, today) + 1, message);
        logbook.write(entry);
    }
}

