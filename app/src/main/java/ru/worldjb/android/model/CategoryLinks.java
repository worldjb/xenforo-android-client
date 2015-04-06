package ru.worldjb.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Links
 * Created by alex_xpert on 26.02.2015.
 */
public class CategoryLinks {
    public String permalink;
    public String detail;
    @SerializedName("sub-categories")
    public String sub_categories;
    @SerializedName("sub-forums")
    public String sub_forums;

    public CategoryLinks() {}
}
