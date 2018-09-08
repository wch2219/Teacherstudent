package com.jiaoshizige.teacherexam.zz.utils;

public class ScoreUtils {

    private static int[][] score = new int[54][10];
    private static int[][] num = new int[54][10];

    public static void initScore() {
        //单题分数
        score[46][1] = 2;//单选分数
        score[46][6] = 14;//材料分析分数
        score[46][5] = 50;//写作分数
        score[45][1] = 2;//单选分数
        score[45][6] = 14;//材料分析分数
        score[45][5] = 50;//写作分数
        score[50][1] = 2;//单选分数
        score[50][6] = 14;//材料分析分数
        score[50][5] = 50;//写作分数
        score[39][1] = 2;//单选分数
        score[39][2] = 10;//简答分数
        score[39][3] = 8;//辨析分数
        score[39][6] = 18;//材料分析分数
        score[53][1] = 2;//单选分数
        score[53][2] = 10;//简答分数
        score[53][6] = 20;//材料分析分数
        score[53][8] = 40;//教学设计分数
        score[31][1] = 3;//单选分数
        score[31][2] = 15;//简答分数
        score[31][6] = 20;//材料分析分数
        score[31][9] = 30;//活动设计分数
    }

    private static void initNum() {
        //总题数和总分
        num[31][1] = 30;//单选分数10*3
        num[31][2] = 30;//简答分数15*2
        num[31][4] = 20;//论述分数10*1
        num[31][6] = 40;//材料分析分数20*2
        num[31][9] = 30;//活动设计分数30*1
        num[53][1] = 40;//单选分数20*2
        num[53][2] = 30;//简答分数10*3
        num[53][6] = 40;//材料分析分数20*2
        num[53][8] = 40;//教学设计分数40*1
        num[39][1] = 42;//单选分数21*2
        num[39][2] = 40;//简答分数10*4
        num[39][3] = 32;//辨析分数8*4
        num[39][6] = 36;//材料分析分数18*2
        num[50][1] = 58;//单选分数29*2
        num[50][6] = 42;//材料分析分数14*3
        num[50][5] = 50;//写作分数50*1
        num[45][1] = 58;//单选分数
        num[45][6] = 42;//材料分析分数
        num[45][5] = 50;//写作分数
        num[46][1] = 58;//单选分数
        num[46][6] = 42;//材料分析分数
        num[46][5] = 50;//写作分数
    }

    public static int getScore(int i, int j) {
        initScore();
        try {
            int scores = score[i][j];
            return scores;
        } catch (Exception e) {

        }
        return 0;
    }

    public static int getNum(int i, int j) {
        initNum();
        try {
            int nu = num[i][j];
            return nu;
        } catch (Exception e) {

        }
        return 0;
    }

}
