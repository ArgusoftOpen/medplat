// Gemini API helper for generative Q&A and chart suggestion with auto-chart creation

export async function askGemini({ apiKey, question, columns, sql, data }) {
  const url = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=${apiKey}`;

  // Enhanced prompt for structured responses
  let prompt = `You are a professional data analyst assistant specializing in business intelligence and data visualization.

CONTEXT:
- You are analyzing a dataset from a SQL query
- Your role is to provide accurate, insightful analysis based ONLY on the provided data
- When recommending visualizations, you can ONLY suggest: Bar Chart, Line Chart, or Pie Chart

DATASET INFORMATION:
`;

  if (sql) {
    prompt += `SQL Query: ${sql}\n`;
  }

  if (columns && columns.length > 0) {
    prompt += `Available Columns: ${columns.join(", ")}\n`;
  }

  if (data && data.length > 0) {
    const dataCount = data.length;
    const sampleData = data.slice(0, 3);
    prompt += `Dataset Size: ${dataCount} rows\n`;
    prompt += `Sample Data (first 3 rows): ${JSON.stringify(
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
            ? "text"
            : "unknown";
        return `${col} (${type})`;
      });
      prompt += `Column Types: ${columnTypes.join(", ")}\n`;
    }
  }

  if (question) {
    prompt += `\nUSER QUESTION: ${question}\n`;
  }

  prompt += `\nINSTRUCTIONS:
1. Analyze the data thoroughly and provide accurate insights
2. ALWAYS end your response with a chart recommendation in this EXACT format:
   [CHART_RECOMMENDATION: chart_type]
   Where chart_type is one of: bar, line, pie
3. Choose chart types based on these rules:
   - Bar Chart (bar): Best for comparing categories, showing frequency distributions, discrete data
   - Line Chart (line): Best for showing trends over time, continuous data, sequential data
   - Pie Chart (pie): Best for showing proportions/percentages of a whole (max 6-8 categories)
4. Provide specific insights about the data patterns, trends, or anomalies
5. If asked for statistics, calculate them from the provided data
6. Base your answers ONLY on the data provided - do not make assumptions
7. Be concise but informative in your responses
8. If the question cannot be answered from the available data, clearly state this

RESPONSE FORMAT:
- Start with a direct answer to the question
- Provide supporting data insights with specific numbers when possible
- Explain why the recommended chart type is most appropriate
- ALWAYS end with: [CHART_RECOMMENDATION: chart_type]

Example response:
"Based on the data analysis, there are 5 distinct categories with varying frequencies. Category A has the highest count at 45, followed by Category B at 32. This shows a clear distribution pattern across categories. A bar chart would be most effective for comparing these categorical frequencies. [CHART_RECOMMENDATION: bar]"

Please provide your analysis:`;

  const body = {
    contents: [
      {
        parts: [{ text: prompt }],
      },
    ],
    generationConfig: {
      temperature: 0.2, // Lower temperature for more consistent responses
      maxOutputTokens: 600,
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
      json.candidates?.[0]?.content?.parts?.[0]?.text || "No answer.";

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
    console.error("Gemini API Error:", error);
    return {
      text: "Error getting answer from Gemini: " + error.message,
      recommendedChart: null,
      hasRecommendation: false,
    };
  }
}

// Helper function to get chart recommendation based on data analysis
export function getAutomaticChartRecommendation(data, columns) {
  if (!data || data.length === 0) return "bar";

  const firstRow = data[0];
  const columnKeys = Object.keys(firstRow);

  // Check if data has time-based columns
  const timeColumns = columnKeys.filter(
    (col) =>
      col.toLowerCase().includes("date") ||
      col.toLowerCase().includes("time") ||
      col.toLowerCase().includes("year") ||
      col.toLowerCase().includes("month") ||
      col.toLowerCase().includes("day")
  );

  if (timeColumns.length > 0) {
    return "line"; // Time series data works best with line charts
  }

  // Check if suitable for pie chart (limited categories)
  if (columnKeys.length === 2 && data.length <= 8) {
    const firstCol = columnKeys[0];
    const uniqueValues = [...new Set(data.map((row) => row[firstCol]))];
    if (uniqueValues.length <= 6) {
      return "pie"; // Small number of categories work well with pie charts
    }
  }

  // Default to bar chart for categorical comparisons
  return "bar";
}

// If you have any axios/fetch calls to localhost:5000, update to localhost:8181
