package ru.zy2ba.tmtrck.entity;

/**
 * класс-сущность описывающий группу, её факультет/кафедру, имеет поле дя комментариев/доп описания
 * @task номализовать имя, дать возможность добавлять дополнительную информацию
 * @Link GroupManager обеспечивает работу с этим классом посредством
 * @See Group2Dao
 * @author Zy2ba
 * @since 23.04.15
 */

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "GROUP2")
public class Group2 {

    /**
     * @value id, не может быть null
     */
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    /**
     * @value Название группы
     * Осторощно! не гарантируется, что в имени групы записана одна группа
     * возможно что там комбинация навроде
     * Ю-32-2, Ю-33-2
     * возможно разделение с помощю
     * ", " запятая с пробелом
     * "+" плюс
     * "~" тильда
     * на 15.05.2015 последний символ у всех групп пробел, впереди рефакторинг, так что не гарантируется
     */
    @Column(name = "name")
    private String name;

    /**
    * @value факультет
    */
 @Column(name = "faculty")
    private String faculty;


    /**
     * @value кафедра
     */
    @Column(name = "subfaculty")
    private String subfaculty;


    /**
     * @value дополнительная информация, временно не используется, планируется к оживлению после нормализации имени группы
     */
    @Column(name = "info")
    private String info;


    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(int targetLength){
        String name = this.name;
        while (name.length()<targetLength+1){
            name=name+" ";
        }
        return name;
    }

    public String getName(){
        return this.name;
    }

    public void setFaculty(String faculty){
        this.faculty = faculty;
    }

    public String getFaculty(){
        return this.faculty;
    }

    public void setSubfaculty(String subfaculty){
        this.subfaculty = subfaculty;
    }

    public String getSubfaculty(){
        return this.subfaculty;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public String getInfo(){
        return this.info;
    }
}
