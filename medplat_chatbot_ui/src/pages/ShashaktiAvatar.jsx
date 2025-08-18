// ShashaktiAvatar.jsx
export default function ShashaktiAvatar({
  size = 48,
  gradientFrom = "#6D28D9", // indigo-700
  gradientTo = "#EC4899",   // pink-500
  vest = "#16A34A",         // green-600
  helmet = "#FBBF24",       // amber-400
  skin = "#FCD7B8",
}) {
  const id = Math.random().toString(36).slice(2); // unique gradient id per instance
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 128 128"
      role="img"
      aria-label="Shashakti – field support avatar"
    >
      <defs>
        <linearGradient id={`bg-${id}`} x1="0" y1="0" x2="1" y2="1">
          <stop offset="0" stopColor={gradientFrom}/>
          <stop offset="1" stopColor={gradientTo}/>
        </linearGradient>
      </defs>

      <circle cx="64" cy="64" r="60" fill={`url(#bg-${id})`} />

      <path d="M82 52a18 18 0 10-36 0v4h36v-4z" fill={helmet}/>
      <rect x="45" y="53" width="38" height="4" fill="#D97706" rx="1"/>
      <circle cx="64" cy="64" r="14" fill={skin}/>
      <path d="M57 68c2.5 3 7.5 3 10 0" stroke="#7C2D12" strokeWidth="2" fill="none" strokeLinecap="round"/>

      <path d="M40 98c2-14 10-22 24-22s22 8 24 22v10H40V98z" fill={vest}/>
      <path d="M64 76v32" stroke="#065F46" strokeWidth="3" opacity="0.5"/>
      <path d="M52 88h24" stroke="#065F46" strokeWidth="3" opacity="0.5"/>

      <rect x="82" y="66" width="18" height="24" rx="3" fill="#E5E7EB" stroke="#374151" strokeWidth="2"/>
      <rect x="86" y="62" width="10" height="6" rx="2" fill="#9CA3AF" stroke="#374151" strokeWidth="2"/>
      <path d="M86 74h10M86 80h10M86 86h7" stroke="#374151" strokeWidth="2" strokeLinecap="round"/>

      <path d="M34 78l8 8 14-16" fill="none" stroke="#FFFFFF" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round" opacity="0.9"/>
    </svg>
  );
}
