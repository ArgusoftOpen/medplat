package com.argusoft.sewa.android.app.databean;

/**
 * Created by prateek on 12/20/19
 */
public class ListItemDataBean {

    private String text;

    private boolean verified;
    private boolean isHealthId;

    private String boldText;
    private String regularText;

    private int notificationImageId;
    private String notificationText;
    private String notificationCount;

    private String date;
    private String visit;
    private String vaccines;
    private boolean isTaskOverDue;
    private String vaccinesTitle;

    private String info;
    private String familyId;
    private String memberId;
    private String memberName;
    private String bookmarkText;
    private String bookmarkNote;
    private int position;
    private boolean isCrossRefBookmark;
    private boolean isAudio;
    private Boolean anyScreeningDone;
    private Boolean highlightView;
    private boolean displayEarly;
    private String memberGender;
    private String memberAge;
    private String screeningStatus;
    private Integer isScreeningDone;
    private int pendingAbhaCount;
    private String color;

    private String healthInfraName;
    private String requestStatus;
    private String requestReason;
    private String rejectReason;
    private Boolean isFromKiosk;
    private String createdDate;
    private String completedDate;
    private String announcementTitle;
    private String publishDate;
    private String fileName;
    private boolean hasSeen;
    private String state;
    private boolean isRequestView;
    private StringBuilder contactNumber;

    public ListItemDataBean() {
    }

    public ListItemDataBean(String text) {
        this.text = text;
    }

    public ListItemDataBean(String boldText, String regularText) {
        this.boldText = boldText;
        this.regularText = regularText;
        this.verified = false;
    }

    public ListItemDataBean(String boldText, String regularText, boolean verified) {
        this.boldText = boldText;
        this.regularText = regularText;
        this.verified = verified;
    }

    public ListItemDataBean(int notificationImageId, String notificationText, String notificationCount) {
        this.notificationImageId = notificationImageId;
        this.notificationText = notificationText;
        this.notificationCount = notificationCount;
    }

    public ListItemDataBean(String boldText, String regularText, String info) {
        this.boldText = boldText;
        this.regularText = regularText;
        this.info = info;
    }

    public ListItemDataBean(String announcementTitle, String publishDate, String fileName, boolean hasSeen) {
        this.announcementTitle = announcementTitle;
        this.publishDate = publishDate;
        this.fileName = fileName;
        this.hasSeen = hasSeen;
    }

    public ListItemDataBean(String date, String boldText, String regularText, String visit, boolean isTaskOverDue) {
        this.date = date;
        this.boldText = boldText;
        this.regularText = regularText;
        this.visit = visit;
        this.isTaskOverDue = isTaskOverDue;
    }

    public ListItemDataBean(String familyId, String memberId, String memberName, String rel, String relName) {
        this.familyId = familyId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.boldText = rel;
        this.regularText = relName;
    }

    public ListItemDataBean(String memberName, String uniqueId, String memberAge, String gender, String screeningStatus, String healthInfraName, Integer isScreeningDone) {
        this.memberName = memberName;
        this.memberId = uniqueId;
        this.memberAge = memberAge;
        this.memberGender = gender;
        this.screeningStatus = screeningStatus;
        this.isScreeningDone = isScreeningDone;
        this.healthInfraName =  healthInfraName;
    }

    public ListItemDataBean(String healthInfraName, String requestStatus, String requestReason, Boolean isFromKiosk, String createdDate, String completedDate, String rejectReason, StringBuilder contactNumber, boolean isRequestView) {
        this.healthInfraName = healthInfraName;
        this.requestStatus = requestStatus;
        this.requestReason = requestReason;
        this.isFromKiosk = isFromKiosk;
        this.createdDate = createdDate;
        this.completedDate = completedDate;
        this.rejectReason = rejectReason;
        this.contactNumber = contactNumber;
        this.isRequestView = isRequestView;
    }

    public ListItemDataBean(String date, String boldText, String regularText, String visit, String vaccinesTitle, String vaccines, boolean isTaskOverDue) {
        this.date = date;
        this.boldText = boldText;
        this.regularText = regularText;
        this.vaccinesTitle = vaccinesTitle;
        this.visit = visit;
        this.vaccines = vaccines;
        this.isTaskOverDue = isTaskOverDue;
    }

    public ListItemDataBean(String familyId, String memberId, String memberName, String rel, String relName, boolean verified) {
        this.familyId = familyId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.boldText = rel;
        this.regularText = relName;
        this.verified = verified;
    }

    public ListItemDataBean(String familyId, String memberId, String memberName, String rel, String relName, boolean verified,int pendingAbhaCount) {
        this.familyId = familyId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.boldText = rel;
        this.regularText = relName;
        this.verified = verified;
        this.pendingAbhaCount = pendingAbhaCount;
    }

    public ListItemDataBean(String memberId, String relName, boolean verified, boolean isHealthId) {
        this.memberId = memberId;
        this.regularText = relName;
        this.verified = verified;
        this.isHealthId = isHealthId;
    }

    public ListItemDataBean(Boolean highlightView, String state, String boldText, String regularText) {
        this.boldText = boldText;
        this.regularText = regularText;
        this.state = state;
        this.highlightView = highlightView;
    }

    public ListItemDataBean(String familyId, String memberId, String memberName, String rel, String relName, boolean verified, Boolean highlightView) {
        this.familyId = familyId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.boldText = rel;
        this.regularText = relName;
        this.verified = verified;
        this.highlightView = highlightView;
    }

    public ListItemDataBean(String date, String boldText, String regularText, String visit, boolean isTaskOverDue, Boolean highlightView, boolean displayEarly) {
        this.date = date;
        this.boldText = boldText;
        this.regularText = regularText;
        this.visit = visit;
        this.isTaskOverDue = isTaskOverDue;
        this.highlightView = highlightView;
        this.displayEarly = displayEarly;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getBoldText() {
        return boldText;
    }

    public void setBoldText(String boldText) {
        this.boldText = boldText;
    }

    public String getRegularText() {
        return regularText;
    }

    public void setRegularText(String regularText) {
        this.regularText = regularText;
    }

    public int getNotificationImageId() {
        return notificationImageId;
    }

    public void setNotificationImageId(int notificationImageId) {
        this.notificationImageId = notificationImageId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public String getVaccines() {
        return vaccines;
    }

    public void setVaccines(String vaccines) {
        this.vaccines = vaccines;
    }

    public boolean isTaskOverDue() {
        return isTaskOverDue;
    }

    public void setTaskOverDue(boolean taskOverDue) {
        isTaskOverDue = taskOverDue;
    }

    public String getVaccinesTitle() {
        return vaccinesTitle;
    }

    public void setVaccinesTitle(String vaccinesTitle) {
        this.vaccinesTitle = vaccinesTitle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getBookmarkText() {
        return bookmarkText;
    }

    public void setBookmarkText(String bookmarkText) {
        this.bookmarkText = bookmarkText;
    }

    public String getBookmarkNote() {
        return bookmarkNote;
    }

    public void setBookmarkNote(String bookmarkNote) {
        this.bookmarkNote = bookmarkNote;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCrossRefBookmark() {
        return isCrossRefBookmark;
    }

    public void setCrossRefBookmark(boolean crossRefBookmark) {
        isCrossRefBookmark = crossRefBookmark;
    }

    public boolean isAudio() {
        return isAudio;
    }

    public void setAudio(boolean audio) {
        isAudio = audio;
    }

    public boolean isHealthId() {
        return isHealthId;
    }

    public void setHealthId(boolean healthId) {
        isHealthId = healthId;
    }

    public Boolean getAnyScreeningDone() {
        return anyScreeningDone;
    }

    public void setAnyScreeningDone(Boolean anyScreeningDone) {
        this.anyScreeningDone = anyScreeningDone;
    }

    public Boolean getHighlightView() {
        return highlightView;
    }

    public void setHighlightView(Boolean highlightView) {
        this.highlightView = highlightView;
    }

    public boolean isDisplayEarly() {
        return displayEarly;
    }

    public void setDisplayEarly(boolean displayEarly) {
        this.displayEarly = displayEarly;
    }

    public String getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(String memberGender) {
        this.memberGender = memberGender;
    }

    public String getMemberAge() {
        return memberAge;
    }

    public void setMemberAge(String memberAge) {
        this.memberAge = memberAge;
    }

    public String getScreeningStatus() {
        return screeningStatus;
    }

    public void setScreeningStatus(String screeningStatus) {
        this.screeningStatus = screeningStatus;
    }

    public int getPendingAbhaCount() {return pendingAbhaCount;}

    public void setPendingAbhaCount(int pendingAbhaCount) { this.pendingAbhaCount = pendingAbhaCount;}

    public String getHealthInfraName() {
        return healthInfraName;
    }

    public void setHealthInfraName(String healthInfraName) {
        this.healthInfraName = healthInfraName;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Boolean getFromKiosk() {
        return isFromKiosk;
    }

    public void setFromKiosk(Boolean fromKiosk) {
        isFromKiosk = fromKiosk;
    }

    public Integer getIsScreeningDone() {
        return isScreeningDone;
    }

    public void setIsScreeningDone(Integer isScreeningDone) {
        this.isScreeningDone = isScreeningDone;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(boolean hasSeen) {
        this.hasSeen = hasSeen;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isRequestView() {
        return isRequestView;
    }

    public void setRequestView(boolean requestView) {
        isRequestView = requestView;
    }

    public StringBuilder getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(StringBuilder contactNumber) {
        this.contactNumber = contactNumber;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
