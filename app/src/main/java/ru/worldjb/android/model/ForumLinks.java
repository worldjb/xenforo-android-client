package ru.worldjb.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Forum Links
 * Created by alex_xpert on 27.02.2015.
 */
public class ForumLinks {
    public String permalink;
    public String detail;
    public String followers;
    @SerializedName("sub-categories")
    public String sub_categories;
    @SerializedName("sub-forums")
    public String sub_forums;
    public String threads;

    public ForumLinks() {}
}
