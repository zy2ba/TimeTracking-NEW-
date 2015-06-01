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
 * @author Zy2ba
 * @since 29.05.2015
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
        Sheet shExtra = wb.getSheet("ФБп");
        if (sh == null|| shExtra == null) throw new IOException();
        Iterator<Row> rowIterator = sh.rowIterator();
        Iterator<Row> rowIteratorExtra = shExtra.rowIterator();
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
        while (rowIteratorExtra.hasNext()) {
            row = rowIteratorExtra.next();
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
                cell.setCellValue( plan.getRate());
                cell = cellIterator.next();
                if(lectionHours > 0.1) cell.setCellValue( lectionHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if(labHours > 0.1)cell.setCellValue( labHours);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (practiceHours > 0.1) cell.setCellValue(practiceHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (consultsHours > 0.1) cell.setCellValue(consultsHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (examsHours > 0.1) cell.setCellValue(examsHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (zachetsHours > 0.1) cell.setCellValue(zachetsHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (kursRabsHours > 0.1) cell.setCellValue(kursRabsHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (kursProjectsHours > 0.1) cell.setCellValue(kursProjectsHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (diplomasHours > 0.1) cell.setCellValue(diplomasHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (gakHours > 0.1) cell.setCellValue(gakHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (practicesHours > 0.1) cell.setCellValue(practicesHours );
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();
                cell = cellIterator.next();
                cell.setCellValue("Штатная");





                row = rowIteratorExtra.next();
                cellIterator = row.cellIterator();
                cellIterator.next();
                cell = cellIterator.next();
                cell.setCellValue(prepod.getLastName() + " " + prepod.getName() + " " + prepod.getMiddleName());
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();
                cell = cellIterator.next();
                if (plan.getRang() != null) {
                    cell.setCellValue(plan.getRangString());
                }
                cell = cellIterator.next();
                cell.setCellValue(plan.getRate());
                cell = cellIterator.next();
                if (lectionHoursExtra > 0.1) cell.setCellValue(lectionHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (labHoursExtra > 0.1) cell.setCellValue(labHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (practiceHoursExtra > 0.1) cell.setCellValue(practiceHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (consultsHoursExtra > 0.1) cell.setCellValue(consultsHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (examsHoursExtra > 0.1) cell.setCellValue(examsHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (zachetsHoursExtra > 0.1) cell.setCellValue(zachetsHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (kursRabsHoursExtra > 0.1) cell.setCellValue(kursRabsHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (kursProjectsHoursExtra > 0.1) cell.setCellValue(kursProjectsHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (diplomasHoursExtra > 0.1) cell.setCellValue(diplomasHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (gakHoursExtra > 0.1) cell.setCellValue(gakHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cell = cellIterator.next();
                if (practicesHoursExtra > 0.1) cell.setCellValue(practicesHoursExtra);
                else cell.setCellType(Cell.CELL_TYPE_BLANK);
                cellIterator.next();
                cellIterator.next();
                cellIterator.next();
                cell = cellIterator.next();
                cell.setCellValue("Распоряжение");
            }
        }
        }

        sh = wb.getSheet("ПВЗш");
        shExtra = wb.getSheet("ПВЗп");
        if (sh == null|| shExtra == null) throw new IOException();
        rowIterator = sh.rowIterator();
        rowIteratorExtra = shExtra.rowIterator();

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

        while (rowIteratorExtra.hasNext()) {
            row = rowIteratorExtra.next();
            cellIterator = row.cellIterator();
            if (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getStringCellValue().contains("N")) break;
                }
            }
        }

        for (Prepod prepod : prepods) {
            Plan plan = planManager.getByPrepodAndStartYear(prepod, autumnSpringSpacer.getStartYear());
            if (plan!=null)
            {


                //  PlanTable planTable;
                //  if (autumnSpringSpacer.getSpacerDate().isAfter(finishDate)){
                //      planTable = plan.getPlanTableAutumnPlatno();
                //  }else planTable = plan.getPlanTableSpringPlatno();
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

                    double platnoNormalHours = 0.0;

                   /* int lectionHours = 0;
                    int practiceHours = 0;
                    int labHours = 0;
                    int otherHours = 0;
                    int lectionHoursExtra = 0;
                    int practiceHoursExtra = 0;
                    int labHoursExtra = 0;
                    int otherHoursExtra = 0;*/


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
                            /*for(Pair pair : pairManager.findCarriedPairsForPrepodByDate(prepod,pairDate)) if (pair!=null){
                                if (pair.getPairName().getPairType() == PairType.lab){
                                    if(platnoNormalHours+0.95<plan.getPlatnoRegular()){
                                        labHours++;
                                        platnoNormalHours++;
                                    } else labHoursExtra++;
                                } else if (pair.getPairName().getPairType() == PairType.practice)  {if(platnoNormalHours+0.95<plan.getPlatnoRegular()){
                                    practiceHours++;
                                    platnoNormalHours++;
                                } else practiceHoursExtra++;
                                } else if (pair.getPairName().getPairType() == PairType.lecture) {if (platnoNormalHours+0.95<plan.getPlatnoRegular()){

                                    lectionHours++;
                                    platnoNormalHours++;
                                } else lectionHoursExtra++;
                                }
                            }*/
                            for(Consult consult:consultManager.getByPrepodDateTypeOfLoad(prepod, pairDate.getDate(), TypeOfLoad.OFF_BUDGET)){ if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                consultsHours += consult.getHours();
                                platnoNormalHours += consult.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    consultsHours+=plan.getPlatnoRegular()-platnoNormalHours;
                                }
                            }else consultsHoursExtra += consult.getHours();
                            }

                            for(Exam exam:examManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.OFF_BUDGET)){ if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                examsHours += exam.getHours();
                                platnoNormalHours += exam.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    examsHours+=plan.getPlatnoRegular()-platnoNormalHours;
                                }
                                else examsHoursExtra += exam.getHours();
                            }
                            }

                            for(Zachet zachet:zachetManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.OFF_BUDGET)){if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                zachetsHours+=zachet.getHours();
                                platnoNormalHours += zachet.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    zachetsHours+=plan.getPlatnoRegular()-platnoNormalHours;
                                }
                                else zachetsHoursExtra += zachet.getHours();
                            }
                            }

                            for(KursRab kursRab:kursRabManager.getByPrepodAndDate(prepod, startDate, finishDate)){if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                kursRabsHours +=kursRab.getHours();
                                platnoNormalHours += kursRab.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    kursRabsHours+=plan.getPlatnoRegular()-platnoNormalHours;
                                }
                                else kursRabsHours += kursRab.getHours();
                            }
                            }
                            for (KursProject kursProject:kursProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.OFF_BUDGET)){if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                kursProjectsHours += kursProject.getHours();
                                platnoNormalHours += kursProject.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    kursProjectsHours+=plan.getPlatnoRegular()-platnoNormalHours;
                                }
                                else kursProjectsHoursExtra += kursProject.getHours();
                            }
                            }
                            for(DiplomaProject diplomaProject: diplomaProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.OFF_BUDGET)){if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                diplomasHours+=diplomaProject.getHours();
                                platnoNormalHours += diplomaProject.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    diplomasHours+=plan.getPlatnoRegular()-platnoNormalHours;
                                }
                                else diplomasHoursExtra += diplomaProject.getHours();
                            }
                            }

                            for(GAK gak:gakManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.OFF_BUDGET)){if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                gakHours+=gak.getHours();
                                platnoNormalHours += gak.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    gakHours+=plan.getPlatnoRegular()-platnoNormalHours;
                                }
                                else gakHoursExtra += gak.getHours();
                            }
                            }
                            for(Practice practice:practiceManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.OFF_BUDGET)){if(platnoNormalHours+0.01<plan.getPlatnoRegular()) {
                                practicesHours+=practice.getHours();
                                platnoNormalHours += practice.getHours();
                                if( platnoNormalHours>plan.getPlatnoRegular()){
                                    practicesHours+=plan.getPlatnoRegular()-platnoNormalHours;
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
                    cell.setCellValue( plan.getRate());
                    cell = cellIterator.next();/*
                    if(lectionHours > 0.1) cell.setCellValue( lectionHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                   /* if(labHours > 0.1)cell.setCellValue( labHours);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                  /*  if (practiceHours > 0.1) cell.setCellValue(practiceHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                    if (consultsHours > 0.1) cell.setCellValue(consultsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (examsHours > 0.1) cell.setCellValue(examsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (zachetsHours > 0.1) cell.setCellValue(zachetsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursRabsHours > 0.1) cell.setCellValue(kursRabsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursProjectsHours > 0.1) cell.setCellValue(kursProjectsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (diplomasHours > 0.1) cell.setCellValue(diplomasHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (gakHours > 0.1) cell.setCellValue(gakHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (practicesHours > 0.1) cell.setCellValue(practicesHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cellIterator.next();
                    cellIterator.next();
                    cellIterator.next();
                    cell = cellIterator.next();
                    cell.setCellValue("Штатная");





                    row = rowIteratorExtra.next();
                    cellIterator = row.cellIterator();
                    cellIterator.next();
                    cell = cellIterator.next();
                    cell.setCellValue(prepod.getLastName() + " " + prepod.getName() + " " + prepod.getMiddleName());
                    cellIterator.next();
                    cellIterator.next();
                    cellIterator.next();
                    cell = cellIterator.next();
                    if (plan.getRang() != null) {
                        cell.setCellValue(plan.getRangString());
                    }
                    cell = cellIterator.next();
                    cell.setCellValue(plan.getRate());
                    cell = cellIterator.next();
                    /*if (lectionHoursExtra > 0.1) cell.setCellValue(lectionHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                    /*if (labHoursExtra > 0.1) cell.setCellValue(labHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                  /*  if (practiceHoursExtra > 0.1) cell.setCellValue(practiceHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                    if (consultsHoursExtra > 0.1) cell.setCellValue(consultsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (examsHoursExtra > 0.1) cell.setCellValue(examsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (zachetsHoursExtra > 0.1) cell.setCellValue(zachetsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursRabsHoursExtra > 0.1) cell.setCellValue(kursRabsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursProjectsHoursExtra > 0.1) cell.setCellValue(kursProjectsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (diplomasHoursExtra > 0.1) cell.setCellValue(diplomasHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (gakHoursExtra > 0.1) cell.setCellValue(gakHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (practicesHoursExtra > 0.1) cell.setCellValue(practicesHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cellIterator.next();
                    cellIterator.next();
                    cellIterator.next();
                    cell = cellIterator.next();
                    cell.setCellValue("Распоряжение");
                }
            }
        }


        sh = wb.getSheet("ПВЗ(сокращенное)");
        shExtra = wb.getSheet("ПВЗ(сокращенное)-ВЗФ");
        if (sh == null|| shExtra == null) throw new IOException();
        rowIterator = sh.rowIterator();
        rowIteratorExtra = shExtra.rowIterator();

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

        while (rowIteratorExtra.hasNext()) {
            row = rowIteratorExtra.next();
            cellIterator = row.cellIterator();
            if (cellIterator.hasNext()) {
                cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (cell.getStringCellValue().contains("N")) break;
                }
            }
        }

        for (Prepod prepod : prepods) {
            Plan plan = planManager.getByPrepodAndStartYear(prepod, autumnSpringSpacer.getStartYear());
            if (plan!=null)
            {


                //  PlanTable planTable;
                //  if (autumnSpringSpacer.getSpacerDate().isAfter(finishDate)){
                //      planTable = plan.getPlanTableAutumnShort();
                //  }else planTable = plan.getPlanTableSpringShort();
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

                    double shortNormalHours = 0.0;

                   /* int lectionHours = 0;
                    int practiceHours = 0;
                    int labHours = 0;
                    int otherHours = 0;
                    int lectionHoursExtra = 0;
                    int practiceHoursExtra = 0;
                    int labHoursExtra = 0;
                    int otherHoursExtra = 0;*/


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
                            /*for(Pair pair : pairManager.findCarriedPairsForPrepodByDate(prepod,pairDate)) if (pair!=null){
                                if (pair.getPairName().getPairType() == PairType.lab){
                                    if(shortNormalHours+0.95<plan.getShortRegular()){
                                        labHours++;
                                        shortNormalHours++;
                                    } else labHoursExtra++;
                                } else if (pair.getPairName().getPairType() == PairType.practice)  {if(shortNormalHours+0.95<plan.getShortRegular()){
                                    practiceHours++;
                                    shortNormalHours++;
                                } else practiceHoursExtra++;
                                } else if (pair.getPairName().getPairType() == PairType.lecture) {if (shortNormalHours+0.95<plan.getShortRegular()){

                                    lectionHours++;
                                    shortNormalHours++;
                                } else lectionHoursExtra++;
                                }
                            }*/
                            for(Consult consult:consultManager.getByPrepodDateTypeOfLoad(prepod, pairDate.getDate(), TypeOfLoad.SHORT)){ if(shortNormalHours+0.01<plan.getShortRegular()) {
                                consultsHours += consult.getHours();
                                shortNormalHours += consult.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    consultsHours+=plan.getShortRegular()-shortNormalHours;
                                }
                            }else consultsHoursExtra += consult.getHours();
                            }

                            for(Exam exam:examManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.SHORT)){ if(shortNormalHours+0.01<plan.getShortRegular()) {
                                examsHours += exam.getHours();
                                shortNormalHours += exam.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    examsHours+=plan.getShortRegular()-shortNormalHours;
                                }
                                else examsHoursExtra += exam.getHours();
                            }
                            }

                            for(Zachet zachet:zachetManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.SHORT)){if(shortNormalHours+0.01<plan.getShortRegular()) {
                                zachetsHours+=zachet.getHours();
                                shortNormalHours += zachet.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    zachetsHours+=plan.getShortRegular()-shortNormalHours;
                                }
                                else zachetsHoursExtra += zachet.getHours();
                            }
                            }

                            for(KursRab kursRab:kursRabManager.getByPrepodAndDate(prepod, startDate, finishDate)){if(shortNormalHours+0.01<plan.getShortRegular()) {
                                kursRabsHours +=kursRab.getHours();
                                shortNormalHours += kursRab.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    kursRabsHours+=plan.getShortRegular()-shortNormalHours;
                                }
                                else kursRabsHours += kursRab.getHours();
                            }
                            }
                            for (KursProject kursProject:kursProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.SHORT)){if(shortNormalHours+0.01<plan.getShortRegular()) {
                                kursProjectsHours += kursProject.getHours();
                                shortNormalHours += kursProject.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    kursProjectsHours+=plan.getShortRegular()-shortNormalHours;
                                }
                                else kursProjectsHoursExtra += kursProject.getHours();
                            }
                            }
                            for(DiplomaProject diplomaProject: diplomaProjectManager.getByPrepodDateTypeOfLoad(prepod, startDate, finishDate, TypeOfLoad.SHORT)){if(shortNormalHours+0.01<plan.getShortRegular()) {
                                diplomasHours+=diplomaProject.getHours();
                                shortNormalHours += diplomaProject.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    diplomasHours+=plan.getShortRegular()-shortNormalHours;
                                }
                                else diplomasHoursExtra += diplomaProject.getHours();
                            }
                            }

                            for(GAK gak:gakManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.SHORT)){if(shortNormalHours+0.01<plan.getShortRegular()) {
                                gakHours+=gak.getHours();
                                shortNormalHours += gak.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    gakHours+=plan.getShortRegular()-shortNormalHours;
                                }
                                else gakHoursExtra += gak.getHours();
                            }
                            }
                            for(Practice practice:practiceManager.getByPrepodDateTypeOfLoad(prepod,startDate,finishDate,TypeOfLoad.SHORT)){if(shortNormalHours+0.01<plan.getShortRegular()) {
                                practicesHours+=practice.getHours();
                                shortNormalHours += practice.getHours();
                                if( shortNormalHours>plan.getShortRegular()){
                                    practicesHours+=plan.getShortRegular()-shortNormalHours;
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
                    cell.setCellValue( plan.getRate());
                    cell = cellIterator.next();/*
                    if(lectionHours > 0.1) cell.setCellValue( lectionHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                   /* if(labHours > 0.1)cell.setCellValue( labHours);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                  /*  if (practiceHours > 0.1) cell.setCellValue(practiceHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                    if (consultsHours > 0.1) cell.setCellValue(consultsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (examsHours > 0.1) cell.setCellValue(examsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (zachetsHours > 0.1) cell.setCellValue(zachetsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursRabsHours > 0.1) cell.setCellValue(kursRabsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursProjectsHours > 0.1) cell.setCellValue(kursProjectsHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (diplomasHours > 0.1) cell.setCellValue(diplomasHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (gakHours > 0.1) cell.setCellValue(gakHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (practicesHours > 0.1) cell.setCellValue(practicesHours );
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cellIterator.next();
                    cellIterator.next();
                    cellIterator.next();
                    cell = cellIterator.next();
                    cell.setCellValue("Штатная");





                    row = rowIteratorExtra.next();
                    cellIterator = row.cellIterator();
                    cellIterator.next();
                    cell = cellIterator.next();
                    cell.setCellValue(prepod.getLastName() + " " + prepod.getName() + " " + prepod.getMiddleName());
                    cellIterator.next();
                    cellIterator.next();
                    cellIterator.next();
                    cell = cellIterator.next();
                    if (plan.getRang() != null) {
                        cell.setCellValue(plan.getRangString());
                    }
                    cell = cellIterator.next();
                    cell.setCellValue(plan.getRate());
                    cell = cellIterator.next();
                    /*if (lectionHoursExtra > 0.1) cell.setCellValue(lectionHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                    /*if (labHoursExtra > 0.1) cell.setCellValue(labHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                  /*  if (practiceHoursExtra > 0.1) cell.setCellValue(practiceHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);*/
                    cell = cellIterator.next();
                    if (consultsHoursExtra > 0.1) cell.setCellValue(consultsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (examsHoursExtra > 0.1) cell.setCellValue(examsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (zachetsHoursExtra > 0.1) cell.setCellValue(zachetsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursRabsHoursExtra > 0.1) cell.setCellValue(kursRabsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (kursProjectsHoursExtra > 0.1) cell.setCellValue(kursProjectsHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (diplomasHoursExtra > 0.1) cell.setCellValue(diplomasHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (gakHoursExtra > 0.1) cell.setCellValue(gakHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cell = cellIterator.next();
                    if (practicesHoursExtra > 0.1) cell.setCellValue(practicesHoursExtra);
                    else cell.setCellType(Cell.CELL_TYPE_BLANK);
                    cellIterator.next();
                    cellIterator.next();
                    cellIterator.next();
                    cell = cellIterator.next();
                    cell.setCellValue("Распоряжение");
                }
            }
        }



        FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 5) + "_returned.xlsm");
        wb.write(fileOutputStream);
        fileOutputStream.close();
        fileInputStream.close();
    }
}
