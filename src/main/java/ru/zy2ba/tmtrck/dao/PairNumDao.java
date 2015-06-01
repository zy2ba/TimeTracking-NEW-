package ru.zy2ba.tmtrck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.PairNum;
import ru.zy2ba.tmtrck.entity.enums.Week;

/**
 * Обеспечивает работу с сущностью номер пары
 * @link PairNum
 * @See PairNumManager
 * То, что делают методы без реализации, можно понять по их названию
 * @author Zy2ba
 * @since 05.05.15
 */
public interface PairNumDao extends JpaRepository<PairNum,Long> {
    /**
     * запись номера пары по её номеру
     * @param num - номер пары
     * @param week - первая/вторая неделя
     * @return запись нужного номера пары на нужной неделе
     */
    PairNum findByNumAndWeek(int num, Week week);
}
