package ru.worldjb.android.model;

/**
 * Notification
 * Created by alex_xpert on 28.02.2015.
 */
public class Notification {
    public int notification_id;
    public long notification_create_date;
    public int creator_user_id;
    public String creator_username;
    public String notification_type;
    public String notification_html;

    public Notification() {}
}
