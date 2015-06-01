package ru.zy2ba.tmtrck.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;

/**
 * @author Zy2ba
 * @since 27.05.2015
 */
@Entity
@Table(name = "AUTUMN_SPRING_SPACER")
public class AutumnSpringSpacer {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    @Column(name = "startYear")
    private int startYear;

    @Column(name = "spacerDate")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate spacerDate;

    /**
     * @value id, не может быть null
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public LocalDate getSpacerDate() {
        return spacerDate;
    }

    public void setSpacerDate(LocalDate spacerDate) {
        this.spacerDate = spacerDate;
    }
}
