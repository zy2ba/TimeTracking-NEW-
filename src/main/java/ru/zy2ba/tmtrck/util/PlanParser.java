package ru.zy2ba.tmtrck.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.zy2ba.tmtrck.entity.Plan;
import ru.zy2ba.tmtrck.entity.PlanTable;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.PrepodRang;
import ru.zy2ba.tmtrck.manager.PrepodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by Zy2ba on 19.05.2015.
 */
public class PlanParser {
    private File file;
    private Workbook wb;
    private Iterator<Row> rowIterator;
    Row row;
    String prepodName;
    String prepodLastName;
    Prepod prepod;
    private int startYear;
    private int finishYear;
    private PlanTable autumnBudgetTable;
    private PlanTable springBudgetTable;
    private PlanTable autumnPlatnoTable;
    private PlanTable springPlatnoTable;
    private PlanTable autumnShortTable;
    private PlanTable springShortTable;
    private Plan makeablePlan;
    private Plan platnoPlan;
    private Plan shortPlan;

    public PlanParser(File file){
        this.file = file;
    }
    public Plan parsePlan(){
        try {
            wb = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        makeablePlan = new Plan();
        Sheet sheet = wb.getSheetAt(0);
        rowIterator = sheet.iterator();
        while(rowIterator.hasNext()){
            row = rowIterator.next();
            Iterator<Cell> cells = row.cellIterator();
            Cell cell = cells.next();
            if(cell.getCellType()==Cell.CELL_TYPE_STRING ){
                {
                    if (cell.getStringCellValue().equals("ИНДИВИДУАЛЬНЫЙ ПЛАН РАБОТЫ ПРЕПОДАВАТЕЛЯ")){
                        rowIterator.next();
                        row = rowIterator.next();
                        cell = row.getCell(0);
                        StringTokenizer names = new StringTokenizer(cell.getStringCellValue());
                        prepodLastName = names.nextToken();
                        prepodName = names.nextToken();
                        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
                        prepod = prepodManager.findByNameAndLastName(prepodName,prepodLastName);
                        if(prepod==null) break;
                        rowIterator.next();
                        rowIterator.next();
                        row = rowIterator.next();
                        cell = row.getCell(0);
                        switch (cell.getStringCellValue().toLowerCase()){
                            case "ст.преподаватель" :
                            case "старший преподаватель" : prepod.setRang(PrepodRang.SENIOR_PREPOD); break;
                            case "декан" : prepod.setRang(PrepodRang.DEAN); break;
                            case "доцент" : prepod.setRang(PrepodRang.DOCENT); break;
                            case "профессор" : prepod.setRang(PrepodRang.PROFESSOR); break;
                            case "заведующий выпускающей кафедрой" :
                            case "зав.кафедрой (выпускающей)" : prepod.setRang(PrepodRang.HEAD_OF_DEPARTMENT_PRODUCING); break;
                            case "pаведующий невыпускающей кафедрой" : prepod.setRang(PrepodRang.HEAD_OF_DEPARTMENT_NONPRODUCING); break;
                            case "заведующий кафедрой физвоспитания" : prepod.setRang(PrepodRang.HEAD_OF_DEPARTMENT_PHYSICAL_EDUCATION); break;
                            case "педагогическая работа" :
                            case "пед.работа" : prepod.setRang(PrepodRang.PED_WORK); break;
                            default : prepod.setRang(PrepodRang.PREPOD); break;
                        }
                        rowIterator.next();
                        rowIterator.next();
                        row = rowIterator.next();
                        cell = row.getCell(15);
                        double rate = cell.getNumericCellValue();
                        makeablePlan.setRate(rate);
                        rowIterator.next();
                        rowIterator.next();
                        row = rowIterator.next();
                        cell = row.getCell(0);
                        StringTokenizer years = new StringTokenizer(cell.getStringCellValue()," |/");
                        years.nextToken();
                        startYear = Integer.decode(years.nextToken());
                        finishYear = Integer.decode(years.nextToken());
                    }
                    if(cell.getStringCellValue().indexOf("Бюджетная нагрузка") != -1){
                        while (!cell.getStringCellValue().equals("ИТОГО за семестр")){
                            row = rowIterator.next();
                            cell = row.getCell(0);
                        }
                        cells = row.cellIterator();
                        autumnBudgetTable = new PlanTable();
                        cells.next();
                        cells.next();
                        cells.next();
                        cell = cells.next();
                        autumnBudgetTable.setLection(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setLab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setPractice(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setConsult(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setExam(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setZachet(cell.getNumericCellValue());
                        cells.next();
                        cell = cells.next();
                        autumnBudgetTable.setLeadingKRab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setLeadingKProject(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setDiplomDesign(cell.getNumericCellValue());
                        cells.next();
                        cell = cells.next();
                        autumnBudgetTable.setPractice(cell.getNumericCellValue());

                        row = rowIterator.next();
                        cell = row.getCell(0);
                        while (!cell.getStringCellValue().equals("ИТОГО за семестр")){
                            row = rowIterator.next();
                            cell = row.getCell(0);
                        }
                        cells = row.cellIterator();

                        springBudgetTable = new PlanTable();
                        cells.next();
                        cells.next();
                        cell = cells.next();
                        springBudgetTable.setLection(cell.getNumericCellValue());
                        cell = cells.next();
                        springBudgetTable.setLab(cell.getNumericCellValue());
                        cell = cells.next();
                        springBudgetTable.setPractice(cell.getNumericCellValue());
                        cell = cells.next();
                        springBudgetTable.setConsult(cell.getNumericCellValue());
                        cell = cells.next();
                        springBudgetTable.setExam(cell.getNumericCellValue());
                        cell = cells.next();
                        springBudgetTable.setZachet(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        springBudgetTable.setLeadingKRab(cell.getNumericCellValue());
                        cell = cells.next();
                        springBudgetTable.setLeadingKProject(cell.getNumericCellValue());
                        cell = cells.next();
                        springBudgetTable.setDiplomDesign(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        springBudgetTable.setPractice(cell.getNumericCellValue());
                        cell = row.getCell(0);

                        makeablePlan.setPlanTableAutumnBudget(autumnBudgetTable);
                        makeablePlan.setPlanTableSpringBudget(springBudgetTable);
                       // autumnBudgetTable.setPlan(makeablePlan);
                       // springBudgetTable.setPlan(makeablePlan);
                    }


                    if(cell.getStringCellValue().indexOf("Почасовая нагрузка") != -1){
                        while (!cell.getStringCellValue().equals("ИТОГО за семестр")){
                            row = rowIterator.next();
                            cell = row.getCell(0);
                        }
                        cells = row.cellIterator();
                        cells.next();
                        cells.next();
                        autumnPlatnoTable = new PlanTable();
                        cell = cells.next();
                        autumnPlatnoTable.setLection(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnPlatnoTable.setLab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnPlatnoTable.setPractice(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnPlatnoTable.setConsult(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnPlatnoTable.setExam(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnPlatnoTable.setZachet(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        autumnPlatnoTable.setLeadingKRab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnPlatnoTable.setLeadingKProject(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnPlatnoTable.setDiplomDesign(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        autumnPlatnoTable.setPractice(cell.getNumericCellValue());

                        row = rowIterator.next();
                        cell = row.getCell(0);
                        while (!cell.getStringCellValue().equals("ИТОГО за семестр")){
                            row = rowIterator.next();
                            cell = row.getCell(0);
                        }
                        cells = row.cellIterator();
                        cells.next();
                        cells.next();

                        springPlatnoTable = new PlanTable();
                        cell = cells.next();
                        springPlatnoTable.setLection(cell.getNumericCellValue());
                        cell = cells.next();
                        springPlatnoTable.setLab(cell.getNumericCellValue());
                        cell = cells.next();
                        springPlatnoTable.setPractice(cell.getNumericCellValue());
                        cell = cells.next();
                        springPlatnoTable.setConsult(cell.getNumericCellValue());
                        cell = cells.next();
                        springPlatnoTable.setExam(cell.getNumericCellValue());
                        cell = cells.next();
                        springPlatnoTable.setZachet(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        springPlatnoTable.setLeadingKRab(cell.getNumericCellValue());
                        cell = cells.next();
                        springPlatnoTable.setLeadingKProject(cell.getNumericCellValue());
                        cell = cells.next();
                        springPlatnoTable.setDiplomDesign(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        springPlatnoTable.setPractice(cell.getNumericCellValue());

                        cell = row.getCell(0);
                        makeablePlan.setPlanTableAutumnPlatno(autumnPlatnoTable);
                        makeablePlan.setPlanTableSpringPlatno(springPlatnoTable);
                       // autumnPlatnoTable.setPlan(makeablePlan);
                      //  springPlatnoTable.setPlan(makeablePlan);
                    }


                    if(cell.getStringCellValue().indexOf("сокращенное обучение") != -1){
                        while (!cell.getStringCellValue().equals("ИТОГО за семестр")){
                            row = rowIterator.next();
                            cell = row.getCell(0);
                        }
                        cells = row.cellIterator();
                        cells.next();
                        cells.next();
                        autumnShortTable = new PlanTable();
                        cell = cells.next();
                        autumnShortTable.setLection(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnShortTable.setLab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnShortTable.setPractice(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnShortTable.setConsult(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnShortTable.setExam(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnShortTable.setZachet(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        autumnShortTable.setLeadingKRab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnShortTable.setLeadingKProject(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnShortTable.setDiplomDesign(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        autumnShortTable.setPractice(cell.getNumericCellValue());

                        row = rowIterator.next();
                        cell = row.getCell(0);
                        while (!cell.getStringCellValue().equals("ИТОГО за семестр")){
                            row = rowIterator.next();
                            cell = row.getCell(0);
                        }
                        cells = row.cellIterator();
                        cells.next();
                        cells.next();

                        springShortTable = new PlanTable();
                        cell = cells.next();
                        springShortTable.setLection(cell.getNumericCellValue());
                        cell = cells.next();
                        springShortTable.setLab(cell.getNumericCellValue());
                        cell = cells.next();
                        springShortTable.setPractice(cell.getNumericCellValue());
                        cell = cells.next();
                        springShortTable.setConsult(cell.getNumericCellValue());
                        cell = cells.next();
                        springShortTable.setExam(cell.getNumericCellValue());
                        cell = cells.next();
                        springShortTable.setZachet(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        springShortTable.setLeadingKRab(cell.getNumericCellValue());
                        cell = cells.next();
                        springShortTable.setLeadingKProject(cell.getNumericCellValue());
                        cell = cells.next();
                        springShortTable.setDiplomDesign(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        springShortTable.setPractice(cell.getNumericCellValue());
                        cell = row.getCell(0);

                        makeablePlan.setPlanTableAutumnShort(autumnShortTable);
                        makeablePlan.setPlanTableSpringShort(springShortTable);
                     //   autumnShortTable.setPlan(makeablePlan);
                     //   springShortTable.setPlan(makeablePlan);
                    }

                    if(cell.getStringCellValue().indexOf("Распределение учебной нагрузки") != -1){
                        rowIterator.next();
                        row = rowIterator.next();
                        cells = row.cellIterator();
                        cell = cells.next();
                        makeablePlan.setBudgetRegular(cell.getNumericCellValue());
                        cells.next();
                        cell = cells.next();
                        makeablePlan.setBudgetHourly(cell.getNumericCellValue());
                        cells.next();
                        cells.next();
                        cell = cells.next();
                        makeablePlan.setPlantoRegular(cell.getNumericCellValue());
                        cells.next();
                        cells.next();
                        cell = cells.next();
                        makeablePlan.setPlantoHourly(cell.getNumericCellValue());
                        cells.next();
                        cell = cells.next();
                        makeablePlan.setShortRegular(cell.getNumericCellValue());
                        cells.next();
                        cells.next();
                        cell = cells.next();
                        makeablePlan.setShortHourly(cell.getNumericCellValue());
                        cell = row.getCell(0);
                    }
/*
                    if(cell.getStringCellValue().con.equals("ИТОГО за семестр")){
                        PlanTable autumnBudgetTable = new PlanTable();
                        cell = cells.next();
                        autumnBudgetTable.setLection(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setLab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setPractice(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setConsult(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setExam(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setZachet(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        autumnBudgetTable.setLeadingKRab(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setLeadingKProject(cell.getNumericCellValue());
                        cell = cells.next();
                        autumnBudgetTable.setDiplomDesign(cell.getNumericCellValue());
                        cell = cells.next();
                        cell = cells.next();
                        autumnBudgetTable.setPractice(cell.getNumericCellValue());
                    }*/


                }

            }
        }
        makeablePlan.setPrepod(prepod);
        makeablePlan.setStartYear(startYear);
        makeablePlan.setFinishYear(finishYear);
        makeablePlan.setRang(prepod.getRang());
        makeablePlan.setPosition(prepod.getPosition());
        return makeablePlan;
    }
}
