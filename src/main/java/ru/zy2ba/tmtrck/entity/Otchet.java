package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Zy2ba on 19.05.2015.
 */
@Entity
@Table(name = "OTCHET")
public class Otchet {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment",strategy = "increment")
    private long id;
    /**
     * @See Prepod
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PREPOD_ID")
    private Prepod prepod;

    public void setPrepod(Prepod prepod){
        this.prepod = prepod;
    }

    public Prepod getPrepod(){
        return this.prepod;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
