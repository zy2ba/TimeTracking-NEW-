package ru.zy2ba.tmtrck.entity.ActivityTypes;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.PairDate;
import ru.zy2ba.tmtrck.entity.Prepod;
import ru.zy2ba.tmtrck.entity.TimeTableElement;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.manager.PairDateManager;
import ru.zy2ba.tmtrck.util.ResourceLocator;

import javax.persistence.*;

/**
 * Created by Zy2ba on 21.05.2015.
 */

@Entity
@Table(name = "EXAM")
public class Exam implements SettableActivity,TimeTableElement{
    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    /**
     * @See Prepod
     */
    @ManyToOne//(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PREPOD_ID")
    private Prepod prepod;

    @Column(name = "date")
    // @Temporal(TemporalType.TIMESTAMP)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate date; //дата пары

    @Column(name = "peoplesNum")
    private int peoplesNum;

    @Column(name = "typeOfLoad")
    @Enumerated(EnumType.STRING)
    private TypeOfLoad typeOfLoad;

    private static final double factor =0.4 ;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPeoplesNum() {
        return peoplesNum;
    }

    private void setPeoplesNum(int peoplesNum) {
        this.peoplesNum = peoplesNum;
    }

    public void setPrepod(Prepod prepod){
        this.prepod = prepod;
    }

    public Prepod getPrepod(){
        return this.prepod;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getHours(){
        return factor*peoplesNum;
    }
    public static double getFactor(){
        return factor;
    }

    public TypeOfLoad getTypeOfLoad() {
        return typeOfLoad;
    }

    @Override
    public void setTypeOfLoad(TypeOfLoad typeOfLoad) {
        this.typeOfLoad = typeOfLoad;
    }

    @Override
    public void setNum(int Num) {
        setPeoplesNum(Num);
    }

    @Override
    public String getDayOfWeekString() {
        int ch = date.getDayOfWeek();
        switch (ch){
            case 1:return "Пн";
            case 2:return "Вт";
            case 3:return "Ср";
            case 4:return "Чт";
            case 5:return "Пт";
            case 6:return "Сб";
            case 7:return "Вс";
        }
        return "Ошибка";
    }

    @Override
    public int getNum() {
        return -1;
    }

    @Override
    public String getTime() {
        return "...........";
    }

    @Override
    public String getLocation() {
        return ".....";
    }

    @Override
    public String getGroupSpacer(int length) {
        //String spacer = new String();
        String tolString;
        switch (typeOfLoad){
            case BUDGET:tolString = "Бюджетная"; break;
            case OFF_BUDGET:tolString = "Внебюджетная";break;
            case SHORT:tolString = "Сокращённая";break;
            default:tolString = "Бюджетная"; break;
        }
        if(tolString.length()>length){
            tolString = tolString.substring(0,length-1);
        }
        while (tolString.length()<length){
            tolString+=".";
        }
        return tolString;
    }

    @Override
    public String getName() {
        return "Экзамен "+String.format("%.2g", factor *peoplesNum)+" часов";
    }

    @Override
    public String getStringIsCarriedOut(boolean lowCaseFactor) {
        return (peoplesNum+" человек");
    }

    @Override
    public boolean getIsCarriedOut() {
        return true;
    }

    @Override
    public boolean getIsOnHoliday() {
        PairDateManager pairDateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");
        PairDate pairDate = pairDateManager.findByDate(date);
        if (pairDate==null) return false;
        return (pairDateManager.findByDate(date)).getHoliday();
    }
}
