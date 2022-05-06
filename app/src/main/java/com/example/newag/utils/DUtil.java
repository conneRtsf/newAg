package com.example.newag.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DUtil {
        public static String getLastDayOfMonth(String yearMonth) {
            int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
            int month = Integer.parseInt(yearMonth.split("-")[1]); //月
            Calendar cal = Calendar.getInstance();
            // 设置年份
            cal.set(Calendar.YEAR, year);
            // 设置月份
            // cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.MONTH, month); //设置当前月的上一个月
            // 获取某月最大天数
            //int lastDay = cal.getActualMaximum(Calendar.DATE);
            int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
            // 设置日历中月份的最大天数
            //cal.set(Calendar.DAY_OF_MONTH, lastDay);
            cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
            // 格式化日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(cal.getTime());
        }
        public static void main(String[] args) {
            System.out.println(getLastDayOfMonth("2019-02"));
        }
    }
