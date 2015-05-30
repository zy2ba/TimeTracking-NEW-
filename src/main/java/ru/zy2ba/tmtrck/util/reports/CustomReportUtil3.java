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
public class CustomReportUtil3 {
    private final File file;
    private final LocalDate startDate;
    private final LocalDate finishDate;
    private final String kafedra;
    private Iterator<Cell> cellIterator2;
    private Row row2;
    private Cell cell2;
    private int startYear;
    private Iterator<Row> rowIterator2;

    public CustomReportUtil3(File filein, LocalDate dateStart, LocalDate dateFinish, String faculty) {
        file = filein;
        this.startDate = dateStart;
        this.finishDate = dateFinish;
        this.kafedra = faculty;
    }

    public void makeReport() throws IOException, InvalidFormatException, WrongDatesException {
        AutumnSpringSpacerManager autumnSpringSpacerManager = (AutumnSpringSpacerManager) ResourceLocator.getBean("autumnSpringSpacerManager");
        AutumnSpringSpacer autumnSpringSpacer = autumnSpringSpacerManager.getSpacerForDate(startDate);
        AutumnSpringSpacer autumnSpringSpacer2 = autumnSpringSpacerManager.getSpacerForDate(finishDate);
        if(autumnSpringSpacer.getStartYear()!=autumnSpringSpacer2.getStartYear())throw new WrongDatesException();

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

        PlanManager planManager = (PlanManager) ResourceLocator.getBean("planManager");
        PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
        for (Prepod prepod : prepods) {
            Plan plan = planManager.getByPrepodAndStartYear(prepod, autumnSpringSpacer.getStartYear());
            if (plan!=null)
        {


          //  PlanTable planTable;
          //  if (autumnSpringSpacer.getSpacerDate().isAfter(finishDate)){
          //      planTable = plan.getPlanTableAutumnBudget();
          //  }else planTable = plan.getPlanTableSpringBudget();
            if (rowIterator.hasNext()) {
                ArrayList<Pair> pairs = new ArrayList<>();

                Collections.sort(pairDates, new Comparator<PairDate>() {
                    @Override
                    public int compare(PairDate o1, PairDate o2) {
                        if (o1.getDate().isAfter(o2.getDate())) {
                            return 1;
                        } else if (o2.getDate().isAfter(o1.getDate())) {
                            return -1;
                        } else
                            return 0;
                    }
                });

                double budgetNormalHours = 0.0;

                int lectionHours = 0;
                int practiceHours = 0;
                int labHours = 0;
                int otherHours = 0;
                int lectionHoursExtra = 0;
                int practiceHoursExtra = 0;
                int labHoursExtra = 0;
                int otherHoursExtra = 0;


                double consultsHours=0.0;
                double examsHours = 0.0;
                double zachetsHours = 0.0;
                double kursRabsHours = 0.0;
                double kursProjectsHours = 0.0;
                double diplomasHours =0.0;
                double gakHours = 0.0;
                double practicesHours = 0.0;
                double consultsHoursExtra=0.0;
                double examsHoursExtra = 0.0;
                double zachetsHoursExtra = 0.0;
                double kursRabsHoursExtra = 0.0;
                double kursProjectsHoursExtra = 0.0;
                double diplomasHoursExtra =0.0;
                double gakHoursExtra = 0.0;
                double practicesHoursExtra = 0.0;


                for(PairDate pairDate:pairDates){
                    if(!pairDate.getHoliday()){
                       for(Pair pair : pairManager.findCarriedPairsForPrepodByDate(prepod,pairDate)) if (pair!=null){
                           if (pair.getPairName().getPairType() == PairType.lab){
                               if(budgetNormalHours+0.95<plan.getBudgetRegular()){
                               labHours++;
                               budgetNormalHours++;
                           } else labHoursExtra++;
                           } else if (pair.getPairName().getPairType() == PairType.practice)  {if(budgetNormalHours+0.95<plan.getBudgetRegular()){
                               practiceHours++;
                               budgetNormalHours++;
                           } else practiceHoursExtra++;
                               } else if (pair.getPairName().getPairType() == PairType.lecture) {if (budgetNormalHours+0.95<plan.getBudgetRegular()){

                                   lectionHours++;
                                   budgetNormalHours++;
                               } else lectionHoursExtra++;
                           }
                       }
                        for(Consult consult:consultManager.getByPrepodDateTypeOfLoad(prepod, pairDate.getDate(), TypeOfLoad.BUDGET)){ if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            consultsHours += consult.getHours();
                            budgetNormalHours += consult.getHours();
                           if( budgetNormalHours>plan.getBudgetRegular()){
                               consultsHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                        }else consultsHoursExtra += consult.getHours();
                        }

                        for(Exam exam:examManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){ if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            examsHours += exam.getHours();
                            budgetNormalHours += exam.getHours();
                            if( budgetNormalHours>plan.getBudgetRegular()){
                                examsHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                        else examsHoursExtra += exam.getHours();
                        }
                        }

                        for(Zachet zachet:zachetManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            zachetsHours+=zachet.getHours();
                            budgetNormalHours += zachet.getHours();
                            if( budgetNormalHours>plan.getBudgetRegular()){
                                zachetsHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                            else zachetsHoursExtra += zachet.getHours();
                        }
                        }

                        for(KursRab kursRab:kursRabManager.getByPrepodAndDate(prepod, startDate, finishDate)){if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            kursRabsHours +=kursRab.getHours();
                            budgetNormalHours += kursRab.getHours();
                            if( budgetNormalHours>plan.getBudgetRegular()){
                                kursRabsHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                            else kursRabsHours += kursRab.getHours();
                        }
                        }
                        for (KursProject kursProject:kursProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            kursProjectsHours += kursProject.getHours();
                            budgetNormalHours += kursProject.getHours();
                            if( budgetNormalHours>plan.getBudgetRegular()){
                                kursProjectsHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                            else kursProjectsHoursExtra += kursProject.getHours();
                        }
                        }
                        for(DiplomaProject diplomaProject: diplomaProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.BUDGET)){if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            diplomasHours+=diplomaProject.getHours();
                            budgetNormalHours += diplomaProject.getHours();
                            if( budgetNormalHours>plan.getBudgetRegular()){
                                diplomasHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                            else diplomasHoursExtra += diplomaProject.getHours();
                        }
                        }

                        for(GAK gak:gakManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.BUDGET)){if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            gakHours+=gak.getHours();
                            budgetNormalHours += gak.getHours();
                            if( budgetNormalHours>plan.getBudgetRegular()){
                                gakHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                            else gakHoursExtra += gak.getHours();
                        }
                        }
                        for(Practice practice:practiceManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.BUDGET)){if(budgetNormalHours+0.01<plan.getBudgetRegular()) {
                            practicesHours+=practice.getHours();
                            budgetNormalHours += practice.getHours();
                            if( budgetNormalHours>plan.getBudgetRegular()){
                                practicesHours+=plan.getBudgetRegular()-budgetNormalHours;
                            }
                            else practicesHoursExtra += practice.getHours();
                        }
                        }

                    }
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
                cell.setCellValue(practicesHours > 0.1 ? String.format("%.2g", (double) practicesHours) : (" "));


               /* Sheet sh2 = wb.getSheet("ФБп");
                rowIterator2 = sh2.rowIterator();
                while (rowIterator2.hasNext()) {
                    row2 = rowIterator.next();
                    cellIterator2 = row.cellIterator();
                    if (cellIterator2.hasNext()) {
                        cell2 = cellIterator2.next();
                        if (cell2.getCellType() == Cell.CELL_TYPE_STRING) {
                            if (cell2.getStringCellValue().contains("N")) break;
                        }
                    }

                }
                row2 = rowIterator2.next();
                cellIterator2 = row2.cellIterator();
                cellIterator2.next();
                cellIterator2.next();
                cellIterator2.next();
                cellIterator2.next();
                cellIterator2.next();
                cellIterator2.next();
                cell2 =cellIterator2.next();
                cell2.setCellValue(String.format("%.2g",(double)plan.getRate()));
                cell2 = cellIterator2.next();
                cell2.setCellValue(lectionHoursExtra > 0.1 ? String.format("%.2g", (double) lectionHoursExtra) : " ");
                cell2 = cellIterator2.next();

                cell2.setCellValue(labHoursExtra > 0.1 ? String.format("%.2g", (double) labHoursExtra) : " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(practiceHoursExtra > 0.1 ? String.format("%.2g", (double) practiceHoursExtra) : " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(consultsHoursExtra > 0.1 ? String.format("%.2g", (double) consultsHoursExtra) : " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(examsHoursExtra > 0.1 ? String.format("%.2g", (double) examsHoursExtra) : " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(zachetsHoursExtra > 0.1 ? String.format("%.2g",(double)zachetsHoursExtra): " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(" ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(kursRabsHoursExtra > 0.1 ? String.format("%.2g", (double) kursRabsHoursExtra) : " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(kursProjectsHoursExtra > 0.1 ? String.format("%.2g", (double) kursProjectsHoursExtra) : " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(diplomasHoursExtra > 0.1 ? String.format("%.2g", (double) diplomasHoursExtra) : " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(gakHoursExtra > 0.1 ? String.format("%.2g",(double)gakHoursExtra): " ");
                cell2 = cellIterator2.next();
                cell2.setCellValue(practicesHoursExtra > 0.1 ? String.format("%.2g", (double) practicesHoursExtra) : (" "));*/
            }
        }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 5) + "_returned.xlsm");
        wb.write(fileOutputStream);
        fileOutputStream.close();
        fileInputStream.close();
    }
}
