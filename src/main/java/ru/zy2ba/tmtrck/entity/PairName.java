package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;
import ru.zy2ba.tmtrck.entity.enums.PairType;

import javax.persistence.*;

/**
 * класс-сущность описывающий название предмета, а так же её тип
 * @Link PairNameManager обеспечивает работу с этим классом посредством
 * @See PairNameDao
 * @author Zy2ba
 * @since 23.04.15
 */
@Entity
@Table(name = "PAIRNAME")
public class PairName {
    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    /**
     * @value тип занятия
     * лаба/лекция/практика/другое
     */
    @Column(name = "pairType")
    @Enumerated(EnumType.STRING)
    private PairType pairType;

    /**
     * @value название предмета
     */
    @Column(name = "name")
    private String name;



    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return this.id;
    }

    public void setPairType(PairType pairType){
        this.pairType = pairType;
    }

    public PairType getPairType(){
        return this.pairType;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    /**
     *
     * @param longFormat позволяет выбрать между длинным и коротким форматом, например
     *                   если true, о вернёт "лабораторная",
     *                   а если false, то вернёт "лаб."
     * @return тип пары в виде текса
     */
    public String getPairTypeToString(@SuppressWarnings("SameParameterValue") boolean longFormat){
        if(this.pairType==PairType.lab){
            return longFormat ? "лабораторная" : "лаб.";
        }
        if(this.pairType==PairType.lecture){
            return longFormat ? "лекция" : "лек.";
        }
        if(this.pairType==PairType.practice){
            return longFormat ? "практика" : "пр. ";
        }
        return longFormat?"неизвестный тип пары":"?   ";
    }

}
