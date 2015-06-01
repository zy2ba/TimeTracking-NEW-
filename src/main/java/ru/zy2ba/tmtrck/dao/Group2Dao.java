package ru.zy2ba.tmtrck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zy2ba.tmtrck.entity.Group2;

/**
 *
 * Обеспечивает работу с сущностью группа
 * @link Group2
 * @See GroupManager
 * То, что делают методы без реализации, можно понять по их названию
 * @author Zy2ba
 * @since 05.05.2015
 */
public interface Group2Dao extends JpaRepository<Group2,Long> {
  /**
   * Ищет группу по её названию
   * Осторощно! не гарантируется, что в имени групы записана одна группа
   * возможно что там комбинация навроде
   * Ю-32-2, Ю-33-2
   * возможно разделение с помощю
   * ", " запятая с пробелом
   * "+" плюс
   * "~" тильда
   * на 15.05.2015 последний символ у всех групп пробел, впереди рефакторинг, так что не гарантируется
   * @param name
   * @return одну группу или несколько групп одной строкой
   */
    Group2 findByName(String name);
}
