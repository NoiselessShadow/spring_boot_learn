package com.itheima.controller;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;


/**
 * Excel数据联动
 *
 * @author wuming
 * @date 2023-09-14
 **/
public class ExcelTest {

    /**
     * 影响最大行数
     */
    private static final int XLS_MAX_ROW = 60000;


    public static void main(String[] args) {


        //省数据
        List<String> provinceList = Arrays.asList("江苏省", "河南省", "天津市", "北京市");
        //父子类关系
        Map<String, List<String>> childrenMap = new HashMap<>();
        childrenMap.put("江苏省", Arrays.asList("苏州市", "南通市", "无锡市", "常州市"));
        childrenMap.put("河南省", Arrays.asList("新乡市", "开封市", "洛阳市"));
        childrenMap.put("天津市", Arrays.asList("南开区", "和平区"));
        childrenMap.put("北京市", Arrays.asList("海淀区", "朝阳区"));


        try {
            exportHSSFTemplate(provinceList, childrenMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 导出模板
     *
     * @param sexList      性别列表
     * @param provinceList 省级列表
     * @param cityMap      市级下拉列表（Map中省级对应市级列表）
     */
    public static void exportHSSFTemplate(List<String> provinceList, Map<String, List<String>> cityMap) throws IOException {

        String[] titleList = new String[]{"姓名", "性别", "所在省", "所在市"};

        FileInputStream in = new FileInputStream(new File("e://test.xlsx"));

        //创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        XSSFSheet sheet = workbook.getSheet("sheet1");

//        }
        // 创建隐藏目录
        createHideSheetHSSF(workbook, provinceList, cityMap);

        //设置属性下拉
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        // 四个参数分别是：起始行、终止行、起始列、终止列  1 (下拉框代表从excel第1+1行开始) 10(下拉框代表从excel第1+10行结束) 5(代表第几列开始，0是第一列，1是第二列) 5(代表第几列结束，0是第一列，1是第二列)
        CellRangeAddressList provRangeAddressList = new CellRangeAddressList(1, XLS_MAX_ROW, 0, 0);
        // 下拉框可选的数据，作为约束
        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(provinceList.toArray(new String[]{}));
        DataValidation deviceTypeDataValidation = dvHelper.createValidation(dvConstraint,
                provRangeAddressList);
        deviceTypeDataValidation.setShowErrorBox(true);
        deviceTypeDataValidation.createPromptBox("Error", "请选择或输入有效的选项！");
        sheet.addValidationData(deviceTypeDataValidation);

        // 市级下拉规则
        CellRangeAddressList cityRange = new CellRangeAddressList(1, XLS_MAX_ROW, 1, 1);
        DataValidation cityValidation = dvHelper.createValidation(dvHelper.createFormulaListConstraint("INDIRECT($A2)"),cityRange);
        cityValidation.createErrorBox("error", "请选择正确的市级");
        sheet.addValidationData(cityValidation);

        try {
            FileOutputStream fileOut = new FileOutputStream("e://excel_template111.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建隐藏页
     *
     * @param workbook     sheet页
     * @param provinceList 级联父级
     * @param cityMap      级联子级
     */
    public static void createHideSheetHSSF(XSSFWorkbook workbook, List<String> provinceList, Map<String, List<String>> cityMap) {
        //创建一个专门用来存放地区信息的隐藏sheet页
        XSSFSheet hideSheet = workbook.createSheet("city");

        int rowId = 0;
        // 设置第1行，存省的信息
        Row provinceRow = hideSheet.createRow(rowId++);
        for (int i = 0; i < provinceList.size(); i++) {
            Cell provinceCell = provinceRow.createCell(i);
            provinceCell.setCellValue(provinceList.get(i));
        }
        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        if (!cityMap.isEmpty()) {
            for (String province : provinceList) {
                // 获取省份信息对应的市级信息列表
                List<String> cityList = cityMap.get(province);
                Row prow = hideSheet.createRow(rowId++);
                prow.createCell(0).setCellValue(province);
                for (int j = 0; j < cityList.size(); j++) {
                    Cell cell = prow.createCell(j + 1);
                    cell.setCellValue(cityList.get(j));
                }
                // 添加名称管理器
                String range = getRange(1, rowId, cityList.size());
                System.out.println(range);
                Name name = workbook.createName();
                // key不可重复
                name.setNameName(province);
                String formula = "city!" + range;
                name.setRefersToFormula(formula);
            }
        }
//        workbook.setSheetHidden(3, true);
    }

    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

}
