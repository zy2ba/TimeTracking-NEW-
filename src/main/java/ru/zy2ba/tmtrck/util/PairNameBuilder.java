package ru.zy2ba.tmtrck.util;

import ru.zy2ba.tmtrck.entity.PairName;
import ru.zy2ba.tmtrck.entity.enums.PairType;

/**
 * @author Zy2ba
 * @since 05.05.15
 */
public class PairNameBuilder implements Builder<PairName> {
    private long id;
    private PairType pairType;
    private String name;
    private  PairNameBuilder(){}
    public static PairNameBuilder getPairNameBuilder(){
        return new PairNameBuilder();
    }

    public PairNameBuilder withId(long param){
        this.id = param;
        return this;
    }

    public PairNameBuilder withPairType(PairType param){
        this.pairType = param;
        return this;
    }

    public PairNameBuilder withName(String param){
        this.name = param;
        return this;
    }
    @Override
    public PairName build() throws Exception {
        if(name == null){
            throw new Exception();
        }
        PairName pairName = new PairName();
        pairName.setId(id);
        pairName.setName(name);
        pairName.setPairType(pairType);

        return pairName;
    }
}
