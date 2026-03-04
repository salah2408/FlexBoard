function checkMinMaxPreis(){
	let min = document.getElementById("minPreis");
	let max = document.getElementById("maxPreis");
	
	let minValue = min.value;
	let maxValue = max.value;
	
	
	if(minValue > maxValue)
		max.value = "";
}