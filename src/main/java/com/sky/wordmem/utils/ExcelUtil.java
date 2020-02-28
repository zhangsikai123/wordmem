package com.sky.wordmem.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sikaizhang@xiaohongshu.com
 * @date 2/23/20
 * @description com.sky.wordmem.utils
 */
@Slf4j
public class ExcelUtil {
    public static void writeFile(List<JSONObject> list, OutputStream outputStream) {
        try {
            Map<String, Integer> nameDict = new HashMap<>();
            HSSFWorkbook workbook = new HSSFWorkbook();
            for (JSONObject jo : list) {
                String sheetName = jo.getString("name");
                List<List<String>> sheetData = (List<List<String>>) jo.get("data");
                int cnt = nameDict.getOrDefault(sheetName, 0);
                if (cnt > 0) {
                    nameDict.put(sheetName, cnt + 1);
                    sheetName = String.format("%s%s)", sheetName, cnt);
                } else {
                    nameDict.put(sheetName, 1);
                }
                HSSFSheet sheet = workbook.createSheet(sheetName);
                writeSheet(sheet, sheetData);
            }
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            log.error("fail to export excel", e);
            throw new RuntimeException(e.toString());
        }

    }

    private static void writeSheet(HSSFSheet sheet, List<List<String>> sheetData) {
        int row = 0;
        for (List<String> line : sheetData) {
            int column = 0;
            HSSFRow rowData = sheet.createRow(row);
            for (String cellstr : line) {
                HSSFCell cell = rowData.createCell(column);
                cell.setCellValue(cellstr);
                column++;
            }
            row++;
        }
    }

    public static List<List<String>> readExcelFromFile(InputStream fileInputFile, int sheetNo) {
        Workbook wb = null;
        List<List<String>> sheetData = new ArrayList<>();

        try {
            // 创建文档
            // wb = new HSSFWorkbook(fileInputFile); // xsl
            wb = new XSSFWorkbook(fileInputFile); // xsxl

            DataFormatter objDefaultFormat = new DataFormatter();
            FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wb);

            // 读取sheet(页)
            Sheet sheet = wb.getSheetAt(sheetNo);
            if (sheet != null) {
                int totalRows = sheet.getLastRowNum();
                // 读取Row
                for (int rowNum = 0; rowNum <= totalRows; rowNum++) {
                    List<String> rowData = new ArrayList<>();
                    Row row = sheet.getRow(rowNum);
                    if (row != null) {
                        // 读取列，从第0列开始
                        for (int c = 0; c <= row.getLastCellNum() + 1; c++) {
                            String cellData = null;
                            Cell cell = row.getCell(c);
                            if (cell != null) {
                                // if (CellType.STRING.equals(cell.getCellType())) {
                                //     cellData = cell.getStringCellValue();
                                // } else if (CellType.NUMERIC.equals(cell.getCellType())) {
                                //     cellData = cell.getNumericCellValue() + "";
                                // } else {
                                //     cellData = cell.toString();

                                // }
                                // cell.setCellType(CellType.STRING);
                                // cell.getStringCellValue();

                                objFormulaEvaluator.evaluate(cell);
                                cellData = objDefaultFormat.formatCellValue(cell, objFormulaEvaluator);
                                cellData = cellData == "" ? null : cellData; // 替换空字符串
                            }
                            rowData.add(cellData);
                        }
                    }
                    // 检测是不是空行
                    if (CommonUtil.any(rowData)) {
                        sheetData.add(rowData);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sheetData;

    }

    public static List<Map<String, Object>> parseExcelToMapList(InputStream fileInputFile, int sheetNo, SimpleExcelParserSchema schema) {
        ExcelParseHelper helper = new ExcelParseHelper(schema);
        return helper.parseSheetData(
                readExcelFromFile(fileInputFile, sheetNo)
        );
    }

    private static class ExcelParseHelper {

        public SimpleExcelParserSchema schema;
        // public Boolean keepNone;

        public ExcelParseHelper(SimpleExcelParserSchema schema) {
            this.schema = schema;
            // this.keepNone = keepNone == true ? true : false;
        }

        public List<Map<String, Object>> parseSheetData(List<List<String>> sheetData) {
            List<Map<String, Object>> ret = new ArrayList<>();
            if (sheetData.size() < 2) {
                return ret;
            }
            List<String> header = sheetData.get(0).stream().map(cell -> cell == null ? null : cell.toString()).collect(Collectors.toList()); // header应为String的list
            sheetData = sheetData.subList(1, sheetData.size());

            schema.buildKeyRouter(header);

            int emptyCount = 0;
            for (int i = 0; i < sheetData.size(); i++) {
                List<String> row = sheetData.get(i);
                // 若连续空行超过十行，默认为结束，防止误操作造成上百万空行，导致超时
                if (row.size() == 0 || CollectionUtils.isEmpty(row)) {
                    emptyCount += 1;
                    if (emptyCount >= 10) break;
                    continue;
                } else {
                    emptyCount = 0;
                }

                ret.add(
                        parseRow(row)
                );
            }

            return ret;
        }

        public Map<String, Object> parseRow(List<String> row) {
            Map<String, Object> ret = new HashMap<>();
            int index = 0;
            for (String cell : row) {
                if (!Strings.isEmpty(cell)) {
                    ColumnSchema columnSchema = schema.getColumnSchemaByIndex(index);
                    String key = columnSchema.getKey();
                    // Object value = columnSchema.getType()()
                    Object v = ret.get(key);
                    if (v != null) {
                        if (v instanceof List) {
                            List<String> li = (List<String>) v;
                            li.add(cell);
                            ret.put(key, li);
                        } else {
                            List<String> li = new ArrayList<>();
                            li.add((String) v);
                            li.add(cell);
                            ret.put(key, li);
                        }

                    } else {
                        ret.put(key, cell);
                    }
                }

                index++;
            }
            return ret;
        }


    }


    public static class SimpleExcelParserSchema {

        final ColumnSchema undefinedColumnSchema = new ColumnSchema("__unrecognized");

        private Map<String, ColumnSchema> map;
        private Map<Integer, ColumnSchema> keyRouter;

        public SimpleExcelParserSchema() {
            map = new HashMap<>();
            keyRouter = new HashMap<>();
        }

        public ColumnSchema getColumnSchemaByTitle(String title) {
            return map.get(title);
        }

        public ColumnSchema getColumnSchemaByIndex(Integer index) {
            return index < keyRouter.size() ? keyRouter.get(index) : keyRouter.get(keyRouter.size() - 1);
        }

        public void setColumnSchema(String title, String key) {
            map.put(title, new ColumnSchema(key));
        }

        public void buildKeyRouter(List<String> headerRow) {
            if (CollectionUtils.isEmpty(headerRow)) {
                return;
            }
            if (Strings.isEmpty(headerRow.get(0))) {
                return;
            }
            ColumnSchema prevColumnSchema = null;
            int index = 0;
            for (String cell : headerRow) {
                ColumnSchema currentColumnSchema = map.get(cell);
                if (currentColumnSchema == null) {
                    if (!Strings.isEmpty(cell)) {
                        // header无对应schema，用默认schema
                        keyRouter.put(index, undefinedColumnSchema);
                        prevColumnSchema = undefinedColumnSchema;
                    } else {
                        // 无header，用上一个schema
                        keyRouter.put(index, prevColumnSchema);
                    }
                } else {
                    // 存在header对应的schema
                    keyRouter.put(index, currentColumnSchema);
                    prevColumnSchema = currentColumnSchema;
                }

                index += 1;
            }
        }

        public int getHeaderLength() {
            return keyRouter.keySet().size();
        }


    }

    @Data
    public static class ColumnSchema {
        private String key;

        // private Class type;
        public ColumnSchema(String key) {
            setKey(key);
            // setType(type); // TODO AUTOTYPE
        }
    }


}