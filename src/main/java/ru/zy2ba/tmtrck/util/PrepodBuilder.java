package ru.zy2ba.tmtrck.util;
import ru.zy2ba.tmtrck.entity.Prepod;

/**
 * @author Zy2ba
 * @since 04.05.2015
 */
public class PrepodBuilder implements ru.zy2ba.tmtrck.util.Builder<Prepod> {
    private long id;
    private String Name;
    private String lastName;
    private String middleName;
    private String faculty;
    private String password;
    private PrepodBuilder(){}
    public static PrepodBuilder getPrepodBuilder(){
        return new PrepodBuilder();
    }

    public PrepodBuilder withId(long expectedId){
        this.id = expectedId;
        return this;
    }


    public PrepodBuilder withName(String string) {
        this.Name = string;
        return this;
    }

    public PrepodBuilder withFaculty(String string) {
        this.faculty = string;
        return this;
    }
    public PrepodBuilder withLastName(String string) {
        this.lastName = string;
        return this;
    }
    public PrepodBuilder withMiddleName(String string) {
        this.middleName = string;
        return this;
    }
    public PrepodBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    @Override
    public Prepod build() {
        Prepod prepod = new Prepod();
        prepod.setId(id);
        prepod.setName(Name);
        if(lastName!=null) {
            prepod.setLastName(lastName);
        }else prepod.setLastName("");
        if(middleName!=null) {
            prepod.setMiddleName(middleName);
        }else prepod.setMiddleName("");
        if(faculty!=null){
            prepod.setFaculty(faculty);
        } else prepod.setFaculty("");
        if(this.password==null) {

            prepod.setPassword(prepod.getLastName().hashCode());
        }else {
            prepod.setPassword(this.password.hashCode());
        }
        prepod.setIsWorking(false);
        prepod.setLastActivity();
        prepod.setPasskey();
        return prepod;

    }
}
