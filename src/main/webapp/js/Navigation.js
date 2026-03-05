function setCurrentSite(){
	let currSite = document.URL;
	let urlArray = currSite.split("/");
	
	let hiddenInput = document.getElementById("currSite");
	
	hiddenInput.value = urlArray[urlArray.length - 1];
}