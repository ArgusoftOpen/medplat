function generateOccurrences(event,max=20){

 let occurrences=[]
 let current=new Date(event.start_time)

 while(occurrences.length<max){

   if(event.end_date && current>new Date(event.end_date)) break
   if(event.occurrence_limit && occurrences.length>=event.occurrence_limit) break

   occurrences.push(new Date(current))

   switch(event.recurrence_type){

     case "daily":
       current.setDate(current.getDate()+event.interval_value)
       break

     case "weekly":
       current.setDate(current.getDate()+(7*event.interval_value))
       break

     case "monthly":

       let day=event.day_of_month || current.getDate()

       current.setMonth(current.getMonth()+event.interval_value)

       let lastDay=new Date(
         current.getFullYear(),
         current.getMonth()+1,
         0
       ).getDate()

       current.setDate(Math.min(day,lastDay))

       break

     default:
       return occurrences
   }
 }

 return occurrences
}

module.exports=generateOccurrences