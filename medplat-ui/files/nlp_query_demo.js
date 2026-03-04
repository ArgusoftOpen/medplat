document.addEventListener('DOMContentLoaded', function(){
  const btn = document.getElementById('previewBtn');
  const ta = document.getElementById('nlq');
  const out = document.getElementById('result');
  btn.addEventListener('click', async function(){
    const q = ta.value.trim();
    if(!q){ out.innerHTML = '<em>Enter a query first.</em>'; return; }
    out.innerHTML = '<em>Loading preview...</em>';
    try{
      const res = await fetch('http://localhost:5003/preview', {
        method: 'POST',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify({query: q})
      });
      const data = await res.json();
      if(res.ok){
        out.innerHTML = `<pre>${data.preview_sql}\n\nParams: ${JSON.stringify(data.params)}</pre>`;
      } else {
        out.innerHTML = `<div style="color:red">Error: ${data.message || 'Unknown error'}</div>`;
      }
    } catch(err){
      out.innerHTML = `<div style="color:red">Connection error: ${err.message}</div>`;
    }
  });
});
