package ru.zy2ba.tmtrck.util;

import ru.zy2ba.tmtrck.entity.Otchet;
import ru.zy2ba.tmtrck.entity.OtchetTable;
import ru.zy2ba.tmtrck.entity.enums.OtchetTableType;

/**
 * Created by Zy2ba on 20.05.2015.
 */
public class OtchetTableBuilder implements Builder<OtchetTable> {
    private long id;
    private Otchet otchet;
    private OtchetTableType otchetTableType;
    private double lectionFact;
    private double lectionPlan;
    private double labFact;
    private double labPlan;
    private double practiceFact;
    private double practicePlan;
    private double consultFact;
    private double consultPlan;
    private double examFact;
    private double examPlan;
    private double zachetFact;
    private double zachetPlan;
    private double reviewFact;
    private double reviewPlan;
    private double leadingKRabFact;
    private double leadingKRabPlan;
    private double LeadingKProjectFact;
    private double LeadingKProjectPlan;
    private double diplomDesignFact;
    private double diplomDesignPlan;
    private double meetingFact;
    private double meetingPlan;
    private double leadingPracticeFact;
    private double leadingPracticePlan;
    private double leadingMagistersFact;
    private double leadingMagistersPlan;
    private double leadingAspirantsFact;
    private double leadingAspirantsPlan;

    @Override
    public OtchetTable build() throws Exception {
        OtchetTable otchetTable = new OtchetTable();

        if(this.otchet ==null){
            otchetTable.setOtchet(new Otchet());
        }else otchetTable.setOtchet(this.otchet);
        if(this.otchetTableType==null){
            otchetTable.setOtchetTableType(OtchetTableType.FB);
        }else otchetTable.setOtchetTableType(this.otchetTableType);
        otchetTable.setConsultFact(this.consultFact);
        otchetTable.setConsultPlan(this.consultPlan);

        otchetTable.setDiplomDesignFact(this.diplomDesignFact);
        otchetTable.setDiplomDesignPlan(this.diplomDesignPlan);

        otchetTable.setPracticeFact(this.practiceFact);
        otchetTable.setPracticePlan(this.practiceFact);

        otchetTable.setExamPlan(this.examPlan);
        otchetTable.setExamFact(this.examFact);

        otchetTable.setLabPlan(this.labPlan);
        otchetTable.setLabFact(this.labFact);

        otchetTable.setLectionPlan(this.lectionPlan);
        otchetTable.setLectionFact(this.lectionFact);

        otchetTable.setMeetingPlan(this.meetingPlan);
        otchetTable.setMeetingFact(this.meetingFact);

        otchetTable.setReviewPlan(this.reviewPlan);
        otchetTable.setReviewFact(this.reviewFact);

        otchetTable.setLeadingAspirantsFact(this.leadingAspirantsFact);
        otchetTable.setLeadingAspirantsPlan(this.leadingAspirantsPlan);

        otchetTable.setLeadingKProjectFact(this.LeadingKProjectFact);
        otchetTable.setLeadingKProjectPlan(this.LeadingKProjectPlan);

        otchetTable.setLeadingKRabFact(this.leadingKRabFact);
        otchetTable.setLeadingKRabPlan(this.leadingKRabPlan);

        otchetTable.setLeadingMagistersFact(this.leadingMagistersFact);
        otchetTable.setLeadingMagistersPlan(this.leadingMagistersPlan);

        otchetTable.setLeadingPracticeFact(this.leadingPracticeFact);
        otchetTable.setLeadingPracticePlan(this.leadingPracticePlan);

        otchetTable.setZachetFact(this.zachetFact);
        otchetTable.setZachetPlan(this.zachetPlan);

        return otchetTable;
    }
}
