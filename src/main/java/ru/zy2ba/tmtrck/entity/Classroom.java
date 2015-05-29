package ru.zy2ba.tmtrck.entity;

/**
 * класс-сущность описывающий аудиторию, её расположение
 * так же планируется, что у аудитории будет тип()
 * @Task сделать тип с ENUM лекционная/лаборатория/малая/другая и обеспечить возможность заполнения
 * @Link ClassroomManager обеспечивает работу с этим классом посредством
 * @See ClassroomDao
 * @author zy2ba
 * @since 23.04.2015
 */

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "CLASSROOM")
public class Classroom  {
    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    /**
     * @value номер на этаже
     */
    @Column(name = "num")
    private int num;

    /**
     * @value этаж, так же является первой цифрой в @link num
     */
    @Column(name = "floor")
    private int floor;

    /**
     * @value номер корпуса
     */
    @Column(name = "building")
    private int building;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building ) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor   ) {
        this.floor = floor;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num   ) {
        this.num = num;
    }

}
