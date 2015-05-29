package ru.zy2ba.tmtrck.util;

import ru.zy2ba.tmtrck.entity.Classroom;

/**
 * @author Zy2ba
 * @since 05.05.15
 */
public class ClassroomBuilder implements Builder<Classroom> {
    private long id;
    private Integer num;
    private Integer floor;
    private Integer building;

    private ClassroomBuilder(){}

    public static ClassroomBuilder getClassroomBuilder(){
        return new ClassroomBuilder();
    }
    public ClassroomBuilder withId(long param){
        this.id = param;
        return this;
    }
    public ClassroomBuilder withNum(int param){
        this.num = param;
        return this;
    }
    public ClassroomBuilder withFloor(){
        if (this.num>100) {
            this.floor = this.num / 100;
        }else if (this.num>10){
            this.floor = this.num/10;
        } else{
            this.floor = this.num;
        }
        return this;
    }
    public ClassroomBuilder withFloor(int hardfloor){
        this.floor = hardfloor;
        return this;
    }
    public ClassroomBuilder withBuilding(Integer param){
        this.building = param;
        return this;
    }
    @Override
    public Classroom build() throws Exception {
        if(building==null && floor==null && num ==null) {
            building = 0;
            floor = 0;
            num = 0;
           // throw new Exception();
        }
        Classroom classroom = new Classroom();
        classroom.setId(id);
        classroom.setBuilding(building);
        classroom.setFloor(floor);
        classroom.setNum(num);

        return classroom;
    }
}
