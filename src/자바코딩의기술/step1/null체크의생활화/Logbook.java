package 자바코딩의기술.step1.null체크의생활화;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Collections;

/**
 * NullPointException 방지를 위한 Null Check는 필수적인 부분이다.
 */

class Logbook {

    void writeMessage(String message, Path location) throws IOException {
        if (Files.isDirectory(location)) {
            throw new IllegalArgumentException("The path is invalid!");
        }
        if (message.trim().equals("") || message == null) {
            throw new IllegalArgumentException("The message is invalid!");
        }
        String entry = LocalDate.now() + ": " + message;
        Files.write(location, Collections.singletonList(entry),
            StandardCharsets.UTF_8, StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
    }


    //null 체크 로직을 추가하여 사전에 NPE를 방지한다.
    void writeMessage_improving(String message, Path location) throws IOException {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("The message is invalid!");
        }
        if (location == null || Files.isDirectory(location)) {
            throw new IllegalArgumentException("The path is invalid!");
        }

        String entry = LocalDate.now() + ": " + message;
        Files.write(location, Collections.singletonList(entry),
            StandardCharsets.UTF_8, StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
    }
}