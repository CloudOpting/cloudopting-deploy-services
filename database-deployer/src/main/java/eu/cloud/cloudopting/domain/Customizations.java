/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud.cloudopting.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author danielpo
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"}),
    @UniqueConstraint(columnNames = {"status_id"}),
    @UniqueConstraint(columnNames = {"application_id"})})
@NamedQueries({
    @NamedQuery(name = "Customizations.findAll", query = "SELECT c FROM Customizations c")})
public class Customizations implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "customization_tosca_file", nullable = false)
    private Serializable customizationToscaFile;
    @Basic(optional = false)
    @Column(name = "customization_creation", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date customizationCreation;
    @Basic(optional = false)
    @Column(name = "customization_activation", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date customizationActivation;
    @Basic(optional = false)
    @Column(name = "customization_decommission", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date customizationDecommission;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String username;
    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private Applications applicationId;
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private Status statusId;

    public Customizations() {
    }

    public Customizations(Long id) {
        this.id = id;
    }

    public Customizations(Long id, Serializable customizationToscaFile, Date customizationCreation, Date customizationActivation, Date customizationDecommission, String username) {
        this.id = id;
        this.customizationToscaFile = customizationToscaFile;
        this.customizationCreation = customizationCreation;
        this.customizationActivation = customizationActivation;
        this.customizationDecommission = customizationDecommission;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serializable getCustomizationToscaFile() {
        return customizationToscaFile;
    }

    public void setCustomizationToscaFile(Serializable customizationToscaFile) {
        this.customizationToscaFile = customizationToscaFile;
    }

    public Date getCustomizationCreation() {
        return customizationCreation;
    }

    public void setCustomizationCreation(Date customizationCreation) {
        this.customizationCreation = customizationCreation;
    }

    public Date getCustomizationActivation() {
        return customizationActivation;
    }

    public void setCustomizationActivation(Date customizationActivation) {
        this.customizationActivation = customizationActivation;
    }

    public Date getCustomizationDecommission() {
        return customizationDecommission;
    }

    public void setCustomizationDecommission(Date customizationDecommission) {
        this.customizationDecommission = customizationDecommission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Applications getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Applications applicationId) {
        this.applicationId = applicationId;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Customizations)) {
            return false;
        }
        Customizations other = (Customizations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customizations[ id=" + id + " ]";
    }
    
}
