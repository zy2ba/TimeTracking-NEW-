package ru.zy2ba.tmtrck.entity;



import org.hibernate.annotations.GenericGenerator;
import org.joda.time.LocalDate;

import javax.persistence.*;

/**
 * класс-сущность описывающий пару и компилирующий в себе сведения из других классов-сущностей
 * @Link PairManager обеспечивает работу с этим классом посредством
 * @See PairDao
 * @author Zy2ba
 * @since 05.05.15
 */
@Entity
@Table(name = "PAIR")
public class Pair implements TimeTableElement{
    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;


    /**
     * @value отметка о нормальном проведении пары, по умолчанию поставлена
     * у пользователей с правами преподавателя есть возможности для редактирования этого свойства
     * однако только у принадлежащих им пар
     */
    @Column(name = "isCarriedOut")
    private boolean isCarriedOut;

    /**
     * @See Prepod
     */
    @ManyToOne//(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PREPOD_ID")
    private Prepod prepod;

    /**
     * @See Group2
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "GROUP2_ID")
    private Group2 group2;

    /**
     * @Value
     * @link Classroom
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "CLASSROOM_ID")
    private Classroom classroom;

    /**
     * @value
     * @link PairDate
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PAIRDATE_ID")
    private PairDate pairDate;

    /**
     * @value
     * @Link PairName
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PAIRNAME_ID")
    private PairName pairName;

    /**
     * @value
     * @Link PairNum
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PAIRNUM_ID")
    private  PairNum pairNum;

    public void setIsCarriedOut(boolean prop){
        this.isCarriedOut = prop;
    }

    public boolean getIsCarriedOut(){
        return this.isCarriedOut;
    }

    @Override
    public boolean getIsOnHoliday() {
        return this.pairDate.getHoliday();
    }

    public void setClassroom(Classroom classroom){
        this.classroom =classroom;
    }

    public Classroom getClassroom(){
        return this.classroom;
    }

    public void setPairDate(PairDate pairDate){
        this.pairDate =pairDate;
    }
    public PairDate getPairDate(){
        return this.pairDate;
    }

    public void setPairName(PairName pairName){
        this.pairName = pairName;
    }
    public PairName getPairName(){
        return this.pairName;
    }

    public void setPairNum(PairNum pairNum){
        this.pairNum =pairNum;
    }

    public PairNum getPairNum(){
        return this.pairNum;
    }

    public void setGroup2(Group2 group2){
        this.group2 = group2;
    }

    public Group2 getGroup2(){
        return this.group2;
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

    /**
     *
     * @param lowCaseFactor если true - возвращет в нижнем регистре, если false - с заглавной
     * @return сообщает строкой, проведена ли пара "Проведена" или "Не проведна"
     *
     */
    public String getStringIsCarriedOut(boolean lowCaseFactor){
        if(this.isCarriedOut){
            return lowCaseFactor ? "проведена" :"Проведена";
        }else {
            return lowCaseFactor ? "не проведена": "не проведена";
        }
    }

    @Override
    public LocalDate getDate() {
        return this.getPairDate().getDate();
    }

    @Override
    public String getDayOfWeekString() {
        return this.getPairDate().getDateToString();
    }

    @Override
    public int getNum() {
        return getPairNum().getNum();
    }

    @Override
    public String getTime() {
        return this.getPairNum().getTime();
    }

    @Override
    public String getLocation() {
        String location = this.getClassroom().getBuilding()+"-"+this.getClassroom().getNum();
        return location;
    }

    @Override
    public String getGroupSpacer(int length) {
        return this.getGroup2().getName(length);
    }

    @Override
    public String getName() {
        return this.getPairName().getPairTypeToString(false)+this.getPairName().getName();
    }
}
