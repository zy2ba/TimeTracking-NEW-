package ru.zy2ba.tmtrck.manager.impl;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.dao.PairDao;
import ru.zy2ba.tmtrck.entity.*;
import ru.zy2ba.tmtrck.manager.*;
import ru.zy2ba.tmtrck.util.ResourceLocator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Zy2ba
 * @since 05.05.2015
 * Created by Zy2ba on 05.05.2015.
 */

public class PairManagerImpl implements PairManager {


    @Autowired
    private
    PairDao pairDao;

    @Override
    public void create(ArrayList<Pair> entitys){
        PairNameManager pairNameManager = (PairNameManager) ResourceLocator.getBean("pairNameManager");
        PairDateManager dateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");
        ClassroomManager classroomManager = (ClassroomManager) ResourceLocator.getBean("classroomManager");
        GroupManager groupManager = (GroupManager) ResourceLocator.getBean("groupManager");
        PairNumManager pairNumManager = (PairNumManager) ResourceLocator.getBean("pairNumManager");
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        for (Pair pair:entitys){
            pair.setPairName(pairNameManager.create(pair.getPairName()));
        }
        for (Pair pair:entitys){
            pair.setPairDate(dateManager.create(pair.getPairDate()));
        }
        for (Pair pair:entitys){
            pair.setClassroom(classroomManager.create(pair.getClassroom()));
        }
        for (Pair pair:entitys){
            pair.setGroup2(groupManager.create(pair.getGroup2()));
        }
        for (Pair pair:entitys){
            pair.setPairNum(pairNumManager.create(pair.getPairNum()));
        }
        for (Pair pair:entitys){
            pair.setPrepod(prepodManager.create(pair.getPrepod()));
        }
        for (Pair pair:entitys){
            if(!findPairDublicate(pair)) {
                pairDao.saveAndFlush(pair);
            }
        }
    }

    @Override
    public Pair create(Pair entity) throws HibernateJdbcException {

        PairNameManager pairNameManager = (PairNameManager) ResourceLocator.getBean("pairNameManager");
        entity.setPairName(pairNameManager.create(entity.getPairName()));

        PairDateManager dateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");
        entity.setPairDate(dateManager.create(entity.getPairDate()));

        ClassroomManager classroomManager = (ClassroomManager) ResourceLocator.getBean("classroomManager");
        entity.setClassroom(classroomManager.create(entity.getClassroom()));

        GroupManager groupManager = (GroupManager) ResourceLocator.getBean("groupManager");
        entity.setGroup2(groupManager.create(entity.getGroup2()));

        PairNumManager pairNumManager = (PairNumManager) ResourceLocator.getBean("pairNumManager");
        entity.setPairNum(pairNumManager.create(entity.getPairNum()));

        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        entity.setPrepod(prepodManager.create(entity.getPrepod()));

        if(!this.findPairDublicate(entity)){
            return pairDao.saveAndFlush(entity);
        }else return null;
    }

    @Override
    public Pair update(Pair entity) throws HibernateJdbcException{
        return pairDao.saveAndFlush(entity);
    }

    @Override
    public Pair delete(Pair entity)throws HibernateJdbcException {
        return null ;
    }

    @Override
    public Pair get(long entityId)throws HibernateJdbcException {
        return pairDao.findOne(entityId);
    }

    @Override
    public ArrayList<Pair> findByPrepod(Prepod prepod) throws HibernateJdbcException{
        return pairDao.findByPrepod(prepod);
    }

    @Override
    public boolean findPairDublicate(Pair entity) {
       Pair persistPair = pairDao.findByGroup2AndPairDateAndPairNameAndPairNumAndPrepod( entity.getGroup2(), entity.getPairDate(), entity.getPairName(), entity.getPairNum(), entity.getPrepod());
        return persistPair != null && persistPair.getId() > -1;
    }

    @Override
    public ArrayList<Pair> findDatesPrepod(Prepod prepod, ArrayList<PairDate> pairDates)
    {
        ArrayList<Pair> resulList = new ArrayList<>();
        for(PairDate date:pairDates){
            ArrayList<Pair> oneDatePairs = pairDao.findByPrepodAndPairDate(prepod,date);
            for(Pair pair:oneDatePairs){
                resulList.add(pair);
            }
        }
        return resulList;
    }

    @Override
    public ArrayList<Pair> findCarriedPairsForPrepodByDate(Prepod prepod, PairDate pairDate) {
        return pairDao.findByPrepodAndPairDateAndIsCarriedOut(prepod,pairDate,true);
    }

    /**
     * ищет пары для преподавателя с отсеиванием по условиям
     * @param prepodName
     * @param prepodLastName
     * @param startDate
     * @param finishDate
     * @param isCarrieadOutStatus
     * @param isOnHolidayStatus
     * @return
     */
    @Override
    public ArrayList<Pair> searchCustom(String prepodName, String prepodLastName, LocalDate startDate, LocalDate finishDate, int isCarrieadOutStatus, int isOnHolidayStatus) {
        PairDateManager pairDateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");
        ArrayList<PairDate> dates = isOnHolidayStatus==0?pairDateManager.findByDate(startDate, finishDate.plusDays(1)):pairDateManager.findByDateAndHoliday(startDate,finishDate.plusDays(1),isOnHolidayStatus==1);
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod = prepodManager.findByNameAndLastName(prepodName,prepodLastName);
        ArrayList<Pair> arrayListPrepodPairs = new ArrayList<>();
        for(PairDate date:dates){
            ArrayList<Pair> findPairs = isCarrieadOutStatus==0?pairDao.findByPrepodAndPairDate(prepod,date):pairDao.findByPrepodAndPairDateAndIsCarriedOut(prepod,date,isCarrieadOutStatus==1);
            for(Pair pair: findPairs){
                arrayListPrepodPairs.add(pair);
            }
        }
        Collections.sort(arrayListPrepodPairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                if (o1.getPairDate().getDate().isAfter(o2.getPairDate().getDate())){
                    return 1;
                }else
                if (o2.getPairDate().getDate().isAfter(o1.getPairDate().getDate())){
                    return -1;
                }else
                if (o1.getPairNum().getNum()>o2.getPairNum().getNum()){
                    return 1;
                }else
                if(o1.getPairNum().getNum()<o2.getPairNum().getNum()){
                    return -1;
                }else
                return 0;
            }
        });

        return arrayListPrepodPairs;
    }

    @Override
    public ArrayList<Pair> searchCustom(String prepodName, String prepodLastName, int isCarrieadOutStatus, int isOnHolidayStatus) {
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod =prepodManager.findByNameAndLastName(prepodName,prepodLastName);
        ArrayList<Pair> prepodPairs = isCarrieadOutStatus==0?pairDao.findByPrepod(prepod):pairDao.findByPrepodAndIsCarriedOut(prepod,isCarrieadOutStatus==1);
        ArrayList<Pair> selectedPairs = new ArrayList<>();
        if (isOnHolidayStatus!=0){
            for(Pair pair:prepodPairs){
                if (pair.getPairDate().getHoliday()==(isOnHolidayStatus==1)){
                    selectedPairs.add(pair);
                }
            }
        }else selectedPairs = prepodPairs;
        Collections.sort(selectedPairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                if (o1.getPairDate().getDate().isAfter(o2.getPairDate().getDate())){
                    return 1;
                }else
                if (o2.getPairDate().getDate().isAfter(o1.getPairDate().getDate())){
                    return -1;
                }else
                return 0;
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for(Pair pair:selectedPairs){
                stringBuilder.append(pair.getPairDate().getDate().toString() +","+pair.getPairDate().getDateToString() + "|№"+(pair.getPairNum().getNum()+1)+ "|" + pair.getPairNum().getTime() + "|" + pair.getClassroom().getBuilding() + "-" + pair.getClassroom().getNum() + " " + pair.getGroup2().getName() + " " + pair.getPairName().getName() + "(" + pair.getStringIsCarriedOut(true) +  ")" + "\r\n");
        }
        return selectedPairs;
    }
}
