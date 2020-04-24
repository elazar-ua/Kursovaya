package com.hpcc.kursovaya.dao.entity.schedule.template;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.hpcc.kursovaya.dao.constant.ConstantApplication;
import com.hpcc.kursovaya.dao.entity.EntityI;
import com.hpcc.kursovaya.dao.query.DBManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class TemplateScheduleWeek extends RealmObject implements EntityI<com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek>, Parcelable, Cloneable {
    private static final String TAG = com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek.class.getSimpleName();

    protected static RealmList<Integer> convert(List<TemplateAcademicHour> templateScheduleDayList) {
        RealmList<Integer> result = new RealmList<>();

        for (TemplateAcademicHour templateAcademicHour : templateScheduleDayList){
            result.add(templateAcademicHour.getId());
        }
        return result;
    }
    protected static List<TemplateAcademicHour> convert(RealmList<Integer> idTemplateAcademicHourList) {
        List<TemplateAcademicHour> result = new ArrayList<>();

        for(int i = 0; i<idTemplateAcademicHourList.size();i++){
            Integer id = idTemplateAcademicHourList.get(i);
            result.add(DBManager.copyObjectFromRealm(DBManager.read(TemplateAcademicHour.class,ConstantApplication.ID,id)));
        }
       /* for (Integer id : idTemplateAcademicHourList){
            result.add(DBManager.read(TemplateAcademicHour.class, ConstantApplication.ID, id));
        }*/
        return result;
    }

    @PrimaryKey
    private int id;
    private String name;
    private RealmList<Integer> idTemplateAcademicHourList;

    {
        id = 0;
        name = "";
        idTemplateAcademicHourList = new RealmList<>();
    }
    public TemplateScheduleWeek() {

    }
    public TemplateScheduleWeek(@NotNull String name, @NotNull List<TemplateAcademicHour> templateAcademicHourList) {
        setName(name);
        setTemplateAcademicHourList(templateAcademicHourList);
    }

    private void setId(int id){
        if (id < ConstantApplication.ONE){
            throw new RuntimeException("Exception! setId()");
        }
        this.id = id;
    }

    public boolean isContainsTemplateAcademicHourList(TemplateAcademicHour template){
        return idTemplateAcademicHourList.contains(template.getId());
    }

    @NotNull
    public String getName() {
        return name;
    }
    public com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek setName(@NotNull String name) {
        if (name.isEmpty()) {
            throw new RuntimeException("Exception! setName()");
        }
        this.name = name;
        return this;
    }

    @NotNull
    public List<TemplateAcademicHour> getTemplateAcademicHourList() {
        if(idTemplateAcademicHourList.isEmpty()){
            return new ArrayList<>();
        } else {
            return convert(idTemplateAcademicHourList);
        }
    }
    public com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek setTemplateAcademicHourList(@NotNull List<TemplateAcademicHour> templateAcademicHourList) {
        if (templateAcademicHourList.size() < ConstantApplication.ONE) {
            throw new RuntimeException("Exception! setTemplateScheduleDayList()");
        }
        this.idTemplateAcademicHourList = convert(templateAcademicHourList);
        return this;
    }

    @Override
    public String toString() {
        return "TemplateScheduleWeek{" +
                "id=" + id +
                ", name=" + name +
                ", idTemplateAcademicHourList=" + idTemplateAcademicHourList.toString() +
                '}';
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // EntityI
    private static int countObj = 0;

    @Override
    public int getId() {
        return id;
    }

    public boolean existsEntity() {
        // TODO Пока коряво
        RealmResults<com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek> existingEntities =
                DBManager.readAll(com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek.class, ConstantApplication.NAME, this.getName());
        for (com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek entity : existingEntities) {
            if (this.equals(entity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEntity() {
        setName(name);
        setTemplateAcademicHourList(getTemplateAcademicHourList());

        return id > ConstantApplication.ZERO;
    }
    @Override
    public void checkEntity() throws Exception {
        try {
            setName(getName());
            setTemplateAcademicHourList(getTemplateAcademicHourList());
        } catch(RuntimeException ex) {
            throw new Exception("Entity = ", ex);
        }
    }
    @Override
    public com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek createEntity() throws Exception {
        if (!isEntity()){
            checkEntity();
            int maxID = DBManager.findMaxID(this.getClass());
            setId((maxID > ConstantApplication.ZERO)? ++maxID : ++countObj);
        }

        return this;
    }

    public static List<String> entityToNameList(List<com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek> entityList) {
        List<String> result = new ArrayList<>();

        for (com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek scheduleWeek : entityList){
            result.add(scheduleWeek.getName());
        }
        return result;
    }
    @Override
    public List<String> entityToNameList() {
        return entityToNameList(DBManager.readAll(com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek.class, ConstantApplication.NAME));
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Parcelable
    protected TemplateScheduleWeek(Parcel in) {
        id = in.readInt();
        name = in.readString();
        in.readList(idTemplateAcademicHourList, Integer.class.getClassLoader());
    }
    public static final Creator<com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek> CREATOR = new Creator<com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek>() {
        @Override
        public com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek createFromParcel(Parcel in) {
            return new com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek(in);
        }

        @Override
        public com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek[] newArray(int size) {
            return new com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeList(idTemplateAcademicHourList);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Cloneable
    @NonNull
    @Override
    public com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek clone() throws CloneNotSupportedException {
        return (com.hpcc.kursovaya.dao.entity.schedule.template.TemplateScheduleWeek) super.clone();
    }

    public void deleteTemplateAcademicHour(int id) {
        idTemplateAcademicHourList.removeAll(Arrays.asList(id));
        if(idTemplateAcademicHourList.isEmpty()){
            DBManager.delete(TemplateScheduleWeek.class,ConstantApplication.ID,getId());
        }
        DBManager.delete(TemplateAcademicHour.class,ConstantApplication.ID,id);
    }
}
