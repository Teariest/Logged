package com.clementf.logged.activity_backend;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "activity_table")
public class ActivityEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "activity")
    private int id;
    private String title;
    private String description;
    private int color;
    private int icon;

    public ActivityEntity(String title, String description, int color, int icon) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.icon = icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {


        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getColor() {
        return color;
    }

    public int getIcon() {
        return icon;
    }
}
