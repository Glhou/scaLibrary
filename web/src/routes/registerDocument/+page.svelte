<script>
    import { onMount } from 'svelte';
    let name = "";
    let author = "";
    let selectedType = -1;
    let selectedLoc = -1;
    let type_id = 0;
    let location_id = 0;
    let location = [];
    let type = [];
    let ty = "";

    onMount(async() => {
        getType();
        getLoc()});
  
    async function registerDocument() {
        type_id = selectedType.type_id;
        location_id = selectedLoc.location_id;
        const data = {
            name,
            author,
            type_id,
            location_id
        };
        // alert(JSON.stringify(data));

        const response = await fetch('http://localhost:7000/document', {
            method:'POST',
            headers: {
            'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
    
        if (response.ok) {
            alert("suucess");
        } else {
            // alert(response.statusText);
            // alert(response.json());
            alert("faild");
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
      } else {
        alert("cannot get location");
      }
    }

    async function getType() {
        const url = "http://localhost:7000/type"
        const response = await fetch(url, {
        method: "GET",
      });
      if (response.ok) {
        // alert('get');
        type = await response.json()
      } else {
        alert("cannot get type");
      }
    }
  </script>

  <main>
    <h1>Document Registration</h1>

    <form on:submit|preventDefault={registerDocument}>
      <label>
        name:
        <input type="text" bind:value={name} />
      </label>
  
      <label>
        author:
        <input type="text" bind:value={author} />
      </label>
  
      <label>
        Type:
        <select bind:value={selectedType}>
            <option value=-1>choice</option>
                {#each type as t}
                    <option value={t}>{t.name}
                    </option>
                {/each}     
        </select>
    </label>
  
      <label>
        Location:
        <select bind:value={selectedLoc}>
            <option value=-1>choice</option>
                {#each location as l}
                    <option value={l}>{l.name}
                    </option>
                {/each}     
        </select>
      </label>
  
      <button type="submit">Register</button>

    </form>
    <a href="/">Home</a>
  </main>
  