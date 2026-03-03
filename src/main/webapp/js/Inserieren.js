
function zeigeZusatzFelder() {
    // 1. Zuerst alle potenziellen Blöcke (1 bis 8) verstecken
    for (let i = 1; i <= 8; i++) {
        let block = document.getElementById('catID' + i);
        if (block) {
            block.hidden = true; // Setzt das Element auf unsichtbar
        }
    }

    // 2. Den Wert der aktuell ausgewählten Kategorie auslesen (z.B. '3')
    let selectElement = document.getElementById('categorySelect');
	if (!selectElement) return;
    let selectedValue = selectElement.value;

    // 3. Wenn ein gültiger Wert (nicht leer) gewählt wurde, den passenden Block einblenden
    if (selectedValue !== '') {
        let aktiverBlock = document.getElementById('catID' + selectedValue);
        if (aktiverBlock) {
            aktiverBlock.hidden = false; // Macht das Element sichtbar
        }
    }
}

// Für Campus-Events
function checkEventPreis() {
    let auswahl = document.getElementById('eventEintritt').value;
    let preisBlock = document.getElementById('eventPreisBlock');
    let preisInput = document.getElementById('eventPreisInput');
	
	if (!preisBlock) return;

    if (auswahl === 'Kostenpflichtig') {
        preisBlock.hidden = false;
    } else {
        preisBlock.hidden = true;
        preisInput.value = '';
    }
}

// Für Jobs & Praktika
function checkJobVerguetung() {
    let auswahl = document.getElementById('jobTypSelect').value;
    let verguetungBlock = document.getElementById('jobVerguetungBlock');
    let verguetungInput = document.getElementById('jobVerguetungInput');
	if (!verguetungBlock) return;

    if (auswahl === 'Suche') {
        verguetungBlock.hidden = true;
        verguetungInput.value = '';
    } else {
        verguetungBlock.hidden = false;
    }
}

// Für Nachhilfe
function checkNachhilfePreis() {
    let auswahl = document.getElementById('nachhilfeTypSelect').value;
    let preisBlock = document.getElementById('nachhilfePreisBlock');
    let preisInput = document.getElementById('nachhilfePreisInput');
	
	if (!preisBlock) return;

    if (auswahl === 'Biete Nachhilfe') {
        preisBlock.hidden = false;
    } else {
        preisBlock.hidden = true;
        preisInput.value = '';
    }
}

document.addEventListener('DOMContentLoaded', function() {
	// Beim Laden sofort ausführen
	   zeigeZusatzFelder();
	   checkEventPreis();
	   checkJobVerguetung();
	   checkNachhilfePreis();
    
    const fileInput = document.getElementById('bildUpload');
    const container = document.getElementById('vorschauContainer');
	const hiddenInput = document.getElementById('imageBase64Input');

    // Diagnose: Prüfen, ob das Script die HTML-Elemente überhaupt findet
    if (!fileInput || !container || !hiddenInput) {
        console.error("Fehler: Das Script findet 'bildUpload' oder 'vorschauContainer' nicht auf der Seite.");
        return; 
    }

    console.log("Upload-Script aktiv und bereit!");

    fileInput.addEventListener('change', function(event) {
        container.innerHTML = ''; 
		hiddenInput.value = '';
        const dateien = event.target.files;

        if (!dateien || dateien.length === 0) return;
		
		const datei = dateien[0];
		
		if (!datei.type.startsWith('image/')) {
		           alert('Bitte wähle ausschließlich Bilddateien aus.');
		           fileInput.value = '';
		           return;

     
            }

			// Canvas-Komprimierung und Base64 Erzeugung
			        const reader = new FileReader();
			        reader.onload = function(e) {
			            const img = new Image();
			            img.onload = function() {
			                // 1. Maximale Größe festlegen
			                const MAX_WIDTH = 800;
			                const MAX_HEIGHT = 800;
			                let width = img.width;
			                let height = img.height;

			                // 2. Seitenverhältnis berechnen und anpassen
			                if (width > height) {
			                    if (width > MAX_WIDTH) {
			                        height = Math.round(height *= MAX_WIDTH / width);
			                        width = MAX_WIDTH;
			                    }
			                } else {
			                    if (height > MAX_HEIGHT) {
			                        width = Math.round(width *= MAX_HEIGHT / height);
			                        height = MAX_HEIGHT;
			                    }
			                }

			                // 3. Canvas erstellen und Bild neu zeichnen
			                const canvas = document.createElement('canvas');
			                canvas.width = width;
			                canvas.height = height;
			                const ctx = canvas.getContext('2d');
			                ctx.drawImage(img, 0, 0, width, height);

			                // 4. Komprimiertes Base64 erzeugen (JPEG mit 70% Qualität)
			                const komprimiertesBase64 = canvas.toDataURL('image/jpeg', 0.7);

			                // 5. In das versteckte Feld einfügen
			                hiddenInput.value = komprimiertesBase64;

			                // 6. Vorschau anzeigen
			                const previewImg = document.createElement('img');
			                previewImg.src = komprimiertesBase64;
			                previewImg.style.height = '100px';
			                previewImg.style.width = '100px';
			                previewImg.style.objectFit = 'cover';
			                previewImg.style.borderRadius = '8px';
			                previewImg.style.border = '1px solid #dee2e6';
			                container.appendChild(previewImg);
			            };
			            img.src = e.target.result;
			        };
			        reader.readAsDataURL(datei);
			    });
			});