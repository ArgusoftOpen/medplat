package com.argusoft.medplat.config;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rate_limit_violation_log")
public class RateLimitViolationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "is_sensitive")
    private Boolean isSensitive;

    @Column(name = "violated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date violatedAt;

    public RateLimitViolationLog() {}

    public RateLimitViolationLog(String ipAddress, String endpoint, Boolean isSensitive) {
        this.ipAddress = ipAddress;
        this.endpoint = endpoint;
        this.isSensitive = isSensitive;
        this.violatedAt = new Date();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public Boolean getIsSensitive() { return isSensitive; }
    public void setIsSensitive(Boolean isSensitive) { this.isSensitive = isSensitive; }
    public Date getViolatedAt() { return violatedAt; }
    public void setViolatedAt(Date violatedAt) { this.violatedAt = violatedAt; }
}