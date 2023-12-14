package com.company;

import javax.swing.*;

public class InputUtil {
    public static int getCorrectInput(JFrame frameInput, String inputMessage, String errorMessage) {
        int parsedInput;
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(inputMessage);
                parsedInput = Integer.parseInt(input);
                return parsedInput;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frameInput, errorMessage);
            }
        }
    }
}
