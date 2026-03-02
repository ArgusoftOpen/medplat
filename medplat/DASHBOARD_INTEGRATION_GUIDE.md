# AI-Enabled Dashboard System - Integration Guide

## Quick Start Integration

This guide shows how to integrate the AI-enabled dashboard system into the existing MEDPlat application.

## Backend Integration (Spring Boot)

### 1. Update pom.xml

Add the following dependencies to `medplat-web/pom.xml` if not already present:

```xml
<!-- AI/ML Dependencies -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-math3</artifactId>
    <version>3.6.1</version>
</dependency>

<!-- Cache Management -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<!-- Scheduling for Background Tasks -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 2. Update Spring Configuration

Add to `application.properties` or `application.yml`:

```yaml
# Dashboard Configuration
dashboard:
  default:
    refresh-interval: 300
  max-widgets: 20
  cache:
    enabled: true
    ttl-minutes: 60

# AI Features
ai:
  insights:
    enabled: true
  anomaly-detection:
    enabled: true
  forecast:
    enabled: true
  nlp:
    enabled: true

# Database
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration
```

### 3. Enable Component Scanning

Update your main Spring Boot application class:

```java
@SpringBootApplication
@EnableCaching
@EnableScheduling
@ComponentScan(basePackages = {
    "com.argusoft.medplat.dashboard.aientabled",
    // ... other packages
})
public class MedplatApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedplatApplication.class, args);
    }
}
```

### 4. Database Migration

The system uses Flyway for database migrations. The migration file is located at:
```
medplat-web/src/main/resources/db/migration/V__AI_Dashboard_Initialization.sql
```

The migration will automatically run on application startup.

### 5. Configure CORS

Update your CORS configuration to allow dashboard API calls:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/dashboard/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

## Frontend Integration (AngularJS)

### 1. Add Dashboard Module to Main App

Update `medplat-ui/app/app.js`:

```javascript
var as = angular.module("imtecho", [
    // ... existing modules ...
    "imtecho.dashboard.aientabled"  // Add this line
]);
```

### 2. Include Dashboard JS Files

Add to `medplat-ui/index.html` in the scripts section:

```html
<!-- AI Dashboard System -->
<script src="app/dashboard/ai/controllers/aiDashboard.controller.js"></script>
<!-- Additional scripts will be auto-loaded -->
```

### 3. Add Router Configuration

Update your router configuration (e.g., `config.router.js`):

```javascript
.state('dashboard.ai', {
    url: '/ai',
    templateUrl: 'app/dashboard/ai/views/ai-dashboard.html',
    controller: 'AIDashboardController'
})
.state('dashboard.builder', {
    url: '/builder/:dashboardId',
    templateUrl: 'app/dashboard/ai/views/dashboard-builder.html',
    controller: 'DashboardBuilderController'
})
.state('dashboard.insights', {
    url: '/insights/:userId',
    templateUrl: 'app/dashboard/ai/views/ai-insights.html',
    controller: 'AIInsightsController'
})
```

### 4. Add Navigation Menu Item

Update your navigation template to add a link to the AI Dashboard:

```html
<li>
    <a href="#!/dashboard/ai">
        <i class="fa fa-magic"></i> AI Dashboard
    </a>
</li>
```

## Testing the Integration

### 1. Backend API Test

```bash
# Test dashboard creation
curl -X POST http://localhost:8080/api/dashboard/create \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "dashboardName": "Test Dashboard",
    "dashboardType": "ANALYTICS",
    "aiEnabled": true
  }'

# Test NLP query
curl -X POST http://localhost:8080/api/dashboard/nlp/query \
  -H "Content-Type: application/json" \
  -d '{
    "query": "Show me patient count",
    "userId": 1
  }'
```

### 2. Frontend Testing

Open browser console and test:

```javascript
// Inject the service
angular.element(document.body).injector().get('DashboardService')
    .getDashboards(1)
    .then(response => console.log(response.data));
```

## Configuration per Module

### Dashboard Configuration Service

```java
// In your service initialization
@Bean
public DashboardConfigurationService dashboardConfigService(
        DashboardConfigurationRepository repo) {
    return new DashboardConfigurationServiceImpl();
}
```

### Data Source Configuration

Define your data sources in application.properties or through the UI:

```properties
# Database Data Source
datasource.medplat-db.type=DATABASE
datasource.medplat-db.query=SELECT * FROM patients
datasource.medplat-db.cache-strategy=SIMPLE
```

### AI Services Configuration

```properties
# Anomaly Detection
ai.anomaly.zscore-threshold=3.0
ai.anomaly.iqr-multiplier=1.5

# Forecast
ai.forecast.method=exponential-smoothing
ai.forecast.alpha=0.3

# NLP
ai.nlp.min-confidence=0.5
ai.nlp.language=en
```

## Data Source Setup Examples

### 1. Database Data Source

```sql
-- Create a data source record
INSERT INTO dashboard_data_source (
    source_name, 
    source_type, 
    query, 
    is_active
) VALUES (
    'patient_data',
    'DATABASE',
    'SELECT id, name, status FROM patients WHERE created_date > ?',
    true
);
```

### 2. API Data Source

```json
{
    "sourceName": "external_api",
    "sourceType": "REST_API",
    "query": "https://api.example.com/data",
    "connectionConfig": {
        "authType": "bearer",
        "token": "your-api-token"
    },
    "cachingStrategy": "SIMPLE",
    "cacheTtlSeconds": 3600
}
```

### 3. CSV Data Source

```json
{
    "sourceName": "csv_import",
    "sourceType": "CSV",
    "query": "/path/to/data.csv",
    "schemaMapping": {
        "col1": "patient_id",
        "col2": "patient_name",
        "col3": "status"
    }
}
```

## Role-Based Dashboard Access

### Define Dashboard Access by Role

```java
// In DashboardConfiguration
dashboard.setRoleBasedAccess("DOCTOR,NURSE,MANAGER");

// The system will automatically check user roles
// Only users with these roles can access the dashboard
```

### Update Spring Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/dashboard/**").hasAnyRole("DOCTOR", "NURSE", "MANAGER", "ADMIN")
            .antMatchers("/api/dashboard/nlp/**").hasAnyRole("DOCTOR", "NURSE", "MANAGER", "ADMIN")
            .antMatchers("/api/dashboard/ai-insights/**").hasAnyRole("DOCTOR", "NURSE", "MANAGER", "ADMIN")
            .anyRequest().authenticated();
    }
}
```

## Customization Examples

### 1. Custom Chart Configuration

```javascript
// In widget configuration
$scope.selectedWidget.visualizationConfig = {
    chart: {
        type: 'line',
        height: 300
    },
    colors: ['#2196F3', '#FF9800'],
    legend: {
        position: 'bottom'
    },
    xaxis: {
        title: 'Date'
    },
    yaxis: {
        title: 'Count'
    }
};
```

### 2. Custom AI Insight Rules

```java
@Component
public class CustomAnomalyDetector {
    public List<DashboardAIInsight> detectCustomAnomalies(
            Long widgetId, 
            List<Map<String, Object>> data) {
        // Implement custom anomaly detection logic
        // Use domain-specific thresholds
    }
}
```

### 3. Custom NLP Intent Handler

```java
@Component
public class CustomNLPHandler extends DashboardNLPServiceImpl {
    @Override
    public String extractIntent(String query) {
        // Add custom intent recognition logic
        // Could use domain-specific keywords
        return super.extractIntent(query);
    }
}
```

## Monitoring & Maintenance

### Database Maintenance

```sql
-- Clean up old insights (older than 90 days)
DELETE FROM dashboard_ai_insight 
WHERE expires_date < NOW() - INTERVAL '90 days';

-- Clean up old activity logs
DELETE FROM dashboard_user_activity 
WHERE action_timestamp < NOW() - INTERVAL '180 days';
```

### Performance Monitoring

Monitor these key metrics:

1. Dashboard load time
2. Widget rendering time
3. API response times
4. Cache hit rates
5. Database query performance

### Logging

Enable debug logging for troubleshooting:

```properties
logging.level.com.argusoft.medplat.dashboard.aientabled=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Troubleshooting

### Issue: "Module not found" errors

**Solution**: Ensure all Java classes are properly registered with Spring:
- Check `@Component` and `@Service` annotations
- Verify `componentScan` configuration

### Issue: Database tables not created

**Solution**: Check Flyway migrations:
```bash
# Check migration status
SELECT * FROM flyway_schema_history;

# Verify migration file location
ls medplat-web/src/main/resources/db/migration/
```

### Issue: API endpoints returning 404

**Solution**: Verify:
- REST controller `@RequestMapping` paths
- Spring bean initialization
- CORS configuration

### Issue: Dashboard widgets not rendering

**Solution**: Check:
- Data source connectivity
- Query syntax
- Widget configuration
- Browser console for JavaScript errors

## Next Steps

1. **Deploy to Production**: Follow MEDPlat deployment procedures
2. **Configure Data Sources**: Set up data sources for your use case
3. **Customize Widgets**: Create custom widgets as needed
4. **Train NLP Model**: Provide feedback for improved query understanding
5. **Monitor Usage**: Track user interactions and optimize

## Support

For issues or questions:
1. Check DASHBOARD_AI_README.md for comprehensive documentation
2. Review logs in `medplat-web/logs/` directory
3. Check browser console for client-side errors
4. Review Spring Boot application logs

---

**Integration Date**: March 2024
**Version**: 2.0
