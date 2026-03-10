require("dotenv").config()
const express=require("express")
const db=require("./db")

const generateOccurrences=require("./recurrence")
const app=express()

app.use(express.json())
app.use(express.static("public"))

app.post("/event",(req,res)=>{

 const data=req.body

 const sql=`
 INSERT INTO events 
 (title,start_time,recurrence_type,interval_value,day_of_week,day_of_month,end_date,occurrence_limit)
 VALUES (?,?,?,?,?,?,?,?)
 `

 db.query(sql,[
  data.title,
  data.start_time,
  data.recurrence_type,
  data.interval_value,
  data.day_of_week,
  data.day_of_month,
  data.end_date,
  data.occurrence_limit
 ],(err,result)=>{

   if(err) return res.send(err)

   res.send("Event created")
 })

})

app.get("/occurrences/:id",(req,res)=>{

 db.query(
  "SELECT * FROM events WHERE id=?",
  [req.params.id],
  (err,result)=>{

   if(err) return res.send(err)

   const occurrences=generateOccurrences(result[0])

   res.json(occurrences)
 })

})

app.listen(3000,()=>{
 console.log("Server running on port 3000")
})