<script>
    import { onMount } from 'svelte';
    let user_id = "";
    let document_id = "";
    let searchResult = [];
    let location = [];

    onMount(async() => {getLoc()});
  
    async function Return() {
      const data = {
        user_id
      };
      const url = "http://localhost:7000/document/" + document_id + "/return"
      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });
  
      if (response.ok) {
        // alert("success");
        searchDoc()
      } else {
        alert("fail");
      }
    }

    async function searchDoc() {
        const data = {
        user_id
      };
      const url = "http://localhost:7000/user/" + user_id + "/documents"
      const response = await fetch(url, {
        method: "GET",
      });
  
      if (response.ok) {
        searchResult = await response.json()

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
        // alert('get');
        location = await response.json()
        // alert(JSON.stringify(location))
      } else {
        alert("cannot get location");
      }
    }
  </script>
  
  <main>
    <h1>Return Page</h1>
    <a href="/">Home</a>
    
    <h2>renting</h2>
    <form on:submit|preventDefault={searchDoc}>
      <label>
        User ID:
        <input type="number" bind:value={user_id} />
      </label>

      <button type="submit">Search</button>
    
    </form>

    <p>Result Num: {searchResult.length}</p>
    <!--
        {#if searchResult.length > 0}
        <ul>
        {#each searchResult as doc}
        <li>document_id: {doc.document_id}, name: {doc.name}</li>
        {/each}
        </ul>
    {/if}
    -->
    <table id='dataTable' cellpadding="10">
        <thead>
        <tr align="left"><th>Document ID</th><th>Document Name</th><th>Location</th></tr>
        </thead>
    </table>

    <h2>Enter the document_id you wish to return</h2>
    <form on:submit|preventDefault={Return}>
      <label>
        Document ID:
        <input type="number" bind:value={document_id} />
      </label>
  
      <button type="submit">Return</button>
    </form>

  </main>
  