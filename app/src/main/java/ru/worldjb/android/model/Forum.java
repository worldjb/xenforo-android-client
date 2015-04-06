package ru.worldjb.android.model;

/**
 * Forum
 * Created by alex_xpert on 27.02.2015.
 */
public class Forum {
    public int forum_id;
    public String forum_title;
    public String forum_description;
    public int forum_thread_count;
    public int forum_post_count;
    public boolean forum_is_follow;
    public ForumLinks links;
    public ForumPermissions permissions;

    public Forum() {}
}
