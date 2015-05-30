package ru.zy2ba.tmtrck.util.reports;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.ActivityTypes.KursRab;
import ru.zy2ba.tmtrck.entity.Pair;
import ru.zy2ba.tmtrck.entity.PairDate;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.PairType;
import ru.zy2ba.tmtrck.manager.*;
import ru.zy2ba.tmtrck.util.ResourceLocator;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Zy2ba on 20.05.2015.
 */
class CustomReportUtil {
    private final File file;
    private final LocalDate startDate;
    private final LocalDate finishDate;
    private final String kafedra;

    public CustomReportUtil(File filein, LocalDate dateStart, LocalDate dateFinish, String faculty){
        file = filein;
        this.startDate = dateStart;
        this.finishDate = dateFinish;
        this.kafedra = faculty;
    }
    public void makeReport() throws IOException, InvalidFormatException {

        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        ArrayList<Prepod> prepods = prepodManager.findByFaculty(kafedra);
        PairDateManager pairDateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");
        ArrayList<PairDate> pairDates = pairDateManager.findByDate(startDate,finishDate);

        FileInputStream fileInputStream = new FileInputStream(file);

        Workbook wb = WorkbookFactory.create(fileInputStream);
        Sheet sh = wb.getSheet("ФБш");
        if(sh==null) throw new IOException();
        Iterator<Row> rowIterator = sh.rowIterator();
        Iterator<Cell> cellIterator;
        Row row;
        Cell cell;
        while (rowIterator.hasNext()){
            row = rowIterator.next();
            cellIterator = row.cellIterator();
            if (cellIterator.hasNext()){
            cell = cellIterator.next();
                if (cell.getCellType()==Cell.CELL_TYPE_STRING){
                    if (cell.getStringCellValue().contains("N")) break;
                }
            }

        }
        ConsultManager consultManager = (ConsultManager) ResourceLocator.getBean("consultManager");
        ExamManager examManager = (ExamManager) ResourceLocator.getBean("examManager");
        KursProjectManager kursProjectManager = (KursProjectManager) ResourceLocator.getBean("kursProjectManager");
        KursRabManager kursRabManager = (KursRabManager) ResourceLocator.getBean("kursRabManager");
        DiplomaProjectManager diplomaProjectManager = (DiplomaProjectManager) ResourceLocator.getBean("diplomaProjectManager");
        PracticeManager practiceManager = (PracticeManager) ResourceLocator.getBean("practiceManager");




        PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
        for(Prepod prepod:prepods){
            if (rowIterator.hasNext()){
                ArrayList<Pair> pairs = new ArrayList<>();
                ArrayList<Pair> pairsOnDates = pairManager.findDatesPrepod(prepod,pairDates);
                for(Pair pair:pairsOnDates){
                    if(!pair.getPairDate().getHoliday() && pair.getIsCarriedOut()){
                        pairs.add(pair);
                    }
                }
                int lectionHours = 0;
                int practiceHours = 0;
                int labHours = 0;
                int otherHours = 0;
                int examsHours = examManager.getByPrepodAndDate(prepod,startDate,finishDate).size();
                int consultsHours = consultManager.getByPrepodAndDate(prepod,startDate,finishDate).size();
                int kursProjectsHours = kursProjectManager.getByPrepodAndDate(prepod,startDate,finishDate).size();
                int kursRabsHours = kursRabManager.getByPrepodAndDate(prepod,startDate,finishDate).size();
                int diplomasHours = diplomaProjectManager.getByPrepodAndDate(prepod,startDate,finishDate).size();
                int practicesHours = practiceManager.getByPrepodAndDate(prepod,startDate,finishDate).size();
                for (Pair pair : pairs) {
                    if (pair.getPairName().getPairType() == PairType.lab) {
                        labHours++;
                    } else if (pair.getPairName().getPairType() == PairType.practice) {
                        practiceHours++;
                    } else if (pair.getPairName().getPairType() == PairType.lecture) {
                        lectionHours++;
                    } else otherHours++;
                }
                row = rowIterator.next();
                cellIterator = row.cellIterator();
                cellIterator.next();
                cell = cellIterator.next();
                cell.setCellValue(prepod.getLastName() + " " + prepod.getName() + " " + prepod.getMiddleName());
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();
                cell = cellIterator.next();
                cell.setCellValue(lectionHours > 0.1 ? String.format("%.2g", (double) lectionHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(labHours > 0.1 ? String.format("%.2g", (double) labHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(practiceHours > 0.1 ? String.format("%.2g", (double) practiceHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(consultsHours > 0.1 ? String.format("%.2g", (double) consultsHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(examsHours > 0.1 ? String.format("%.2g", (double) examsHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(" ");
                cell = cellIterator.next();
                cell.setCellValue(" ");
                cell = cellIterator.next();
                cell.setCellValue(kursRabsHours > 0.1 ? String.format("%.2g", (double) kursRabsHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(kursProjectsHours > 0.1 ? String.format("%.2g", (double) kursProjectsHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(diplomasHours > 0.1 ? String.format("%.2g", (double) diplomasHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(" ");
                cell = cellIterator.next();
                cell.setCellValue(practiceHours > 0.1 ? String.format("%.2g", (double) practiceHours) : (" "));
            }
        }


        FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 5) + "_returned.xlsm");
        wb.write(fileOutputStream);
        fileOutputStream.close();
        fileInputStream.close();
    }
}
