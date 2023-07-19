<script>
    import { onMount } from 'svelte';
    let name = "";
    let location_id = 0;
    let document_limit = 0;
    let selectedLoc = -1;
    let location = [];
  
    onMount(async() => {getLoc()});

    async function registerUser() {
      const data = {
        name,
        location_id,
        document_limit
      };
  
      const response = await fetch("http://localhost:7000/user", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
      });
  
      if (response.ok) {
        alert("success");
      } else {
        alert("failed");
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
    <h1>User Registration</h1>
  
    <form on:submit|preventDefault={registerUser}>
      <label>
        name:
        <input type="text" bind:value={name} />
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
  
      <label>
        Document limit:
        <input type="number" bind:value={document_limit} />
      </label>
  
      <button type="submit">Register</button>
    </form>
  
    <a href="/">Home</a>
  </main>
  