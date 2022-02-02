package com.epam.ems.entity;

import java.util.Objects;

public class CertificateTag extends BaseEntity {
    private long certId;
    private long tagId;
    private String tagName;

    public long getCertId() {
        return certId;
    }

    public void setCertId(long certId) {
        this.certId = certId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Tag getTag(){
        return new Tag(tagId,tagName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateTag that = (CertificateTag) o;
        return certId == that.certId && tagId == that.tagId && Objects.equals(tagName, that.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certId, tagId, tagName);
    }

    @Override
    public String toString() {
        return "CertificateTag{" +
                "certId=" + certId +
                ", tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}
