package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoresLoadUtil implements Serializable {
    public static List<Score> loadHighScoreList() {
        List<Score> scoreList = new ArrayList<>();

        boolean filesExists = ScoresSaveUtil.fileExists(ScoresSaveUtil.SCORES_BIN_PATH);

        if (!filesExists) {
            return scoreList;
        }

        try (FileInputStream fileInputStream = new FileInputStream(ScoresSaveUtil.SCORES_BIN_PATH)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            while (true) {
                try {
                    Object objectMoment = objectInputStream.readObject();
                    if (objectMoment instanceof Score score) {
                        scoreList.add(score);
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    String errorMessage = "Class not found!\n" + e.getMessage();
                    System.out.println(errorMessage);
                }
            }
        } catch (IOException e) {
            String errorMessage = "IO Exception!\n" + e.getMessage();
            System.out.println(errorMessage);
        }
        return scoreList;
    }
}
