document.addEventListener("DOMContentLoaded", function () {
    const checkbox = document.getElementById("agbAccepted");
    const openTermsLink = document.getElementById("openTermsLink");
    const termsHint = document.getElementById("termsHint");

    const modalEl = document.getElementById("termsModal");
    const scrollBox = document.getElementById("termsScrollBox");
    const contentBox = document.getElementById("termsContent");
    const acceptBtn = document.getElementById("termsAcceptBtn");
    const progressText = document.getElementById("termsProgress");

    if (!checkbox || !modalEl || !scrollBox || !contentBox || !acceptBtn) return;

    const termsModal = new bootstrap.Modal(modalEl);
    let accepted = false;

    // Terms aus JS rendern
    const terms = window.FLEXBOARD_TERMS || [];
    let html = "";
    terms.forEach(section => {
        html += "<h6>" + section.title + "</h6>";
        if (section.text) {
            html += "<p>" + section.text + "</p>";
        }
        if (section.list && Array.isArray(section.list)) {
            html += "<ul>";
            section.list.forEach(item => {
                html += "<li>" + item + "</li>";
            });
            html += "</ul>";
        }
    });
    html += "<hr><p class='text-muted mb-0'>Bitte bis zum Ende scrollen, dann wird der Button aktiv.</p>";
    contentBox.innerHTML = html;

    function isBottom(el) {
        return el.scrollTop + el.clientHeight >= el.scrollHeight - 8;
    }

    checkbox.addEventListener("click", function (e) {
        if (!accepted) {
            e.preventDefault();
            termsModal.show();
        }
    });

    openTermsLink.addEventListener("click", function (e) {
        e.preventDefault();
        termsModal.show();
    });

    modalEl.addEventListener("show.bs.modal", function () {
        if (accepted) {
            acceptBtn.disabled = false;
            progressText.textContent = "Bereits akzeptiert.";
            return;
        }
        scrollBox.scrollTop = 0;
        acceptBtn.disabled = true;
        progressText.textContent = "Noch nicht bis zum Ende gescrollt.";
    });

    scrollBox.addEventListener("scroll", function () {
        if (!accepted && isBottom(scrollBox)) {
            acceptBtn.disabled = false;
            progressText.textContent = "Du kannst jetzt akzeptieren.";
        }
    });

    acceptBtn.addEventListener("click", function () {
        accepted = true;
        checkbox.checked = true;
        termsHint.textContent = "Nutzungsbedingungen akzeptiert.";
        termsHint.classList.remove("text-muted");
        termsHint.classList.add("text-success");
        termsModal.hide();
    });
});