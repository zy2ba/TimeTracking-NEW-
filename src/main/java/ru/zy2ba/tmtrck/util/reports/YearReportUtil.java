package ru.zy2ba.tmtrck.util.reports;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.Pair;
import ru.zy2ba.tmtrck.entity.PairDate;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.PairType;
import ru.zy2ba.tmtrck.manager.PairDateManager;
import ru.zy2ba.tmtrck.manager.PairManager;
import ru.zy2ba.tmtrck.manager.PrepodManager;
import ru.zy2ba.tmtrck.util.ResourceLocator;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * данный класс отвечает за создание годового отчёта
 * @author Zy2ba
 *
 * Created by Zy2ba on 17.05.2015.
 */
public class YearReportUtil {
    private FileOutputStream fileOutputStream;

    public void invoke(Component comp, LocalDate startDate, LocalDate finishDate) {
        Workbook wb = new HSSFWorkbook();

        Sheet sheet = wb.createSheet(WorkbookUtil.createSafeSheetName("Лист 1"));

        Row row = sheet.createRow((short) 0);
        short i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue("Фамилия И.О.");
        i++;
        cell = row.createCell(i);
        cell.setCellValue("Всего");
        i += 2;
        cell = row.createCell(i);
        cell.setCellValue("Лекций");
        i += 2;
        cell = row.createCell(i);
        cell.setCellValue("Практика");
        i += 2;
        cell = row.createCell(i);
        cell.setCellValue("Лаб.раб");
        i += 2;
        cell = row.createCell(i);
        cell.setCellValue("Курс.пр.");
        i += 2;
        cell = row.createCell(i);
        cell.setCellValue("Дипл.пр.");
        i += 2;
        cell = row.createCell(i);
        cell.setCellValue("Прочая");
        i += 2;
        cell = row.createCell(i);
        cell.setCellValue("Примеч");
        row = sheet.createRow(1);
        for (i = 1; i < 8; i++) {
            cell = row.createCell(i * 2 - 1);
            cell.setCellValue("План");

            cell = row.createCell(i * 2);
            cell.setCellValue("Факт");
        }


        ArrayList<PairDate> pairDates = new ArrayList<>();
        PairDateManager dateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");

        for (LocalDate d = startDate; !d.equals(finishDate.plusDays(1)); d = d.plusDays(1)) {
            pairDates.add(dateManager.findByDate(d));
        }
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");

        java.util.List<Prepod> prepods = prepodManager.getAll();
        prepods.clear();
        prepods.add(prepodManager.findByNameAndLastName("Мария", "Долженкова"));
        prepods.add(prepodManager.findByNameAndLastName("Геннадий", "Чистяков"));
        i = 2;

        PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
        for (Prepod prepod : prepods) {//,перебираем преподов
            row = sheet.createRow(i);//строка для взятого препода
            cell = row.createCell(0);//ячейка для Имя Ф.О.
            cell.setCellValue(prepod.getLastName() + " " + prepod.getName().substring(0, 1) + "." + prepod.getMiddleName().substring(0, 1) + ".");             ///её значение
            ArrayList<Pair> prepodPairs = new ArrayList<>();//под доты в которые у этого препода есть пары
            for (PairDate date : pairDates) {//даты по которым отчёт составляется
                ArrayList<Pair> prepodPairsOnDate = pairManager.findCarriedPairsForPrepodByDate(prepod, date);//получем проведённые пары за дату
                for (Pair pair : prepodPairsOnDate) {//записываем в общий список под проведённые пары за весь отчётный период
                    if (!pair.getPairDate().getHoliday()) {
                        prepodPairs.add(pair);
                    }
                }
            }
            int lectionHours = 0;
            int practiceHours = 0;
            int labHours = 0;
            int otherHours = 0;
            for (Pair pair : prepodPairs) {
                if (pair.getPairName().getPairType() == PairType.lab) {
                    labHours++;
                } else if (pair.getPairName().getPairType() == PairType.practice) {
                    practiceHours++;
                } else if (pair.getPairName().getPairType() == PairType.lecture) {
                    lectionHours++;
                } else otherHours++;
            }
            cell = row.createCell(2);
            cell.setCellValue(prepodPairs.size());
            cell = row.createCell(4);
            cell.setCellValue(lectionHours);
            cell = row.createCell(6);
            cell.setCellValue(practiceHours);
            cell = row.createCell(8);
            cell.setCellValue(labHours);
            i++;
        }

        int p = 0;
        int l = 0;
        for (Row row1 = sheet.getRow(p); p > sheet.getLastRowNum(); row1 = sheet.getRow(p)) {
            l = 0;
            for (Cell cell1 = row.getCell(l); l > row.getLastCellNum(); cell1 = row.getCell(l)) {
                CellUtil.setAlignment(cell1, wb, CellStyle.ALIGN_CENTER);
                l++;
            }
            p++;
        }

        sheet.autoSizeColumn(0);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row
                1, //last row
                0, //first column
                0  //last column
        ));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 10));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 12));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 14));

        try {
            JFileChooser saveFile = new JFileChooser();
            if (saveFile.showSaveDialog(comp) == JFileChooser.APPROVE_OPTION) {
                fileOutputStream = new FileOutputStream(saveFile.getSelectedFile());
                wb.write(fileOutputStream);
                fileOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(comp, "Выполнено");
    }
}
