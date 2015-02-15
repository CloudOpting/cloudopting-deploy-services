/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud.cloudopting.domain;

import ro.tn.events.api.entity.BaseEntity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"status_id"})})
@NamedQueries({
    @NamedQuery(name = "Applications.findAll", query = "SELECT a FROM Applications a")})
public class Applications implements BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "application_name", nullable = false, length = 50)
    private String applicationName;
    @Basic(optional = false)
    @Column(name = "application_description", nullable = false, length = 2147483647)
    private String applicationDescription;
    @Basic(optional = false)
    @Column(name = "application_tosca_template", nullable = false)
    private Serializable applicationToscaTemplate;
    @Basic(optional = false)
    @Column(name = "application_version", nullable = false, length = 10)
    private String applicationVersion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "applicationId")
    private Customizations customizations;
    @OneToOne(mappedBy = "applicationId")
    private ApplicationMedia applicationMedia;
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private Status statusId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    public Applications() {
    }

    public Applications(Long id) {
        this.id = id;
    }

    public Applications(Long id, String applicationName, String applicationDescription, Serializable applicationToscaTemplate, String applicationVersion) {
        this.id = id;
        this.applicationName = applicationName;
        this.applicationDescription = applicationDescription;
        this.applicationToscaTemplate = applicationToscaTemplate;
        this.applicationVersion = applicationVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public Serializable getApplicationToscaTemplate() {
        return applicationToscaTemplate;
    }

    public void setApplicationToscaTemplate(Serializable applicationToscaTemplate) {
        this.applicationToscaTemplate = applicationToscaTemplate;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public Customizations getCustomizations() {
        return customizations;
    }

    public void setCustomizations(Customizations customizations) {
        this.customizations = customizations;
    }

    public ApplicationMedia getApplicationMedia() {
        return applicationMedia;
    }

    public void setApplicationMedia(ApplicationMedia applicationMedia) {
        this.applicationMedia = applicationMedia;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Applications)) {
            return false;
        }
        Applications other = (Applications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Applications[ id=" + id + " ]";
    }
    
}
