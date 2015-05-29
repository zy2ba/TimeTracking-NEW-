package ru.zy2ba.tmtrck.util;

import ru.zy2ba.tmtrck.entity.Group2;

/**
 * @author Zy2ba
 * @since 05.05.2015
 */
public class GroupBuilder implements Builder<Group2> {
    private long id;
    private String name;
    private String faculty;
    private String subfaculty;
    private String info;
    private GroupBuilder(){}
    public static GroupBuilder getGroupBuilder(){
        return new GroupBuilder();
    }

    public GroupBuilder withId(long id){
        this.id =  id;
        return this;
    }

    public GroupBuilder withName(String name){
        this.name = name;
        return this;
    }

    public GroupBuilder withFaculty(String faculty){
        this.faculty = faculty;
        return this;
    }

    public GroupBuilder withSubfaculty(String subfaculty){
        this.subfaculty = subfaculty;
        return this;
    }

    public GroupBuilder withInfo(String info){
        this.info = info;
        return this;
    }

    @Override
    public Group2 build() {


        Group2 group2 = new Group2();

        group2.setId(this.id);
        if (this.name==null){
            this.name ="";
        }
        group2.setName(this.name.substring(0,name.length()-3));
        if (this.faculty==null){
            this.faculty ="";
        }
        group2.setFaculty(this.faculty);
        if (this.subfaculty==null){
            this.subfaculty ="";
        }
        group2.setSubfaculty(this.subfaculty);
        if (this.info==null){
            this.info ="";
        }
        group2.setInfo(this.info);
        return group2;
    }
}
