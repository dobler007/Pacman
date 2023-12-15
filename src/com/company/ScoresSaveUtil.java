package com.company;

import java.io.*;

public class ScoresSaveUtil implements Serializable {
    public static String SCORES_BIN_PATH = "resources/scores.bin";
    public static void serializeScoreList() {
        fileExists("resources/scores.bin");

        try (FileOutputStream fileOutputStream = new FileOutputStream(SCORES_BIN_PATH)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            HighScoreTable.SCORE_LIST.forEach(score -> {
                try {
                    objectOutputStream.writeObject(score);
                } catch (IOException e) {
                    String errorMessage = "IO Exception!\n" + e.getMessage();
                    System.out.println(errorMessage);
                }
            });

            objectOutputStream.close();
        } catch (IOException e) {
            String errorMessage = "IO Exception!\n" + e.getMessage();
            System.out.println(errorMessage);
        }
    }

    public static boolean fileExists(String path) {
        File file = new File(path);
        boolean fileExists = file.exists();
        if (!fileExists) {
            createFile(file);
        }
        return fileExists;
    }

    private static boolean createFile(File file) {
        try {
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (dirCreated) {
                    System.out.println("Folder created: " + parentDir.getAbsolutePath());
                } else {
                    String errorMessage = "Folder could not be created! Please try again!";
                    throw new FileNotFoundException(errorMessage);
                }
            }

            boolean fileCreated = file.createNewFile();
            if (fileCreated) {
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                String errorMessage = "File could not be created! Please try again!";
                throw new FileNotFoundException(errorMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
