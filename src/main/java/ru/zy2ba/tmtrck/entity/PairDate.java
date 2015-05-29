package ru.zy2ba.tmtrck.entity;

/**
 * класс-сущность описывающий дату для пары и свойств этой даты, таки как является ли дата праздницной
 * @Link PairDateManager обеспечивает работу с этим классом посредством
 * @See PairDateDao
 * @author Zy2ba
 * @since 23.04.15
 */

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;


import javax.persistence.*;

@Entity
@Table(name = "PAIRDATE")
public class PairDate implements Comparable{

    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    /**
     * @value непосредственно дата
     */
    @Column(name = "date")
   // @Temporal(TemporalType.TIMESTAMP)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate date; //дата пары

    /**
     * @value указывает на то, не выпадает ли на эту дану праздник ну или каникулы
     */
    @Column(name = "holiday")
    private boolean holiday;


    public void setDate(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public void setHoliday(boolean holiday){
        this.holiday = holiday;
    }

    public boolean getHoliday(){
        return this.holiday;
    }

    public void setId(Long id){
        this.id = id;
    }
    public long getId(){
        return this.id;
    }

    /**
     * @return Чётность педели строкой
     */
    public String getParityToString(){
        int ch = date.getWeekOfWeekyear();
        switch (ch%2){
            case 0:return "Чётная неделя";
            case 1:return "Нечётная неделя";
        }
        return "Ошибка";
    }

    /**
     * @return текстовое представление дня недели, на который выпадает дата
     * на данный момент в коротком формате(пример:"Пн")
     */
    public String getDateToString(){
       int ch = date.getDayOfWeek();
        switch (ch){
            case 1:return "Пн";
            case 2:return "Вт";
            case 3:return "Ср";
            case 4:return "Чт";
            case 5:return "Пт";
            case 6:return "Сб";
            case 7:return "Вс";
        }
        return "Ошибка";
    }

    @Override
    public int compareTo(Object o) {
        
        return 0;
    }
}
