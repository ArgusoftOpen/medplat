// Gemini API helper for generative Q&A and chart suggestion with auto-chart creation for Healthcare Domain

export async function askGemini({ apiKey, question, columns, sql, data }) {
    const url = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}`;

    // Enhanced prompt for healthcare domain with structured responses
    let prompt = `You are a professional healthcare data analyst assistant specializing in medical informatics, public health analytics, and healthcare business intelligence.

DOMAIN CONTEXT:
- You are analyzing HEALTHCARE DATA from a medical information system
- This data may include patient records, clinical indicators, treatment outcomes, hospital operations, or public health metrics
- Your analysis should consider healthcare-specific patterns, seasonal trends, and medical significance
- Be mindful of healthcare privacy and present insights in aggregate form only
- Consider clinical relevance and public health implications in your analysis

DATASET INFORMATION:
`;

    if (sql) {
        prompt += `SQL Query: ${sql}\n`;
    }

    if (columns && columns.length > 0) {
        prompt += `Available Healthcare Data Columns: ${columns.join(", ")}\n`;
    }

    if (data && data.length > 0) {
        const dataCount = data.length;
        const sampleData = data.slice(0, 3);
        prompt += `Healthcare Dataset Size: ${dataCount} records\n`;
        prompt += `Sample Healthcare Data (first 3 records): ${JSON.stringify(
            sampleData,
            null,
            2
        )}\n`;

        if (data.length > 0) {
            const firstRow = data[0];
            const columnTypes = Object.keys(firstRow).map((col) => {
                const sampleValue = firstRow[col];
                const type =
                    typeof sampleValue === "number"
                        ? "numeric"
                        : typeof sampleValue === "string"
                        ? "categorical/text"
                        : "unknown";
                return `${col} (${type})`;
            });
            prompt += `Healthcare Data Column Types: ${columnTypes.join(", ")}\n`;
        }
    }

    if (question) {
        prompt += `\nHEALTHCARE ANALYSIS QUESTION: ${question}\n`;
    }

    prompt += `\nHEALTHCARE DATA ANALYSIS INSTRUCTIONS:

1. Analyze the healthcare data with medical and public health perspective
2. Provide insights relevant to healthcare outcomes, patient care, or operational efficiency
3. Consider clinical significance and statistical relevance in healthcare context
4. Look for patterns that might indicate:
   - Patient care trends
   - Treatment effectiveness
   - Seasonal health patterns
   - Resource utilization
   - Quality indicators
   - Population health metrics

5. ALWAYS end your response with a chart recommendation in this EXACT format:
[CHART_RECOMMENDATION: chart_type]

Where chart_type is one of: bar, line, pie

6. Choose chart types based on healthcare visualization best practices:
   - Bar Chart (bar): Best for comparing clinical categories, patient demographics, treatment types, facility comparisons
   - Line Chart (line): Best for showing health trends over time, patient monitoring data, epidemic curves, seasonal patterns
   - Pie Chart (pie): Best for showing healthcare resource distribution, patient demographics (max 6-8 categories)

7. Provide specific healthcare insights about data patterns, clinical trends, or operational metrics
8. If asked for healthcare statistics, calculate them from the provided data
9. Base your medical analysis ONLY on the data provided - do not make clinical assumptions
10. Be precise and professional, suitable for healthcare stakeholders
11. If the question cannot be answered from available healthcare data, clearly state this

HEALTHCARE RESPONSE FORMAT:
- Start with a direct answer to the healthcare question
- Provide supporting clinical/operational insights with specific numbers
- Explain the medical or public health significance of findings
- Recommend appropriate visualization for healthcare stakeholders
- ALWAYS end with: [CHART_RECOMMENDATION: chart_type]

Example healthcare response:
"Based on the patient admission data analysis, there are 5 different admission types with varying frequencies. Emergency admissions have the highest count at 145 cases, followed by scheduled surgeries at 89 cases. This indicates high emergency department utilization. The peak admission months show seasonal patterns typical in healthcare settings. A bar chart would be most effective for comparing these admission categories for healthcare administrators and clinical staff. [CHART_RECOMMENDATION: bar]"

Please provide your healthcare data analysis:`;

    const body = {
        contents: [
            {
                parts: [{ text: prompt }],
            },
        ],
        generationConfig: {
            temperature: 0.2, // Lower temperature for more consistent clinical responses
            maxOutputTokens: 800, // Increased for detailed healthcare analysis
            topP: 0.8,
            topK: 40,
        },
    };

    try {
        const res = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
        });

        if (!res.ok) throw new Error("Gemini API error: " + res.statusText);

        const json = await res.json();
        const fullResponse =
            json.candidates?.[0]?.content?.parts?.[0]?.text || "No healthcare analysis available.";

        // Parse the response to extract chart recommendation
        const chartRecommendationMatch = fullResponse.match(
            /\[CHART_RECOMMENDATION:\s*(bar|line|pie)\]/i
        );

        const recommendedChart = chartRecommendationMatch
            ? chartRecommendationMatch[1].toLowerCase()
            : null;

        // Clean the response text by removing the chart recommendation tag
        const cleanResponse = fullResponse
            .replace(/\[CHART_RECOMMENDATION:\s*(bar|line|pie)\]/i, "")
            .trim();

        return {
            text: cleanResponse,
            recommendedChart: recommendedChart,
            hasRecommendation: !!recommendedChart,
        };
    } catch (error) {
        console.error("Healthcare Data Analysis - Gemini API Error:", error);
        return {
            text: "Error getting healthcare analysis from Gemini: " + error.message,
            recommendedChart: null,
            hasRecommendation: false,
        };
    }
}

// Helper function to get automatic chart recommendation for healthcare data
export function getAutomaticChartRecommendation(data, columns) {
    if (!data || data.length === 0) return "bar";

    const firstRow = data[0];
    const columnKeys = Object.keys(firstRow);

    // Check if data has healthcare time-based columns (common in medical data)
    const timeColumns = columnKeys.filter(
        (col) =>
            col.toLowerCase().includes("date") ||
            col.toLowerCase().includes("time") ||
            col.toLowerCase().includes("year") ||
            col.toLowerCase().includes("month") ||
            col.toLowerCase().includes("day") ||
            col.toLowerCase().includes("admission") ||
            col.toLowerCase().includes("discharge") ||
            col.toLowerCase().includes("treatment") ||
            col.toLowerCase().includes("visit")
    );

    if (timeColumns.length > 0) {
        return "line"; // Healthcare time series data (patient monitoring, trends)
    }

    // Check if suitable for pie chart (healthcare categories like patient demographics)
    if (columnKeys.length === 2 && data.length <= 8) {
        const firstCol = columnKeys[0];
        const uniqueValues = [...new Set(data.map((row) => row[firstCol]))];
        if (uniqueValues.length <= 6) {
            return "pie"; // Healthcare categories (gender, age groups, departments)
        }
    }

    // Default to bar chart for healthcare categorical comparisons (treatments, facilities, outcomes)
    return "bar";
}
