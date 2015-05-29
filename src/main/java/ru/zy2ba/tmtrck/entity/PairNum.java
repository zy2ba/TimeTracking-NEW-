package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;
import ru.zy2ba.tmtrck.entity.enums.Week;

import javax.persistence.*;

/** *
 * класс-сущность описывающий номер пары
 * а так же принадлежность её к неделе, что в общем то странно, неделе должена принадлежать пара, тащемта
 * @Task перенести номер неделина дату, ибо его нахождение здесь, безотносительно даты - бредятина
 * @Link PairManager  обеспечивает работу с этим классом посредством
 * @See PairDao
 * @author Zy2ba
 * @since 23.04.15
 */
@Entity
@Table(name = "PAIRNUM")
public class PairNum {

    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    /**
     * @value номер пары, возможные значения 1-8
     */
    @Column(name = "num")
    private int num;

    /**
     * @value время, в которое проходит пара
     */
    @Column(name = "time")
    private String time;

    /**
     * @value неделя, на которой проходит пара
     */
    @Column(name = "week")
    @Enumerated(EnumType.STRING)
    private Week week;

    public void setWeek(Week week){
        this.week = week;
    }
    public Week getWeek(){
        return this.week;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public void setNum(int num){
        this.num = num;
    }

    public int getNum(){
        return num;
    }

    public void setTime(String time){
        this.time = time;
    }



    public String getTime(){
        return this.time;
    }
}
