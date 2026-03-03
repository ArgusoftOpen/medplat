Chatbot UI additions

This README documents the new AI Chat frontend and integration points added under `app/chat/`.

Quick summary
- Chat UI: text + voice + attachments
- Ticketing: localStorage fallback + optional `TICKET_API_ENDPOINT` for server
- Uploads: local metadata store + optional `UPLOAD_API_ENDPOINT` and `UPLOAD_API_TOKEN`
- AI: `CHAT_API_ENDPOINT` and `CHAT_API_TOKEN` for production AI
- Realtime: `TICKET_WS_ENDPOINT` for WebSocket updates; polling fallback every 5s

How to enable production integrations
1. AI backend: set global JS variables before app bootstrap (e.g., in `index.html`):

```html
<script>
  window.CHAT_API_ENDPOINT = 'https://api.example.com/chat';
  window.CHAT_API_TOKEN = 'your-token-if-needed';
  window.UPLOAD_API_ENDPOINT = 'https://api.example.com/upload';
  window.UPLOAD_API_TOKEN = 'upload-token-if-needed';
  window.TICKET_API_ENDPOINT = 'https://api.example.com/tickets';
  window.TICKET_WS_ENDPOINT = 'wss://api.example.com/tickets/ws';
</script>
```

2. Server contract expectations
- `POST CHAT_API_ENDPOINT` with `{text, lang}` returns `{reply: '...'}` or a string payload.
- `POST UPLOAD_API_ENDPOINT` accepts multipart form data `file` and returns JSON metadata `{id, name, url, size}`.
- `GET TICKET_API_ENDPOINT` returns an array of tickets. `POST` creates, `PATCH /:id` updates.
- WebSocket messages from `TICKET_WS_ENDPOINT` should send JSON ticket objects on updates.

Local development fallback
- Without endpoints the services use localStorage mocks so features are testable locally.

Notes
- I added WebSocket fallback and polling in `controllers/tickets.controller.js`.
- Uploads and chat service now support optional auth tokens via `window.CHAT_API_TOKEN` and `window.UPLOAD_API_TOKEN`.

Next steps to reach 100%
- Provide production API endpoints and tokens, or implement server endpoints in `medplat-web` backend. Run build/tests and add CI pipeline.
