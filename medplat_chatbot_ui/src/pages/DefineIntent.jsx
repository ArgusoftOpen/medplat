import { useState, useEffect } from "react";
import axios from "axios";

export default function DefineIntent() {
  const [intentName, setIntentName] = useState("");
  const [examples, setExamples] = useState([""]);
  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [fileInput, setFileInput] = useState(null);
  const [yamlFile, setYamlFile] = useState(null);
  // ⚠️ Add missing states
const [intents, setIntents] = useState([]);           
const [selectedIntent, setSelectedIntent] = useState(null);  
const [newExample, setNewExample] = useState("");   // ✅ added


  const handleExampleChange = (index, value) => {
    const newExamples = [...examples];
    newExamples[index] = value;
    setExamples(newExamples);
  };

  const addExampleField = () => {
    setExamples([...examples, ""]);
  };

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
  };

  // ⚠️ Add missing fetchIntents function
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setSuccessMsg("");
    setErrorMsg("");

    if (fileInput) {
      const formData = new FormData();
      formData.append("file", fileInput);
      formData.append("type", "txt");

      try {
        const res = await axios.post("http://localhost:5000/upload_file", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        if (res.status === 200) {
          setSuccessMsg("✅ Intent(s) from .txt uploaded successfully!");
          setFileInput(null);
        }
      } catch (err) {
        setErrorMsg("❌ Failed to upload .txt intent file.");
      } finally {
        setLoading(false);
      }
      return;
    }

    if (yamlFile) {
      const formData = new FormData();
      formData.append("file", yamlFile);

      try {
        const res = await axios.post("http://localhost:5000/upload_yaml", formData, {
          headers: { "Content-Type": "multipart/form-data" }
        });
        if (res.status === 200) {
          setSuccessMsg("✅ YAML intent uploaded successfully!");
          setYamlFile(null);
        }
      } catch (err) {
        setErrorMsg("❌ Failed to upload YAML file.");
      } finally {
        setLoading(false);
      }
      return;
    }

    try {
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
        fetchIntents(); // refresh intent list
      }
    } catch (err) {
      setErrorMsg("❌ Failed to submit intent. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateExamples = async () => {
    if (!selectedIntent) {
      setErrorMsg("⚠️ Please select an intent first.");
      return;
    }
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

  return (
    <div className="form-container" style={{ maxWidth: "600px", margin: "auto", padding: "2rem", border: "1px solid #ccc", borderRadius: "8px" }}>
      <h2 style={{ textAlign: "center", color: "green" }}>✨ Define New Intent</h2>

      <form onSubmit={handleSubmit}>
        <label>Upload .txt File</label>
        <input type="file" accept=".txt" onChange={handleTxtUpload} />

        <br /><br />
        <label>OR Upload .yml/.yaml File</label>
        <input type="file" accept=".yml,.yaml" onChange={handleYamlUpload} />

        <br /><br />
        {!yamlFile && !fileInput && (
          <>
            <label>Intent Name</label>
            <input
              type="text"
              placeholder="e.g., report_problem"
              value={intentName}
              onChange={(e) => setIntentName(e.target.value)}
              required
            />

            <label>User Examples</label>
            {examples.map((ex, idx) => (
              <input
                key={idx}
                type="text"
                placeholder={`Example ${idx + 1}`}
                value={ex}
                onChange={(e) => handleExampleChange(idx, e.target.value)}
                required
              />
            ))}
            <button type="button" onClick={addExampleField}>
              + Add Another Example
            </button>

            <label>Bot Response</label>
            <textarea
              placeholder="e.g., I'm sorry to hear that. Can you describe the issue?"
              rows="3"
              value={response}
              onChange={(e) => setResponse(e.target.value)}
              required
            />
          </>
        )}

        <div style={{ marginTop: "2rem", borderTop: "1px solid #ccc", paddingTop: "1rem" }}>
  <h3>✏️ Update or Add Examples for Existing Intent</h3>

  {/* Intent Selector */}
  <select
    value={selectedIntent ? selectedIntent.intent : ""}
    onChange={(e) => {
      const intentObj = intents.find((i) => i.intent === e.target.value);
      setSelectedIntent(intentObj || null);
    }}
  >
    <option value="">-- Select Intent --</option>
    {intents.map((i, idx) => (
      <option key={idx} value={i.intent}>
        {i.intent}
      </option>
    ))}
  </select>

  {/* Update Examples Section */}
  {selectedIntent &&
    selectedIntent.examples.map((ex, idx) => (
      <input
        key={idx}
        type="text"
        value={ex}
        style={{ display: "block", margin: "0.5rem 0" }}
        onChange={(e) => {
          const newExamples = [...selectedIntent.examples];
          newExamples[idx] = e.target.value;
          setSelectedIntent({ ...selectedIntent, examples: newExamples });
        }}
      />
    ))}

  {/* Add Example Section */}
  {selectedIntent && (
    <div style={{ marginTop: "1rem" }}>
      <input
        type="text"
        placeholder="➕ Add new example"
        value={newExample}
        onChange={(e) => setNewExample(e.target.value)}
        style={{ marginRight: "0.5rem" }}
      />
      <button
        type="button"
        onClick={() => {
          if (newExample.trim() !== "") {
            const updatedExamples = [...selectedIntent.examples, newExample.trim()];
            setSelectedIntent({ ...selectedIntent, examples: updatedExamples });
            setNewExample(""); // clear input
          }
        }}
      >
        Add
      </button>
    </div>
  )}

  {/* Update Button */}
  {selectedIntent && (
    <button
      type="button"
      onClick={handleUpdateExamples}
      disabled={loading}
      style={{ marginTop: "1rem", display: "block" }}
    >
      {loading ? "Updating..." : "Update Examples"}
    </button>
  )}
</div>


        <br />
        <button type="submit" disabled={loading}>
          {loading ? "Submitting..." : "Submit"}
        </button>

        {successMsg && <p style={{ color: "green" }}>{successMsg}</p>}
        {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
      </form>
    </div>
  );
}
