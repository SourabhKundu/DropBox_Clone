package com.dropbox.main.model;

public class Notification {

    private int fileId;
    private String fileName;
    private String sharedBy;
    private boolean access;

    public Notification(int fileId, String fileName, String sharedBy, boolean access) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.sharedBy = sharedBy;
        this.access = access;
    }

    public Notification() {
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }
}