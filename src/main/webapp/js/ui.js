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