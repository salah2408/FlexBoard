document.addEventListener("click", function(e){

    const btn = e.target.closest(".favorite-toggle");
    if(!btn) return;

    // Wenn Gast -> normales Verhalten (Login)
    if(!btn.dataset.id) return;

    e.preventDefault();

	if(btn.dataset.loading) return;

	btn.dataset.loading = "1";

    const id = btn.dataset.id;
    const action = btn.dataset.action;

	fetch("./NavbarAppl.jsp?action=" + (action === "add" ? "addFavorite" : "removeFavorite") + "&id=" + id)
	.then(res => res.text())

    .then(() => {

		// globalen Favoritenstatus speichern (für andere Seiten)
		    localStorage.setItem("favoriteChanged", Date.now());

				const counter = document.getElementById("favoriteCounter");

				if(counter){

				    let current = parseInt(counter.innerText) || 0;

				    if(action === "add"){
				        counter.innerText = current + 1;
				    }else{
				        counter.innerText = Math.max(0, current - 1);
				    }

				}

        const icon = btn.querySelector("i");

        if(icon){

            if(action === "add"){

                icon.classList.remove("bi-heart");
                icon.classList.add("bi-heart-fill");

                btn.dataset.action = "remove";

            } else {

                icon.classList.remove("bi-heart-fill");
                icon.classList.add("bi-heart");

                btn.dataset.action = "add";

            }

        }

        if(btn.classList.contains("btn")){

            if(action === "add"){

                btn.dataset.action = "remove";
                btn.classList.remove("btn-outline-danger");
                btn.classList.add("btn-danger");

                btn.innerHTML = "<i class='bi bi-heart-fill'></i> Favorit entfernen";

            } else {

                btn.dataset.action = "add";
                btn.classList.remove("btn-danger");
                btn.classList.add("btn-outline-danger");

                btn.innerHTML = "<i class='bi bi-heart'></i> Zu Favoriten hinzufügen";

            }

        }

        const card = btn.closest(".favorite-card");

        if(card && action === "remove"){

            card.classList.add("removing");

            setTimeout(() => {

                card.remove();

                const container = document.getElementById("favoritesContainer");

                if(
                    container &&
                    container.querySelectorAll(".favorite-card").length === 0 &&
                    !container.querySelector(".empty-favorites")
                ){

                    container.insertAdjacentHTML(
                        "beforeend",
                        "<div class='text-muted mt-4 empty-favorites'>Du hast noch keine Favoriten gespeichert.</div>"
                    );

                }

            },300);

        }
        if(typeof showToast === "function"){

            showToast(
                action === "add"
                ? "Zu Favoriten hinzugefügt"
                : "Favorit entfernt"
            );

        }

        btn.dataset.loading = "";

    })

	.catch(() => {
	  btn.dataset.loading = "";
	});

});
// Favoriten synchronisieren (Homepage / Suche / Detailseite)

window.addEventListener("storage", function(event){

    if(event.key === "favoriteChanged"){

        const hearts = document.querySelectorAll(".favorite-toggle");

        hearts.forEach(function(btn){

            const id = btn.dataset.id;
            if(!id) return;

            fetch("./NavbarAppl.jsp?action=isFavorite&id=" + id)
            .then(res => res.text())
            .then(result => {

                const icon = btn.querySelector("i");

                if(result === "true"){

                    if(icon){
                        icon.classList.remove("bi-heart");
                        icon.classList.add("bi-heart-fill");
                    }

                    btn.dataset.action = "remove";

                } else {

                    if(icon){
                        icon.classList.remove("bi-heart-fill");
                        icon.classList.add("bi-heart");
                    }

                    btn.dataset.action = "add";

                }

            });

        });

    }

});