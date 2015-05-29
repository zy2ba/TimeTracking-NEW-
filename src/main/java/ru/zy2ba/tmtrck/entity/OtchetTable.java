package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;
import ru.zy2ba.tmtrck.entity.enums.OtchetTableType;

import javax.persistence.*;

/**
 * Created by Zy2ba on 19.05.2015.
 */
@Entity
@Table(name = "OTCHET_TABLE")
public class OtchetTable {
    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "OTCHET_ID")
    private Otchet otchet;


    @Column(name = "otchetTableType")
    @Enumerated(EnumType.STRING)
    private OtchetTableType otchetTableType;

    @Column(name = "lectionFact")
    private double lectionFact;

    @Column(name = "lectionPlan")
    private double lectionPlan;

    @Column(name = "labFact")
    private double labFact;

    @Column(name = "labPlan")
    private double labPlan;

    @Column(name = "practiceFact")
    private double practiceFact;

    @Column(name = "practicePlan")
    private double practicePlan;

    @Column(name = "consultFact")
    private double consultFact;

    @Column(name = "consultPlan")
    private double consultPlan;

    @Column(name = "examFact")
    private double examFact;

    @Column(name = "examPlan")
    private double examPlan;

    @Column(name = "zachetFact")
    private double zachetFact;

    @Column(name = "zachetPlan")
    private double zachetPlan;

    @Column(name = "reviewFact")
    private double reviewFact;

    @Column(name = "reviewPlan")
    private double reviewPlan;

    @Column(name = "leadingKRabFact")
    private double leadingKRabFact;

    @Column(name = "leadingKRabPlan")
    private double leadingKRabPlan;

    @Column(name = "LeadingKProjectFact")
    private double LeadingKProjectFact;

    @Column(name = "LeadingKProjectPlan")
    private double LeadingKProjectPlan;

    @Column(name = "diplomDesignFact")
    private double diplomDesignFact;

    @Column(name = "diplomDesignPlan")
    private double diplomDesignPlan;

    @Column(name = "meetingFact")
    private double meetingFact;

    @Column(name = "meetingPlan")
    private double meetingPlan;

    @Column(name = "leadingPracticeFact")
    private double leadingPracticeFact;

    @Column(name = "leadingPracticePlan")
    private double leadingPracticePlan;

    @Column(name = "leadingMagistersFact")
    private double leadingMagistersFact;

    @Column(name = "leadingMagistersPlan")
    private double leadingMagistersPlan;

    @Column(name = "leadingAspirantsFact")
    private double leadingAspirantsFact;

    @Column(name = "leadingAspirantsPlan")
    private double leadingAspirantsPlan;

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

    public double getLectionFact() {
        return lectionFact;
    }

    public void setLectionFact(double lectionFact) {
        this.lectionFact = lectionFact;
    }

    public double getLectionPlan() {
        return lectionPlan;
    }

    public void setLectionPlan(double lectionPlan) {
        this.lectionPlan = lectionPlan;
    }

    public double getLabFact() {
        return labFact;
    }

    public void setLabFact(double labFact) {
        this.labFact = labFact;
    }

    public double getLabPlan() {
        return labPlan;
    }

    public void setLabPlan(double labPlan) {
        this.labPlan = labPlan;
    }

    public double getPracticeFact() {
        return practiceFact;
    }

    public void setPracticeFact(double practiceFact) {
        this.practiceFact = practiceFact;
    }

    public double getPracticePlan() {
        return practicePlan;
    }

    public void setPracticePlan(double practicePlan) {
        this.practicePlan = practicePlan;
    }

    public double getConsultFact() {
        return consultFact;
    }

    public void setConsultFact(double consultFact) {
        this.consultFact = consultFact;
    }

    public double getConsultPlan() {
        return consultPlan;
    }

    public void setConsultPlan(double consultPlan) {
        this.consultPlan = consultPlan;
    }

    public double getExamFact() {
        return examFact;
    }

    public void setExamFact(double examFact) {
        this.examFact = examFact;
    }

    public double getExamPlan() {
        return examPlan;
    }

    public void setExamPlan(double examPlan) {
        this.examPlan = examPlan;
    }

    public double getZachetFact() {
        return zachetFact;
    }

    public void setZachetFact(double zachetFact) {
        this.zachetFact = zachetFact;
    }

    public double getZachetPlan() {
        return zachetPlan;
    }

    public void setZachetPlan(double zachetPlan) {
        this.zachetPlan = zachetPlan;
    }

    public double getReviewFact() {
        return reviewFact;
    }

    public void setReviewFact(double reviewFact) {
        this.reviewFact = reviewFact;
    }

    public double getReviewPlan() {
        return reviewPlan;
    }

    public void setReviewPlan(double reviewPlan) {
        this.reviewPlan = reviewPlan;
    }

    public double getLeadingKRabFact() {
        return leadingKRabFact;
    }

    public void setLeadingKRabFact(double leadingKRabFact) {
        this.leadingKRabFact = leadingKRabFact;
    }

    public double getLeadingKRabPlan() {
        return leadingKRabPlan;
    }

    public void setLeadingKRabPlan(double leadingKRabPlan) {
        this.leadingKRabPlan = leadingKRabPlan;
    }

    public double getLeadingKProjectFact() {
        return LeadingKProjectFact;
    }

    public void setLeadingKProjectFact(double leadingKProjectFact) {
        LeadingKProjectFact = leadingKProjectFact;
    }

    public double getLeadingKProjectPlan() {
        return LeadingKProjectPlan;
    }

    public void setLeadingKProjectPlan(double leadingKProjectPlan) {
        LeadingKProjectPlan = leadingKProjectPlan;
    }

    public double getDiplomDesignFact() {
        return diplomDesignFact;
    }

    public void setDiplomDesignFact(double diplomDesignFact) {
        this.diplomDesignFact = diplomDesignFact;
    }

    public double getDiplomDesignPlan() {
        return diplomDesignPlan;
    }

    public void setDiplomDesignPlan(double diplomDesignPlan) {
        this.diplomDesignPlan = diplomDesignPlan;
    }

    public double getMeetingFact() {
        return meetingFact;
    }

    public void setMeetingFact(double meetingFact) {
        this.meetingFact = meetingFact;
    }

    public double getMeetingPlan() {
        return meetingPlan;
    }

    public void setMeetingPlan(double meetingPlan) {
        this.meetingPlan = meetingPlan;
    }

    public double getLeadingPracticeFact() {
        return leadingPracticeFact;
    }

    public void setLeadingPracticeFact(double leadingPracticeFact) {
        this.leadingPracticeFact = leadingPracticeFact;
    }

    public double getLeadingPracticePlan() {
        return leadingPracticePlan;
    }

    public void setLeadingPracticePlan(double leadingPracticePlan) {
        this.leadingPracticePlan = leadingPracticePlan;
    }

    public double getLeadingMagistersFact() {
        return leadingMagistersFact;
    }

    public void setLeadingMagistersFact(double leadingMagistersFact) {
        this.leadingMagistersFact = leadingMagistersFact;
    }

    public double getLeadingMagistersPlan() {
        return leadingMagistersPlan;
    }

    public void setLeadingMagistersPlan(double leadingMagistersPlan) {
        this.leadingMagistersPlan = leadingMagistersPlan;
    }

    public double getLeadingAspirantsFact() {
        return leadingAspirantsFact;
    }

    public void setLeadingAspirantsFact(double leadingAspirantsFact) {
        this.leadingAspirantsFact = leadingAspirantsFact;
    }

    public double getLeadingAspirantsPlan() {
        return leadingAspirantsPlan;
    }

    public void setLeadingAspirantsPlan(double leadingAspirantsPlan) {
        this.leadingAspirantsPlan = leadingAspirantsPlan;
    }

    public Otchet getOtchet() {
        return otchet;
    }

    public void setOtchet(Otchet otchet) {
        this.otchet = otchet;
    }
}

