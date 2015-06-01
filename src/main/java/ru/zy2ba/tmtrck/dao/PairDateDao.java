package ru.zy2ba.tmtrck.dao;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.PairDate;

/**
 * Обеспечивает работу с сущностью дата пар
 * @link PairDate
 * @See PairDateManager
 * То, что делают методы без реализации, можно понять по их названию
 * @author Zy2ba
 * @since 05.05.15
 */
public interface PairDateDao extends JpaRepository<PairDate,Long> {
    /**
     *
     * по сути конвертит LocalDate в PairDate
     * @param date
     * @return возвращает дату по дате
     * как бы это странно не звучало     *
     */
    PairDate findByDate(LocalDate date);

    PairDate findByDateAndHoliday(LocalDate date,boolean holiday);

}
