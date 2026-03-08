function showToast(message, type = "success") {

    const toast = document.createElement("div");
    toast.className = "app-toast " + type;
    toast.innerText = message;

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.classList.add("show");
    }, 100);

    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => {
            document.body.removeChild(toast);
        }, 400);
    }, 3000);
}
// ==============================
// Homepage Carousel Page Indicator
// ==============================

window.addEventListener("load", function () {

    const carouselElement = document.getElementById("homepageCarousel");
    const indicator = document.getElementById("carouselPageIndicator");

    if (!carouselElement || !indicator) return;

    const slides = carouselElement.querySelectorAll(".carousel-item");
    const totalSlides = slides.length;

    if (totalSlides === 0) return;

    indicator.textContent = "1 von " + totalSlides;

    carouselElement.addEventListener("slid.bs.carousel", function (event) {
        indicator.textContent = (event.to + 1) + " von " + totalSlides;
    });

});

