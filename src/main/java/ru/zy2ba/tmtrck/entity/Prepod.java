package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.LocalTime;
import ru.zy2ba.tmtrck.entity.enums.PrepodRang;

import javax.persistence.*;

/**
 * класс-сущность описывающий преподавател€ - ‘.».ќ. и принадлежность к факультету
 * @Task нарастить класс до уровн€ пользовательской записи
 * @Link PrepodManager обеспечивает работу с этим классом посредством
 * @See PrepodDao
 * @author Zy2ba
 * @since 04.05.2015
 */
@Entity
@Table(name = "PREPOD")
public class Prepod {
    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment",strategy = "increment")
    private long id;

    /**
     * @value им€
     */
    @Column(name = "name")
    private String name;

    /**
     * @value фамили€
     */
    @Column(name = "lastName")
    private String lastName;

    /**
     * @value отчество
     */
    @Column(name = "middleName")
    private String middleName;

    @Column(name = "isWorking")
    private boolean isWorking;

    /**
     * @value кафедра
     */
    @Column(name = "faculty")
    private String faculty;

    @Column(name = "password")
    private int password;

    @Column(name = "passkey")
    private int passkey;

    @Column(name = "lastActivity")
    private LocalTime lastActivity;

    @Column(name = "canMakeOthchet")
    private boolean canMakeOtchet;

    @Column(name = "canAddPlans")
    private boolean canAddPlans;

    @Column(name = "rang")
    @Enumerated(EnumType.STRING)
    private PrepodRang rang ;//звание

    @Column(name = "position")
    private String position ;//должность

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Plan activePlan;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPasskey(){
        return this.passkey;
    }

    public void setPasskey(){
        this.passkey = lastActivity.hashCode();
    }


    public void setLastActivity(){
        this.lastActivity = new LocalTime();
    }

    public LocalTime getLastActivity(){
        return this.lastActivity;
    }

    public boolean getIsWorking(){
        return this.isWorking;
    }

    public void setIsWorking(boolean param){
        this.isWorking = param;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }


    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFaculty() {
        return faculty;
    }


    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }


    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
}

    public boolean isCanMakeOtchet() {
        return canMakeOtchet;
    }

    public void setCanMakeOtchet(boolean canMakeOtchet) {
        this.canMakeOtchet = canMakeOtchet;
    }

    public PrepodRang getRang() {
        return rang;
    }

    public void setRang(PrepodRang rang) {
        this.rang = rang;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Plan getActivePlan() {
        return activePlan;
    }

    public void setActivePlan(Plan activePlan) {
        this.activePlan = activePlan;
    }

    public boolean isCanAddPlans() {
        return canAddPlans;
    }

    public void setCanAddPlans(boolean canAddPlans) {
        this.canAddPlans = canAddPlans;
    }
}
