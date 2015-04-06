package ru.worldjb.android.model;

/**
 * Post
 * Created by alex_xpert on 28.02.2015.
 */
public class Post {
    public int post_id;
    public int thread_id;
    public int poster_user_id;
    public String poster_username;
    public long post_create_date;
    public String post_body;
    public String post_body_html;
    public String post_body_plain_text;
    public String signature;
    public String signature_html;
    public String signature_plain_text;
    public int post_like_count;
    public int post_attachment_count;
    public boolean post_is_published;
    public boolean post_is_deleted;
    public boolean post_is_first_post;
    public boolean post_is_liked;
    public Attachment[] attachments;
    public PostLinks links;
    public PostPermissions permissions;

    public Post() {}
}
