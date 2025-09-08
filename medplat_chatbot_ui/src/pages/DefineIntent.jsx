import { useState, useEffect } from "react";
import axios from "axios";

export default function DefineIntent() {
  // ------------------- State -------------------
  const [intentName, setIntentName] = useState("");
  const [examples, setExamples] = useState([""]);
  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [fileInput, setFileInput] = useState(null);
  const [yamlFile, setYamlFile] = useState(null);
  const [intents, setIntents] = useState([]);
  const [selectedIntent, setSelectedIntent] = useState(null);
  const [newExample, setNewExample] = useState("");

  // ------------------- Fetch existing intents -------------------
  const fetchIntents = async () => {
    try {
      const res = await axios.get("http://localhost:5000/get_intents");
      setIntents(res.data.intents || []);
    } catch (err) {
      console.error("Error fetching intents:", err);
    }
  };

  useEffect(() => {
    fetchIntents();
  }, []);

  // ------------------- Handle user examples -------------------
  const handleExampleChange = (index, value) => {
    const updated = [...examples];
    updated[index] = value;
    setExamples(updated);
  };

  const addExampleField = () => setExamples([...examples, ""]);

  // ------------------- Handle file uploads -------------------
  const handleTxtUpload = (e) => {
    const file = e.target.files[0];
    if (!file || !file.name.endsWith(".txt")) {
      setErrorMsg("Please upload a valid .txt file");
      return;
    }
    setFileInput(file);
    setYamlFile(null);
    setErrorMsg("");
  };

  const handleYamlUpload = (e) => {
    const file = e.target.files[0];
    if (!file || !(file.name.endsWith(".yaml") || file.name.endsWith(".yml"))) {
      setErrorMsg("Please upload a valid YAML file");
      return;
    }
    setYamlFile(file);
    setFileInput(null);
    setErrorMsg("");
  };

  // ------------------- Submit Intent -------------------
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setSuccessMsg("");
    setErrorMsg("");

    try {
      if (fileInput) {
        const formData = new FormData();
        formData.append("file", fileInput);
        formData.append("type", "txt");
        const res = await axios.post("http://localhost:5000/upload_file", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        if (res.status === 200) setSuccessMsg("✅ Intent(s) from .txt uploaded successfully!");
        setFileInput(null);
      } else if (yamlFile) {
        const formData = new FormData();
        formData.append("file", yamlFile);
        const res = await axios.post("http://localhost:5000/upload_yaml", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        if (res.status === 200) setSuccessMsg("✅ YAML intent uploaded successfully!");
        setYamlFile(null);
      } else {
        const res = await axios.post("http://localhost:5000/add_intent", {
          intent: intentName,
          examples,
          response,
        });
        if (res.status === 200) {
          setSuccessMsg("✅ Intent defined successfully!");
          setIntentName("");
          setExamples([""]);
          setResponse("");
          fetchIntents();
        }
      }
    } catch (err) {
      setErrorMsg("❌ Failed to submit intent. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  // ------------------- Update Examples for Existing Intent -------------------
  const handleUpdateExamples = async () => {
    if (!selectedIntent) return setErrorMsg("⚠️ Please select an intent first.");

    setLoading(true);
    setSuccessMsg("");
    setErrorMsg("");

    try {
      await axios.post("http://localhost:5000/update_intent_examples", {
        intent: selectedIntent.intent,
        examples: selectedIntent.examples.filter((ex) => ex.trim() !== ""),
      });
      setSuccessMsg("✅ Examples updated successfully!");
      fetchIntents();
    } catch (err) {
      setErrorMsg("❌ Failed to update examples.");
    } finally {
      setLoading(false);
    }
  };

  // ------------------- Render -------------------
  return (
    <div className="form-container" style={{ maxWidth: "600px", margin: "auto", padding: "2rem", border: "1px solid #ccc", borderRadius: "8px" }}>
      <h2 style={{ textAlign: "center", color: "green" }}>✨ Define New Intent</h2>

      <form onSubmit={handleSubmit}>
        {/* File Upload Section */}
        <label>Upload .txt File</label>
        <input type="file" accept=".txt" onChange={handleTxtUpload} />

        <br /><br />
        <label>OR Upload .yml/.yaml File</label>
        <input type="file" accept=".yml,.yaml" onChange={handleYamlUpload} />

        {/* Manual Intent Form */}
        {!yamlFile && !fileInput && (
          <>
            <label>Intent Name</label>
            <input type="text" placeholder="e.g., report_problem" value={intentName} onChange={(e) => setIntentName(e.target.value)} required />

            <label>User Examples</label>
            {examples.map((ex, idx) => (
              <input key={idx} type="text" placeholder={`Example ${idx + 1}`} value={ex} onChange={(e) => handleExampleChange(idx, e.target.value)} required />
            ))}
            <button type="button" onClick={addExampleField}>+ Add Another Example</button>

            <label>Bot Response</label>
            <textarea placeholder="e.g., I'm sorry to hear that. Can you describe the issue?" rows="3" value={response} onChange={(e) => setResponse(e.target.value)} required />
          </>
        )}

        {/* Update Existing Intent Examples */}
        <div style={{ marginTop: "2rem", borderTop: "1px solid #ccc", paddingTop: "1rem" }}>
          <h3>✏️ Update or Add Examples for Existing Intent</h3>

          <select value={selectedIntent ? selectedIntent.intent : ""} onChange={(e) => {
            const intentObj = intents.find((i) => i.intent === e.target.value);
            setSelectedIntent(intentObj || null);
          }}>
            <option value="">-- Select Intent --</option>
            {intents.map((i, idx) => <option key={idx} value={i.intent}>{i.intent}</option>)}
          </select>

          {selectedIntent && selectedIntent.examples.map((ex, idx) => (
            <input key={idx} type="text" value={ex} style={{ display: "block", margin: "0.5rem 0" }} onChange={(e) => {
              const updated = [...selectedIntent.examples];
              updated[idx] = e.target.value;
              setSelectedIntent({ ...selectedIntent, examples: updated });
            }} />
          ))}

          {selectedIntent && (
            <div style={{ marginTop: "1rem" }}>
              <input type="text" placeholder="➕ Add new example" value={newExample} onChange={(e) => setNewExample(e.target.value)} style={{ marginRight: "0.5rem" }} />
              <button type="button" onClick={() => {
                if (newExample.trim()) {
                  setSelectedIntent({ ...selectedIntent, examples: [...selectedIntent.examples, newExample.trim()] });
                  setNewExample("");
                }
              }}>Add</button>
            </div>
          )}

          {selectedIntent && (
            <button type="button" onClick={handleUpdateExamples} disabled={loading} style={{ marginTop: "1rem", display: "block" }}>
              {loading ? "Updating..." : "Update Examples"}
            </button>
          )}
        </div>

        <br />
        <button type="submit" disabled={loading}>{loading ? "Submitting..." : "Submit"}</button>

        {successMsg && <p style={{ color: "green" }}>{successMsg}</p>}
        {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
      </form>
    </div>
  );
}
