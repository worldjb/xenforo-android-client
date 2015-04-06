package ru.worldjb.android.model;

/**
 * Thread
 * Created by alex_xpert on 28.02.2015.
 */
public class Thread {
    public int thread_id;
    public int forum_id;
    public String thread_title;
    public int thread_view_count;
    public int thread_post_count;
    public int creator_user_id;
    public String creator_username;
    public long thread_create_date;
    public long thread_update_date;
    public boolean thread_is_published;
    public boolean thread_is_deleted;
    public boolean thread_is_sticky;
    public boolean thread_is_followed;
    public Post first_post;
    ThreadLinks links;
    ThreadPermissions permissions;

    public Thread() {}
}
