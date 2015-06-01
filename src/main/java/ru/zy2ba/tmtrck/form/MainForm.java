package ru.zy2ba.tmtrck.form;

import com.toedter.calendar.JDateChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import ru.zy2ba.tmtrck.entity.*;
import ru.zy2ba.tmtrck.entity.ActivityTypes.*;
import ru.zy2ba.tmtrck.entity.enums.TypeOfLoad;
import ru.zy2ba.tmtrck.exception.AlreadyInUseException;
import ru.zy2ba.tmtrck.exception.OutOfHoursException;
import ru.zy2ba.tmtrck.exception.TranstortAcrossSemesterException;
import ru.zy2ba.tmtrck.exception.WrongDatesException;
import ru.zy2ba.tmtrck.manager.*;
import ru.zy2ba.tmtrck.util.PairDateBuilder;
import ru.zy2ba.tmtrck.util.Parser;
import ru.zy2ba.tmtrck.util.PlanParser;
import ru.zy2ba.tmtrck.util.ResourceLocator;
import ru.zy2ba.tmtrck.util.reports.CustomReportUtil3;
import ru.zy2ba.tmtrck.util.reports.YearReportUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;

/**
 * @author Zy2ba
 * @since 04.05.2015
 */
class MainForm {
    private JFrame jfrm;//главная форма
    private JScrollPane jspTMTmTbl;//скролл панели внутри вкладки с расписанием
    private JDateChooser jdcAddDatesStart;
    private JTextField jtfTmTblLastName;
    private JDateChooser jdcAddDatesFinish;
    private JDateChooser jcReportFinish;
    private JDateChooser jdcReportStart;
    private JTextField jtfReportName;
    private JTextField jtfReportLastName;
    private JDateChooser jcHolidayStart;
    private JDateChooser jcHolidayFinish;
    private JButton jbAddDates;
    private JComboBox<String> cbHolidayFactor;
    private JTextField jtfTmTblName;
    private JButton jbTmTblSearch;
    private JComboBox<String> jcbTmTblIsHoliday;
    private ArrayList<Pair> arrayListPrepodPairs;
    private String str;
    private boolean admin;
    private JComboBox<String> jcbReportType;
    private JDateChooser jdcCarriedOuterByDateStart;
    private JDateChooser jdcCarriedOuterByDateFinish;
    private JButton jbCarriedOuterTmTbl;
    private JComboBox<String> jcbCarriedOuterByDate;
    private static final Dimension dStandartSmallElement = new Dimension(130,25);
    private static final Dimension dDateChooser = new Dimension(95,30);
    private JComboBox<String> jcbTmTblIsCarriedOut;
    private JDateChooser jdcTmTblStart;
    private JDateChooser jdcTmTblFinish;
    private JCheckBox jchbTmTblDate;
    private Prepod userAccount;
    private JComboBox<String> jcbPairNum;
    private JList jlstTmTbl;
    private DefaultListModel<String> listModel;
    private JButton jbTmTblCarriedOut;
    private JPasswordField jpfCPCOldPass;
    private JPasswordField jpfCPCNewPass;
    private JPasswordField jpfCPCNewPassRepeat;
    private SpinnerNumberModel snmCOFOConsult;
    private SpinnerNumberModel snmCOFOExam;
    private SpinnerNumberModel snmCOFOKursPrjct;
    private SpinnerNumberModel snmCOFOKursRab;
    private SpinnerNumberModel snmCOFODiplomPrjct;
    private SpinnerNumberModel snmCOFOPractice;

    private final String[] vcbCarriedOuterForOther = {
            "Консультация",
            "Экзамен",
            "Курсовой проект",
            "Курсовая работа",
            "Дипломное проектирование",
            "Практика",
            "Зачёт",
            "Участие в ГАК"
    };

    private final String[] vcbTypeOfLoad = {
            "Бюджет",
            "Внебюджет",
            "Сокращённое обучение"
    };

    private final String[] vcbTmTblIsHoliday = {
            "Без разницы",
            "Выходной",
            "Не выходной"
    };
    private  final String[] vcbTmTblIsCarriedOut = {
            "Без разницы",
            "Проведена",
            "Не проведена"
    };
    private final String[] vcbHolidays = {
            "Не праздник",
            "Праздник"
    };
    private final String[] vcbAddDates = {
            "Чётные",
            "Нечётные"
    };
    private final String[] vcbSearch = {
            "Преподаватель"
    };
    private final String[] vcbReport ={
            "Годовой",
            "Внутрисеместровый"
            //,"Сводная ведомость"
    };
    private final String[] vcbCarriedOuterByDate ={
            "Поставить отметку о проведении",
            "Снять отметку о проведении"
    };
    private  final String[] vcbPairNum = {
            "Все",
            "Первая",
            "Вторая",
            "Третья",
            "Четвёртая",
            "Пятая",
            "Шестая",
            "Седьмая"
    };
   /* private final String[] vcbYears = {
            "2010/11",
            "2011/12",
            "2012/13",
            "2013/14",
            "2014/15",
            "2015/16",
            "2016/17",
            "2017/18",
            "2018/19"
    };*/
    private ArrayList<TimeTableElement> timeTableElements;
    private LocalDate ldCOFO;
    private PlanTable planTable;
    private double alreadyHours;

    private MainForm(String dialogHead){

        InputPanel ip = new InputPanel();
        int res = JOptionPane.showConfirmDialog(null, ip, dialogHead,
                JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.OK_OPTION){
            PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");

            try {
                userAccount = prepodManager.checkPassword(ip.getUsername(), ip.getUserlastname(), ip.getPassWord());
            } catch (AlreadyInUseException e) {
                JOptionPane.showMessageDialog(null,"Данный пользователь уже используется, повторите попытку позже");
            }
            if (userAccount!=null){

                initComponents();
            }else new MainForm("Введены некорректные данные. Повторите попытку:");
           /* if(ip. getUsername().equals("root") && ip.getPassWord().equals("root")){
                admin = true;
            }else admin = false;*/
        } else {
            System.out.println("Exit");
        }

    }

    /**
     * обеспечивает инициализацию пользовательского интерфейса, производит установку реакций на действия пользователей
     */
    private void initComponents(){

        jfrm = new JFrame("Project Rasp");
        jfrm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jfrm.setVisible(false);
                jfrm.dispose();
                PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
                userAccount.setIsWorking(false);
                userAccount.setLastActivity();
                prepodManager.update(userAccount);
                //super.windowClosing(e);
                new MainForm("Был совершён выход из системы. ");
            }
        });
        jfrm.setPreferredSize(new Dimension(860, 540));
        jfrm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jfrm.setLayout(new GridLayout(1, 1));

        JTabbedPane jtpTopMenu = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        JPanel jpTMReport = new JPanel(new FlowLayout());
        jdcReportStart = new JDateChooser(new LocalDate().toDate());
        jdcReportStart.setPreferredSize(dDateChooser);
        jdcReportStart.setAlignmentY(Component.CENTER_ALIGNMENT);
        jdcReportStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        jcReportFinish = new JDateChooser(new LocalDate().toDate());
        jcReportFinish.setPreferredSize(dDateChooser);

        JButton jbMakeReport = new JButton("Создать отчёт");
        jbMakeReport.setPreferredSize(dStandartSmallElement);
        jbMakeReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcbReportType.getSelectedIndex() == 0) {
                    performYearReport();

                } else if (jcbReportType.getSelectedIndex() == 1) {
                    performCustomReport();
                }
            }
        });

        jcbReportType = new JComboBox<>(vcbReport);
        jpTMReport.add(jcbReportType);
        jpTMReport.add(jdcReportStart);
        jpTMReport.add(jcReportFinish);
        jpTMReport.add(jbMakeReport);

        JScrollPane jspTMReport = new JScrollPane(jpTMReport);
        jspTMReport.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jspTMReport.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        JPanel jpTMTmTbl = new JPanel();
        GridBagLayout gblTmTbl = new GridBagLayout();
        jpTMTmTbl.setLayout(gblTmTbl);
        GridBagConstraints gbcTmTbl = new GridBagConstraints();
       // jpTMTmTbl.setLayout(new FlowLayout());
        //jspTMTmTbl = new JScrollPane(jpTMTmTbl);
        //jspTMTmTbl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        //jspTMTmTbl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JTabbedPane jtpAdd = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);


        JTextField jtfAddPrepodLastName = new JTextField();
        jtfAddPrepodLastName.setPreferredSize(dStandartSmallElement);
        JTextField jtfAddPrepodMiddleName = new JTextField();
        jtfAddPrepodMiddleName.setPreferredSize(dStandartSmallElement);

        JPanel jpAddDates = new JPanel(new FlowLayout());
        jdcAddDatesStart = new JDateChooser(new LocalDate().toDate());
        jdcAddDatesStart.setPreferredSize(dDateChooser);
        jdcAddDatesStart.setToolTipText("Дата на которой начинает применяться шаблон");
        jdcAddDatesFinish = new JDateChooser(new LocalDate().toDate());
        jdcAddDatesFinish.setToolTipText("дата на которой оканчивается применяться шаблон");
        jdcAddDatesFinish.setPreferredSize(dDateChooser);
        jpAddDates.add(new JLabel("Дата "));
        jpAddDates.add(jdcAddDatesStart);
        jpAddDates.add(jdcAddDatesFinish);
        JComboBox<String> jComboBox = new JComboBox<>(vcbAddDates);
        jComboBox.setSelectedIndex(1);
        jpAddDates.add(new JLabel("Первыми являются недели:"));
        jpAddDates.add(jComboBox);
        jbAddDates = new JButton("Подгрузить");
        jbAddDates.setPreferredSize(dStandartSmallElement);
        jbAddDates.setToolTipText("Прогрузить использую xls файл в качестве шаблона");
        jbAddDates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbAddDates.setText("Загружаю...");
                jfrm.setEnabled(false);
                try {
                    addDatesActionPerformed();
                } catch (AlreadyInUseException e1) {
                    rebootUser();
                }
                jfrm.setEnabled(true);
                jbAddDates.setText("Подгрузить");
            }
        });

        JPanel jpAddHolidays = new JPanel(new FlowLayout());
        jcHolidayStart = new JDateChooser(new LocalDate().toDate());
        jcHolidayStart.setPreferredSize(dDateChooser);
        jcHolidayFinish = new JDateChooser(new LocalDate().toDate());
        jcHolidayFinish.setPreferredSize(dDateChooser);

        cbHolidayFactor = new JComboBox<>(vcbHolidays);
        cbHolidayFactor.setEditable(false);

        ////////////расписание
        gbcTmTbl.insets = new Insets(5,5,5,5);
        gbcTmTbl.weighty = 0.5;
        gbcTmTbl.weightx = 0.5;
        gbcTmTbl.gridwidth = 2;
        gbcTmTbl.gridx = GridBagConstraints.RELATIVE;
        gbcTmTbl.gridy = GridBagConstraints.RELATIVE;
        gbcTmTbl.anchor = GridBagConstraints.CENTER;
        gbcTmTbl.fill =GridBagConstraints.NONE;
        PairDate datenow = new PairDate();
        datenow.setDate(new LocalDate());

        JLabel jlTmTblTimeViewer = new JLabel(datenow.getDateToString()+", "+new LocalDate().toString() + " " + datenow.getParityToString()+"." );
        gblTmTbl.setConstraints(jlTmTblTimeViewer, gbcTmTbl);
        jpTMTmTbl.add(jlTmTblTimeViewer);

        gbcTmTbl.gridwidth = 1;
        gbcTmTbl.weightx = 0.95;
        gbcTmTbl.anchor = GridBagConstraints.EAST;
        JLabel jlTmTbl1 = new JLabel("Имя:");
        gblTmTbl.setConstraints(jlTmTbl1, gbcTmTbl);
        jpTMTmTbl.add(jlTmTbl1);

        gbcTmTbl.weightx = 1.5;
        gbcTmTbl.fill =GridBagConstraints.BOTH;
        jtfTmTblName = new JTextField();
        jtfTmTblName.setPreferredSize(dStandartSmallElement);
        gblTmTbl.setConstraints(jtfTmTblName, gbcTmTbl);
        jpTMTmTbl.add(jtfTmTblName);

        gbcTmTbl.weightx = 0.5;
        gbcTmTbl.fill =GridBagConstraints.NONE;
        JLabel jlTmTblLastName = new JLabel("Фамилия:");
        gblTmTbl.setConstraints(jlTmTblLastName,gbcTmTbl);
        jpTMTmTbl.add(jlTmTblLastName);

        gbcTmTbl.gridwidth = GridBagConstraints.REMAINDER;
        gbcTmTbl.weightx = 1.5;
        gbcTmTbl.fill =GridBagConstraints.BOTH;
        jtfTmTblLastName = new JTextField();
        if(!userAccount.getFaculty().equals("service")){
            jtfTmTblLastName.setText(userAccount.getLastName());
            jtfTmTblName.setText(userAccount.getName());
        }
        jtfTmTblLastName.setPreferredSize(dStandartSmallElement);
        gblTmTbl.setConstraints(jtfTmTblLastName, gbcTmTbl);
        jpTMTmTbl.add(jtfTmTblLastName);

        gbcTmTbl.weightx = 0.3;
        gbcTmTbl.gridwidth =1;
        gbcTmTbl.fill =GridBagConstraints.NONE;
        jchbTmTblDate = new JCheckBox("Отсеивание по дате:",false);
        jchbTmTblDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jdcTmTblStart.setEnabled(jchbTmTblDate.isSelected());
                jdcTmTblFinish.setEnabled(jchbTmTblDate.isSelected());
            }
        });
        gblTmTbl.setConstraints(jchbTmTblDate, gbcTmTbl);
        jpTMTmTbl.add(jchbTmTblDate);

        gbcTmTbl.weightx = 1.5;
        gbcTmTbl.gridx = GridBagConstraints.RELATIVE;
        gbcTmTbl.fill =GridBagConstraints.BOTH;
        jdcTmTblStart = new JDateChooser(new Date());
        jdcTmTblStart.setEnabled(false);
        jdcTmTblStart.setPreferredSize(dDateChooser);
        gblTmTbl.setConstraints(jdcTmTblStart, gbcTmTbl);
        jpTMTmTbl.add(jdcTmTblStart);

        jdcTmTblFinish = new JDateChooser(new Date());
        jdcTmTblFinish.setEnabled(false);
        jdcTmTblFinish.setPreferredSize(dDateChooser);
        gblTmTbl.setConstraints(jdcTmTblFinish, gbcTmTbl);
        jpTMTmTbl.add(jdcTmTblFinish);

        gbcTmTbl.gridwidth =1;
        gbcTmTbl.weightx = 0.3;
        gbcTmTbl.fill =GridBagConstraints.NONE;
        JLabel jlTmTblCmbBx = new JLabel("Доп свойства:");
        gblTmTbl.setConstraints(jlTmTblCmbBx, gbcTmTbl);
        jpTMTmTbl.add(jlTmTblCmbBx);

        gbcTmTbl.weightx = 1.5;
        gbcTmTbl.fill =GridBagConstraints.BOTH;
        jcbTmTblIsHoliday = new JComboBox<>(vcbTmTblIsHoliday);
        jcbTmTblIsHoliday.setPreferredSize(dStandartSmallElement);
        gblTmTbl.setConstraints(jcbTmTblIsHoliday, gbcTmTbl);
        jpTMTmTbl.add(jcbTmTblIsHoliday);

        gbcTmTbl.gridwidth = GridBagConstraints.REMAINDER;
        jcbTmTblIsCarriedOut = new JComboBox<>(vcbTmTblIsCarriedOut);
        jcbTmTblIsCarriedOut.setPreferredSize(dStandartSmallElement);
        gblTmTbl.setConstraints(jcbTmTblIsCarriedOut, gbcTmTbl);
        jpTMTmTbl.add(jcbTmTblIsCarriedOut);

        gbcTmTbl.insets = new Insets(0,15,0,15);
        jbTmTblSearch = new JButton("Поиск");
        jbTmTblSearch.setPreferredSize(dStandartSmallElement);
        jbTmTblSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbTmTblSearch.setText("Ищу...");
                try {
                    searchByPrepod();
                } catch (HibernateJdbcException e1) {
                    JOptionPane.showMessageDialog(jfrm, "Сбой подключения к базе данных. Проверьте её доступность и повторите попытку", "Внимание!", JOptionPane.WARNING_MESSAGE);
                }
                jbTmTblSearch.setText("Поиск");
                jfrm.pack();
            }
        });
        gblTmTbl.setConstraints(jbTmTblSearch, gbcTmTbl);
        jpTMTmTbl.add(jbTmTblSearch);

        listModel = new DefaultListModel<>();

        jlstTmTbl = new JList<>(listModel);
        jlstTmTbl.setFont(new Font("MONOSPACED", Font.BOLD, 14));
        jlstTmTbl.setBackground(new Color(230,240,235));
        JScrollPane jspTmTblTA = new JScrollPane(jlstTmTbl);

        gblTmTbl.setConstraints(jspTmTblTA, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 100, 100, GridBagConstraints.CENTER, GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
        jpTMTmTbl.add(jspTmTblTA);

        jbTmTblCarriedOut = new JButton("Изменить пометку о проведении");

        jbTmTblCarriedOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sIndex = jlstTmTbl.getSelectedIndex();
                if (timeTableElements.get(jlstTmTbl.getSelectedIndex()).getNum()!=-1){
                    Pair pair = (Pair)timeTableElements.get(sIndex);
                    PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
                    pair.setIsCarriedOut(!pair.getIsCarriedOut());
                    pairManager.update(pair);
                    searchByPrepod();
                    jlstTmTbl.setSelectedIndex(sIndex);
                }else{
                    TimeTableElement tte= timeTableElements.get(sIndex);
                    JOptionPane.showMessageDialog(jfrm,"Можно изменить проведённость только у пар","Внимание!",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        gblTmTbl.setConstraints(jbTmTblCarriedOut,new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,GridBagConstraints.REMAINDER,GridBagConstraints.REMAINDER,0.5,0.5,GridBagConstraints.CENTER, GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0));
        jpTMTmTbl.add(jbTmTblCarriedOut);
        jbTmTblCarriedOut.setVisible(false);

        JButton jbHolidater = new JButton("Применить");
        jbHolidater.setPreferredSize(dStandartSmallElement);
        jbHolidater.setToolTipText("Устанавливает свойство для УЖЕ существующих дат");
        jbHolidater.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcHolidayStart.getDate().after(jcHolidayFinish.getDate())) {
                    JOptionPane.showMessageDialog(jfrm, "Дата начала не должна быть позже даты конца!");
                } else
                    try {
                        setHolidaysActionPerfomed();
                    } catch (AlreadyInUseException e1) {
                        rebootUser();
                    }

            }
        });

        jpAddHolidays.add(cbHolidayFactor);
        jpAddHolidays.add(jcHolidayStart);
        jpAddHolidays.add(jcHolidayFinish);
        jpAddHolidays.add(jbHolidater);
        jpAddDates.add(jbAddDates);

        JPanel jpAddAutumnSpringSpacer = new JPanel(new FlowLayout());

        final JDateChooser jdcAASSDate = new JDateChooser(new Date());
        jdcAASSDate.setPreferredSize(dStandartSmallElement);
        JButton jbAASSSetTransportDate = new JButton("Установить дату переходной");
        jbAASSSetTransportDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate spacerDate = new LocalDate(jdcAASSDate.getDate()).plusDays(0);
                AutumnSpringSpacerManager autumnSpringSpacerManager = (AutumnSpringSpacerManager) ResourceLocator.getBean("autumnSpringSpacerManager");
                AutumnSpringSpacer autumnSpringSpacer = autumnSpringSpacerManager.getSpacerForDate(spacerDate);
                if(autumnSpringSpacer==null){
                    autumnSpringSpacer= new AutumnSpringSpacer();
                    autumnSpringSpacer.setSpacerDate(spacerDate);
                    int lastAugustDateNumber;
                    int year = autumnSpringSpacer.getSpacerDate().getYear();
                    if ((year%4==0&& year%100!=0)||(year%400==0) ){
                        lastAugustDateNumber = 244;
                    } else lastAugustDateNumber = 243;

                    if (autumnSpringSpacer.getSpacerDate().getDayOfYear()>lastAugustDateNumber){
                        autumnSpringSpacer.setStartYear(year);
                    }else autumnSpringSpacer.setStartYear(--year);
                    autumnSpringSpacerManager.create(autumnSpringSpacer);
                } else if(!autumnSpringSpacer.getSpacerDate().equals(spacerDate)) {
                    int res = JOptionPane.showConfirmDialog(jfrm,"Для данного учебного года уже выбрана дата перехода, это "+autumnSpringSpacer.getSpacerDate().toString(DateTimeFormat.mediumDate().withLocale(new Locale("ru")))+". Заменить её на "+spacerDate.toString(DateTimeFormat.mediumDate().withLocale(new Locale("ru")))+"?","Подтвердите ввод",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (res== JOptionPane.OK_OPTION){
                        autumnSpringSpacer= new AutumnSpringSpacer();

                        autumnSpringSpacer.setSpacerDate(spacerDate);
                        int lastAugustDateNumber;
                        int year = autumnSpringSpacer.getSpacerDate().getYear();
                        if ((year%4==0&& year%100!=0)||(year%400==0) ){
                            lastAugustDateNumber = 244;
                        } else lastAugustDateNumber = 243;

                        if (autumnSpringSpacer.getSpacerDate().getDayOfYear()>lastAugustDateNumber){
                            autumnSpringSpacer.setStartYear(year);
                        }else autumnSpringSpacer.setStartYear(--year);
                        autumnSpringSpacerManager.create(autumnSpringSpacer);
                    }

                } else {
                    JOptionPane.showMessageDialog(jfrm,"Для данного учебного года " + spacerDate.toString(DateTimeFormat.mediumDate().withLocale(new Locale("ru"))) + " уже выбрана в качестве переходной.", "Инфорация к ознакомлению:",JOptionPane.INFORMATION_MESSAGE);
                }
        }});

        jpAddAutumnSpringSpacer.add(jdcAASSDate);
        jpAddAutumnSpringSpacer.add(jbAASSSetTransportDate);

        jtpAdd.add("Добавление дат", jpAddDates);
        jtpAdd.add("Установка праздников и каникул", jpAddHolidays);
        jtpAdd.add("Переходы между семестрами", jpAddAutumnSpringSpacer);


        JTabbedPane jtpCarriedOuter = new JTabbedPane();
        JPanel jpCarriedOuter = new JPanel(new FlowLayout());
        jpCarriedOuter.add(new JDateChooser(new LocalDate().plusDays(1).toDate()), dStandartSmallElement);
        jpCarriedOuter.add(new JButton("Вывести"));
        jpCarriedOuter.add(new JTextArea(20, 60));

        jpCarriedOuter.add(new JButton("Учесть"));
        jdcCarriedOuterByDateStart = new JDateChooser(new Date());
        jdcCarriedOuterByDateFinish = new JDateChooser(new Date());
        JPanel jpCarriedOuterTmTblIn1 = new JPanel(new GridLayout(1, 0, 50, 0));
        JPanel jpCarriedOuterTmTblIn2 = new JPanel(new GridLayout(1, 0, 25, 0));
        JPanel jpCarriedOuterTmTblIn3 = new JPanel(new GridLayout(1, 0, 50, 0));
        JPanel jpCarriedOuterTmTblIn4 = new JPanel(new GridLayout(1, 0, 50, 0));
        JPanel jpCarriedOuterByDateIn5 = new JPanel(new GridLayout(1,0,50,0));
        GridLayout cobdGridLayoutOut = new GridLayout(0,1);
        cobdGridLayoutOut.setHgap(50);
        cobdGridLayoutOut.setVgap(20);
        JPanel jpCarriedOuterTimeTbl = new JPanel(cobdGridLayoutOut);

        jpCarriedOuterTmTblIn1.add(new JLabel(("Что хотим сделать:"), SwingConstants.CENTER));
        jcbCarriedOuterByDate = new JComboBox<>(vcbCarriedOuterByDate);

        jpCarriedOuterTmTblIn1.add(jcbCarriedOuterByDate);
        jpCarriedOuterTmTblIn2.add(new JLabel("Имя:", SwingConstants.RIGHT));
        JTextField jtfCOBDName = new JTextField();
        jtfCOBDName.setText(userAccount.getName());
        jtfCOBDName.setEditable(false);
        jpCarriedOuterTmTblIn2.add(jtfCOBDName);
        jpCarriedOuterTmTblIn2.add(new JLabel("Фамилия:", SwingConstants.RIGHT));
        JTextField jtfCOBDLastName = new JTextField();
        jtfCOBDLastName.setText(userAccount.getLastName());
        jtfCOBDLastName.setEditable(false);
        jcbPairNum = new JComboBox<>(vcbPairNum);
        jpCarriedOuterTmTblIn2.add(jtfCOBDLastName);
        jpCarriedOuterTmTblIn3.add(new JLabel("Начиная с:", SwingConstants.CENTER));
        jpCarriedOuterTmTblIn3.add(jdcCarriedOuterByDateStart);
        jpCarriedOuterTmTblIn4.add(new JLabel("Заканчивая:", SwingConstants.CENTER));
        jpCarriedOuterTmTblIn4.add(jdcCarriedOuterByDateFinish);
        jpCarriedOuterByDateIn5.add(new JLabel("Номер пар(ы)",SwingConstants.RIGHT));
        jpCarriedOuterByDateIn5.add(jcbPairNum);


        jbCarriedOuterTmTbl = new JButton("Применить");
        jbCarriedOuterTmTbl.setPreferredSize(dStandartSmallElement);
        jbCarriedOuterTmTbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jbCarriedOuterTmTbl.setText("Применяю...");

                try {
                    performCarriedOutByDate();

                } catch (AlreadyInUseException e1) {
                    //JOptionPane.showMessageDialog(jfrm,"Обнаружено другое активное подключение пользователя!","Внимание!",JOptionPane.ERROR_MESSAGE);
                    rebootUser();
                }
                jbCarriedOuterTmTbl.setText("Применить");
            }
        });
        jpCarriedOuterTimeTbl.add(jpCarriedOuterTmTblIn1);
        jpCarriedOuterTimeTbl.add(jpCarriedOuterTmTblIn2);
        jpCarriedOuterTimeTbl.add(jpCarriedOuterTmTblIn3);
        jpCarriedOuterTimeTbl.add(jpCarriedOuterTmTblIn4);
        jpCarriedOuterTimeTbl.add((jpCarriedOuterByDateIn5));
        jpCarriedOuterTimeTbl.add(jbCarriedOuterTmTbl);

        /*
        GridBagLayout gblCOForOther = new GridBagLayout();
        GridBagConstraints gbcCOForOther = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE,GridBagConstraints.REMAINDER,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
        JPanel jpCarriedOuterForOther = new JPanel(gblCOForOther);
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        JLabel jlCOFOConsult = new JLabel("Консыльтации():");
        snmCOFOConsult = new SpinnerNumberModel(userAccount.getConsultacii(),userAccount.getConsultacii(),1000,2);
        JSpinner jsCOFconsult = new JSpinner(snmCOFOConsult);
        jsCOFconsult.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });

        JLabel jlCOFOExam = new JLabel();
        snmCOFOExam = new SpinnerNumberModel(userAccount.getConsultacii(),userAccount.getConsultacii(),1000,0.4);
        JSpinner jsCOFOExam = new JSpinner(snmCOFOExam);

        JLabel jlCOFOKursPrjct = new JLabel();
        snmCOFOKursPrjct = new SpinnerNumberModel(userAccount.getConsultacii(),userAccount.getConsultacii(),1000,4);
        JSpinner jsCOFOKursPrjct = new JSpinner(snmCOFOKursPrjct);

        JLabel jlCOFOKursRab = new JLabel();
        snmCOFOKursRab = new SpinnerNumberModel(userAccount.getConsultacii(),userAccount.getConsultacii(),1000,2);
        JSpinner jsCOFOKursRab = new JSpinner(snmCOFOKursRab);

        JLabel jlCOFODiplomPrjct = new JLabel();
        snmCOFODiplomPrjct = new SpinnerNumberModel(userAccount.getConsultacii(),userAccount.getConsultacii(),1000,17);
        JSpinner jsCOFODiplomPrjct = new JSpinner(snmCOFODiplomPrjct);

        JLabel jlCOFOPractice = new JLabel();
        snmCOFOPractice = new SpinnerNumberModel(userAccount.getConsultacii(),userAccount.getConsultacii(),1000,3);
        JSpinner jsCOFOPractice = new JSpinner(snmCOFOPractice);

        JButton jbCOFOSave = new JButton("Сохранить");
        jbCOFOSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performCarriedOutForOther();
            }
        });

        gblCOForOther.setConstraints(jlCOFOConsult,gbcCOForOther);
        gblCOForOther.setConstraints(jsCOFconsult,gbcCOForOther);
        gblCOForOther.setConstraints(jlCOFOExam,gbcCOForOther);
        gblCOForOther.setConstraints(jsCOFOExam,gbcCOForOther);
        gblCOForOther.setConstraints(jlCOFOKursPrjct,gbcCOForOther);
        gblCOForOther.setConstraints(jsCOFOKursPrjct,gbcCOForOther);
        gblCOForOther.setConstraints(jlCOFOKursRab,gbcCOForOther);
        gblCOForOther.setConstraints(jsCOFOKursRab,gbcCOForOther);
        gblCOForOther.setConstraints(jlCOFODiplomPrjct,gbcCOForOther);
        gblCOForOther.setConstraints(jsCOFODiplomPrjct,gbcCOForOther);
        gblCOForOther.setConstraints(jlCOFOPractice,gbcCOForOther);
        gblCOForOther.setConstraints(jsCOFOPractice,gbcCOForOther);
        gblCOForOther.setConstraints(jbCOFOSave,gbcCOForOther);

        jpCarriedOuterForOther.add(jlCOFOConsult);
        jpCarriedOuterForOther.add(jsCOFconsult);
        jpCarriedOuterForOther.add(jlCOFOExam);
        jpCarriedOuterForOther.add(jsCOFOExam);
        jpCarriedOuterForOther.add(jlCOFOKursPrjct);
        jpCarriedOuterForOther.add(jsCOFOKursPrjct);
        jpCarriedOuterForOther.add(jlCOFOKursRab);
        jpCarriedOuterForOther.add(jsCOFOKursRab);
        jpCarriedOuterForOther.add(jlCOFODiplomPrjct);
        jpCarriedOuterForOther.add(jsCOFODiplomPrjct);
        jpCarriedOuterForOther.add(jlCOFOPractice);
        jpCarriedOuterForOther.add(jsCOFOPractice);
        jpCarriedOuterForOther.add(jbCOFOSave);*/

        JPanel jpCOforActivites = new JPanel(new FlowLayout());

        final JDateChooser jdcCOForActivites = new JDateChooser(new Date());
        jdcCOForActivites.setPreferredSize(dStandartSmallElement);
        final JComboBox<String> jcbCOForActivites = new JComboBox<>(vcbCarriedOuterForOther);
        final JComboBox<String> jcbCOFATypeOfLoad = new JComboBox<>(vcbTypeOfLoad);
        JButton jbCOFOrActivites = new JButton("Добавить запись о проведении");
        jbCOFOrActivites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {try{
                AutumnSpringSpacerManager autumnSpringSpacerManager = (AutumnSpringSpacerManager) ResourceLocator.getBean("autumnSpringSpacerManager");
                ldCOFO = new LocalDate(jdcCOForActivites.getDate());
                AutumnSpringSpacer autumnSpringSpacer = autumnSpringSpacerManager.getSpacerForDate(ldCOFO);
                if (autumnSpringSpacer == null) throw new TranstortAcrossSemesterException();
                ActivityInterface ip = null;
                switch (jcbCOForActivites.getSelectedIndex()){
                    case 0:
                        ip = new ConsultationInputPanel();
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        ip = new ActivityInputPanel(jcbCOForActivites.getSelectedIndex()+1);
                        break;
                    case 7:
                        ip = new GAKInputPanel();
                        break;
                    default:   ip = new ActivityInputPanel(2);
                }

                int res = JOptionPane.showConfirmDialog(jfrm, ip, "Введите количество",
                        JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(res == JOptionPane.OK_OPTION){
                    EntityManager entityManager;
                    SettableActivity activity =null;
                    switch (jcbCOForActivites.getSelectedIndex()){
                        case 0:
                            activity = new Consult();
                            entityManager = (ConsultManager) ResourceLocator.getBean("consultManager");
                            break;
                        case 1:
                            activity = new Exam();
                            entityManager = (ExamManager) ResourceLocator.getBean("examManager");
                            break;
                        case 2:
                            activity = new KursProject();
                            entityManager = (KursProjectManager) ResourceLocator.getBean("kursProjectManager");
                            break;
                        case 3:
                            activity = new KursRab();
                            entityManager = (KursRabManager) ResourceLocator.getBean("kursRabManager");
                            break;
                        case 4:
                            activity = new DiplomaProject();
                            entityManager = (DiplomaProjectManager) ResourceLocator.getBean("diplomaProjectManager");
                            break;
                        case 5:
                            activity = new Practice();
                            entityManager = (PracticeManager) ResourceLocator.getBean("practiceManager");
                            break;
                        case 6:
                            activity = new Zachet();
                            entityManager = (ZachetManager) ResourceLocator.getBean("zachetManager");
                            break;
                        case 7:
                            activity = new GAK();
                            entityManager = (GAKManager) ResourceLocator.getBean("GAKManager");
                            break;
                        default:
                            activity = new Exam();
                            entityManager = (ExamManager) ResourceLocator.getBean("examManager");
                            break;
                    }
                    activity.setNum(ip.getNumberOf());
                    activity.setPrepod(userAccount);
                    activity.setDate(new LocalDate(jdcCOForActivites.getDate()).plusDays(1));
                    switch (jcbCOFATypeOfLoad.getSelectedIndex()){
                        case 0:activity.setTypeOfLoad(TypeOfLoad.BUDGET); break;
                        case 1:activity.setTypeOfLoad(TypeOfLoad.OFF_BUDGET); break;
                        case 2:activity.setTypeOfLoad(TypeOfLoad.SHORT);break;
                        default:activity.setTypeOfLoad(TypeOfLoad.BUDGET); break;
                    }
                    PlanManager planManager = (PlanManager) ResourceLocator.getBean("planManager");
                    Plan plan = planManager.getByPrepodAndStartYear(userAccount,autumnSpringSpacer.getStartYear());
                    LocalDate startSearchDate = new LocalDate();
                    if(plan==null) throw new IOException();

                    startSearchDate.withYear(autumnSpringSpacer.getStartYear());
                    if ((startSearchDate.getYear()%4==0&& startSearchDate.getYear()%100!=0)||(startSearchDate.getYear()%400==0) ){
                        startSearchDate.withDayOfYear(244);
                    } else startSearchDate.withDayOfYear(243);
                    startSearchDate.withDayOfYear(243);
                    if(autumnSpringSpacer.getSpacerDate().isAfter(ldCOFO)){
                    /*
                        KursRabManager kursRabManager = (KursRabManager) ResourceLocator.getBean("kursRabManager");

                        ArrayList<Exam> exams = examManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, autumnSpringSpacer.getSpacerDate(), activity.getTypeOfLoad());
                        ArrayList<Consult> consults = consultManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, autumnSpringSpacer.getSpacerDate(), activity.getTypeOfLoad());
                        ArrayList<DiplomaProject> diplomaProjects = diplomaProjectManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, autumnSpringSpacer.getSpacerDate(), activity.getTypeOfLoad());
                        ArrayList<KursProject> kursProjects = kursProjectManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, autumnSpringSpacer.getSpacerDate(), activity.getTypeOfLoad());
                        ArrayList<KursRab> kursRabs = kursRabManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, autumnSpringSpacer.getSpacerDate(), activity.getTypeOfLoad());
                        ArrayList<Practice> practices = practiceManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, autumnSpringSpacer.getSpacerDate(), activity.getTypeOfLoad());*/
                        switch (activity.getTypeOfLoad()) {
                            case BUDGET:
                                planTable = plan.getPlanTableAutumnBudget();
                                /*
                                PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");

                                ArrayList<Pair> pairs = pairManager.searchCustom(userAccount.getName(),userAccount.getLastName(),startSearchDate,autumnSpringSpacer.getSpacerDate(),1,2,0);
                                if ((pairs.size()+exams.size()+consults.size()+diplomaProjects.size()+kursProjects.size()+kursRabs.size()+practices.size())>plan.getBudgetHourly()) throw new OutOfHoursException();*/
                                break;
                            case OFF_BUDGET:
                                planTable = plan.getPlanTableAutumnPlatno();
                                break;
                            case SHORT:
                                planTable = plan.getPlanTableAutumnShort();
                                break;
                        }

                    }else {
                        switch (activity.getTypeOfLoad()){
                            case BUDGET:
                                planTable = plan.getPlanTableSpringBudget();
                                break;
                            case OFF_BUDGET:
                                planTable = plan.getPlanTableSpringPlatno();
                                break;
                            case SHORT:
                                planTable = plan.getPlanTableAutumnShort();
                                break;
                        }
                    }
                    if(planTable==null) throw new Exception();
                    if(!ldCOFO.isAfter(autumnSpringSpacer.getSpacerDate())){//поменять на месте а не здесь
                        //LocalDate lol =ldCOFO;
                        //ldCOFO=startSearchDate;
                        startSearchDate = autumnSpringSpacer.getSpacerDate();
                    }

                    switch (jcbCOForActivites.getSelectedIndex()){
                        case 0:
                            ConsultManager consultManager = (ConsultManager) ResourceLocator.getBean("consultManager");
                            ArrayList<Consult> consults = consultManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for(Consult consult:consults){
                                alreadyHours += consult.getHours();
                            }
                            if(alreadyHours +activity.getHours()>=(planTable.getConsult())){
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getConsult())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;

                        case 1:
                            ExamManager examManager = (ExamManager) ResourceLocator.getBean("examManager");
                            ArrayList<Exam> exams =examManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (Exam exam:exams){
                                alreadyHours += exam.getHours();
                            }
                            if(alreadyHours +activity.getHours()>=(planTable.getExam())) {
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getExam())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;

                        case 2:
                            KursProjectManager kursProjectManager = (KursProjectManager) ResourceLocator.getBean("kursProjectManager");
                            ArrayList<KursProject> kursProjects =kursProjectManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (KursProject kursProject:kursProjects){
                                MainForm.this.alreadyHours += kursProject.getHours();
                            }
                            if(MainForm.this.alreadyHours +activity.getHours()>=(planTable.getLeadingKProject())){
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getLeadingKProject())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;

                        case 3:
                            KursRabManager kursRabManager = (KursRabManager) ResourceLocator.getBean("kursRabManager");
                            ArrayList<KursRab> kursRabs = kursRabManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (KursRab kursRab:kursRabs){
                                alreadyHours += kursRab.getHours();
                            }
                            if(alreadyHours+activity.getHours()>=(planTable.getLeadingKRab())) {
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getLeadingKRab())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;
                        case 4:
                            DiplomaProjectManager diplomaProjectManager = (DiplomaProjectManager) ResourceLocator.getBean("diplomaProjectManager");
                            ArrayList<DiplomaProject> diplomaProjects = diplomaProjectManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (DiplomaProject diplomaProject:diplomaProjects){
                                alreadyHours += diplomaProject.getHours();
                            }

                            if(alreadyHours+activity.getHours()>=(planTable.getDiplomDesign())) {
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getDiplomDesign())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;
                        case 5:
                            PracticeManager practiceManager = (PracticeManager) ResourceLocator.getBean("practiceManager");
                            ArrayList<Practice> practices = practiceManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (Practice practice:practices){
                                alreadyHours += practice.getHours();
                            }

                            if(alreadyHours+activity.getHours()>=(planTable.getPractice())) {
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getPractice())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;
                        case 6:
                            ZachetManager zachetManager = (ZachetManager) ResourceLocator.getBean("zachetManager");
                            ArrayList<Zachet> zachets = zachetManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (Zachet zachet:zachets){
                                alreadyHours += zachet.getHours();
                            }
                            if(alreadyHours+activity.getHours()>=(planTable.getZachet())) {
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getZachet())+" часов.\nПосле добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }

                            break;
                        case 7:
                            GAKManager gakManager = (GAKManager) ResourceLocator.getBean("GAKManager");
                            ArrayList<GAK> gaks = gakManager.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (GAK gak:gaks){
                                alreadyHours += gak.getHours();
                            }
                            if(alreadyHours+activity.getHours()>=(planTable.getMeeting())) {
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getMeeting())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;
                        default:
                            ExamManager examManager1 = (ExamManager) ResourceLocator.getBean("examManager");
                            ArrayList<Exam> exams1 =examManager1.getByPrepodDateTypeOfLoad(userAccount, startSearchDate, ldCOFO, activity.getTypeOfLoad());
                            alreadyHours = 0;
                            for (Exam exam:exams1){
                                alreadyHours += exam.getHours();
                            }
                            if(alreadyHours +activity.getHours()>=(planTable.getExam())) {
                                res = JOptionPane.showConfirmDialog(jfrm, "Плановое время для выбранного типа нагрузки на этот семестр " + String.format("%.2g", planTable.getExam())+" часов.\n" +
                                        "После добавления этой нагрузки, фактическое значение будет составлять "+String.format("%.2g", alreadyHours +activity.getHours()) + " часов, что выходит за заданные границы. Вы уверены, что желаете добавить это занятие?","Преввышение плана.",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
                                if(res!=JOptionPane.OK_OPTION) throw new OutOfHoursException();
                            }
                            break;
                    }

                    //noinspection unchecked
                    entityManager.create(activity);
                    JOptionPane.showMessageDialog(jfrm,"Выполнено");
            }
            }catch (IOException e1){
                JOptionPane.showMessageDialog(jfrm,"Не найден индивидуальный план семестра");
            }
            catch (OutOfHoursException e2){
                JOptionPane.showMessageDialog(jfrm,"В связи с превышением плана, добавление было отменено","Добаление прервано",JOptionPane.WARNING_MESSAGE);
            }
            catch (TranstortAcrossSemesterException ex){
                JOptionPane.showMessageDialog(jfrm,"Не найдена дата перехода между осенним и летним семестром");
            } catch (Exception e1) {
                JOptionPane.showConfirmDialog(jfrm,"Не обработанная ошибка");
            }
            }
        });
        jpCOforActivites.add(jdcCOForActivites);
        jpCOforActivites.add(jcbCOForActivites);
        jpCOforActivites.add(jcbCOFATypeOfLoad);
        jpCOforActivites.add(jbCOFOrActivites);
       // jpCOforActivites.add();

        JScrollPane jspCarriedOuter = new JScrollPane(jpCarriedOuterTimeTbl);
        jtpCarriedOuter.add("Для пар по расписанию", jpCarriedOuterTimeTbl);
        jtpCarriedOuter.add("Для остального",jpCOforActivites);
        //jtpCarriedOuter.setEnabledAt(1,!userAccount.getFaculty().equals("service"));
       // jtpCarriedOuter.setEnabledAt(,!userAccount.getFaculty().equals("service"));


        JTabbedPane jtpCabinet = new JTabbedPane();


        GridBagLayout gblCabinetInfo = new GridBagLayout();
        GridBagConstraints gbcCabinetInfo = new GridBagConstraints();
        JPanel jpCabinetInfo = new JPanel();
        jpCabinetInfo.setLayout(gblCabinetInfo);

        gbcCabinetInfo.weighty = 1.0;
        gbcCabinetInfo.weightx = 1.0;
        gbcCabinetInfo.gridwidth = 1;
        gbcCabinetInfo.gridx = 0;
        gbcCabinetInfo.gridy = 0;
        gbcCabinetInfo.anchor = GridBagConstraints.EAST;
        JLabel jlCIName = new JLabel("Имя:");
        gblCabinetInfo.addLayoutComponent(jlCIName,gbcCabinetInfo);
        gbcCabinetInfo.gridx = 1;
        gbcCabinetInfo.gridwidth =GridBagConstraints.REMAINDER;
        gbcCabinetInfo.anchor = GridBagConstraints.CENTER;
        JLabel jlCabinetInfoName = new JLabel(userAccount.getName());
        gblCabinetInfo.addLayoutComponent(jlCabinetInfoName,gbcCabinetInfo);

        gbcCabinetInfo.gridx = 0;
        gbcCabinetInfo.gridy = 1;
        gbcCabinetInfo.gridwidth = 1;
        gbcCabinetInfo.anchor = GridBagConstraints.EAST;
        JLabel jlCIMiddleName = new JLabel("Отчество:");
        gblCabinetInfo.addLayoutComponent(jlCIMiddleName,gbcCabinetInfo);
        gbcCabinetInfo.gridx = 1;
        gbcCabinetInfo.gridwidth =GridBagConstraints.REMAINDER;
        gbcCabinetInfo.anchor = GridBagConstraints.CENTER;
        JLabel jlCabinetInfoMiddleName = new JLabel(userAccount.getMiddleName());
        gblCabinetInfo.addLayoutComponent(jlCabinetInfoMiddleName,gbcCabinetInfo);

        gbcCabinetInfo.gridx = 0;
        gbcCabinetInfo.gridy = 2;
        gbcCabinetInfo.gridwidth = 1;
        gbcCabinetInfo.anchor = GridBagConstraints.EAST;
        JLabel jlCILastName = new JLabel("Фамилия:");
        gblCabinetInfo.addLayoutComponent(jlCILastName,gbcCabinetInfo);
        gbcCabinetInfo.gridx = 1;
        gbcCabinetInfo.gridwidth =GridBagConstraints.REMAINDER;
        gbcCabinetInfo.anchor = GridBagConstraints.CENTER;
        JLabel jlCabinetInfoLastName = new JLabel(userAccount.getLastName());
        gblCabinetInfo.addLayoutComponent(jlCabinetInfoLastName,gbcCabinetInfo);

        gbcCabinetInfo.gridx = 0;
        gbcCabinetInfo.gridy = 3;
        gbcCabinetInfo.gridwidth = 1;
        gbcCabinetInfo.anchor = GridBagConstraints.EAST;
        JLabel jlCIFaculty = new JLabel("Кафедра:");
        gblCabinetInfo.addLayoutComponent(jlCIFaculty,gbcCabinetInfo);
        gbcCabinetInfo.gridx = 1;
        gbcCabinetInfo.gridwidth =GridBagConstraints.REMAINDER;
        gbcCabinetInfo.anchor = GridBagConstraints.CENTER;
        JLabel jlCabinetInfoFaculty = new JLabel(userAccount.getFaculty());
        gblCabinetInfo.addLayoutComponent(jlCabinetInfoFaculty,gbcCabinetInfo);

        JPanel jpCabinetPlan = new JPanel(new FlowLayout());
        JButton jbCabinetPlan = new JButton("Выбрать индивидуальный график");
        jbCabinetPlan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xls");
                fileChooser.setFileFilter(filter);
                int ret = fileChooser.showDialog(jfrm,"Выбрать входной файл");
                if(ret == JFileChooser.APPROVE_OPTION){
                    PlanParser planParser = new PlanParser(fileChooser.getSelectedFile());
                    //PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
                    Plan plan = planParser.parsePlan();
                    PlanManager planManager = (PlanManager) ResourceLocator.getBean("planManager");
                    planManager.create(plan);
                   // pairManager.create(parser.parsePlan());
                    JOptionPane.showMessageDialog(jfrm,"Записи добавлены в базу данных");
                }
            }
        });
        jpCabinetPlan.add(jbCabinetPlan);


        jpCabinetInfo.add(jlCIName);
        jpCabinetInfo.add(jlCabinetInfoName);
        jpCabinetInfo.add(jlCIMiddleName);
        jpCabinetInfo.add(jlCabinetInfoMiddleName);
        jpCabinetInfo.add(jlCILastName);
        jpCabinetInfo.add(jlCabinetInfoLastName);
        jpCabinetInfo.add(jlCIFaculty);
        jpCabinetInfo.add(jlCabinetInfoFaculty);

        GridBagLayout gblCPassChanger = new GridBagLayout();
        GridBagConstraints gbcCPassChanger = new GridBagConstraints(GridBagConstraints.RELATIVE,GridBagConstraints.RELATIVE,GridBagConstraints.REMAINDER,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
        JPanel jpCabinetPasswordChanger = new JPanel(gblCPassChanger);

        JLabel jlCPCOldPass = new JLabel("Введите старый пароль:");
        jpfCPCOldPass = new JPasswordField();
        JLabel jlCPCNewPass = new JLabel("Введите новый пароль");
        jpfCPCNewPass = new JPasswordField();
        JLabel jlCPCNewPassRepeat =new JLabel("Повторите новый пароль");
        jpfCPCNewPassRepeat = new JPasswordField();
        JButton jbCPCSetNewPassword = new JButton("Изменить пароль");
        jbCPCSetNewPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSetNewPassword();
            }
        });

        gblCPassChanger.setConstraints(jlCPCOldPass,gbcCPassChanger);
        gblCPassChanger.setConstraints(jpfCPCOldPass,gbcCPassChanger);
        gblCPassChanger.setConstraints(jlCPCNewPass,gbcCPassChanger);
        gblCPassChanger.setConstraints(jpfCPCNewPass,gbcCPassChanger);
        gblCPassChanger.setConstraints(jlCPCNewPassRepeat,gbcCPassChanger);
        gblCPassChanger.setConstraints(jpfCPCNewPassRepeat,gbcCPassChanger);
        gblCPassChanger.setConstraints(jbCPCSetNewPassword,gbcCPassChanger);

        jpCabinetPasswordChanger.add(jlCPCOldPass);
        jpCabinetPasswordChanger.add(jpfCPCOldPass);
        jpCabinetPasswordChanger.add(jlCPCNewPass);
        jpCabinetPasswordChanger.add(jpfCPCNewPass);
        jpCabinetPasswordChanger.add(jlCPCNewPassRepeat);
        jpCabinetPasswordChanger.add(jpfCPCNewPassRepeat);
        jpCabinetPasswordChanger.add(jbCPCSetNewPassword);

        jtpCabinet.add("Сводная информация", jpCabinetInfo);
        jtpCabinet.add("Индивидуальный график", jpCabinetPlan);
        //jtpCabinet.setEnabledAt(1,false);
        jtpCabinet.add("Изменение пароля", jpCabinetPasswordChanger);



        jtpTopMenu.add("Расписание", jpTMTmTbl);
        jtpTopMenu.add("Отчёты", jspTMReport);
        jtpTopMenu.add("Учёт времни", jtpCarriedOuter);
        jtpTopMenu.add("Преподаватель", jtpCabinet);
        jtpTopMenu.add("Функции администратора", jtpAdd);

        jfrm.add(jtpTopMenu);

        jtpTopMenu.setEnabledAt(4, userAccount.getName().equals("root"));
        jtpTopMenu.setEnabledAt(1, !userAccount.getName().equals("guest"));
        jtpTopMenu.setEnabledAt(2, !userAccount.getName().equals("guest"));
        jtpTopMenu.setEnabledAt(3, !userAccount.getName().equals("guest"));

        jfrm.pack();
       jfrm.setLocationRelativeTo(null);
        jfrm.setVisible(true);
    }
/*
    private void performCarriedOutForOther() {
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod = prepodManager.findByNameAndLastName(userAccount.getName(),userAccount.getLastName());
        if(prepod.getIsWorking() && prepod.getPasskey()!=userAccount.getPasskey() && prepod.getLastActivity().plusMinutes(15).isAfter(new LocalTime())){
            rebootUser();
        }else {
            prepod.setLastActivity();
            prepod.setConsultacii((Double) snmCOFOConsult.getNumber());
            prepod.setExams((Double) snmCOFOExam.getNumber());
            prepod.setKursPrjct((Double) snmCOFOKursPrjct.getNumber());
            prepod.setKursRab((Double) snmCOFOKursRab.getNumber());
            prepod.setDiplPrjct((Double) snmCOFODiplomPrjct.getNumber());
            prepod.setPractice((Double) snmCOFOPractice.getNumber());
            prepodManager.update(prepod);
            JOptionPane.showMessageDialog(jfrm,"Изменения сохранены","Сделано",JOptionPane.INFORMATION_MESSAGE);
        }

    }*/

    private void rebootUser() {
        jfrm.setVisible(false);
        jfrm.dispose();
        new MainForm("Обнаружено другое активное подключение пользователя!");
    }

    private void performSetNewPassword() {

        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod = prepodManager.findByNameAndLastName(userAccount.getName(),userAccount.getLastName());
        if (prepod.getIsWorking() && prepod.getPasskey()!=userAccount.getPasskey()){
            rebootUser();

        }else{
            @SuppressWarnings("deprecation") int pass1=jpfCPCNewPass.getText().hashCode();
            @SuppressWarnings("deprecation") int pass2=jpfCPCNewPassRepeat.getText().hashCode();
            @SuppressWarnings("deprecation") int pass3=jpfCPCOldPass.getText().hashCode();
            if(pass1!=pass2){
                JOptionPane.showMessageDialog(jfrm,"Новый пароль и его повтор не совпадают!","Ошибка",JOptionPane.WARNING_MESSAGE);
            }else {
                if (pass3!=prepod.getPassword()){
                    JOptionPane.showMessageDialog(jfrm,"Введён недействительный пароль пользователя","Отказано в изменении",JOptionPane.ERROR_MESSAGE);
                }else {
                    userAccount.setPassword(pass1);
                    prepodManager.update(userAccount);
                    JOptionPane.showMessageDialog(jfrm,"Пароль успешно изменён","Сделано",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }


    }

    private void performCustomReport() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsm");
        fileChooser.setFileFilter(filter);
        int ret = fileChooser.showDialog(jfrm,"Выбрать входной файл");
        if(ret == JFileChooser.APPROVE_OPTION) {
            CustomReportUtil3 customReportUtil = new CustomReportUtil3(fileChooser.getSelectedFile(), new LocalDate(jdcReportStart.getDate()), new LocalDate(jcReportFinish.getDate()), userAccount.getFaculty());
            try {
                customReportUtil.makeReport();
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            } catch (WrongDatesException e) {
                    JOptionPane.showMessageDialog(jfrm,"Ошибка в датах, возможно дата начала и конца находятся в разных семестрах","Ошибка в датах",JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(jfrm, "OK");
        }
    }


    private void performYearReport() {
         LocalDate startDate = new LocalDate(jdcReportStart.getDate());
         LocalDate finishDate = new LocalDate(jcReportFinish.getDate());
         new YearReportUtil().invoke(jfrm,new LocalDate(jdcReportStart.getDate()),new LocalDate(jcReportFinish.getDate()));JFileChooser fileChooser = new JFileChooser();


    }

    private void performCarriedOutByDate() throws AlreadyInUseException{
        Boolean CarriedOutFactor;
        CarriedOutFactor = jcbCarriedOuterByDate.getSelectedIndex() == 0;
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod = prepodManager.findByNameAndLastName(userAccount.getName(), userAccount.getLastName());
        if(userAccount.getPasskey()!=prepod.getPasskey()){
            throw new AlreadyInUseException();
        }

        ArrayList<PairDate> pairDates = new ArrayList<>();
        PairDateManager dateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");

        LocalDate d = new LocalDate();
        for(d= new LocalDate(jdcCarriedOuterByDateStart.getDate()  ); !d.equals(new LocalDate(jdcCarriedOuterByDateFinish.getDate()).plusDays(1));d=d.plusDays(1) ){
            pairDates.add(dateManager.findByDate(d));
        }

        PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
        ArrayList<Pair> pairs = pairManager.findDatesPrepod(prepod,pairDates);
        for (Pair pair:pairs){

            if(!pair.getPairDate().getHoliday()&&((jcbPairNum.getSelectedIndex()==0)||(pair.getPairNum().getNum()+1==jcbPairNum.getSelectedIndex()))) {
                    pair.setIsCarriedOut(CarriedOutFactor);
                    pairManager.update(pair);

            }
        }
        if(CarriedOutFactor){
            JOptionPane.showMessageDialog(jfrm,"Отметки проставлены.");
        } else JOptionPane.showMessageDialog(jfrm,"Отметки убраны");

    }

    /**
     * Отвечает за простановку нерабочих дней и их убирание
     */
    private void setHolidaysActionPerfomed() throws AlreadyInUseException{
        LocalDate lcd1 = new LocalDate(jcHolidayStart.getDate()).plusDays(1);
        LocalDate lcd2 = new LocalDate(jcHolidayFinish.getDate()).plusDays(1);
        int allMarkedDates = 0;
        int newMarkedDates = 0;
        if(lcd1.isAfter(lcd2)){
            JOptionPane.showMessageDialog(jfrm,"Дата начала не должнабыть позже даты окончания");
            return;
        }
        Boolean holidFact = cbHolidayFactor.getSelectedIndex() != 0;
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod = prepodManager.findByNameAndLastName(userAccount.getName(), userAccount.getLastName());
        if(userAccount.getPasskey()!=prepod.getPasskey()){
            throw new AlreadyInUseException();
        }
        PairDateBuilder pairDateBuilder = PairDateBuilder.getPairDateBuilder();
        pairDateBuilder = pairDateBuilder.withHoliday(holidFact);
        PairDateManager pairDateManager = (PairDateManager) ResourceLocator.getBean("pairDateManager");
        for(LocalDate j=lcd1;!j.equals(lcd2.plusDays(1));j=j.plusDays(1)){
            PairDate persistDate = null;
            try {
                persistDate = pairDateManager.findByDate(j);
            } catch ( HibernateJdbcException e) {
                JOptionPane.showMessageDialog(jfrm, "Сбой подключения к базе данных. Проверьте её доступность и повторите попытку", "Внимание!", JOptionPane.WARNING_MESSAGE);
            }try {
            if(persistDate!= null && persistDate.getId()>-1 && persistDate.getHoliday()!=holidFact) {
                persistDate.setHoliday(holidFact);


                pairDateManager.update(persistDate);
                allMarkedDates++;
            }else {

                pairDateBuilder.withPairDate(lcd1);
                PairDate date = pairDateBuilder.build();
                pairDateManager.create(date);

                allMarkedDates++;
            }
            }catch (HibernateJdbcException e) {
                JOptionPane.showMessageDialog(jfrm,"Сбой во время работы с базой данных. Проверьте её доступность и повторите попытку","Операция не завершена",JOptionPane.ERROR_MESSAGE);

            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Поставлено ").append(allMarkedDates).append(" отметок");
        if (newMarkedDates>0) stringBuilder.append("в том числе для ").append(newMarkedDates).append(newMarkedDates>1?" отметок были добавлены даты":"отметки была добавлена дата");
        JOptionPane.showMessageDialog(jfrm,stringBuilder.toString(),"Успешное выполнение",JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Отвечает за поиск пар по преподавателю
     */
    private void searchByPrepod() throws HibernateJdbcException{
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod = prepodManager.findByNameAndLastName(jtfTmTblName.getText(),jtfTmTblLastName.getText());
        PairManager manager = (PairManager) ResourceLocator.getBean("pairManager");
        ArrayList<Pair> displayedPairs;
        if(jchbTmTblDate.isSelected()){
            displayedPairs = manager.searchCustom(jtfTmTblName.getText(), jtfTmTblLastName.getText(), new LocalDate(jdcTmTblStart.getDate()), new LocalDate(jdcAddDatesFinish.getDate()), jcbTmTblIsCarriedOut.getSelectedIndex(), jcbTmTblIsHoliday.getSelectedIndex());
        }else {
            displayedPairs =manager.searchCustom(jtfTmTblName.getText(), jtfTmTblLastName.getText(), jcbTmTblIsCarriedOut.getSelectedIndex(), jcbTmTblIsHoliday.getSelectedIndex());
        }
        listModel.clear();
        int maxLengthPairName=0;
                for(Pair pair: displayedPairs){
                    if (pair.getGroup2().getName().length()>maxLengthPairName)
                        maxLengthPairName=pair.getGroup2().getName().length();
                }

        ConsultManager consultManager = (ConsultManager) ResourceLocator.getBean("consultManager");
        DiplomaProjectManager diplomaProjectManager = (DiplomaProjectManager) ResourceLocator.getBean("diplomaProjectManager");
        ExamManager examManager = (ExamManager) ResourceLocator.getBean("examManager");
        KursProjectManager kursProjectManager = (KursProjectManager) ResourceLocator.getBean("kursProjectManager");
        KursRabManager kursRabManager = (KursRabManager) ResourceLocator.getBean("kursRabManager");
        PracticeManager practiceManager = (PracticeManager) ResourceLocator.getBean("practiceManager");

        ArrayList<Practice> practiceArrayList;
        ArrayList<KursRab> kursRabArrayList;
        ArrayList<KursProject> kursProjectArrayList;
        ArrayList<Exam> examArrayList;
        ArrayList<DiplomaProject> diplomaProjectArrayList;
        ArrayList<Consult> consultArrayList;
        if(jchbTmTblDate.isSelected()){
            consultArrayList = consultManager.getByPrepodAndDate(prepod,new LocalDate(jdcTmTblStart.getDate()), new LocalDate(jdcAddDatesFinish.getDate()));
            diplomaProjectArrayList = diplomaProjectManager.getByPrepodAndDate(prepod,new LocalDate(jdcTmTblStart.getDate()), new LocalDate(jdcAddDatesFinish.getDate()));
            examArrayList = examManager.getByPrepodAndDate(prepod,new LocalDate(jdcTmTblStart.getDate()), new LocalDate(jdcAddDatesFinish.getDate()));
           kursProjectArrayList = kursProjectManager.getByPrepodAndDate(prepod,new LocalDate(jdcTmTblStart.getDate()), new LocalDate(jdcAddDatesFinish.getDate()));
            kursRabArrayList = kursRabManager.getByPrepodAndDate(prepod,new LocalDate(jdcTmTblStart.getDate()), new LocalDate(jdcAddDatesFinish.getDate()));
             practiceArrayList = practiceManager.getByPrepodAndDate(prepod,new LocalDate(jdcTmTblStart.getDate()), new LocalDate(jdcAddDatesFinish.getDate()));
        }else {
            consultArrayList = consultManager.getByPrepod(prepod);
            diplomaProjectArrayList = diplomaProjectManager.getByPrepod(prepod);
            examArrayList = examManager.getByPrepod(prepod);
            kursProjectArrayList = kursProjectManager.getByPrepod(prepod);
            kursRabArrayList = kursRabManager.getByPrepod(prepod);
            practiceArrayList = practiceManager.getByPrepod(prepod);
        }

        timeTableElements = new ArrayList<>();

        for(Pair pair: displayedPairs){
            timeTableElements.add(pair);
        }

        for (Consult consult: consultArrayList){
            timeTableElements.add(consult);
        }

        for (DiplomaProject diplomaProject: diplomaProjectArrayList){
            timeTableElements.add(diplomaProject);
        }

        for (Exam exam: examArrayList){
            timeTableElements.add(exam);
        }

        for (KursProject kursProject: kursProjectArrayList){
            timeTableElements.add(kursProject);
        }

        for (KursRab kursRab: kursRabArrayList){
            timeTableElements.add(kursRab);
        }

        for (Practice practice: practiceArrayList){
            timeTableElements.add(practice);
        }

        Collections.sort(timeTableElements, new Comparator<TimeTableElement>() {
            @Override
            public int compare(TimeTableElement o1, TimeTableElement o2) {
                if (o1.getDate().isAfter(o2.getDate())){
                    return 1;
                }else
                if (o2.getDate().isAfter(o1.getDate())){
                    return -1;
                }else
                if(o1.getNum()>o2.getNum()){
                    return 1;
                }else if(o1.getNum()>o2.getNum()){
                    return -1;
                }
                    return 0;
            }
        });

        for(TimeTableElement element: timeTableElements){
            if (element.getNum()==-1){
                if (element.getIsOnHoliday()){
                    listModel.addElement((String)("<html><font color=orange>"+element.getDate().toString()+","+element.getDayOfWeekString()+"|№"+(element.getNum()+1)+"|"+element.getTime()+element.getLocation()+" "+element.getGroupSpacer(maxLengthPairName)+" "+element.getName()+"("+element.getStringIsCarriedOut(false)+")"));

                }else {
                    listModel.addElement((String)("<html><font color=green>"+element.getDate().toString()+","+element.getDayOfWeekString()+"|№"+(element.getNum()+1)+"|"+element.getTime()+element.getLocation()+" "+element.getGroupSpacer(maxLengthPairName)+" "+element.getName()+"("+element.getStringIsCarriedOut(false)+")"));
                }

            }else {
                if (!element.getIsOnHoliday()&&element.getIsCarriedOut()){
                    listModel.addElement((String)("<html><font color=blue>"+element.getDate().toString()+","+element.getDayOfWeekString()+"|№"+(element.getNum()+1)+"|"+element.getTime()+element.getLocation()+" "+element.getGroupSpacer(maxLengthPairName)+element.getName()+"("+element.getStringIsCarriedOut(false)+")"));

                }else if(element.getIsOnHoliday()){
                    listModel.addElement((String)("<html><font color=red>"+element.getDate().toString()+","+element.getDayOfWeekString()+"|№"+(element.getNum()+1)+"|"+element.getTime()+element.getLocation()+" "+element.getGroupSpacer(maxLengthPairName)+element.getName()+"("+element.getStringIsCarriedOut(false)+")"));

                }else if (!element.getIsCarriedOut()){
                    listModel.addElement((String)("<html><font color=purple>"+element.getDate().toString()+","+element.getDayOfWeekString()+"|№"+(element.getNum()+1)+"|"+element.getTime()+element.getLocation()+" "+element.getGroupSpacer(maxLengthPairName)+element.getName()+"("+element.getStringIsCarriedOut(false)+")"));

                }
            }
        }
        /*
        for(Pair pair:displayedPairs){
            if(!pair.getPairDate().getHoliday()&&pair.getIsCarriedOut()) {
                listModel.addElement(("<html><font color=blue>"+pair.getPairDate().getDate().toString() + "," + pair.getPairDate().getDateToString() + "|№" + (pair.getPairNum().getNum() + 1) + "|" + pair.getPairNum().getTime() + "|" + pair.getClassroom().getBuilding() + "-" + pair.getClassroom().getNum() + " " + pair.getGroup2().getName(maxLengthPairName) + pair.getPairName().getPairTypeToString(false) + "" + pair.getPairName().getName() + "(" + pair.getStringIsCarriedOut(true) + ")" + "\r\n"));
            }else{
                if(pair.getPairDate().getHoliday()){
                    listModel.addElement("<html><font color=red>"+(pair.getPairDate().getDate().toString() + "," + pair.getPairDate().getDateToString() + "|№" + (pair.getPairNum().getNum() + 1) + "|" + pair.getPairNum().getTime() + "|" + pair.getClassroom().getBuilding() + "-" + pair.getClassroom().getNum() + " " + pair.getGroup2().getName(maxLengthPairName) + pair.getPairName().getPairTypeToString(false) + "" + pair.getPairName().getName() + "(" + pair.getStringIsCarriedOut(true) + ")" + "\r\n"));
                }else
                if(!pair.getIsCarriedOut()){
                    listModel.addElement("<html><font color=purple>"+(pair.getPairDate().getDate().toString() + "," + pair.getPairDate().getDateToString() + "|№" + (pair.getPairNum().getNum() + 1) + "|" + pair.getPairNum().getTime() + "|" + pair.getClassroom().getBuilding() + "-" + pair.getClassroom().getNum() + " " + pair.getGroup2().getName(maxLengthPairName) + pair.getPairName().getPairTypeToString(false) + "" + pair.getPairName().getName() + "(" + pair.getStringIsCarriedOut(true) + ")" + "\r\n"));
                }
            }
        }*/

        if(jtfTmTblName.getText().toLowerCase().equals(userAccount.getName().toLowerCase()) && jtfTmTblLastName.getText().toLowerCase().equals(userAccount.getLastName().toLowerCase()) && displayedPairs.size()>0 && !userAccount.getFaculty().equals("service")){
            jlstTmTbl.setSelectedIndex(jlstTmTbl.getFirstVisibleIndex());
            jbTmTblCarriedOut.setVisible(true);
        }else jbTmTblCarriedOut.setVisible(false);
    }

    /**
     * основная функция обеспечивающая занесение содержимого в БД
     * выбор файла, вызов функции парсинга и вызов функции занесения этого в бд, сообщение о завершении
     */
    private void addDatesActionPerformed() throws AlreadyInUseException {
        if(jdcAddDatesStart.getDate().after(jdcAddDatesFinish.getDate())){
            JOptionPane.showMessageDialog(jfrm,"Дата начала не должнабыть позже даты окончания");
            return;
        }
        PrepodManager prepodManager = (PrepodManager) ResourceLocator.getBean("prepodManager");
        Prepod prepod = prepodManager.findByNameAndLastName(userAccount.getName(), userAccount.getLastName());
        if(userAccount.getPasskey()!=prepod.getPasskey()){
            throw new AlreadyInUseException();
        }
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xls");
        fileChooser.setFileFilter(filter);
        int ret = fileChooser.showDialog(jfrm,"Выбрать входной файл");
        if(ret == JFileChooser.APPROVE_OPTION){
            Parser parser = new Parser(fileChooser.getSelectedFile(), jfrm);
            PairManager pairManager = (PairManager) ResourceLocator.getBean("pairManager");
            pairManager.create(parser.parsePair(new LocalDate(jdcAddDatesStart.getDate()).plusDays(1), new LocalDate(jdcAddDatesFinish.getDate()).plusDays(1), cbHolidayFactor.getSelectedIndex() + 1));
            JOptionPane.showMessageDialog(jfrm,"Записи добавлены в базу данных");
        }
    }

    public static void main(String args[])
    {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new MainForm("Заполните поля:");
            }
        });

    }

}