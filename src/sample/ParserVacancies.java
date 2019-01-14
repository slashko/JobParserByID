package sample;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ParserVacancies {

    public ParserVacancies(ArrayList<Vacancy> vacanciesList) {
        parserStart(vacanciesList);
    }

    public void parserStart(ArrayList<Vacancy> vacanciesList) {
        InputStream inputStream = null;
        OutputStream outputStream;
        XSSFWorkbook workBook = null;

        try {
            inputStream = new FileInputStream("ReportHH.xlsx");
            workBook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = workBook.getSheetAt(0);
        int rownum = 0;
        Row row;
        Cell cell;

        for (Vacancy vacancy : vacanciesList) {
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellFormula("VLOOKUP(H" + (rownum + 1) + ",'Для ВПР'!$A$2:$B$352,2,0)");
            cell = row.createCell(1);
            cell.setCellFormula("VLOOKUP(A" + (rownum + 1) + ",'Для ВПР'!$B$2:$D$352,3,0)");
            cell = row.createCell(2);
            cell.setCellFormula("VLOOKUP(B" + (rownum + 1) + ",'Для ВПР'!$D$2:$E$352,2,0)");
            cell = row.createCell(3);
            cell.setCellFormula("VLOOKUP(E" + (rownum + 1) + ",'Для ВПР'!$F$2:$G$352,2,0)");
            cell = row.createCell(4);
            cell.setCellFormula("VLOOKUP(C" + (rownum + 1) + ",'Для ВПР'!$E$2:$F$352,2,0)");
            cell = row.createCell(5);
            cell.setCellFormula("VLOOKUP(G" + (rownum + 1) + ",'Метро по адресам'!$B$2:$C$202,2,0)");
            cell = row.createCell(6);
            cell.setCellFormula("VLOOKUP(X" + (rownum + 1) + ",'Метро по адресам'!$A$2:$B$202,2,0)");
            //ЮРЛ организации
            cell = row.createCell(7, CellType.NUMERIC);
            String str = vacancy.getURLOrganization().replaceAll("\\s*(https://hh.ru/employer/)\\s*", "");
            str = str.replaceAll("\\s*(https://career.ru/employer/)\\s*", "");
            cell.setCellValue(Integer.parseInt(str));
            //Вакансия
            cell = row.createCell(8);
            cell.setCellValue(vacancy.getName());
            //Зарплата от
            if (vacancy.getSalaryFrom() != null) {
                cell = row.createCell(9);
                cell.setCellValue(vacancy.getSalaryFrom());
            }
            //Зарплата до
            if (vacancy.getSalaryTo() != null) {
                cell = row.createCell(10);
                cell.setCellValue(vacancy.getSalaryTo());
            }
            //За вычетом налога или нет
            if (vacancy.getGross() != null) {
                if (vacancy.getGross()) {
                    cell = row.createCell(11);
                    cell.setCellValue("до вычетов налогов");
                } else {
                    cell = row.createCell(11);
                    cell.setCellValue("на руки");
                }
            }
            //График работы
            cell = row.createCell(12);
            cell.setCellValue(vacancy.getSchedule());
            //Тип занятости
            cell = row.createCell(13);
            cell.setCellValue(vacancy.getEmployment());
            //Опыт работы
            cell = row.createCell(14);
            cell.setCellValue(vacancy.getExperience());
            //Ключевые навыки
            if (vacancy.getKey_skills() != null) {
                str = "";
                for (String s : vacancy.getKey_skills()) {
                    str += s + "; ";
                }
                cell = row.createCell(15);
                cell.setCellValue(str);
            }
            //Специализация
            if (vacancy.getSpecializations() != null) {
                str = "";
                for (String s : vacancy.getSpecializations()) {
                    str += s + "; ";
                }
                cell = row.createCell(16);
                cell.setCellValue(str);
            }
            //Обязаности
            cell = row.createCell(17);
            cell.setCellValue(vacancy.getDescription());
            //Контакты
            cell = row.createCell(20);
            cell.setCellValue(vacancy.getEmail());
            //сайт
            //Контакты
            cell = row.createCell(21);
            cell.setCellValue("https://hh.ru/");
            //Наименование организации
            cell = row.createCell(22);
            cell.setCellValue(vacancy.getOrganization());
            //Город
            cell = row.createCell(23);

            str = vacancy.getCity() + ", " + vacancy.getStreet() + ", " + vacancy.getBuilding();
            if (str.equals(", , ")) {
                if (!(str.equals("деревня Сватово") || str.endsWith("г. Тюмень") || str.equals("Санкт-Петербург") || str.equals("село Остров"))) {
                    cell.setCellFormula("VLOOKUP(A" + (rownum + 1) + ",'Для ВПР'!$B$2:$H$352,7,0)");
                }
            } else {
                if (str != null && str.length() > 0 && str.charAt(str.length() - 2) == ',') {
                    str = str.substring(0, str.length() - 2);
                    if (str != null && str.length() > 0 && str.charAt(str.length() - 2) == ',') {
                        str = str.substring(0, str.length() - 2);
                    }
                }
                cell.setCellValue(str);
            }
            //Широта
            if (vacancy.getLat() != 0) {
                cell = row.createCell(24, CellType.NUMERIC);
                cell.setCellValue(vacancy.getLat());
            }
            //Долгота
            if (vacancy.getLon() != 0) {
                cell = row.createCell(25, CellType.NUMERIC);
                cell.setCellValue(vacancy.getLon());
            }

            cell = row.createCell(26);
            cell.setCellFormula("VLOOKUP(F" + (rownum + 1) + ",'Метро по адресам'!$C$2:$D$202,2,0)");
        }

        try {
            outputStream = new FileOutputStream(System.getProperty("user.home") + "\\Desktop\\ReportHH_" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + ".xlsx");
            workBook.write(outputStream);
            workBook.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
