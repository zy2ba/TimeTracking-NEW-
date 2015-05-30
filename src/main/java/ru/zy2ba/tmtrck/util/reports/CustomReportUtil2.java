package ru.zy2ba.tmtrck.util.reports;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.*;
import ru.zy2ba.tmtrck.entity.ActivityTypes.*;
import ru.zy2ba.tmtrck.entity.enums.PairType;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.exception.WrongDatesException;
import ru.zy2ba.tmtrck.manager.*;
import ru.zy2ba.tmtrck.util.ResourceLocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Zy2ba on 29.05.2015.
 */
class CustomReportUtil2 {
    private FileOutputStream fileOutputStream;
    private final File file;
    private final LocalDate startDate;
    private final LocalDate finishDate;
    private final String kafedra;
    private int startYear;

    public CustomReportUtil2(File filein, LocalDate dateStart, LocalDate dateFinish, String faculty) {
        file = filein;
        this.startDate = dateStart;
        this.finishDate = dateFinish;
        this.kafedra = faculty;
    }

    public void makeReport() throws IOException, InvalidFormatException, WrongDatesException {
        AutumnSpringSpacerManager autumnSpringSpacerManager = (AutumnSpringSpacerManager) ResourceLocator.getBean("autumnSpringSpacerManager");
        AutumnSpringSpacer autumnSpringSpacer = autumnSpringSpacerManager.getSpacerForDate(startDate);
        AutumnSpringSpacer autumnSpringSpacer2 = autumnSpringSpacerManager.getSpacerForDate(finishDate);
        if(autumnSpringSpacer.getSpacerDate()!=autumnSpringSpacer2.getSpacerDate())throw new WrongDatesException();

        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        ArrayList<Prepod> prepods = prepodManager.findByFaculty(kafedra);
        PairDateManager pairDateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");
        ArrayList<PairDate> pairDates = pairDateManager.findByDate(startDate, finishDate);

        FileInputStream fileInputStream = new FileInputStream(file);

        Workbook wb = WorkbookFactory.create(fileInputStream);
        Sheet sh = wb.getSheet("ФБш");
        if (sh == null) throw new IOException();
        Iterator<Row> rowIterator = sh.rowIterator();
        Iterator<Cell> cellIterator;
        Row row;
        Cell cell;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            cellIterator = row.cellIterator();
            if (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getStringCellValue().contains("N")) break;
                }
            }

        }
        ConsultManager consultManager = (ConsultManager) ResourceLocator.getBean("consultManager");
        ExamManager examManager = (ExamManager) ResourceLocator.getBean("examManager");
        ZachetManager zachetManager = (ZachetManager) ResourceLocator.getBean("zachetManager");
        KursRabManager kursRabManager = (KursRabManager) ResourceLocator.getBean("kursRabManager");
        KursProjectManager kursProjectManager = (KursProjectManager) ResourceLocator.getBean("kursProjectManager");
        DiplomaProjectManager diplomaProjectManager = (DiplomaProjectManager) ResourceLocator.getBean("diplomaProjectManager");
        GAKManager gakManager = (GAKManager) ResourceLocator.getBean("GAKManager");
        PracticeManager practiceManager = (PracticeManager) ResourceLocator.getBean("practiceManager");


        PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
        for (Prepod prepod : prepods) {
            PlanManager planManager = (PlanManager) ResourceLocator.getBean("planManager");
            Plan plan = planManager.getByPrepodAndStartYear(prepod, autumnSpringSpacer.getStartYear());
            PlanTable planTable;
            if (autumnSpringSpacer.getSpacerDate().isAfter(finishDate)){
                planTable = plan.getPlanTableAutumnBudget();
            }else planTable = plan.getPlanTableSpringBudget();
            if (rowIterator.hasNext()) {
                ArrayList<Pair> pairs = new ArrayList<>();

                Collections.sort(pairDates, new Comparator<PairDate>() {
                    @Override
                    public int compare(PairDate o1, PairDate o2) {
                        if (o1.getDate().isAfter(o2.getDate())){
                            return 1;
                        }else
                        if (o2.getDate().isAfter(o1.getDate())){
                            return -1;
                        }else
                        return 0;
                    }
                });

                for(PairDate pairDate:pairDates){
                    if(!pairDate.getHoliday()){

                    }
                }



                ArrayList<Pair> pairsOnDates = pairManager.findDatesPrepod(prepod, pairDates);
                for (Pair pair : pairsOnDates) {
                    if (!pair.getPairDate().getHoliday() && pair.getIsCarriedOut()) {
                        pairs.add(pair);
                    }
                }
                int lectionHours = 0;
                int practiceHours = 0;
                int labHours = 0;
                int otherHours = 0;

                double consultsHours=0.0;
                for(Consult consult:consultManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){
                    consultsHours += consult.getHours();
                }

                double examsHours = 0.0;
                for(Exam exam:examManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){
                    examsHours += exam.getHours();
                }

                double zachetsHours = 0.0;
                for(Zachet zachet:zachetManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){
                    zachetsHours+=zachet.getHours();
                }

                double kursRabsHours = 0.0;
                for(KursRab kursRab:kursRabManager.getByPrepodAndDate(prepod, startDate, finishDate)){
                    kursRabsHours +=kursRab.getHours();
                }

                double kursProjectsHours = 0.0;
                for (KursProject kursProject:kursProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){
                    kursProjectsHours += kursProject.getHours();
                }
                double diplomasHours =0.0;
                for(DiplomaProject diplomaProject: diplomaProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){
                    diplomasHours+=diplomaProject.getHours();
                }
                double gakHours = 0.0;
                for(GAK gak:gakManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.BUDGET)){
                    gakHours+=gak.getHours();
                }
                double practicesHours = 0.0;
                for(Practice practice:practiceManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.BUDGET)){
                    practiceHours+=practice.getHours();
                }
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
                cell = cellIterator.next();
                if(plan.getRang()!=null){
                    cell.setCellValue(plan.getRangString());
                }
                cell = cellIterator.next();
                cell.setCellValue(String.format("%.2g", (double) plan.getRate()));
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
                cell.setCellValue(zachetsHours > 0.1 ? String.format("%.2g", (double) zachetsHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(" ");
                cell = cellIterator.next();
                cell.setCellValue(kursRabsHours > 0.1 ? String.format("%.2g", (double) kursRabsHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(kursProjectsHours > 0.1 ? String.format("%.2g", (double) kursProjectsHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(diplomasHours > 0.1 ? String.format("%.2g", (double) diplomasHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(gakHours > 0.1 ? String.format("%.2g", (double) gakHours) : " ");
                cell = cellIterator.next();
                cell.setCellValue(practiceHours > 0.1 ? String.format("%.2g", (double) practiceHours) : (" "));
            }
        }
    }
}
