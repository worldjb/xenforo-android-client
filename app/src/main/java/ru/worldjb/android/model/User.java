package ru.worldjb.android.model;

/**
 * User
 * Created by alex_xpert on 28.02.2015.
 */
public class User {
    public int user_id;
    public String username;
    public String user_title;
    public int user_message_count;
    public long user_register_date;
    public int user_like_count;
    public boolean user_is_visitor;
    public String user_email;
    public int user_dob_day;
    public int user_dob_month;
    public int user_dob_year;
    public int user_timezone_offset;
    public boolean user_has_password;
    public int user_unread_notification_count;
    public int user_unread_conversation_count;
    public boolean user_is_valid;
    public boolean user_is_verified;
    public boolean user_is_followed;
    public UserCustomFields user_custom_fields;
    public UserGroup[] user_groups;
    public UserLinks links;
    public UserPermissions permissions;
    public UserSelfPermissions self_permissions;
}
