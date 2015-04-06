package ru.worldjb.android.model;

/**
 * Message
 * Created by alex_xpert on 28.02.2015.
 */
public class Message {
    public int message_id;
    public int conversation_id;
    public int creator_user_id;
    public String creator_username;
    public long message_create_date;
    public String message_body;
    public String message_body_html;
    public String message_body_plain_text;
    public String signature;
    public String signature_html;
    public String signature_plain_text;
    public int message_account_count;
    public Attachment[] attachments;
    public MessageLinks links;
    public MessagePermissions permissions;

    public Message() {}
}
