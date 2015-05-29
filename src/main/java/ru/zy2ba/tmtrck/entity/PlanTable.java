package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;
import ru.zy2ba.tmtrck.entity.enums.OtchetTableType;

import javax.persistence.*;

/**
 * Created by Zy2ba on 19.05.2015.
 */
@Entity
@Table(name = "PLAN_TABLE")
public class PlanTable {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    //@ManyToOne(cascade = {CascadeType.ALL})
    //@JoinColumn(name = "PLAN_ID")
    //private Plan plan;

    @Column(name = "otchetTableType")
    @Enumerated(EnumType.STRING)
    private OtchetTableType otchetTableType;

    @Column(name = "stavka")
    private double stavka;

    @Column(name = "lection")
    private double lection;

    @Column(name = "lab")
    private double lab;

    @Column(name = "practice")
    private double practice;

    @Column(name = "consult")
    private double consult;

    @Column(name = "exam")
    private double exam;

    @Column(name = "zachet")
    private double zachet;

    @Column(name = "review")
    private double review;

    @Column(name = "leadingKRab")
    private double leadingKRab;

    @Column(name = "LeadingKProject")
    private double LeadingKProject;

    @Column(name = "diplomDesign")
    private double diplomDesign;

    @Column(name = "meeting")
    private double meeting;

    @Column(name = "leadingPractice")
    private double leadingPractice;

    @Column(name = "leadingMagisters")
    private double leadingMagisters;

    @Column(name = "leadingAspirants")
    private double leadingAspirants;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OtchetTableType getOtchetTableType() {
        return otchetTableType;
    }

    public void setOtchetTableType(OtchetTableType otchetTableType) {
        this.otchetTableType = otchetTableType;
    }

    public double getStavka() {
        return stavka;
    }

    public void setStavka(double stavka) {
        this.stavka = stavka;
    }

    public double getLection() {
        return lection;
    }

    public void setLection(double lection) {
        this.lection = lection;
    }

    public double getLab() {
        return lab;
    }

    public void setLab(double lab) {
        this.lab = lab;
    }

    public double getPractice() {
        return practice;
    }

    public void setPractice(double practice) {
        this.practice = practice;
    }

    public double getConsult() {
        return consult;
    }

    public void setConsult(double consult) {
        this.consult = consult;
    }

    public double getExam() {
        return exam;
    }

    public void setExam(double exam) {
        this.exam = exam;
    }

    public double getZachet() {
        return zachet;
    }

    public void setZachet(double zachet) {
        this.zachet = zachet;
    }

    public double getReview() {
        return review;
    }

    public void setReview(double review) {
        this.review = review;
    }

    public double getLeadingKRab() {
        return leadingKRab;
    }

    public void setLeadingKRab(double leadingKRab) {
        this.leadingKRab = leadingKRab;
    }

    public double getLeadingKProject() {
        return LeadingKProject;
    }

    public void setLeadingKProject(double leadingKProject) {
        LeadingKProject = leadingKProject;
    }

    public double getDiplomDesign() {
        return diplomDesign;
    }

    public void setDiplomDesign(double diplomDesign) {
        this.diplomDesign = diplomDesign;
    }

    public double getMeeting() {
        return meeting;
    }

    public void setMeeting(double meeting) {
        this.meeting = meeting;
    }

    public double getLeadingPractice() {
        return leadingPractice;
    }

    public void setLeadingPractice(double leadingPractice) {
        this.leadingPractice = leadingPractice;
    }

    public double getLeadingMagisters() {
        return leadingMagisters;
    }

    public void setLeadingMagisters(double leadingMagisters) {
        this.leadingMagisters = leadingMagisters;
    }

    public double getLeadingAspirants() {
        return leadingAspirants;
    }

    public void setLeadingAspirants(double leadingAspirants) {
        this.leadingAspirants = leadingAspirants;
    }

   /* public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }*/
}
