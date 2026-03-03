import React, { useState } from 'react'
import { motion } from 'framer-motion'

export default function AppointmentForm(){
  const [form, setForm] = useState({ name: '', age: '', email: '', datetime: '' })
  const [errors, setErrors] = useState({})
  const [submitted, setSubmitted] = useState(false)

  function validate(){
    const e = {}
    if(!form.name.trim()) e.name = 'Name is required'
    if(!form.age || isNaN(form.age) || Number(form.age) <= 0) e.age = 'Valid age required'
    if(!form.email.match(/^[^@\s]+@[^@\s]+\.[^@\s]+$/)) e.email = 'Valid email required'
    if(!form.datetime) e.datetime = 'Choose date and time'
    return e
  }

  function handleChange(e){
    const { name, value } = e.target
    setForm(f=> ({...f, [name]: value}))
  }

  function handleSubmit(ev){
    ev.preventDefault()
    const e = validate()
    setErrors(e)
    if(Object.keys(e).length === 0){
      setSubmitted(true)
      setTimeout(()=>{
        setSubmitted(false)
        setForm({ name: '', age: '', email: '', datetime: '' })
        alert('Appointment submitted (mock)')
      }, 800)
    }
  }

  return (
    <motion.form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md"
      initial={{ opacity: 0, y: 12 }} whileInView={{ opacity: 1, y: 0 }} viewport={{ once: true }}
    >
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm text-gray-600">Name</label>
          <input name="name" value={form.name} onChange={handleChange} className="mt-1 w-full border rounded px-3 py-2" />
          {errors.name && <div className="text-xs text-red-500 mt-1">{errors.name}</div>}
        </div>

        <div>
          <label className="block text-sm text-gray-600">Age</label>
          <input name="age" value={form.age} onChange={handleChange} className="mt-1 w-full border rounded px-3 py-2" />
          {errors.age && <div className="text-xs text-red-500 mt-1">{errors.age}</div>}
        </div>

        <div className="md:col-span-2">
          <label className="block text-sm text-gray-600">Email</label>
          <input name="email" value={form.email} onChange={handleChange} className="mt-1 w-full border rounded px-3 py-2" />
          {errors.email && <div className="text-xs text-red-500 mt-1">{errors.email}</div>}
        </div>

        <div className="md:col-span-2">
          <label className="block text-sm text-gray-600">Date & Time</label>
          <input name="datetime" value={form.datetime} onChange={handleChange} type="datetime-local" className="mt-1 w-full border rounded px-3 py-2" />
          {errors.datetime && <div className="text-xs text-red-500 mt-1">{errors.datetime}</div>}
        </div>
      </div>

      <div className="mt-4 flex items-center justify-end">
        <motion.button whileHover={{ scale: 1.03 }} whileTap={{ scale: 0.97 }} type="submit"
          className="bg-sky-600 text-white px-5 py-2 rounded-md shadow"
        >
          {submitted ? 'Submitting...' : 'Submit'}
        </motion.button>
      </div>
    </motion.form>
  )
}
