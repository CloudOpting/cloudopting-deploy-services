/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud.cloudopting.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author danielpo
 */
@Entity
@Table(name = "application_media", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"application_id"})})
@NamedQueries({
    @NamedQuery(name = "ApplicationMedia.findAll", query = "SELECT a FROM ApplicationMedia a")})
public class ApplicationMedia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Lob
    @Column(name = "media_content")
    private byte[] mediaContent;
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    @OneToOne
    private Applications applicationId;

    public ApplicationMedia() {
    }

    public ApplicationMedia(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getMediaContent() {
        return mediaContent;
    }

    public void setMediaContent(byte[] mediaContent) {
        this.mediaContent = mediaContent;
    }

    public Applications getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Applications applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApplicationMedia)) {
            return false;
        }
        ApplicationMedia other = (ApplicationMedia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ApplicationMedia[ id=" + id + " ]";
    }
    
}
