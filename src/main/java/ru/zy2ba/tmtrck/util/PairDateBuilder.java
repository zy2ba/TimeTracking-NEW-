package ru.zy2ba.tmtrck.util;

import org.joda.time.LocalDate;
import ru.zy2ba.tmtrck.entity.PairDate;

/**
 * @author Zy2ba
 * @since 05.05.15
 */
public class PairDateBuilder implements Builder<PairDate> {
    private long id;
    private LocalDate pairDate;
    private boolean holiday;

    private PairDateBuilder(){}

    public static PairDateBuilder getPairDateBuilder(){
        return new PairDateBuilder();
    }

    public PairDateBuilder withId(long param){
        this.id = param;
        return this;
    }

    public PairDateBuilder withPairDate(LocalDate param){
        this.pairDate = param;
        return this;
    }
    public PairDateBuilder withHoliday(Boolean param){
        this.holiday = param;
        return this;
    }

    @Override
    public PairDate build() {
        PairDate pairDate = new PairDate();
        pairDate.setHoliday(this.holiday);
        pairDate.setDate(this.pairDate);
        pairDate.setId(this.id);
        return pairDate;
    }


}
