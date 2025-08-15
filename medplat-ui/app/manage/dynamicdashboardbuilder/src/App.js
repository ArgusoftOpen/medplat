import React, { useState } from "react";
import "./App.css";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link,
  Navigate,
  useLocation,
} from "react-router-dom";
import UploadPage from "./pages/UploadPage";
import VisualizePage from "./pages/VisualizePage";
import SqlQueryPage from "./pages/SqlQueryPage";
import IndicatorMasterPage from "./pages/IndicatorMasterPage";
import DerivedAttributePage from "./pages/DerivedAttributePage";
import DatasetBuilderPage from "./pages/DatasetBuilderPage";
import DatasetMasterListPage from "./pages/DatasetMasterListPage";
import DatasetGraphPage from "./pages/DatasetGraphPage";
import "./App.css";

function NavigationSidebar() {
  const location = useLocation();
  const [isCollapsed, setIsCollapsed] = useState(false);

  const navigationItems = [
    {
      path: "/upload",
      label: "Upload Data",
      icon: "📤",
      description: "Import Excel files",
      category: "data-management",
    },
    {
      path: "/visualize",
      label: "Visualize Data",
      icon: "📊",
      description: "Create charts",
      category: "analytics",
    },
    {
      path: "/sql",
      label: "SQL Console",
      icon: "💻",
      description: "Execute queries",
      category: "development",
    },
    {
      path: "/indicator-master",
      label: "Indicator Master",
      icon: "📈",
      description: "Manage indicators",
      category: "configuration",
    },
    {
      path: "/derived-attribute",
      label: "Derived Attributes",
      icon: "🧮",
      description: "Calculate attributes",
      category: "configuration",
    },
    {
      path: "/dataset-builder",
      label: "Dataset Builder",
      icon: "🗃️",
      description: "Build custom datasets",
      category: "data-management",
    },
    {
      path: "/dataset-master-list",
      label: "Dataset Master",
      icon: "📋",
      description: "View saved datasets",
      category: "data-management",
    },
  ];

  const categories = {
    "data-management": { label: "Data Management", color: "#059669" },
    analytics: { label: "Analytics", color: "#dc2626" },
    development: { label: "Development", color: "#7c3aed" },
    configuration: { label: "Configuration", color: "#d97706" },
  };

  const groupedItems = navigationItems.reduce((acc, item) => {
    if (!acc[item.category]) acc[item.category] = [];
    acc[item.category].push(item);
    return acc;
  }, {});

  return (
    <nav
      style={{
        width: isCollapsed ? 80 : 280,
        height: "100vh",
        background: "linear-gradient(180deg, #1e40af 0%, #1d4ed8 100%)",
        borderRight: "1px solid #1e3a8a",
        display: "flex",
        flexDirection: "column",
        boxShadow: "4px 0 16px rgba(30, 64, 175, 0.15)",
        transition: "width 0.3s ease-in-out",
        position: "relative",
      }}
    >
      {/* Header */}
      <div
        style={{
          padding: isCollapsed ? "24px 8px" : "24px 20px",
          borderBottom: "1px solid rgba(255, 255, 255, 0.15)",
          background:
            "linear-gradient(135deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%)",
          backdropFilter: "blur(10px)",
          textAlign: isCollapsed ? "center" : "left",
        }}
      >
        {!isCollapsed ? (
          <>
            <h1
              style={{
                color: "#ffffff",
                fontSize: 20,
                fontWeight: 700,
                margin: 0,
                letterSpacing: "-0.025em",
                textShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
              }}
            >
              MedPlat Analytics
            </h1>
            <p
              style={{
                color: "rgba(255, 255, 255, 0.9)",
                fontSize: 13,
                margin: "4px 0 0 0",
                fontWeight: 400,
              }}
            >
              Data Management Platform
            </p>
          </>
        ) : (
          <div
            style={{
              color: "#ffffff",
              fontSize: 24,
              fontWeight: 700,
              textShadow: "0 2px 4px rgba(0, 0, 0, 0.2)",
            }}
          >
            MP
          </div>
        )}
      </div>

      {/* Collapse Toggle */}
      <button
        onClick={() => setIsCollapsed(!isCollapsed)}
        style={{
          position: "absolute",
          top: 30,
          right: -14,
          width: 28,
          height: 28,
          borderRadius: "50%",
          background: "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)",
          border: "2px solid #1e40af",
          color: "#1e40af",
          fontSize: 14,
          cursor: "pointer",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 10,
          fontWeight: 700,
          transition: "all 0.3s ease-in-out",
          boxShadow: "0 4px 12px rgba(30, 64, 175, 0.3)",
        }}
        onMouseEnter={(e) => {
          e.target.style.background =
            "linear-gradient(135deg, #1e40af 0%, #1d4ed8 100%)";
          e.target.style.color = "#ffffff";
          e.target.style.transform = "scale(1.1)";
        }}
        onMouseLeave={(e) => {
          e.target.style.background =
            "linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%)";
          e.target.style.color = "#1e40af";
          e.target.style.transform = "scale(1)";
        }}
      >
        {isCollapsed ? "→" : "←"}
      </button>

      {/* Navigation Items */}
      <div
        style={{
          flex: 1,
          padding: "20px 0",
          overflowY: "auto",
          overflowX: "hidden",
        }}
      >
        {Object.entries(groupedItems).map(([categoryKey, items]) => (
          <div key={categoryKey} style={{ marginBottom: 28 }}>
            {!isCollapsed && (
              <div
                style={{
                  padding: "8px 20px 12px 20px",
                  fontSize: 11,
                  fontWeight: 700,
                  color: "rgba(255, 255, 255, 0.7)",
                  textTransform: "uppercase",
                  letterSpacing: "0.15em",
                  borderBottom: `2px solid ${categories[categoryKey].color}`,
                  marginBottom: 12,
                  background:
                    "linear-gradient(90deg, rgba(255, 255, 255, 0.05) 0%, transparent 100%)",
                }}
              >
                {categories[categoryKey].label}
              </div>
            )}

            {items.map((item) => {
              const isActive = location.pathname === item.path;

              return (
                <Link
                  key={item.path}
                  to={item.path}
                  style={{
                    display: "block",
                    padding: isCollapsed ? "16px 8px" : "14px 20px",
                    margin: isCollapsed ? "8px 8px" : "4px 12px",
                    color: isActive ? "#ffffff" : "rgba(255, 255, 255, 0.85)",
                    textDecoration: "none",
                    background: isActive
                      ? `linear-gradient(135deg, ${
                          categories[item.category].color
                        } 0%, rgba(255, 255, 255, 0.2) 100%)`
                      : "transparent",
                    borderRadius: isCollapsed ? "12px" : "10px",
                    border: isActive
                      ? "1px solid rgba(255, 255, 255, 0.2)"
                      : "1px solid transparent",
                    transition: "all 0.3s ease-in-out",
                    position: "relative",
                    overflow: "hidden",
                    backdropFilter: isActive ? "blur(10px)" : "none",
                  }}
                  onMouseEnter={(e) => {
                    if (!isActive) {
                      e.target.style.background = "rgba(255, 255, 255, 0.08)";
                      e.target.style.color = "#ffffff";
                      e.target.style.transform = "translateX(4px)";
                      e.target.style.border =
                        "1px solid rgba(255, 255, 255, 0.1)";
                    }
                  }}
                  onMouseLeave={(e) => {
                    if (!isActive) {
                      e.target.style.background = "transparent";
                      e.target.style.color = "rgba(255, 255, 255, 0.85)";
                      e.target.style.transform = "translateX(0)";
                      e.target.style.border = "1px solid transparent";
                    }
                  }}
                  title={
                    isCollapsed
                      ? `${item.label} - ${item.description}`
                      : undefined
                  }
                >
                  <div
                    style={{
                      display: "flex",
                      alignItems: "center",
                      gap: isCollapsed ? 0 : 14,
                      justifyContent: isCollapsed ? "center" : "flex-start",
                    }}
                  >
                    <span
                      style={{
                        fontSize: 20,
                        width: 24,
                        textAlign: "center",
                        filter: isActive
                          ? "drop-shadow(0 0 8px rgba(255,255,255,0.6))"
                          : "none",
                        transform: isActive ? "scale(1.1)" : "scale(1)",
                        transition: "all 0.3s ease-in-out",
                      }}
                    >
                      {item.icon}
                    </span>
                    {!isCollapsed && (
                      <div style={{ flex: 1 }}>
                        <div
                          style={{
                            fontSize: 14,
                            fontWeight: isActive ? 700 : 500,
                            marginBottom: 2,
                            textShadow: isActive
                              ? "0 1px 2px rgba(0, 0, 0, 0.1)"
                              : "none",
                          }}
                        >
                          {item.label}
                        </div>
                        <div
                          style={{
                            fontSize: 11,
                            opacity: isActive ? 0.9 : 0.7,
                            fontWeight: 400,
                          }}
                        >
                          {item.description}
                        </div>
                      </div>
                    )}
                  </div>

                  {/* Active indicator line */}
                  {isActive && !isCollapsed && (
                    <div
                      style={{
                        position: "absolute",
                        top: 0,
                        right: 0,
                        width: "4px",
                        height: "100%",
                        background: `linear-gradient(180deg, ${
                          categories[item.category].color
                        } 0%, rgba(255, 255, 255, 0.8) 100%)`,
                        borderRadius: "2px 0 0 2px",
                      }}
                    />
                  )}
                </Link>
              );
            })}
          </div>
        ))}
      </div>

      {/* Footer */}
      <div
        style={{
          padding: isCollapsed ? "16px 8px" : "20px 20px",
          borderTop: "1px solid rgba(255, 255, 255, 0.15)",
          background:
            "linear-gradient(135deg, rgba(0, 0, 0, 0.2) 0%, rgba(0, 0, 0, 0.1) 100%)",
          backdropFilter: "blur(10px)",
          textAlign: isCollapsed ? "center" : "left",
        }}
      >
        {!isCollapsed ? (
          <>
            <div
              style={{
                fontSize: 11,
                color: "rgba(255, 255, 255, 0.7)",
                marginBottom: 4,
                fontWeight: 500,
              }}
            >
              © 2024 MedPlat Analytics
            </div>
            <div
              style={{
                fontSize: 10,
                color: "rgba(255, 255, 255, 0.5)",
                fontWeight: 400,
              }}
            >
              Version 1.0.0 • Professional Edition
            </div>
          </>
        ) : (
          <div
            style={{
              fontSize: 9,
              color: "rgba(255, 255, 255, 0.6)",
              transform: "rotate(-90deg)",
              fontWeight: 500,
            }}
          >
            v1.0
          </div>
        )}
      </div>
    </nav>
  );
}

function App() {
  return (
    <Router>
      <div
        style={{
          display: "flex",
          height: "100vh",
          overflow: "hidden",
          background: "linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%)",
        }}
      >
        <NavigationSidebar />
        <div
          style={{
            flex: 1,
            overflow: "auto",
            background: "linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%)",
            position: "relative",
          }}
        >
          {/* Page Content */}
          <Routes>
            <Route path="/upload" element={<UploadPage />} />
            <Route path="/visualize" element={<VisualizePage />} />
            <Route path="/sql" element={<SqlQueryPage />} />
            <Route path="/indicator-master" element={<IndicatorMasterPage />} />
            <Route
              path="/derived-attribute"
              element={<DerivedAttributePage />}
            />
            <Route path="/dataset-builder" element={<DatasetBuilderPage />} />
            <Route
              path="/dataset-master-list"
              element={<DatasetMasterListPage />}
            />
            <Route path="/dataset-graph/:id" element={<DatasetGraphPage />} />
            <Route path="*" element={<Navigate to="/visualize" replace />} />
          </Routes>

          {/* Enhanced Background Pattern */}
          <div
            style={{
              position: "fixed",
              top: 0,
              left: 0,
              right: 0,
              bottom: 0,
              background: `
                radial-gradient(circle at 20% 80%, rgba(30, 64, 175, 0.03) 0%, transparent 50%),
                radial-gradient(circle at 80% 20%, rgba(29, 78, 216, 0.04) 0%, transparent 50%),
                radial-gradient(circle at 40% 40%, rgba(30, 64, 175, 0.02) 0%, transparent 50%),
                linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(248, 250, 252, 0.9) 100%)
              `,
              pointerEvents: "none",
              zIndex: -1,
            }}
          />

          {/* Subtle texture overlay */}
          <div
            style={{
              position: "fixed",
              top: 0,
              left: 0,
              right: 0,
              bottom: 0,
              background: `url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23f1f5f9' fill-opacity='0.3'%3E%3Ccircle cx='7' cy='7' r='1'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E")`,
              pointerEvents: "none",
              zIndex: -1,
              opacity: 0.5,
            }}
          />
        </div>
      </div>
    </Router>
  );
}

export default App;
