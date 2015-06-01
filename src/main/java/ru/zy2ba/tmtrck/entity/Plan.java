package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;
import ru.zy2ba.tmtrck.entity.enums.PrepodRang;

import javax.persistence.*;

/**
 * Created by Zy2ba on 19.05.2015.
 */
@Entity
@Table(name = "PLAN")
public class Plan {
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAN_TABLE_AUTUMN_BUDGET_ID")
    private PlanTable planTableAutumnBudget;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAN_TABLE_SPRING_BUDGET_ID")
    private PlanTable planTableSpringBudget;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAN_TABLE_AUTUMN_PLATNO_ID")
    private PlanTable planTableAutumnPlatno;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAN_TABLE_SPRING_PLATNO_ID")
    private PlanTable planTableSpringPlatno;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAN_TABLE_AUTUMN_SHORT_ID")
    private PlanTable planTableAutumnShort;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAN_TABLE_SPRING_SHORT_ID")
    private PlanTable planTableSpringShort;

    @Column(name = "startYear")
    private int startYear = 2014;

    @Column(name = "finishYear")
    private int finishYear = 2015;

    @Column(name = "rang")
    @Enumerated(EnumType.STRING)
    private PrepodRang rang ;//звание

    @Column(name = "rate")
    private double rate ;//ставка

    @Column(name = "position")
    private String position ;//должность

    @Column(name = "budgetRegular")
    private double budgetRegular;

    @Column(name = "budgetHourly")
    private double budgetHourly;

    @Column(name = "plantoRegular")
    private double plantoRegular;

    @Column(name = "plantoHourly")
    private double plantoHourly;

    @Column(name = "shortRegular")
    private double shortRegular;

    @Column(name = "shortHourly")
    private double shortHourly;

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

    public PlanTable getPlanTableAutumnBudget() {
        return planTableAutumnBudget;
    }

    public void setPlanTableAutumnBudget(PlanTable planTableAutumnBudget) {
        this.planTableAutumnBudget = planTableAutumnBudget;
    }

    public PlanTable getPlanTableSpringBudget() {
        return planTableSpringBudget;
    }

    public void setPlanTableSpringBudget(PlanTable planTableSpringBudget) {
        this.planTableSpringBudget = planTableSpringBudget;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getFinishYear() {
        return finishYear;
    }

    public void setFinishYear(int finishYear) {
        this.finishYear = finishYear;
    }

    public PrepodRang getRang() {
        return rang;
    }

    public String getRangString(){
        switch (rang){
            case DEAN: return "декан";
            case DOCENT: return "доцент";
            case SENIOR_PREPOD: return  "ст. преподаватель";
            case HEAD_OF_DEPARTMENT_NONPRODUCING:return "зав. кафедрой(не выпускающей)";
            case HEAD_OF_DEPARTMENT_PHYSICAL_EDUCATION:return "зав. кафедрой физ. воспитания";
            case HEAD_OF_DEPARTMENT_PRODUCING:return "зав. кафедрой (выпускающей)";
            case PED_WORK:return "пед.работа";
            case PROFESSOR:return "профессор";
            default: return "преподаватель";
        }
    }

    public void setRang(PrepodRang rang) {
        this.rang = rang;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public PlanTable getPlanTableAutumnPlatno() {
        return planTableAutumnPlatno;
    }

    public void setPlanTableAutumnPlatno(PlanTable planTableAutumnPlatno) {
        this.planTableAutumnPlatno = planTableAutumnPlatno;
    }

    public PlanTable getPlanTableSpringPlatno() {
        return planTableSpringPlatno;
    }

    public void setPlanTableSpringPlatno(PlanTable planTableSpringPlatno) {
        this.planTableSpringPlatno = planTableSpringPlatno;
    }

    public PlanTable getPlanTableAutumnShort() {
        return planTableAutumnShort;
    }

    public void setPlanTableAutumnShort(PlanTable planTableAutumnShort) {
        this.planTableAutumnShort = planTableAutumnShort;
    }

    public PlanTable getPlanTableSpringShort() {
        return planTableSpringShort;
    }

    public void setPlanTableSpringShort(PlanTable planTableSpringShort) {
        this.planTableSpringShort = planTableSpringShort;
    }

    public double getBudgetRegular() {
        return budgetRegular;
    }

    public void setBudgetRegular(double budgetRegular) {
        this.budgetRegular = budgetRegular;
    }

    public double getBudgetHourly() {
        return budgetHourly;
    }

    public void setBudgetHourly(double budgetHourly) {
        this.budgetHourly = budgetHourly;
    }

    public double getPlantoRegular() {
        return plantoRegular;
    }

    public void setPlantoRegular(double plantoRegular) {
        this.plantoRegular = plantoRegular;
    }

    public double getPlantoHourly() {
        return plantoHourly;
    }

    public void setPlantoHourly(double plantoHourly) {
        this.plantoHourly = plantoHourly;
    }

    public double getShortRegular() {
        return shortRegular;
    }

    public void setShortRegular(double shortRegular) {
        this.shortRegular = shortRegular;
    }

    public double getShortHourly() {
        return shortHourly;
    }

    public void setShortHourly(double shortHourly) {
        this.shortHourly = shortHourly;
    }
}
