<script>
      let user_id = "";
      let document_id = "";
      let word = "";
      let searchResult = [];
      let location = [];
    
      async function Rental() {
        const data = {
          user_id
        };
        const url = "http://localhost:7000/document/" + document_id + "/loan"
        const response = await fetch(url, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(data)
        });
    
        if (response.ok) {
          alert("success");
        } else {
          alert("fail");
        }
      }
  
      async function getLoc() {
        const url = "http://localhost:7000/location"
        const response = await fetch(url, {
          method: "GET",
        });
        if (response.ok) {
            location = await response.json()
        } else {
            alert("cannot get location");
        }
      }
  
      async function searchDoc() {
        let tableEle = document.getElementById('dataTable');
        const rows = dataTable.rows.length;
        if (rows > 1) {
          for (let i = rows - 1; i > 0; i--) {
            try{
              dataTable.deleteRow(i);
            } catch (error) {
              alert(error);
            }
            
          }
        }
        getLoc()
          const data = {
          user_id
        };
  
        const url = "http://localhost:7000/document/name/" + word
        const response = await fetch(url, {
          method: "GET",
        });
    
        if (response.ok) {
          searchResult = await response.json()
          // alert(JSON.stringify(searchResult))
  
          for (let i = 0; i < searchResult.length; i++) {
            let tr = document.createElement('tr');
            let td1 = document.createElement('td');
            td1.innerHTML = searchResult[i].document_id;
            tr.appendChild(td1);
  
            let td2 = document.createElement('td');
            td2.innerHTML = searchResult[i].name;
            tr.appendChild(td2);
  
            let td3 = document.createElement('td');
            const locId = searchResult[i].location_id;
            for (let j = 0; j < location.length; j++) {
              if (location[j].location_id == locId) {
                td3.innerHTML = location[j].name;
              }
            }
            tr.appendChild(td3);
            tableEle.appendChild(tr);
          }
          //document.write('<tr>');
          //document.write('<th></th>');
  
  
        } else {
          alert("fail");
        }
      }   

    </script>

    <main>
      <h1>Rental</h1>
      <a href="/">Home</a>
  
      <h2>Input</h2>
      <form on:submit|preventDefault={Rental}>
        <label>
          User ID:
          <input type="number" bind:value={user_id} />
        </label>
    
        <label>
          Document ID:
          <input type="number" bind:value={document_id} />
        </label>
    
        <button type="submit">Rental</button>
      </form>
  
      <h2>Search</h2>
      <form on:submit|preventDefault={searchDoc}>
        <label>
          search word:
          <input type="text" bind:value={word}>
          <button type="submit">Search</button>
        </label>
        <table id='dataTable' cellpadding="10">
          <thead>
            <tr align="left"><th>Document ID</th><th>Document Name</th><th>Location</th></tr>
          </thead>
        </table>
        <script>
          
        </script>
          
      </form>
  
    </main>
