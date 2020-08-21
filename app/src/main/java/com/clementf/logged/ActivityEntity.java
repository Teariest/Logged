package com.clementf.logged;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity_table")
public class ActivityEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "activity")
    private int id;
    private String title;
    private String description;
    private int color;
    private int icon;
    private int order;

    public ActivityEntity(String title, String description, int color, int icon, int order) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.icon = icon;
        this.order = order;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* TODO: may have to use this method if we want to change the order in the future
    public void setOrder(int order) {
        this.order = order;
    }
    */

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

    public int getOrder() {
        return order;
    }
}
