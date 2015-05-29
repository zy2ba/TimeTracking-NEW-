package ru.zy2ba.tmtrck.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.zy2ba.tmtrck.entity.Pair;
import ru.zy2ba.tmtrck.entity.PairDate;
import ru.zy2ba.tmtrck.entity.PairName;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.enums.PairType;
import ru.zy2ba.tmtrck.entity.enums.Week;

import org.joda.time.LocalDate;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * @author Zy2ba
 * @since 05.05.2015
 */


public class Parser {
    private Component parent;
    private File file;
    private HSSFWorkbook wb;
    private String result;
    private int subposition;
    private Iterator<Row> it;
    private Row row;
    private ArrayList<String> pairsList;
    private ArrayList<Integer> wrongCellsNumbers;
    private InputStream in;
    private ArrayList<LocalDate> mondayFirst;
    private ArrayList<LocalDate> tuesdayFirst;
    private ArrayList<LocalDate> wednesdayFirst;
    private ArrayList<LocalDate> thursdayFirst;
    private ArrayList<LocalDate> fridayFirst;
    private ArrayList<LocalDate> saturdayFirst;
    private ArrayList<LocalDate> mondaySecond;
    private ArrayList<LocalDate> tuesdaySecond;
    private ArrayList<LocalDate> wednesdaySecond;
    private ArrayList<LocalDate> thursdaySecond;
    private ArrayList<LocalDate> fridaySecond;
    private ArrayList<LocalDate> saturdaySecond;

    /**
     * На основе дат начала/конца создаёт списки дат под конкретный день недели c учётом того, какая неделя является первой
     * @param iWeekFactor указывает на то, какая неделя является первой
     */
    private void transtormStartFinishToCOncreteDates(LocalDate startDate, LocalDate finishDate,int iWeekFactor){
        mondayFirst = new ArrayList<>();
        tuesdayFirst = new ArrayList<>();
        wednesdayFirst = new ArrayList<>();
        thursdayFirst = new ArrayList<>();
        fridayFirst = new ArrayList<>();
        saturdayFirst = new ArrayList<>();
        mondaySecond = new ArrayList<>();
        tuesdaySecond = new ArrayList<>();
        wednesdaySecond = new ArrayList<>();
        thursdaySecond = new ArrayList<>();
        fridaySecond = new ArrayList<>();
        saturdaySecond = new ArrayList<>();

        for(LocalDate j=startDate;!j.equals(finishDate.plusDays(1));j=j.plusDays(1)){
            if(j.getDayOfWeek()==1 && j.getWeekOfWeekyear()%2==iWeekFactor%2) mondayFirst.add(j);
            if(j.getDayOfWeek()==2 && j.getWeekOfWeekyear()%2==iWeekFactor%2) tuesdayFirst.add(j);
            if(j.getDayOfWeek()==3 && j.getWeekOfWeekyear()%2==iWeekFactor%2) wednesdayFirst.add(j);
            if(j.getDayOfWeek()==4 && j.getWeekOfWeekyear()%2==iWeekFactor%2) thursdayFirst.add(j);
            if(j.getDayOfWeek()==5 && j.getWeekOfWeekyear()%2==iWeekFactor%2) fridayFirst.add(j);
            if(j.getDayOfWeek()==6 && j.getWeekOfWeekyear()%2==iWeekFactor%2) saturdayFirst.add(j);
            if(j.getDayOfWeek()==1 && j.getWeekOfWeekyear()%2==(iWeekFactor+1)%2) mondaySecond.add(j);
            if(j.getDayOfWeek()==2 && j.getWeekOfWeekyear()%2==(iWeekFactor+1)%2) tuesdaySecond.add(j);
            if(j.getDayOfWeek()==3 && j.getWeekOfWeekyear()%2==(iWeekFactor+1)%2) wednesdaySecond.add(j);
            if(j.getDayOfWeek()==4 && j.getWeekOfWeekyear()%2==(iWeekFactor+1)%2) thursdaySecond.add(j);
            if(j.getDayOfWeek()==5 && j.getWeekOfWeekyear()%2==(iWeekFactor+1)%2) fridaySecond.add(j);
            if(j.getDayOfWeek()==6 && j.getWeekOfWeekyear()%2==(iWeekFactor+1)%2) saturdaySecond.add(j);
        }

    }

    public Parser(File file){
        this.file = file;
        this.init();

    }

    public Parser(File file, Component parent){
        this.file = file;
        this.parent = parent;
        this.init();
    }

    private void init(){
        wrongCellsNumbers = new ArrayList<>();
        result = "";
        try {
            in = new FileInputStream(this.file);
            wb = new HSSFWorkbook(in);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, ex);
        }
/**
 * тут описываю собирание временные промежутки для пар
 */
        pairsList = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);
        it = sheet.iterator();
       row = it.next();
        //Iterator<Cell> cells = row.iterator();
        //cells.next();
       // Cell cell ;
        for (int i = 0; i < 6; i++) if(it.hasNext()) it.next();
        for (int i = 0; i < 7; i++) {
            if(it.hasNext()){
                row = it.next();
                pairsList.add(row.getCell(0).getStringCellValue());
                if(it.hasNext()) for (int j = 0; j < 3; j++) it.next();
            }

         }
        sheet = wb.getSheetAt(0);
        it = sheet.iterator();
        int position = 0;
        this.subposition = 6;

    }

    public void setParent(Component parent){
        this.parent = parent;
    }

    public void setFile(File file){
        this.file = file;
    }



    public ArrayList<Pair> parsePair(LocalDate startDate, LocalDate finishDate,int iWeakFactor){
        transtormStartFinishToCOncreteDates(startDate,finishDate,iWeakFactor);
        PairDateBuilder pairDateBuilder = PairDateBuilder.getPairDateBuilder();
        pairDateBuilder.withHoliday(false).withPairDate(new LocalDate());
        ArrayList<PairBuilder> pairBuilderList = new ArrayList<>();
        ArrayList<PairBuilder> pairBuilderListForWrongs = new ArrayList<>();

        ArrayList<Pair> pairs = new ArrayList<>();


        ArrayList<PairName> pairNames = new ArrayList<>();
        ArrayList<PairName> pairNamesForWrong = new ArrayList<>();
        while (it.hasNext()){
            if (subposition==0){
                Iterator<Cell> cells = row.iterator();
                cells.next();
                if(!cells.hasNext())   break;
                Cell cell = cells.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        result = cell.getStringCellValue();
                        break;
                    default:
                        result = "";
                        break;
                }

                PrepodBuilder builder = PrepodBuilder.getPrepodBuilder();

                StringTokenizer tokenizer = new StringTokenizer(result," .");
                if(tokenizer.hasMoreTokens()){
                    builder.withLastName(tokenizer.nextToken());
                }
                if(tokenizer.hasMoreTokens()){
                    builder.withName(tokenizer.nextToken());
                }
                if(tokenizer.hasMoreTokens()){
                    builder.withMiddleName(tokenizer.nextToken());
                }
                cell = cells.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        result = cell.getStringCellValue();
                        break;
                    default:
                        result = "";
                        break;
                }
                tokenizer = new StringTokenizer(result);
                if(tokenizer.hasMoreTokens()){
                    tokenizer.nextToken();
                    StringBuilder stringBuilder = new StringBuilder();
                    while (tokenizer.hasMoreTokens()){
                        stringBuilder.append(tokenizer.nextToken()).append(" ");
                    }
                    builder.withFaculty(stringBuilder.toString());
                }

                Prepod prepod = builder.build();
                row = it.next();
                for (int iter=0; iter<7; iter++) {
                    wrongCellsNumbers.clear();
                    row = it.next();
                    cells = row.iterator();
                    cells.next();
                    for (int i = 0; i < 6; i++)//первая строчка внутри препода
                    if(cells.hasNext())
                    {

                        cell = cells.next();
                       // String qqqqq = cell.getStringCellValue().replace(" ", "");
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING && !cell.getStringCellValue().replace(" ", "").equals("")  && !cell.getStringCellValue().replaceAll(" ", "").equals( null)) {

                            PairBuilder pairBuilder = PairBuilder.getPairBuilder();
                            switch (i) {
                                case 0:
                                    if(mondayFirst!=null)
                                    if(mondayFirst.size()>0){
                                    ArrayList<PairDate> pairDates = new ArrayList<>();
                                    PairDateBuilder dateBuilder = PairDateBuilder.getPairDateBuilder();
                                    for (LocalDate datetime : mondayFirst) {
                                        pairDates.add(dateBuilder.withPairDate(datetime).withHoliday(false).build());
                                    }
                                    pairBuilder.withPairDates(pairDates);
                                    }
                                    break;
                                case 1:
                                    if(tuesdayFirst!=null)
                                    if(tuesdayFirst.size()>0){
                                    ArrayList<PairDate> pairDates1 = new ArrayList<>();
                                    PairDateBuilder dateBuilder1 = PairDateBuilder.getPairDateBuilder();
                                    for (LocalDate datetime : tuesdayFirst) {
                                        pairDates1.add(dateBuilder1.withPairDate(datetime).withHoliday(false).build());
                                    }
                                    pairBuilder.withPairDates(pairDates1);
                                    }
                                    break;
                                case 2:
                                    if(wednesdayFirst!=null)
                                    if(wednesdayFirst.size()>0){
                                    ArrayList<PairDate> pairDates2 = new ArrayList<>();
                                    PairDateBuilder dateBuilder2 = PairDateBuilder.getPairDateBuilder();
                                    for (LocalDate datetime : wednesdayFirst) {
                                        pairDates2.add(dateBuilder2.withPairDate(datetime).withHoliday(false).build());
                                    }
                                    pairBuilder.withPairDates(pairDates2);
                                    }
                                    break;
                                case 3:
                                    if(thursdayFirst!=null)
                                    if(thursdayFirst.size()>0) {
                                        ArrayList<PairDate> pairDates3 = new ArrayList<>();
                                        PairDateBuilder dateBuilder3 = PairDateBuilder.getPairDateBuilder();
                                        for (LocalDate datetime : thursdayFirst) {
                                            pairDates3.add(dateBuilder3.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates3);
                                    }
                                    break;
                                case 4:
                                    if(fridayFirst!=null)
                                    if(fridayFirst.size()>0){
                                    ArrayList<PairDate> pairDates4 = new ArrayList<>();
                                    PairDateBuilder dateBuilder4 = PairDateBuilder.getPairDateBuilder();
                                    for (LocalDate datetime : fridayFirst) {
                                        pairDates4.add(dateBuilder4.withPairDate(datetime).withHoliday(false).build());
                                    }
                                    pairBuilder.withPairDates(pairDates4);
                                    }
                                    break;
                                case 5:
                                    if(saturdayFirst!=null)
                                    if(saturdayFirst.size()>0) {
                                        ArrayList<PairDate> pairDates5 = new ArrayList<>();
                                        PairDateBuilder dateBuilder5 = PairDateBuilder.getPairDateBuilder();
                                        for (LocalDate datetime : saturdayFirst) {
                                            pairDates5.add(dateBuilder5.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates5);
                                    }
                                    break;
                            }
                            pairBuilder.withPrepod(prepod);
                            result = cell.getStringCellValue();
                            GroupBuilder groupBuilder = GroupBuilder.getGroupBuilder();
                            StringTokenizer stringTokenizer = new StringTokenizer(result, ".");
                            groupBuilder.withName(stringTokenizer.nextToken());
                            pairBuilder.withGroup(groupBuilder.build());
                            ClassroomBuilder classroomBuilder = ClassroomBuilder.getClassroomBuilder();
                            if (stringTokenizer.hasMoreTokens()) {
                                StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), " -");
                                if (stringTokenizer1.countTokens() >= 2) {
                                    try{
                                        classroomBuilder.withBuilding(Integer.decode(stringTokenizer1.nextToken()));
                                    } catch (NumberFormatException e){
                                        classroomBuilder.withBuilding(0);
                                    }
                                    String tokenString = stringTokenizer1.nextToken();
                                    try {

                                        classroomBuilder.withNum(Integer.decode(tokenString.substring(0, tokenString.length())));
                                        classroomBuilder.withFloor();
                                    } catch (NumberFormatException e){
                                        classroomBuilder.withNum(0).withFloor(1);
                                    } catch (StringIndexOutOfBoundsException e){
                                        try {
                                            classroomBuilder.withNum(Integer.decode(tokenString.substring(0, 1)));
                                        }catch (StringIndexOutOfBoundsException ex){
                                            classroomBuilder.withNum(Integer.decode(tokenString.substring(0, 0)));
                                        }
                                        classroomBuilder.withFloor(Integer.decode(String.valueOf(tokenString.charAt(0))));
                                    }

                                }
                            }

                            // classroomBuilder.withBuilding(Integer.decode(stringTokenizer1.nextToken())).withNum(Integer.decode(stringTokenizer1.nextToken())).withFloor();
                            // classroomBuilder.withBuilding(1).withNum(202).withFloor();
                           try {
                               pairBuilder.withClassroom(classroomBuilder.build());
                           }catch (Exception e){
                               JOptionPane.showMessageDialog(null,"Подозрительный результат считывания из ячейки со следующим текстом:"+result);
                           }
                            PairNumBuilder pairNumBuilder = PairNumBuilder.getPairNumBuilder();
                          //  PairType[] pairTypes = PairType.values();
                            pairNumBuilder.withNum(iter).withWeek(Week.first).withTime(pairsList.get(iter));
                            pairBuilder.withPairNum(pairNumBuilder.build());
                            pairBuilderList.add(pairBuilder);
                        }

                    }//конец первой строчки
                    Row prevRow = row;
                    row = it.next();
                    cells = row.iterator();
                    for (int i = 0; i < 7; i++)//вторая строчка внутри препода
                    if(cells.hasNext())
                    {
                        cell = cells.next();
                        //String qqqqq = cell.getStringCellValue().replace(" ", "");
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING && !cell.getStringCellValue().replace(" ", "").equals("") && !cell.getStringCellValue().replaceAll(" ", "").equals(null) /* && cell.getStringCellValue().charAt(cell.getStringCellValue().length() - 1) != (char) 32*/) {
                           String qqqqq = prevRow.getCell(cell.getColumnIndex()).getStringCellValue().replace(" ", "");
                            if(qqqqq.length()!=0){
                                result = cell.getStringCellValue();
                                StringTokenizer st = new StringTokenizer(result, ".");
                                PairNameBuilder pairNameBuilder = PairNameBuilder.getPairNameBuilder();
                                String pairType = st.nextToken();
                                switch (pairType) {
                                    case "лек":
                                        pairNameBuilder.withPairType(PairType.lecture);
                                        break;
                                    case " пр":
                                        pairNameBuilder.withPairType(PairType.practice);
                                        break;
                                    case "лаб":
                                        pairNameBuilder.withPairType(PairType.lab);
                                        break;
                                    default:
                                        pairNameBuilder.withPairType(PairType.other);
                                        break;
                                }
                                if (st.hasMoreTokens()) {
                                    pairNameBuilder.withName(st.nextToken());
                                } else pairNameBuilder.withName(pairType);
                                try {
                                    pairNames.add(pairNameBuilder.build());
                                } catch (Exception e){
                                    JOptionPane.showMessageDialog(null,"Подозрительный результат считывания из ячейки со следующим текстом:"+result);
                                }

                            }else{///если пара есть на обоих неделях и объявлена со сдвигом на одну строку
                                wrongCellsNumbers.add(cell.getColumnIndex());
                                PairBuilder pairBuilder = PairBuilder.getPairBuilder();
                                switch (i) {
                                    case 0:
                                        if(((!mondayFirst.equals(null)) && (mondayFirst.size() > 0))||((!mondaySecond.equals(null)) && mondaySecond.size() > 0)){
                                        ArrayList<PairDate> pairDates = new ArrayList<>();
                                        PairDateBuilder dateBuilder = PairDateBuilder.getPairDateBuilder();
                                        if(mondayFirst!=null)for (LocalDate datetime : mondayFirst) {
                                            pairDates.add(dateBuilder.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        if(mondaySecond!=null)for (LocalDate datetime : mondaySecond) {
                                            pairDates.add(dateBuilder.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates);}
                                        break;
                                    case 1:
                                        if(tuesdayFirst.equals(null)?tuesdayFirst.size()>0: (thursdaySecond.equals(null)) && thursdaySecond.size() > 0){
                                        ArrayList<PairDate> pairDates1 = new ArrayList<>();
                                        PairDateBuilder dateBuilder1 = PairDateBuilder.getPairDateBuilder();
                                        if(tuesdayFirst!=null)for (LocalDate datetime : tuesdayFirst) {
                                            pairDates1.add(dateBuilder1.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        if(tuesdayFirst!=null)for (LocalDate datetime : tuesdayFirst) {
                                            pairDates1.add(dateBuilder1.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates1);
                                        }
                                        break;
                                    case 2:
                                        if(wednesdayFirst!=null?wednesdayFirst.size()>0: (wednesdaySecond.equals(null)) && wednesdaySecond.size() > 0){
                                        ArrayList<PairDate> pairDates2 = new ArrayList<>();
                                        PairDateBuilder dateBuilder2 = PairDateBuilder.getPairDateBuilder();
                                        if(wednesdayFirst!=null) for (LocalDate datetime : wednesdayFirst) {
                                            pairDates2.add(dateBuilder2.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        if(wednesdaySecond!=null)for (LocalDate datetime : wednesdaySecond) {
                                            pairDates2.add(dateBuilder2.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates2);
                                        }
                                        break;
                                    case 3:
                                        if(thursdayFirst!=null?thursdayFirst.size()>0: (thursdaySecond.equals(null)) && thursdaySecond.size() > 0){
                                        ArrayList<PairDate> pairDates3 = new ArrayList<>();
                                        PairDateBuilder dateBuilder3 = PairDateBuilder.getPairDateBuilder();
                                        if(thursdayFirst!=null)for (LocalDate datetime : thursdayFirst) {
                                            pairDates3.add(dateBuilder3.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        if(thursdaySecond!=null)for (LocalDate datetime : thursdaySecond) {
                                            pairDates3.add(dateBuilder3.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates3);
                                        }
                                        break;
                                    case 4:

                                        if(fridayFirst!=null?fridayFirst.size()>0: (fridaySecond.equals(null)) && fridaySecond.size() > 0){
                                        ArrayList<PairDate> pairDates4 = new ArrayList<>();
                                        PairDateBuilder dateBuilder4 = PairDateBuilder.getPairDateBuilder();
                                        if(fridayFirst!=null)for (LocalDate datetime : fridayFirst) {
                                            pairDates4.add(dateBuilder4.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        if(fridaySecond!=null)for (LocalDate datetime : fridaySecond) {
                                            pairDates4.add(dateBuilder4.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates4);
                                        }
                                        break;
                                    case 5:
                                        if(saturdayFirst!=null?saturdayFirst.size()>0: (fridaySecond.equals(null)) && fridaySecond.size() > 0){
                                        ArrayList<PairDate> pairDates5 = new ArrayList<>();
                                        PairDateBuilder dateBuilder5 = PairDateBuilder.getPairDateBuilder();
                                        if (saturdayFirst!=null)for (LocalDate datetime : saturdayFirst) {
                                            pairDates5.add(dateBuilder5.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        if(saturdaySecond!=null)for (LocalDate datetime : saturdaySecond) {
                                            pairDates5.add(dateBuilder5.withPairDate(datetime).withHoliday(false).build());
                                        }
                                        pairBuilder.withPairDates(pairDates5);
                                        }
                                        break;
                                }
                                pairBuilder.withPrepod(prepod);
                                result = cell.getStringCellValue();
                                GroupBuilder groupBuilder = GroupBuilder.getGroupBuilder();
                                StringTokenizer stringTokenizer = new StringTokenizer(result, ".");
                                groupBuilder.withName(stringTokenizer.nextToken());
                                pairBuilder.withGroup(groupBuilder.build());
                                ClassroomBuilder classroomBuilder = ClassroomBuilder.getClassroomBuilder();
                                if (stringTokenizer.hasMoreTokens()) {
                                    StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), " -");
                                    if (stringTokenizer1.countTokens() >= 2) {
                                        try{
                                            classroomBuilder.withBuilding(Integer.decode(stringTokenizer1.nextToken()));
                                        } catch (NumberFormatException e){
                                            classroomBuilder.withBuilding(0);
                                        }
                                        String tokenString = stringTokenizer1.nextToken();
                                        try {

                                            classroomBuilder.withNum(Integer.decode(tokenString.substring(0,  tokenString.length())));
                                            classroomBuilder.withFloor();
                                        } catch (NumberFormatException e){
                                            classroomBuilder.withNum(0).withFloor(1);
                                        } catch (StringIndexOutOfBoundsException e){
                                            try {
                                                classroomBuilder.withNum(Integer.decode(tokenString.substring(0, 1)));
                                            }catch (StringIndexOutOfBoundsException ex){
                                                classroomBuilder.withNum(Integer.decode(tokenString.substring(0, 0)));
                                            }
                                            classroomBuilder.withFloor(Integer.decode(String.valueOf(tokenString.charAt(0))));
                                        }
                                    }
                                }

                                // classroomBuilder.withBuilding(Integer.decode(stringTokenizer1.nextToken())).withNum(Integer.decode(stringTokenizer1.nextToken())).withFloor();
                                // classroomBuilder.withBuilding(1).withNum(202).withFloor();
                                try {
                                    pairBuilder.withClassroom(classroomBuilder.build());
                                }catch (Exception e){
                                    JOptionPane.showMessageDialog(null,"Подозрительный результат считывания из ячейки со следующим текстом:"+result);
                                }
                                PairNumBuilder pairNumBuilder = PairNumBuilder.getPairNumBuilder();
                               // PairType[] pairTypes = PairType.values();
                                pairNumBuilder.withNum(iter).withWeek(Week.first).withTime(pairsList.get(iter));
                                pairBuilder.withPairNum(pairNumBuilder.build());
                                //pairBuilder.withPairDate(pairDateBuilder.build());
                                pairBuilderListForWrongs.add(pairBuilder);
                                pairNumBuilder.withNum(iter).withWeek(Week.second).withTime(pairsList.get(iter));
                                pairBuilder.withPairNum(pairNumBuilder.build());
                                pairBuilderListForWrongs.add(pairBuilder);

                            }

                        }
                    }//конец второй строчки
                    //prevRow = row;
                    row = it.next();
                    cells = row.iterator();
                    cells.next();
                    for (int i = 0; i < 6; i++)//первая строчка внутри препода(2)
                    if(cells.hasNext())
                    {
                        cell = cells.next();
                        String qqqqq = cell.getStringCellValue().replace(" ", "");
                        if (qqqqq.length() != 0 && cell.getCellType() == Cell.CELL_TYPE_STRING && !cell.getStringCellValue().replace(" ", "").equals("") && cell.getStringCellValue().replaceAll(" ", "") != null) {
                            if (!wrongCellsNumbers.contains(cell.getColumnIndex())) {
                                PairBuilder pairBuilder = PairBuilder.getPairBuilder();
                                switch (i) {
                                    case 0:
                                        if(mondaySecond!=null)
                                        if (mondaySecond.size()>0) {
                                            ArrayList<PairDate> pairDates = new ArrayList<>();
                                            PairDateBuilder dateBuilder = PairDateBuilder.getPairDateBuilder();
                                            for (LocalDate datetime : mondaySecond) {
                                                pairDates.add(dateBuilder.withPairDate(datetime).withHoliday(false).build());
                                            }
                                            pairBuilder.withPairDates(pairDates);
                                        }
                                        break;
                                    case 1:
                                        if(tuesdayFirst!=null)
                                        if(tuesdayFirst.size()>0) {
                                            ArrayList<PairDate> pairDates1 = new ArrayList<>();
                                            PairDateBuilder dateBuilder1 = PairDateBuilder.getPairDateBuilder();
                                            for (LocalDate datetime : tuesdayFirst) {
                                                pairDates1.add(dateBuilder1.withPairDate(datetime).withHoliday(false).build());
                                            }
                                            pairBuilder.withPairDates(pairDates1);
                                        }
                                        break;
                                    case 2:
                                        if(wednesdaySecond!=null)
                                        if (wednesdaySecond.size()>0) {
                                            ArrayList<PairDate> pairDates2 = new ArrayList<>();
                                            PairDateBuilder dateBuilder2 = PairDateBuilder.getPairDateBuilder();
                                            for (LocalDate datetime : wednesdaySecond) {
                                                pairDates2.add(dateBuilder2.withPairDate(datetime).withHoliday(false).build());
                                            }
                                            pairBuilder.withPairDates(pairDates2);

                                        }
                                        break;
                                    case 3:
                                        if(thursdaySecond!=null)
                                        if(thursdaySecond.size()>0) {
                                            ArrayList<PairDate> pairDates3 = new ArrayList<>();
                                            PairDateBuilder dateBuilder3 = PairDateBuilder.getPairDateBuilder();
                                            for (LocalDate datetime : thursdaySecond) {
                                                pairDates3.add(dateBuilder3.withPairDate(datetime).withHoliday(false).build());
                                            }
                                            pairBuilder.withPairDates(pairDates3);
                                        }
                                        break;
                                    case 4:
                                        if(fridaySecond!=null)
                                        if(fridaySecond.size()>0) {
                                            ArrayList<PairDate> pairDates4 = new ArrayList<>();
                                            PairDateBuilder dateBuilder4 = PairDateBuilder.getPairDateBuilder();
                                            for (LocalDate datetime : fridaySecond) {
                                                pairDates4.add(dateBuilder4.withPairDate(datetime).withHoliday(false).build());
                                            }
                                            pairBuilder.withPairDates(pairDates4);
                                        }
                                        break;
                                    case 5:
                                        if(saturdaySecond!=null)
                                        if(saturdaySecond.size()>0) {
                                            ArrayList<PairDate> pairDates5 = new ArrayList<>();
                                            PairDateBuilder dateBuilder5 = PairDateBuilder.getPairDateBuilder();
                                            for (LocalDate datetime : saturdaySecond) {
                                                pairDates5.add(dateBuilder5.withPairDate(datetime).withHoliday(false).build());
                                            }
                                            pairBuilder.withPairDates(pairDates5);
                                        }
                                        break;
                                }
                                pairBuilder.withPrepod(prepod);
                                result = cell.getStringCellValue();
                                GroupBuilder groupBuilder = GroupBuilder.getGroupBuilder();
                                StringTokenizer stringTokenizer = new StringTokenizer(result, ".");
                                groupBuilder.withName(stringTokenizer.nextToken());
                                pairBuilder.withGroup(groupBuilder.build());
                                ClassroomBuilder classroomBuilder = ClassroomBuilder.getClassroomBuilder();
                                if (stringTokenizer.hasMoreTokens()) {
                                    StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), " -");
                                    if (stringTokenizer1.countTokens() >= 2) {
                                        try{
                                            classroomBuilder.withBuilding(Integer.decode(stringTokenizer1.nextToken()));
                                        } catch (NumberFormatException e){
                                            classroomBuilder.withBuilding(0);
                                        }
                                        String tokenString = stringTokenizer1.nextToken();
                                        try {

                                        classroomBuilder.withNum(Integer.decode(tokenString.substring(0,  tokenString.length())));
                                            classroomBuilder.withFloor();
                                        } catch (NumberFormatException e){
                                            classroomBuilder.withNum(0).withFloor(1);
                                        } catch (StringIndexOutOfBoundsException e){
                                            try {
                                                classroomBuilder.withNum(Integer.decode(tokenString.substring(0, 1)));
                                            }catch (StringIndexOutOfBoundsException ex){
                                                classroomBuilder.withNum(Integer.decode(tokenString.substring(0, 0)));
                                            }
                                            classroomBuilder.withFloor(Integer.decode(String.valueOf(tokenString.charAt(0))));
                                        }
                                    }
                                } else if (result.toLowerCase().equals("физкультура") ){
                                    classroomBuilder.withNum(0).withBuilding(9).withFloor(0);
                                }else classroomBuilder.withNum(0).withBuilding(0).withFloor(0);



                                // classroomBuilder.withBuilding(Integer.decode(stringTokenizer1.nextToken())).withNum(Integer.decode(stringTokenizer1.nextToken())).withFloor();
                                // classroomBuilder.withBuilding(1).withNum(202).withFloor();
                                try {
                                    pairBuilder.withClassroom(classroomBuilder.build());
                                }catch (Exception e){
                                    JOptionPane.showMessageDialog(null,"Подозрительный результат считывания из ячейки со следующим текстом:"+result);
                                }
                                PairNumBuilder pairNumBuilder = PairNumBuilder.getPairNumBuilder();
                              //  PairType[] pairTypes = PairType.values();
                                pairNumBuilder.withNum(iter).withWeek(Week.second).withTime(pairsList.get(iter));
                                pairBuilder.withPairNum(pairNumBuilder.build());
                                pairBuilderList.add(pairBuilder);
                            }else {///если эта колонка из "этих", в которых криво написано
                                result = cell.getStringCellValue();
                                StringTokenizer st = new StringTokenizer(result, ".");
                                PairNameBuilder pairNameBuilder = PairNameBuilder.getPairNameBuilder();
                                String pairType = st.nextToken();
                                switch (pairType) {
                                    case "лек":
                                        pairNameBuilder.withPairType(PairType.lecture);
                                        break;
                                    case " пр":
                                        pairNameBuilder.withPairType(PairType.practice);
                                        break;
                                    case "лаб":
                                        pairNameBuilder.withPairType(PairType.lab);
                                        break;
                                    default:
                                        pairNameBuilder.withPairType(PairType.other);
                                        break;
                                }
                                if (st.hasMoreTokens()) {
                                    pairNameBuilder.withName(st.nextToken());
                                } else pairNameBuilder.withName(pairType);
                                try {
                                    pairNamesForWrong.add(pairNameBuilder.build());
                                } catch (Exception e){
                                    JOptionPane.showMessageDialog(null,"Подозрительный результат считывания из ячейки со следующим текстом:"+result);
                                }
                            }
                        }

                    }//конец первой строчки
                    row = it.next();
                    cells = row.iterator();
                    for (int i = 0; i < 7; i++)//вторая строчка внутри препода
                    if(cells.hasNext())
                    {
                        cell = cells.next();
                        //String cellstr = cell.getStringCellValue().replace(" ","");
                        String qqqqq = cell.getStringCellValue().replace(" ", "");
                        if (qqqqq.length()!=0 && cell.getCellType() == Cell.CELL_TYPE_STRING && !cell.getStringCellValue().replace(" ", "").equals("") && cell.getStringCellValue().replaceAll(" ", "") != null/* && cell.getStringCellValue().charAt(cell.getStringCellValue().length() - 1) != (char) 32*/) {
                            result = cell.getStringCellValue();
                            StringTokenizer st = new StringTokenizer(result, ".");
                            PairNameBuilder pairNameBuilder = PairNameBuilder.getPairNameBuilder();
                            String pairType = st.nextToken();
                            switch (pairType) {
                                case "лек":
                                    pairNameBuilder.withPairType(PairType.lecture);
                                    break;
                                case " пр":
                                    pairNameBuilder.withPairType(PairType.practice);
                                    break;
                                case "лаб":
                                    pairNameBuilder.withPairType(PairType.lab);
                                    break;
                                default:
                                    pairNameBuilder.withPairType(PairType.other);
                                    break;
                            }
                            if (st.hasMoreTokens()) {
                                pairNameBuilder.withName(st.nextToken());
                            } else pairNameBuilder.withName(pairType);
                            try {
                                pairNames.add(pairNameBuilder.build());
                            } catch (Exception e){
                                JOptionPane.showMessageDialog(null,"Подозрительный результат считывания из ячейки со следующим текстом:"+result);
                            }
                        }
                    }//конец второй строчки

                }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
                if (it.hasNext()) this.subposition+=7;
            }
            row = it.next();
            this.subposition--;


        }

        /**
         * короче пары считали, считали их имена, таперь будем списочек на пердачу готовить
         */
        for (int i=0;i< pairNames.size();i++){
            PairBuilder pb = pairBuilderList.get(i);
            pb.withPairName(pairNames.get(i));
            try {
                for (Pair pair : pb.build()) {
                    pairs.add(pair);
                }
            }catch(NullPointerException ignored){
            }
        }
        for(int i=0;i< pairNamesForWrong.size();i++){
            PairBuilder pb = pairBuilderListForWrongs.get(i * 2);
            pb.withPairName(pairNamesForWrong.get(i));
            try {
                for (Pair pair : pb.build()) {
                    pairs.add(pair);
                }
            } catch (NullPointerException ignored){

            }
            pb = pairBuilderListForWrongs.get(i * 2 + 1);
            pb.withPairName(pairNamesForWrong.get(i));
            try {
                for (Pair pair : pb.build()) {
                    pairs.add(pair);
                }
            }catch (NullPointerException ignored){

            }
        }

       /* try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        return pairs;
        //return null;
    }

    public Prepod parsePrepod(){
        if(this.file != null) {
            while (it.hasNext()) {
                Row row = it.next();
                this.subposition--;
                if (subposition==0){
                    Iterator<Cell> cells = row.iterator();
                    cells.next();
                    Cell cell = cells.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            result = cell.getStringCellValue();
                            break;
                        default:
                            result = "";
                            break;
                    }

                    PrepodBuilder builder = PrepodBuilder.getPrepodBuilder();

                    StringTokenizer tokenizer = new StringTokenizer(result," .");
                    if(tokenizer.hasMoreTokens()){
                        builder.withLastName(tokenizer.nextToken());
                    }
                    if(tokenizer.hasMoreTokens()){
                        builder.withName(tokenizer.nextToken());
                    }
                    if(tokenizer.hasMoreTokens()){
                        builder.withMiddleName(tokenizer.nextToken());
                    }
                    cell = cells.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            result = cell.getStringCellValue();
                            break;
                        default:
                            result = "";
                            break;
                    }
                    tokenizer = new StringTokenizer(result);
                    if(tokenizer.hasMoreTokens()){
                        tokenizer.nextToken();
                        StringBuilder stringBuilder = new StringBuilder();
                        while (tokenizer.hasMoreTokens()){
                            stringBuilder.append(tokenizer.nextToken()).append(" ");
                        }
                        builder.withFaculty(stringBuilder.toString());
                    }

                    // subposition = 36;
                    return builder.build();

                }

            }

        }else JOptionPane.showMessageDialog(parent,"Файл не задан");
        return null;
    }
}
