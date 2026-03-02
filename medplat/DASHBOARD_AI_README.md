# AI-Enabled Dynamic Dashboard System for MEDPlat

## Overview

This comprehensive AI-enabled dashboard system has been integrated into the MEDPlat platform to empower users with data-driven decision-making capabilities. The system provides flexible, intelligent dashboards with automatic visualization rendering,  AI-driven insights, natural language query processing, and personalized recommendations.

## Architecture

### System Components

1. **Backend (Spring Boot)**
   - RESTful APIs for dashboard management
   - Data source integration layer
   - AI/ML services for insights generation
   - NLP query processor
   - Role-based access control

2. **Frontend (AngularJS)**
   - Interactive dashboard builder
   - Dynamic widget rendering
   - Real-time data visualization
   - NLP query interface
   - AI insights display

3. **Database (PostgreSQL)**
   - Dashboard configurations
   - Widget definitions
   - Data source mappings
   - AI insights tracking
   - User preferences and activity

## Key Features

### 1. Configurable Dashboard System

**Dashboard Types:**
- `EXECUTIVE`: High-level KPIs and strategic metrics
- `OPERATIONAL`: Operational metrics and tasks
- `CLINICAL`: Patient care and clinical data
- `ANALYTICS`: Advanced data analysis and trends
- `CUSTOM`: User-defined dashboards

**Features:**
- Drag-and-drop widget positioning
- Custom layout configuration
- Filter management
- Role-based access control
- Public/private dashboard sharing
- Dashboard import/export as JSON

### 2. Dynamic Widget System

**Supported Widget Types:**
- `LINE_CHART`: Trend analysis and time-series data
- `BAR_CHART`: Categorical comparisons
- `PIE_CHART`: Part-to-whole relationships
- `TABLE`: Detailed data views
- `KPI`: Key performance indicators
- `GAUGE`: Percentage/progress indicators
- `MAP`: Geographical visualization
- `HEATMAP`: Intensity distribution visualization

**Widget Features:**
- Configurable data sources
- Aggregation options (SUM, AVG, COUNT, MIN, MAX)
- Time-series support (Daily, Weekly, Monthly, Yearly)
- Caching strategies
- Auto-refresh intervals

### 3. Data Source Integration

**Supported Data Source Types:**
- `DATABASE`: Direct database queries
- `REST_API`: External REST endpoints
- `CSV`: CSV file uploads
- `GRAPHQL`: GraphQL API endpoints
- `KAFKA`: Real-time streaming data

**Features:**
- Query configuration management
- Schema mapping and transformation
- Data validation rules
- Pagination support
- Connection pooling and caching
- Real-time data synchronization

### 4. AI-Driven Insights

**Insight Types:**
- `ANOMALY`: Statistical anomalies using Z-score and IQR methods
- `FORECAST`: Predictive forecasting using exponential smoothing
- `SUMMARY`: Automated data summarization
- `TREND`: Trend detection and analysis
- `RECOMMENDATION`: Personalized recommendations
- `CORRELATION`: Cross-variable correlation analysis

**AI Capabilities:**
- Anomaly detection with configurable sensitivity
- Time-series forecasting
- Statistical analysis
- Trend identification
- Confidence scoring
- Automatic insight expiration

### 5. Natural Language Processing (NLP)

**Features:**
- Natural language query processing
- Intent extraction (Chart, KPI, Summary, Filter, Anomaly, Forecast, Comparison)
- Entity extraction (metrics, dimensions, filters, date ranges)
- Widget configuration suggestions
- Chart generation from text descriptions
- Filter hints and recommendations
- Confidence scoring (0-1 scale)
- Clarifying question generation for ambiguous queries

**Supported Date Expressions:**
- "last 7 days", "past week"
- "last 30 days", "past month"
- "last 90 days", "past quarter"
- "last 365 days", "past year"
- "this month", "this quarter", "this year"
- "today", "yesterday"

### 6. Personalization & Recommendations

**Recommendation Types:**
- **Role-Based**: Dashboards and KPIs relevant to user role
- **Usage-Based**: Suggestions based on interaction history
- **Trending**: Popular dashboards in organization
- **Collaborative**: "Users like you also viewed..."
- **Content-Based**: Similar dashboards and widgets
- **Behavioral**: Predicted interest scoring

**Personalization Features:**
- User preferences (theme, language, refresh intervals)
- Interaction tracking
- Default dashboard selection
- Content relevance scoring
- Personalized alerts and notifications

### 7. Role-Based Access Control (RBAC)

**Supported Roles:**
- `ADMIN`: System administration, all dashboards
- `DOCTOR`: Clinical dashboards and patient data
- `NURSE`: Nursing operations and patient care
- `MANAGER`: Management and performance dashboards
- `VIEWER`: Public dashboards and reports

**Access Features:**
- Dashboard-level role restrictions
- Widget visibility by role
- Data-level access control
- Audit logging of access patterns

## API Endpoints

### Dashboard Management

```
POST   /api/dashboard/create              - Create new dash board
GET    /api/dashboard/{dashboardId}       - Get dashboard details
GET    /api/dashboard/user/{userId}       - Get user's dashboards
GET    /api/dashboard/type/{type}         - Get dashboards by type
PUT    /api/dashboard/{dashboardId}       - Update dashboard
DELETE /api/dashboard/{dashboardId}       - Delete dashboard
POST   /api/dashboard/{dashboardId}/clone/{userId} - Clone dashboard
GET    /api/dashboard/{dashboardId}/export         - Export as JSON
POST   /api/dashboard/import/{userId}              - Import from JSON
GET    /api/dashboard/public/all                   - Get public dashboards
GET    /api/dashboard/recommendations/{userId}/{dashboardId} - Get recommendations
```

### NLP Query Processing

```
POST   /api/dashboard/nlp/query                   - Process natural language query
GET    /api/dashboard/nlp/intent                  - Extract intent from query
GET    /api/dashboard/nlp/entities                - Extract entities from query
GET    /api/dashboard/nlp/suggest-widget          - Suggest widget configuration
POST   /api/dashboard/nlp/generate-chart          - Generate chart from query
GET    /api/dashboard/nlp/kpi                     - Query KPI values
POST   /api/dashboard/nlp/suggest-filters         - Suggest filters
GET    /api/dashboard/nlp/parse-date              - Parse date expressions
GET    /api/dashboard/nlp/validate                - Validate query
GET    /api/dashboard/nlp/confidence              - Get confidence score
POST   /api/dashboard/nlp/feedback                - Provide feedback
```

### AI Insights

```
GET    /api/dashboard/ai-insights/widget/{widgetId}           - Get widget insights
GET    /api/dashboard/ai-insights/user/{userId}/recent        - Get recent insights
GET    /api/dashboard/ai-insights/critical                    - Get critical insights
PUT    /api/dashboard/ai-insights/{insightId}/acknowledge     - Acknowledge insight
PUT    /api/dashboard/ai-insights/{insightId}/resolve         - Resolve insight
PUT    /api/dashboard/ai-insights/{insightId}/feedback        - Provide feedback
POST   /api/dashboard/ai-insights/anomaly-detection           - Detect anomalies
POST   /api/dashboard/ai-insights/forecast                    - Generate forecast
POST   /api/dashboard/ai-insights/summary                     - Generate summary
POST   /api/dashboard/ai-insights/detect-trends               - Detect trends
POST   /api/dashboard/ai-insights/correlations                - Analyze correlations
```

## Usage Examples

### 1. Creating a Dashboard

```javascript
// Angular Controller
$scope.createDashboard = function() {
    var dashboardConfig = {
        dashboardName: "Patient Care Overview",
        description: "Real-time patient care metrics",
        dashboardType: "CLINICAL",
        aiEnabled: true,
        nlpEnabled: true,
        anomalyDetectionEnabled: true,
        refreshIntervalSeconds: 300,
        roleBasedAccess: "DOCTOR,NURSE"
    };
    
    DashboardService.createDashboard(dashboardConfig).then(function(response) {
        $scope.dashboardId = response.data.dashboardId;
    });
};
```

### 2. Processing Natural Language Query

```javascript
// NLP Query Example
var nlpQuery = {
    query: "Show me patient count by department for last 30 days",
    userId: 123,
    dashboardId: 456
};

DashboardNLPService.processQuery(nlpQuery).then(function(response) {
    // response contains:
    // - queryType: "CHART_REQUEST"
    // - suggestedWidget: widget configuration
    // - confidenceScore: 0.92
    // - insights: AI-generated insights
});
```

### 3. Getting AI Insights

```javascript
// Get widget insights
DashboardAIService.getWidgetInsights(widgetId).then(function(response) {
    $scope.insights = response.data.insights;
    // Handle anomalies, forecasts, recommendations
});
```

### 4. Generating Anomaly Detection

```javascript
// Request anomaly detection
var data = [...];  // widget data
DashboardAIService.generateAnomalies(widgetId, data).then(function(response) {
    // response contains detected anomalies with severity levels
    $scope.anomalies = response.data.anomalies;
});
```

## Database Schema

### Key Tables

1. **dashboard_configuration**: Main dashboard configuration
2. **dashboard_widget**: Widget definitions and mappings
3. **dashboard_data_source**: Data source configurations
4. **dashboard_ai_insight**: Generated AI insights and anomalies
5. **dashboard_user_preference**: User preferences and settings
6. **dashboard_user_activity**: Interaction tracking for personalization
7. **dashboard_filter**: Filter configurations
8. **dashboard_nlp_query_history**: NLP query history for training

## Configuration

### Application Properties

```properties
# Dashboard Configuration
dashboard.default.refresh.interval=300
dashboard.max.widgets.per.dashboard=20
dashboard.cache.enabled=true
dashboard.cache.ttl.minutes=60

# AI Features
ai.insights.enabled=true
ai.anomaly.detection.enabled=true
ai.forecast.enabled=true
ai.nlp.enabled=true

# Data Sources
datasource.connection.pool.size=10
datasource.query.timeout.seconds=30
datasource.cache.strategy=SIMPLE
```

## Extending the System

### Adding New Widget Type

```java
// 1. Add to widgetTypes array in controller
$scope.widgetTypes = [..., 'CUSTOM_WIDGET'];

// 2. Implement rendering in widget directive
.directive('aiDashboardWidget', function() {
    return {
        templateUrl: 'ai-widget.html',
        link: function(scope) {
            if (scope.widget.widgetType === 'CUSTOM_WIDGET') {
                // Implement custom logic
            }
        }
    };
});
```

### Adding New Data Source Type

```java
// 1. Implement DataSourceExecutor interface
public class CustomDataSourceExecutor implements DataSourceExecutor {
    @Override
    public Map<String, Object> execute(DashboardDataSource source, Map<String, Object> params) {
        // Implement custom data retrieval
    }
}

// 2. Register in DataSourceFactory
@Component
public class DataSourceFactory {
    public DataSourceExecutor getExecutor(String sourceType) {
        switch(sourceType) {
            case "CUSTOM": return new CustomDataSourceExecutor();
            // ...
        }
    }
}
```

## Performance Optimization

### Caching Strategies

1. **Simple Cache**: In-memory caching for single-server deployments
2. **Distributed Cache**: Redis-based caching for multi-server setups
3. **Result Caching**: Cache query results based on TTL

### Best Practices

1. Set appropriate cache TTL based on data freshness requirements
2. Use pagination for large datasets
3. Implement data aggregation in database queries
4. Use time-series aggregation (daily, weekly, monthly)
5. Enable compression for large widget data

## Security

### Data Protection

- Role-based dashboard access
- Row-level data access restrictions
- Query parameter validation
- SQL injection prevention (parameterized queries)
- XSS protection in NLP input handling

### API Authentication

- JWT token-based authentication
- API rate limiting
- Request validation
- Audit logging of user actions

## Troubleshooting

### Common Issues

**Issue**: Dashboard widgets not loading
- Check data source connectivity
- Verify query syntax and credentials
- Check database permissions for user role
- Review browser console for errors

**Issue**: AI insights not generating
- Ensure sufficient data points (minimum 3)
- Check if aiEnabled flag is true
- Verify widget configuration
- Check server logs for errors

**Issue**: NLP queries not working
- Verify nlpEnabled is true
- Check query syntax and clarity
- Review confidence score (< 0.5 may indicate ambiguity)
- Provide feedback to improve NLP model

## Future Enhancements

1. **Advanced ML Models**: Integration with TensorFlow, PyTorch
2. **Real-Time Streaming**: Kafka/WebSocket support for live data
3. **Custom AI Models**: User-defined ML models
4. **Advanced Analytics**: Statistical modeling, regression analysis
5. **Mobile Application**: Mobile dashboard support
6. **Collaborative Dashboards**: Real-time collaboration feature
7. **Advanced Scheduling**: Automated report generation
8. **Custom Visualizations**: D3.js and custom 3D visualizations
9. **Multi-Language Support**: NLP in multiple languages
10. **Voice Interface**: Voice-activated dashboard queries

## Support & Documentation

For more information, refer to:
- API Documentation: `/docs/api`
- User Guide: `/docs/user-guide`
- Developer Guide: `/docs/developer-guide`
- Architecture Diagrams: `/docs/architecture`

## Version

- **System Version**: 2.0
- **Release Date**: 2024
- **Java Version**: 17+
- **Spring Boot Version**: 2.7.17+
- **AngularJS Version**: 1.x

## License

This component is part of the MEDPlat platform and follows the same licensing terms as the parent project.

---

**Last Updated**: March 2024
**Maintained By**: MEDPlat Development Team
