async function getCsrfToken(){
    try {
        const response = await fetch("/get-csrf-token");
        if (response.ok){
            const csrfToken = await response.json();
            console.log(csrfToken);
            console.log(csrfToken.token)
        }

    }catch (error){
        console.error('There was a problem with the fetch operation:', error);
    }
}

getCsrfToken();