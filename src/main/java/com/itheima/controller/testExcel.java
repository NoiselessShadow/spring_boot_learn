//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.util.CellRangeAddressList;
//import org.apache.poi.util.IOUtils;
//import org.apache.poi.xssf.usermodel.*;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//
///**
// * <a> 代码千万行 注释第一行 编程不规范 同事两行泪 </a>
// *
// * @author Enzo
// * @date 2024-05-15 15:33
// * @description excel导出工具类
// */
//@Slf4j
//@Data
//public class ExcelUtil {
//
//    /**
//     * 导出数据最大行数
//     */
//    private static final int MAX_ROWS = 3000;
//    /**
//     * 数据页sheet名称
//     */
//    private static final String DATA_SHEET_NAME = "dataSheet";
//
//    /**
//     * 标头
//     */
//    private List<String> headersList;
//
//    /**
//     * 单选下拉框数据源
//     */
//    private List<String> selectDropdownList;
//
//    /**
//     * 单选下拉框列 开始下标 从0开始
//     */
//    private int singleChoiceColumnIndex;
//
//    /**
//     * 多级联动下拉数据源
//     */
//    private Map<String, List<String>> multilevelDropDownDataSource;
//
//    /**
//     * 多级联动下拉框列 开始下标 从0开始
//     */
//    private int multilevelDropDownStartColumn;
//
//    /**
//     * 多级联动数据源
//     */
//    private int multilevelDropDownLevel;
//
//    public ExcelUtil() {
//    }
//
//    /**
//     * 生成只有标头的模板
//     *
//     * @param headersList 标头
//     */
//    public ExcelUtil(List<String> headersList) {
//        this.headersList = headersList;
//    }
//
//    /**
//     * 生成标头 单选 模板
//     *
//     * @param headersList             标头
//     * @param selectDropdownList      单选数据源
//     * @param singleChoiceColumnIndex 单选开始列 从0计算
//     */
//    public ExcelUtil(List<String> headersList, List<String> selectDropdownList, int singleChoiceColumnIndex) {
//        this.headersList = headersList;
//        this.selectDropdownList = selectDropdownList;
//        this.singleChoiceColumnIndex = singleChoiceColumnIndex;
//    }
//
//    /**
//     * 生成标头 多级联动下拉 模板
//     *
//     * @param headersList                   标头
//     * @param multilevelDropDownDataSource  多级联动下拉 数据源
//     * @param multilevelDropDownStartColumn 多级联动下拉 开始列 从0计算
//     * @param multilevelDropDownLevel       多级联动下拉  层级
//     */
//    public ExcelUtil(List<String> headersList, Map<String, List<String>> multilevelDropDownDataSource, int multilevelDropDownStartColumn,
//                     int multilevelDropDownLevel) {
//        this.headersList = headersList;
//        this.multilevelDropDownDataSource = multilevelDropDownDataSource;
//        this.multilevelDropDownStartColumn = multilevelDropDownStartColumn;
//        this.multilevelDropDownLevel = multilevelDropDownLevel;
//    }
//
//    /**
//     * 生成标头 单选及多级联动下拉 模板
//     *
//     * @param headersList                   标头
//     * @param selectDropdownList            单选数据源
//     * @param singleChoiceColumnIndex       单选开始列 从0计算
//     * @param multilevelDropDownDataSource  多级联动下拉 数据源
//     * @param multilevelDropDownStartColumn 多级联动下拉 开始列 从0计算
//     * @param multilevelDropDownLevel       多级联动下拉  层级
//     */
//    public ExcelUtil(List<String> headersList, List<String> selectDropdownList, int singleChoiceColumnIndex,
//                     Map<String, List<String>> multilevelDropDownDataSource, int multilevelDropDownStartColumn, int multilevelDropDownLevel) {
//        this.headersList = headersList;
//        this.selectDropdownList = selectDropdownList;
//        this.singleChoiceColumnIndex = singleChoiceColumnIndex;
//        this.multilevelDropDownDataSource = multilevelDropDownDataSource;
//        this.multilevelDropDownStartColumn = multilevelDropDownStartColumn;
//        this.multilevelDropDownLevel = multilevelDropDownLevel;
//    }
//
//
//    public XSSFWorkbook exportExcel() {
//        XSSFWorkbook xssfWorkBook = new XSSFWorkbook();
//        XSSFSheet mainSheet = xssfWorkBook.createSheet(DATA_SHEET_NAME);
//        //初始化标头
//        if (CollectionUtils.isNotEmpty(headersList)) {
//            initHeaders(xssfWorkBook, mainSheet, headersList);
//        }
//        //单选框
//        if (CollectionUtils.isNotEmpty(selectDropdownList)) {
//            generateDropDownBox(xssfWorkBook, mainSheet, selectDropdownList, singleChoiceColumnIndex);
//        }
//        //多级联动
//        if (!multilevelDropDownDataSource.isEmpty()) {
//            generateMultilevelDropDownBox(xssfWorkBook, mainSheet, multilevelDropDownDataSource, multilevelDropDownStartColumn,
//                    multilevelDropDownLevel);
//        }
//        return xssfWorkBook;
//    }
//
//
//    /**
//     * 生成单层下拉框
//     *
//     * @param xssfWorkBook 工作簿
//     * @param mainSheet    主sheet
//     * @param dataList     下拉数据
//     */
//    public void generateDropDownBox(XSSFWorkbook xssfWorkBook, XSSFSheet mainSheet, List<String> dataList, int columnIndex) {
//        String sheetName = "singleChoiceSheet";
//        XSSFSheet deviceTypeSheet = xssfWorkBook.createSheet(sheetName);
//        // 设置sheet是否隐藏
//        xssfWorkBook.setSheetHidden(xssfWorkBook.getSheetIndex(deviceTypeSheet), true);
//        writeDropDownData(xssfWorkBook, deviceTypeSheet, dataList, sheetName);
//        //设置属性下拉
//        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(mainSheet);
//        DataValidationConstraint deviceTypeConstraint = dvHelper.createFormulaListConstraint(sheetName);
//        // 四个参数分别是：起始行、终止行、起始列、终止列  1 (下拉框代表从excel第1+1行开始) 10(下拉框代表从excel第1+10行结束) 5(代表第几列开始，0是第一列，1是第二列) 5(代表第几列结束，0是第一列，1是第二列)
//        CellRangeAddressList deviceTypeRangeAddressList = new CellRangeAddressList(1, MAX_ROWS, columnIndex, columnIndex);
//        XSSFDataValidation deviceTypeDataValidation = (XSSFDataValidation) dvHelper.createValidation(deviceTypeConstraint,
//                deviceTypeRangeAddressList);
//        deviceTypeDataValidation.setShowErrorBox(true);
//        deviceTypeDataValidation.createPromptBox("Error", "请选择或输入有效的选项！");
//        mainSheet.addValidationData(deviceTypeDataValidation);
//    }
//
//    /**
//     * 生成多级联动下拉框
//     *
//     * @param xssfWorkBook       工作簿
//     * @param assetSheet         主sheet
//     * @param dropDownDataSource 数据源 Map 父名称 子名称集合
//     * @param columnStep         开始列
//     * @param totalLevel         总层级
//     */
//    public static void generateMultilevelDropDownBox(XSSFWorkbook xssfWorkBook, XSSFSheet assetSheet, Map<String, List<String>> dropDownDataSource,
//                                                     int columnStep, int totalLevel) {
//        String sheetName = "multilevelSheet";
//        log.info("dropDownDataSource:{}", JSON.toJSONString(dropDownDataSource));
//        XSSFSheet dataSourceSheet = xssfWorkBook.createSheet(sheetName);
//        xssfWorkBook.setSheetHidden(xssfWorkBook.getSheetIndex(sheetName), true);
//        XSSFRow headerRow = dataSourceSheet.createRow(0);
//        String[] firstValidationArray = null;
//        boolean firstTime = true;
//        int columnIndex = 0;
//        // 构造名称管理器数据源
//        for (String key : dropDownDataSource.keySet()) {
//            Cell cell = headerRow.createCell(columnIndex);
//            cell.setCellValue(key);
//            if (dropDownDataSource.get(key) == null || dropDownDataSource.get(key).size() == 0) {
//                continue;
//            }
//            ArrayList<String> values = (ArrayList<String>) dropDownDataSource.get(key);
//            if (firstTime) {
//                firstValidationArray = values.toArray(new String[0]);
//            }
//            int dataRowIndex = 1;
//            for (String value : values) {
//                Row row = firstTime ? dataSourceSheet.createRow(dataRowIndex) : dataSourceSheet.getRow(dataRowIndex);
//                if (row == null) {
//                    row = dataSourceSheet.createRow(dataRowIndex);
//                }
//                row.createCell(columnIndex).setCellValue(value);
//                dataRowIndex++;
//            }
//            // 构造名称管理器
//            String start = convertToExcelColumn(columnIndex);
//            int startRow = 2;
//            String range = "$" + start + "$" + startRow + ":$" + start + "$" + (startRow + values.size() - 1);
//            Name name = xssfWorkBook.createName();
//            name.setNameName(key);
//            String formula = sheetName + "!" + range;
//            name.setRefersToFormula(formula);
//            columnIndex++;
//            firstTime = false;
//        }
//        // 第一级设置DataValidation
//        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(assetSheet);
//        DataValidationConstraint firstConstraint = dvHelper.createExplicitListConstraint(firstValidationArray);
//        CellRangeAddressList firstRangeAddressList = new CellRangeAddressList(1, MAX_ROWS, columnStep, columnStep);
//        DataValidation firstDataValidation = dvHelper.createValidation(firstConstraint, firstRangeAddressList);
//        firstDataValidation.setShowErrorBox(true);
//        firstDataValidation.createPromptBox("Error", "请选择有效的选项！");
//        assetSheet.addValidationData(firstDataValidation);
//        // 剩下的层级设置DataValidation
//        for (int i = 1; i < totalLevel; i++) {
//            char[] offset = new char[1];
//            offset[0] = (char) ('A' + columnStep + i - 1);
//            int rowNum = 2;
//            String formulaString = "INDIRECT($" + new String(offset) + (rowNum) + ")";
//            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(formulaString);
//            CellRangeAddressList regions = new CellRangeAddressList(1, MAX_ROWS, columnStep + i, columnStep + i);
//            XSSFDataValidation dataValidationList = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
//            dataValidationList.setShowErrorBox(true);
//            dataValidationList.createPromptBox("Error", "请选择有效的选项！");
//            assetSheet.addValidationData(dataValidationList);
//        }
//    }
//
//    /**
//     * 数据列转为excel列名
//     *
//     * @param num 列序号
//     * @return excel列名
//     */
//    public static String convertToExcelColumn(int num) {
//        int[] buf = new int[8];
//        int temp = num, pos = 7;
//        do {
//            buf[pos--] = num % 26 + 65;
//            num = num / 26;
//        } while (num != 0);
//        if (temp > 25) {
//            buf[pos + 1] = buf[pos + 1] - 1;
//        }
//        return new String(buf, pos + 1, (7 - pos));
//    }
//
//    /**
//     * 初始化标头
//     *
//     * @param xssfWorkbook 工作簿
//     * @param mainSheet    sheet
//     * @param headers      标头数据
//     */
//    private void initHeaders(XSSFWorkbook xssfWorkbook, XSSFSheet mainSheet, List<String> headers) {
//        //表头样式
//        XSSFCellStyle style = xssfWorkbook.createCellStyle();
//        // 创建一个居中格式
//        style.setAlignment(HorizontalAlignment.CENTER);
//        //字体样式
//        XSSFFont fontStyle = xssfWorkbook.createFont();
//        fontStyle.setFontName("微软雅黑");
//        fontStyle.setFontHeightInPoints((short) 12);
//        fontStyle.setBold(true);
//        style.setFont(fontStyle);
//        //生成主内容
//        //第一个sheet的第一行为标题
//        XSSFRow rowFirst = mainSheet.createRow(0);
//        //冻结第一行
//        mainSheet.createFreezePane(0, 1, 0, 1);
//        //写标题
//        for (int i = 0; i < headers.size(); i++) {
//            //获取第一行的每个单元格
//            XSSFCell cell = rowFirst.createCell(i);
//            //设置每列的列宽
//            mainSheet.setColumnWidth(i, 4000);
//            //加样式
//            cell.setCellStyle(style);
//            //往单元格里写数据
//            cell.setCellValue(headers.get(i));
//        }
//    }
//
//    /**
//     * 循环单个下拉框的数据写入sheet的第A列中
//     *
//     * @param xssfWorkBook 工作簿
//     * @param sheet        主sheet
//     * @param list         数据源
//     * @param name         sheet名称
//     */
//    private void writeDropDownData(XSSFWorkbook xssfWorkBook, XSSFSheet sheet, List<String> list, String name) {
//        //循环单个下拉框的数据写入sheet的第A列中
//        for (int i = 0; i < list.size(); i++) {
//            XSSFRow genderRow = sheet.createRow(i);
//            genderRow.createCell(0).setCellValue(list.get(i));
//        }
//        // 创建数据规则
//        Name genderName = xssfWorkBook.createName();
//        genderName.setNameName(name);
//        genderName.setRefersToFormula(sheet.getSheetName() + "!$A$1:$A$" + list.size());
//    }
//
//    /**
//     * 输出到硬盘
//     *
//     * @param filePath     路径
//     * @param xssfWorkBook 工作簿
//     */
//    public void writeToFile(String filePath, XSSFWorkbook xssfWorkBook) {
//        FileOutputStream os = null;
//        try {
//            String existName = filePath.substring(0, filePath.lastIndexOf("/"));
//            File f = new File(existName);
//            if (!f.exists()) {
//                f.mkdirs();
//            }
//            // 创建可写入的Excel工作簿
//            File file = new File(filePath);
//            if (!file.exists()) {
//                file.createNewFile();
//            } else {
//                file.delete();
//                file.createNewFile();
//            }
//            os = new FileOutputStream(filePath);
//            xssfWorkBook.write(os);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            IOUtils.closeQuietly(os);
//        }
//    }
//
//    /**
//     * excel 导入 读取数据
//     *
//     * @param inputStream 文件输入流
//     * @param beanType    实体类
//     * @param <T>         实体类泛型
//     * @return 实体类列表
//     * @throws Exception io 异常
//     */
//    public <T> List<T> importExcel(InputStream inputStream, Class<T> beanType) throws Exception {
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = workbook.getSheet(DATA_SHEET_NAME);
//        List<T> result = new ArrayList<>();
//        for (Row row : sheet) {
//            if (row.getRowNum() == 0) {
//                continue;
//            }
//            T beanClass = beanType.newInstance();
//            Field[] fields = beanType.getDeclaredFields();
//            for (int i = 0; i < fields.length; i++) {
//                Field field = fields[i];
//                if (field.getName().equals("rowNum")) {
//                    Method method = beanType.getDeclaredMethod("setRowNum", Integer.class);
//                    method.invoke(beanClass, row.getRowNum());
//                    break;
//                }
//                Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                if (cell != null) {
//                    DataFormatter dataFormatter = new DataFormatter();
//                    String stringCellValue = dataFormatter.formatCellValue(cell);
//                    String filedName = field.getName();
//                    String methodName = "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
//                    Method method = beanType.getDeclaredMethod(methodName, String.class);
//                    method.invoke(beanClass, stringCellValue);
//                    field.setAccessible(true);
//                }
//            }
//            result.add(beanClass);
//        }
//        return result;
//    }
//
//}