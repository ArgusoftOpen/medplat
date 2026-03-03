# AI ANM Assistant Frontend

This repository contains the frontend for the AI-Powered Chatbot System built with React and Tailwind CSS. It is designed as a standalone project with mock logic for guided issue reporting and ticket management. The UI is responsive and suitable for field workers and administrators.

## Features

- **Chat interface** with language toggle (EN/HI/TE), typing indicator, and guided ticket creation.
- **Sidebar** for navigation: New Chat, Ticket Dashboard, Clear Chat.
- **Ticket Dashboard** with summary cards, searchable/filterable ticket table.
- **Mock ticket state** stored in React context; easily replaceable with backend integration.
- **Responsive layout** and professional styling using Tailwind CSS.

## Folder Structure

```
src/
  components/       # Reusable UI components
  pages/            # Route-level pages (ChatPage, DashboardPage)
  data/             # (empty for now) place mock data here
  utils/            # Utility functions (ticket ID generator, etc.)
  index.jsx         # App entry point
  App.jsx           # Main application with routing and context
```

## Getting Started

1. **Install** dependencies:
   ```bash
   npm install
   ```

2. **Run development server**:
   ```bash
   npm run dev
   ```
   Open [http://localhost:3000](http://localhost:3000) in your browser.

3. **Build for production**:
   ```bash
   npm run build
   ```

## Next Steps

- Integrate with backend APIs (auth, ticket CRUD, chat service).
- Expand language support or integrate an i18n solution.
- Add unit tests and end-to-end testing.
- Enhance UX animations and error handling.

---

This project was bootstrapped manually according to frontend-only requirements. It is ready for further development and backend integration.