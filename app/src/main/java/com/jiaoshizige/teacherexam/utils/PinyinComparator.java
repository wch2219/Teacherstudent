package com.jiaoshizige.teacherexam.utils;



import com.jiaoshizige.teacherexam.model.BasesortLetters;

import java.util.Comparator;

/**
 * Created by ${wj} on 2015/4/8 0008.
 * 排序
 */
public class PinyinComparator implements Comparator<BasesortLetters>{
    @Override
    public int compare(BasesortLetters province, BasesortLetters province2) {
        if(province.getSortLetters().equals("@")
                ||province2.getSortLetters().equals("#"))
        {
            return -1;
        }
        else if(province.getSortLetters().equals("#")
                ||province2.getSortLetters().equals("@"))
        {
            return 1;
        }
        else
        {
            return province.getSortLetters().compareTo(province2.getSortLetters());
        }
    }
}
