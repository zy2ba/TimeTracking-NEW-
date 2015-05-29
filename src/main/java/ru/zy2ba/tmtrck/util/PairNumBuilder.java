package ru.zy2ba.tmtrck.util;

import ru.zy2ba.tmtrck.entity.PairNum;
import ru.zy2ba.tmtrck.entity.enums.Week;

/**
 * @author Zy2ba
 * @since 05.05.15
 */
public class PairNumBuilder implements Builder<PairNum> {
    private long id;
    private int num;
    private String time;
    private Week week;

    private PairNumBuilder(){}

    public static PairNumBuilder getPairNumBuilder(){
        return new PairNumBuilder();
    }

    public PairNumBuilder withId(long param){
        this.id = param;
        return this;
    }

    public PairNumBuilder withWeek(Week param)
    {
        this.week = param;
        return this;
    }
    public PairNumBuilder withNum(int param){
        this.num = param;
        return this;
    }

    public  PairNumBuilder withTime(String param){
        this.time = param;
        return this;
    }

    @Override
    public PairNum build() {
        PairNum pairNum = new PairNum();
        pairNum.setId(id);
        pairNum.setNum(num);
        pairNum.setTime(time);
        pairNum.setWeek(week);
        return pairNum;
    }

}
