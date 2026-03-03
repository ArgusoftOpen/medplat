export function generateTicketId() {
  const timestamp = Date.now().toString(36);
  const random = Math.floor(Math.random() * 9000 + 1000).toString();
  return `T-${timestamp}-${random}`;
}