const onError = (event) =>{
    event.target.src = "../images/no-image.jpg";

    event.target.onerror = null;
};