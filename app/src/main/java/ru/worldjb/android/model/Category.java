package ru.worldjb.android.model;

/**
 * Category
 * Created by alex_xpert on 25.02.2015.
 */
public class Category {
    public int category_id;
    public String category_title;
    public String category_description;
    public CategoryLinks categoryLinks;
    public CategoryPermissions categoryPermissions;

    public Category() {}
}
