package com.argusoft.medplat.config;

import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class RateLimitDao {

    @Autowired
    private SessionFactory sessionFactory;

    // Read config value from DB (e.g. RATE_LIMIT_GLOBAL)
    public String getConfigValue(String key, String defaultValue) {
        try {
            String sql = "SELECT config_value FROM rate_limit_config WHERE config_key = :key";
            NativeQuery<String> query = sessionFactory.getCurrentSession().createNativeQuery(sql);
            query.setParameter("key", key);
            String result = query.uniqueResult();
            return result != null ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // Read sensitive endpoints list from DB
    public List<String> getSensitiveEndpoints() {
        try {
            String sql = "SELECT endpoint_pattern FROM rate_limit_sensitive_endpoints";
            NativeQuery<String> query = sessionFactory.getCurrentSession().createNativeQuery(sql);
            return query.list();
        } catch (Exception e) {
            return List.of("/login", "/users", "/oauth", "/password");
        }
    }

    // Save violation log to DB
    public void saveViolationLog(String ipAddress, String endpoint, boolean isSensitive) {
        try {
            String sql = "INSERT INTO rate_limit_violation_log " +
                         "(ip_address, endpoint, is_sensitive, violated_at) " +
                         "VALUES (:ip, :endpoint, :isSensitive, :violatedAt)";
            NativeQuery<?> query = sessionFactory.getCurrentSession().createNativeQuery(sql);
            query.setParameter("ip", ipAddress);
            query.setParameter("endpoint", endpoint);
            query.setParameter("isSensitive", isSensitive);
            query.setParameter("violatedAt", new Date());
            query.executeUpdate();
        } catch (Exception e) {
            // fallback log to console if DB fails
            System.err.println("[RATE LIMIT LOG FAILED] " + e.getMessage());
        }
    }
}