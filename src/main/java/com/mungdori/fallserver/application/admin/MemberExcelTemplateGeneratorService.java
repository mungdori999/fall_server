package com.mungdori.fallserver.application.admin;

import com.mungdori.fallserver.application.admin.provided.MemberExcelTemplateGenerator;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class MemberExcelTemplateGeneratorService implements MemberExcelTemplateGenerator {
    @Override
    public byte[] generate() {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("참가자 목록");
            var header = sheet.createRow(0);
            var headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {"이름", "성별", "코드"};
            for (int index = 0; index < headers.length; index++) {
                var cell = header.createCell(index);
                cell.setCellValue(headers[index]);
                cell.setCellStyle(headerStyle);
            }

            addExampleRow(sheet, 1, "예시 남성", "남성", "000001");
            addExampleRow(sheet, 2, "예시 여성", "여성", "000002");
            sheet.setColumnWidth(0, 18 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 14 * 256);

            workbook.write(output);
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("예시 엑셀 파일을 만들 수 없습니다.", exception);
        }
    }

    private void addExampleRow(org.apache.poi.ss.usermodel.Sheet sheet, int rowIndex, String name, String gender, String code) {
        var row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(gender);
        var codeCell = row.createCell(2);
        codeCell.setCellValue(code);
        CellStyle codeStyle = sheet.getWorkbook().createCellStyle();
        codeStyle.setDataFormat((short) 49); // Excel 텍스트 형식: 앞자리 0 보존
        codeCell.setCellStyle(codeStyle);
    }
}
