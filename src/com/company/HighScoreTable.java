package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class HighScoreTable extends JTable {
    public static final String[] COLUMN_NAMES = {"Number", "Name", "Score"};
    public static final List<Score> SCORE_LIST = ScoresLoadUtil.loadHighScoreList();

    public HighScoreTable() {
        super(new DefaultTableModel(parseData(), COLUMN_NAMES));
    }

    private static Object[][] parseData() {
        Object[][] data = new Object[SCORE_LIST.size()][3];

        for (int i = 0; i < SCORE_LIST.size(); i++) {
            Score score = SCORE_LIST.get(i);
            data[i][0] = i + 1;
            data[i][1] = score.getName();
            data[i][2] = score.getScore();
        }

        return data;
    }
}
