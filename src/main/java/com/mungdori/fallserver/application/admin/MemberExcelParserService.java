package com.mungdori.fallserver.application.admin;

import com.mungdori.fallserver.application.admin.provided.MemberExcelParser;
import com.mungdori.fallserver.domain.member.Gender;
import com.mungdori.fallserver.domain.member.MemberRegisterRequest;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class MemberExcelParserService implements MemberExcelParser {
    private static final Set<String> NAME_HEADERS = Set.of("이름", "name");
    private static final Set<String> GENDER_HEADERS = Set.of("성별", "gender");
    private static final Set<String> CODE_HEADERS = Set.of("코드", "code");

    @Override
    public List<MemberRegisterRequest> parse(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;
            if (sheet == null || sheet.getPhysicalNumberOfRows() < 2) {
                throw new IllegalArgumentException("헤더와 최소 한 명의 참가자 정보가 필요합니다.");
            }

            DataFormatter formatter = new DataFormatter();
            Map<String, Integer> columns = headerColumns(sheet.getRow(sheet.getFirstRowNum()), formatter);
            List<MemberRegisterRequest> requests = new ArrayList<>();

            for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isBlank(row, columns, formatter)) continue;

                int displayRow = rowIndex + 1;
                String name = value(row, columns.get("name"), formatter);
                String genderValue = value(row, columns.get("gender"), formatter);
                String code = value(row, columns.get("code"), formatter);

                if (name.isBlank()) throw new IllegalArgumentException(displayRow + "행의 이름이 비어 있습니다.");
                if (!code.matches("\\d{6}")) throw new IllegalArgumentException(displayRow + "행의 코드는 숫자 6자리여야 합니다.");
                requests.add(new MemberRegisterRequest(name, toGender(genderValue, displayRow), code));
            }

            if (requests.isEmpty()) throw new IllegalArgumentException("등록할 참가자 정보가 없습니다.");
            return requests;
        } catch (IOException exception) {
            throw new IllegalArgumentException("엑셀 파일을 읽을 수 없습니다.", exception);
        }
    }

    private Map<String, Integer> headerColumns(Row header, DataFormatter formatter) {
        if (header == null) throw new IllegalArgumentException("첫 번째 행에 헤더가 필요합니다.");

        Integer name = null;
        Integer gender = null;
        Integer code = null;
        for (var cell : header) {
            String value = formatter.formatCellValue(cell).trim().toLowerCase(Locale.ROOT);
            if (NAME_HEADERS.contains(value)) name = cell.getColumnIndex();
            if (GENDER_HEADERS.contains(value)) gender = cell.getColumnIndex();
            if (CODE_HEADERS.contains(value)) code = cell.getColumnIndex();
        }
        if (name == null || gender == null || code == null) {
            throw new IllegalArgumentException("첫 행에 이름, 성별, 코드 헤더가 필요합니다.");
        }
        return Map.of("name", name, "gender", gender, "code", code);
    }

    private boolean isBlank(Row row, Map<String, Integer> columns, DataFormatter formatter) {
        return columns.values().stream().allMatch(column -> value(row, column, formatter).isBlank());
    }

    private String value(Row row, int column, DataFormatter formatter) {
        return row.getCell(column) == null ? "" : formatter.formatCellValue(row.getCell(column)).trim();
    }

    private Gender toGender(String value, int row) {
        return switch (value.trim().toUpperCase(Locale.ROOT)) {
            case "MALE", "남성" -> Gender.MALE;
            case "FEMALE", "여성" -> Gender.FEMALE;
            default -> throw new IllegalArgumentException(row + "행의 성별은 남성 또는 여성으로 입력해주세요.");
        };
    }
}
