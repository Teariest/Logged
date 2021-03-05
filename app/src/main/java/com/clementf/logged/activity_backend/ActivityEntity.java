package com.clementf.logged.activity_backend;

import androidx.annotation.IntRange;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "activity_table")
public class ActivityEntity {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private int colorResource; // should be the actual resource location
    private int iconResource;

    public ActivityEntity(int ID, String name, String description, int colorResource, int iconResource) {
        this.id = ID;
        this.name = name;
        this.description = description;
        this.colorResource = colorResource;
        this.iconResource = iconResource;
    }

    public ActivityEntity(String name, String description, int colorResource, int iconResource) {
        this.name = name;
        this.description = description;
        this.colorResource = colorResource;
        this.iconResource = iconResource;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getColorResource() {
        return colorResource;
    }

    public int getIconResource() {
        return iconResource;
    }

    public ActivityEntity getCopy() {
        return new ActivityEntity(id, name, description, colorResource, iconResource);
    }
}
