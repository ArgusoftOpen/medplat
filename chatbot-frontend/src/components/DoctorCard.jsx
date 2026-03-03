import React from 'react'
import { motion } from 'framer-motion'

export default function DoctorCard({ doctor }){
  return (
    <motion.div className="bg-white rounded-lg p-5 shadow-sm hover:shadow-lg cursor-pointer"
      whileHover={{ scale: 1.03 }} initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4 }}
    >
      <div className="flex items-center gap-4">
        <img src={doctor.img} alt={doctor.name} className="w-16 h-16 rounded-full object-cover" />
        <div>
          <div className="font-semibold text-gray-800">{doctor.name}</div>
          <div className="text-sm text-gray-500">{doctor.spec}</div>
        </div>
      </div>

      <div className="mt-4 flex items-center justify-between">
        <div className="text-sm text-gray-600">Available today</div>
        <button className="bg-sky-600 text-white px-3 py-1 rounded-md text-sm">Book</button>
      </div>
    </motion.div>
  )
}
