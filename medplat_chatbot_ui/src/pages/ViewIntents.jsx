import { useState } from "react";
import axios from "axios";

export default function ViewIntents({ intents = [], fetchIntents }) {
  const [msg, setMsg] = useState("");
  const [confirmData, setConfirmData] = useState(null);
  const [loading, setLoading] = useState(false);

  const deleteIntent = async (intent) => {
    setLoading(true);
    try {
      const res = await axios.delete(
        `http://127.0.0.1:5000/intent_with_examples/${intent}`
      );
      setMsg(res.data.message);
      fetchIntents();
    } catch (error) {
      setMsg("❌ Error deleting intent.");
    }
    setLoading(false);
    setConfirmData(null);
  };

  const deleteExample = async (intent, example) => {
    setLoading(true);
    try {
      const res = await axios.delete(
        `http://127.0.0.1:5000/intent/${intent}/example`,
        { data: { example } }
      );
      setMsg(res.data.message);
      fetchIntents();
    } catch (error) {
      setMsg("❌ Error deleting example.");
    }
    setLoading(false);
    setConfirmData(null);
  };

  return (
    <div className="max-w-3xl mx-auto mt-10 p-6 bg-white rounded-2xl shadow-xl relative">
      <h2 className="text-2xl font-bold mb-4 text-center">Existing Intents</h2>

      <ul className="space-y-4">
        {intents.length === 0 ? (
          <p className="text-gray-500 text-center">No intents found.</p>
        ) : (
          intents.map((intentObj, idx) => {
            const intentName = intentObj.intent || intentObj.name;

            return (
              <li key={idx} className="border rounded-lg p-4 shadow-sm bg-gray-50">
                {/* Intent Name + Delete Button */}
                <div className="flex justify-between items-center mb-3">
                  <span className="font-mono font-semibold text-lg">
                    {intentName}
                  </span>
                  <button
                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                    onClick={() =>
                      setConfirmData({ type: "intent", intent: intentName })
                    }
                  >
                    Delete ✖
                  </button>
                </div>

                {/* Intent Examples */}
                {intentObj.examples && intentObj.examples.length > 0 && (
                  <ul className="pl-5 list-disc text-sm text-gray-700 space-y-2">
                    {intentObj.examples.map((ex, i) => (
                      <li key={i} className="flex justify-between items-center">
                        <span>{ex}</span>
                        <button
                          className="ml-3 text-red-500 hover:text-red-700 text-sm"
                          onClick={() =>
                            setConfirmData({
                              type: "example",
                              intent: intentName,
                              example: ex,
                            })
                          }
                        >
                          Delete ✖
                        </button>
                      </li>
                    ))}
                  </ul>
                )}
              </li>
            );
          })
        )}
      </ul>

      {/* ✅ Confirmation Popup */}
      {confirmData && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
          <div className="bg-white rounded-xl shadow-xl p-6 flex items-start space-x-4 w-96 animate-fade-in">
            {/* Avatar */}
            <div className="w-12 h-12 rounded-full bg-blue-500 flex items-center justify-center text-white text-xl">
              👩
            </div>
            {/* Message + Buttons */}
            <div className="flex-1">
              <p className="font-semibold mb-3">
                Are you sure you want to delete{" "}
                <span className="text-red-600">
                  {confirmData.type === "intent"
                    ? `intent "${confirmData.intent}"`
                    : `example "${confirmData.example}"`}
                </span>
                ?
              </p>
              <div className="flex justify-end space-x-3">
                <button
                  className="px-4 py-1 rounded bg-gray-200 hover:bg-gray-300"
                  onClick={() => setConfirmData(null)}
                  disabled={loading}
                >
                  Cancel
                </button>
                <button
                  className="px-4 py-1 rounded bg-red-500 text-white hover:bg-red-600 disabled:opacity-50"
                  onClick={() =>
                    confirmData.type === "intent"
                      ? deleteIntent(confirmData.intent)
                      : deleteExample(confirmData.intent, confirmData.example)
                  }
                  disabled={loading}
                >
                  {loading ? "Deleting..." : "Confirm"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* ✅ Success / Error Message */}
      {msg && (
        <p className="text-center mt-4 font-semibold text-green-600">
          {msg}
        </p>
      )}
    </div>
  );
}
