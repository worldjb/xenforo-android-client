package ru.worldjb.android.model;

/**
 * Attachment
 * Created by alex_xpert on 28.02.2015.
 */
public class Attachment {
    public int attachment_id;
    public int post_id;
    public int attachment_download_count;
    public String filename;
    public boolean attachment_is_inserted;
    public AttachmentLinks links;
    public AttachmentPermissions permissions;

    public Attachment() {}
}
