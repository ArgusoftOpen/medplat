import { useState } from "react";
import axios from "axios";

export default function ViewIntents({ intents = [], fetchIntents }) {
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);

  // Delete an entire intent
  const deleteIntent = async (intentName) => {
    setLoading(true);
    try {
      const res = await axios.delete(
        `http://127.0.0.1:5000/intent_with_examples/${intentName}`
      );
      setMsg(res.data.message);
      fetchIntents(); // refresh list
    } catch (error) {
      console.error(error);
      setMsg("❌ Error deleting intent.");
    } finally {
      setLoading(false);
    }
  };

  // Delete a specific example from an intent
  const deleteExample = async (intentName, example) => {
    setLoading(true);
    try {
      const res = await axios.delete(
        `http://127.0.0.1:5000/intent/${intentName}/example`,
        { data: { example } }
      );
      setMsg(res.data.message);
      fetchIntents(); // refresh list
    } catch (error) {
      console.error(error);
      setMsg("❌ Error deleting example.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto mt-10 p-6 bg-white rounded-2xl shadow-xl">
      <h2 className="text-2xl font-bold mb-4 text-center">Existing Intents</h2>

      {intents.length === 0 ? (
        <p className="text-gray-500 text-center">No intents found.</p>
      ) : (
        <ul className="space-y-4">
          {intents.map((intentObj, idx) => {
            const intentName = intentObj.intent || intentObj.name;

            return (
              <li key={idx} className="border rounded-lg p-4 shadow-sm bg-gray-50">
                {/* Intent header */}
                <div className="flex justify-between items-center mb-3">
                  <span className="font-mono font-semibold text-lg">{intentName}</span>
                  <button
                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                    onClick={() => {
                      if (window.confirm(`Delete intent "${intentName}"?`)) {
                        deleteIntent(intentName);
                      }
                    }}
                    disabled={loading}
                  >
                    Delete ✖
                  </button>
                </div>

                {/* Examples list */}
                {intentObj.examples && intentObj.examples.length > 0 && (
                  <ul className="pl-5 list-disc text-sm text-gray-700 space-y-2">
                    {intentObj.examples.map((ex, i) => (
                      <li key={i} className="flex justify-between items-center">
                        <span>{ex}</span>
                        <button
                          className="ml-3 text-red-500 hover:text-red-700 text-sm"
                          onClick={() => {
                            if (
                              window.confirm(`Delete example "${ex}" from "${intentName}"?`)
                            ) {
                              deleteExample(intentName, ex);
                            }
                          }}
                          disabled={loading}
                        >
                          Delete ✖
                        </button>
                      </li>
                    ))}
                  </ul>
                )}
              </li>
            );
          })}
        </ul>
      )}

      {/* Feedback message */}
      {msg && (
        <p
          className={`text-center mt-4 font-semibold ${
            msg.includes("❌") ? "text-red-600" : "text-green-600"
          }`}
        >
          {msg}
        </p>
      )}
    </div>
  );
}
