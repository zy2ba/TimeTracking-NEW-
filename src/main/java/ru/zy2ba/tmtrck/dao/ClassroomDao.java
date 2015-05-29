package ru.zy2ba.tmtrck.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.zy2ba.tmtrck.entity.Classroom;

import java.util.List;

/**
 * Обеспечивает работу с сущностью аудитория
 * @link Classroom
 * @See ClassroomManager
 * То, что делают методы без реализации, можно понять по их названию
 * @author Zy2ba
 * @since 05.05.2015
 */
public interface ClassroomDao extends JpaRepository<Classroom,Long> {
    /**
     * Ищет аудитории в корпусе
     * @param building
     * @return список аудиторий
     */
    List<Classroom> findByBuilding(int building);

    /**
     * ищет аудиторию по её номеру, безотносительно корпуса
     * @param num
     * @return список аудиторий
     */
    List<Classroom> findByNum(int num);

    /**
     *ищет аудиторию в корпусе
     * @param building здание в котором ищется
     * @param num номер аудитории в здании
     * @return аудиторию
     */
    @Query("From Classroom p where p.building = :building and p.num = :num")
        Classroom findByBuildingAndNum(@Param("building") int building,@Param("num") int num);

}
