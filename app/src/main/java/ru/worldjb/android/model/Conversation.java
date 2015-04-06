package ru.worldjb.android.model;

/**
 * Conversation
 * Created by alex_xpert on 28.02.2015.
 */
public class Conversation {
    public int conversation_id;
    public String conversation_title;
    public int creator_user_id;
    public String creator_username;
    public long conversation_create_date;
    public long conversation_update_date;
    public int conversation_message_count;
    public boolean conversation_has_new_message;
    public boolean conversation_is_open;
    public boolean conversation_is_deleted;
    public Message first_message;
    public User[] recipients;
    public ConversationLinks links;
    public ConversationPermissions permissions;

    public Conversation() {}
}
