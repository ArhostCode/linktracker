package edu.java.scrapper.provider;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    @SneakyThrows
    public static String readAll(String fileName) {
        return new String(Utils.class.getResourceAsStream(fileName).readAllBytes());
    }

}
