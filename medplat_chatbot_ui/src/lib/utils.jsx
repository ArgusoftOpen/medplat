// src/lib/utils.js
export function cn(...classes) {
  return classes.filter(Boolean).join(" ");
}
