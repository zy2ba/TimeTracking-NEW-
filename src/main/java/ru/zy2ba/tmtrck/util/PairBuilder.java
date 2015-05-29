package ru.zy2ba.tmtrck.util;

import ru.zy2ba.tmtrck.entity.*;

import java.util.ArrayList;

/**
 * @author Zy2ba
 * @since 05.05.2015
 */

public class PairBuilder implements Builder< ArrayList<Pair> > {
    private long id;
    private boolean isCarriedOut=true;
    private Prepod prepod;
    private Classroom classroom;
    private Group2 group2;
    private ArrayList<PairDate> pairDates;
    private PairName pairName;
    private PairNum pairNum;
    private PairBuilder(){}
    public static PairBuilder getPairBuilder(){
        return new PairBuilder();
    }
    public PairBuilder withId(long id){
        this.id = id;
        return this;
    }

    public PairBuilder withPairDates(ArrayList<PairDate> pairDates){
        this.pairDates = pairDates;
        return this;
    }

    public PairBuilder withCarriedOut(boolean prop){
        this.isCarriedOut = prop;
        return this;
    }

    public PairBuilder withPrepod(Prepod param){
        this.prepod = param;
        return this;
    }
    public PairBuilder withClassroom(Classroom classroom){
        this.classroom = classroom;
        return this;
    }
    public PairBuilder withGroup(Group2 param){
        this.group2 = param;
        return this;
    }
    public PairBuilder withPairName(PairName param){
        this.pairName = param;
        return this;
    }
    public PairBuilder withPairNum(PairNum param){
        this.pairNum = param;
        return this;
    }

    public ArrayList<Pair>  build(){
        if (pairDates==null) return null;
        ArrayList<Pair> pairs = new ArrayList<>();
        for (PairDate date : pairDates) {
            Pair p = new Pair();
            p.setIsCarriedOut(this.isCarriedOut);
            p.setId(id);
            p.setClassroom(classroom);
            p.setGroup2(group2);
            p.setPairDate(date);
            p.setPairName(pairName);
            p.setPrepod(prepod);
            p.setPairNum(pairNum);
            pairs.add(p);
        }
        return pairs;
    }
}
