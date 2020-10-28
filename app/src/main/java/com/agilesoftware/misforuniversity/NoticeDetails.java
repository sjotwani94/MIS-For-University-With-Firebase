package com.agilesoftware.misforuniversity;

public class NoticeDetails {
    private String NoticeSender;
    private String NoticeSubject;
    private String NoticeImage;
    private String NoticeDescription;

    public NoticeDetails(String NoticeSender, String NoticeSubject, String NoticeImage, String NoticeDescription) {
        setNoticeSender(NoticeSender);
        setNoticeSubject(NoticeSubject);
        setNoticeImage(NoticeImage);
        setNoticeDescription(NoticeDescription);
    }

    public String getNoticeSender() {
        return NoticeSender;
    }

    public void setNoticeSender(String noticeSender) {
        NoticeSender = noticeSender;
    }

    public String getNoticeSubject() {
        return NoticeSubject;
    }

    public void setNoticeSubject(String noticeSubject) {
        NoticeSubject = noticeSubject;
    }

    public String getNoticeImage() {
        return NoticeImage;
    }

    public void setNoticeImage(String noticeImage) {
        NoticeImage = noticeImage;
    }

    public String getNoticeDescription() {
        return NoticeDescription;
    }

    public void setNoticeDescription(String noticeDescription) {
        NoticeDescription = noticeDescription;
    }
}
