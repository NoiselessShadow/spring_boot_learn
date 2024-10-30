package com.itheima;

import org.springframework.util.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseNumberConverter {

//    public static String chineseToArabic(String chineseStr) {
//        String patternString = "[\u4e00-\u9fa5]+[ ]+.*|[\u4e00-\u9fa5]+[.]+.*|[\u4e00-\u9fa5]+[、]+.*";
//        String[] chineseNumsAll = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九","十","百","千","万"};
//        String[] arr = chineseStr.split("");
//        for (String s : arr) {
//            for (String string : chineseNumsAll) {
//
//            }
//        }
//
//
//        if(chineseStr.matches(patternString)){
//            String chineseNumber = "";
//            String[] split = chineseStr.split(",");
//            String[] split1 = chineseStr.split(".");
//            String[] split2 = chineseStr.split("。");
//            String[] split3 = chineseStr.split("、");
//            String[] split4 = chineseStr.split(" ");
//
//
//            String[] chineseNums = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
//            char[] chineseNumChars = chineseNumber.toCharArray();
//            int result = 0;
//            int temp = 1;
//            int count = 0;
//
//            for (int i = 0; i < chineseNumChars.length; i++) {
//                boolean isUnit = false;
//                switch (chineseNumChars[i]) {
//                    case '十':
//                        temp *= 10;
//                        isUnit = true;
//                        break;
//                    case '百':
//                        temp *= 100;
//                        isUnit = true;
//                        break;
//                    case '千':
//                        temp *= 1000;
//                        isUnit = true;
//                        break;
//                    case '万':
//                        temp *= 10000;
//                        isUnit = true;
//                        break;
//                }
//                if (isUnit) {
//                    result += temp;
//                    temp = 1;
//                    count = 0;
//                } else {
//                    for (int j = 0; j < chineseNums.length; j++) {
//                        if (String.valueOf(chineseNumChars[i]).equals(chineseNums[j])) {
//                            temp = j;
//                            if (i == chineseNumChars.length - 1) {
//                                result += temp;
//                            }
//                            count++;
//                            break;
//                        }
//                    }
//                    if (count > 1) {
//                        temp *= Math.pow(10, count - 1);
//                        result += temp;
//                        temp = 1;
//                        count = 0;
//                    }
//                }
//            }
//            return chineseNumber;
//
//        }
//
//    }
//
//    public static void main(String[] args) {
//        String chineseNumber = "一千零二十三";
//        int arabicNumber = chineseToArabic(chineseNumber);
//        System.out.println(chineseNumber + " 对应的阿拉伯数字是: " + arabicNumber);
//    }
        public static String chineseToArabic(String chineseStr) {

            String patternString = "[\u4e00-\u9fa5]+[ ]+.*|[\u4e00-\u9fa5]+[.]+.*|[\u4e00-\u9fa5]+[、]+.*|[\u4e00-\u9fa5]+[。]+.*";
            String  resContent = chineseStr;
            if(chineseStr.matches(patternString)){
                String[] chineseNumsAll = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九","十","百","千","万"};
                String[] symbolArr=  {"\\.", "。", "、", " "};
                for (String string : symbolArr) {
                    String[] split = chineseStr.split(string);
                    //假如是中文汉字，匹配到千为七个字符
                    if(split.length>=2 && split[0].length()<=7 ){
                        String  splitFirst = split[0];
                        for (String nums : chineseNumsAll) {
                            splitFirst = splitFirst.replaceAll(nums, "");
                        }
                        if(ObjectUtils.isEmpty(splitFirst)){
                            resContent = "";
                            for (int i = 1; i < split.length; i++) {
                                resContent += split[i];
                            }
                            break;
                        }
                    }
                }
            }

            return resContent;
        }

    public static void main(String[] args) {

            String testStr = "一。。、测试用。，.的字符串来啦，假如在后边加别的呢";
        String s = chineseToArabic(testStr);
        System.out.println(s);

    }

}