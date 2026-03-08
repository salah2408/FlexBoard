function checkMinMaxPreis() {
    let min = document.getElementById("minPreis");
    let max = document.getElementById("maxPreis");

    let minValue = min.value;
    let maxValue = max.value;


    if (minValue > maxValue)
        max.value = "";
}


document.addEventListener("DOMContentLoaded", function(){

    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');

    tooltipTriggerList.forEach(function(el){
        new bootstrap.Tooltip(el);
    });

});
