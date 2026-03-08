function zeigeZusatzFelder() {
    console.log("--- zeigeZusatzFelder GESTARTET ---");

    // 1. HARD RESET: Alle Blöcke verstecken und required überall entfernen
    for (let i = 1; i <= 9; i++) {
        let block = document.getElementById('catID' + i);
        if (block) {
            block.hidden = true;
            let felder = block.querySelectorAll('input, select, textarea');
            felder.forEach(function(feld) {
                feld.required = false;
            });
        }
    }
    console.log("Reset beendet: Alle Kategorien versteckt und entsperrt.");

    // 2. Aktive Kategorie auslesen
    let selectElement = document.getElementById('categorySelect');
    if (!selectElement) return;
    
    let selectedValue = selectElement.value;
    console.log("Ausgewählte Kategorie-ID: '" + selectedValue + "'");

    // 3. Block anzeigen und Felder aktivieren
    if (selectedValue !== '') {
        let aktiverBlock = document.getElementById('catID' + selectedValue);
        if (aktiverBlock) {
            aktiverBlock.hidden = false;
            
            let neueFelder = aktiverBlock.querySelectorAll('input, select, textarea');
            console.log("Gefundene Felder in Kategorie " + selectedValue + ": " + neueFelder.length);
            
            neueFelder.forEach(function(feld) {
                // Ignoriere Checkboxen und versteckte Felder
                if (feld.type !== 'checkbox' && feld.type !== 'hidden' && feld.type !== 'button' && feld.type !== 'submit') {
                    feld.required = true;
                    console.log(" -> REQUIRED gesetzt für Feld: " + (feld.name || feld.id) + " (Type: " + feld.type + ")");
                }
            });
        }
    }

    // 4. Unter-Funktionen aufrufen (damit z.B. Preis-Felder bei 'Gratis' ihr required wieder verlieren)
    console.log("Prüfe Unter-Kategorien...");
    checkEventPreis();
    checkJobVerguetung();
    checkNachhilfePreis();
    checkSonstigesPreis();

    console.log("--- zeigeZusatzFelder BEENDET ---");
}

function checkEventPreis() {
    let selectElement = document.getElementById('eventEintritt');
    let preisBlock = document.getElementById('eventPreisBlock');
    let preisInput = document.getElementById('eventPreisInput');
	
    if (!selectElement || !preisBlock) return;

    if (selectElement.value === 'Kostenpflichtig') {
        preisBlock.hidden = false;
        if (preisInput) preisInput.required = true;
        console.log("[Event] Eintritt ist Kostenpflichtig -> Event-Preis ist REQUIRED");
    } else {
        preisBlock.hidden = true;
        if (preisInput) {
            preisInput.value = '';
            preisInput.required = false;
            console.log("[Event] Eintritt ist Gratis/Leer -> Event-Preis NICHT required");
        }
    }
}

function checkJobVerguetung() {
    let selectElement = document.getElementById('jobTypSelect');
    let verguetungBlock = document.getElementById('jobVerguetungBlock');
    let verguetungInput = document.getElementById('jobVerguetungInput');
    
    if (!selectElement || !verguetungBlock) return;

    if (selectElement.value === 'Suche') {
        verguetungBlock.hidden = true;
        if (verguetungInput) {
            verguetungInput.value = '';
            verguetungInput.required = false;
            console.log("[Job] Suche Job -> Vergütung NICHT required");
        }
    } else {
        verguetungBlock.hidden = false;
        if (verguetungInput) preisInput.required = true; // BUGFIX HIER WAR VORHER EIN TIPPFEHLER!
        if (verguetungInput) verguetungInput.required = true;
        console.log("[Job] Biete/Leer -> Vergütung ist REQUIRED");
    }
}

function checkNachhilfePreis() {
    let selectElement = document.getElementById('nachhilfeTypSelect');
    let preisBlock = document.getElementById('nachhilfePreisBlock');
    let preisInput = document.getElementById('nachhilfePreisInput');
	
    if (!selectElement || !preisBlock) return;

    if (selectElement.value === 'Biete Nachhilfe') {
        preisBlock.hidden = false;
        if (preisInput) preisInput.required = true;
        console.log("[Nachhilfe] Bietet an -> Preis pro Stunde ist REQUIRED");
    } else {
        preisBlock.hidden = true;
        if (preisInput) {
            preisInput.value = '';
            preisInput.required = false;
            console.log("[Nachhilfe] Sucht/Lerngruppe -> Preis pro Stunde NICHT required");
        }
    }
}

function checkSonstigesPreis() {
    let selectElement = document.getElementById('sonstigesPreisSelect');
    let preisBlock = document.getElementById('sonstigesPreisBlock');
    let preisInput = document.getElementById('sonstigesPreisInput');
    
    if (!selectElement || !preisBlock) return;

    if (selectElement.value === 'Preis') {
        preisBlock.hidden = false;
        if (preisInput) preisInput.required = true;
        console.log("[Sonstiges] Festpreis gewählt -> Preis-Feld ist REQUIRED");
    } else {
        preisBlock.hidden = true;
        if (preisInput) {
            preisInput.value = '';
            preisInput.required = false;
            console.log("[Sonstiges] Gratis/Auf Anfrage -> Preis-Feld NICHT required");
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    zeigeZusatzFelder();
    checkEventPreis();
    checkJobVerguetung();
    checkNachhilfePreis();
    checkSonstigesPreis();
    
    const fileInput = document.getElementById('bildUpload');
    const container = document.getElementById('vorschauContainer');
    const hiddenInput = document.getElementById('imageBase64Input');

    if (!fileInput || !container || !hiddenInput) {
        return; 
    }

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

        const reader = new FileReader();
        reader.onload = function(e) {
            const img = new Image();
            img.onload = function() {
                const MAX_WIDTH = 800;
                const MAX_HEIGHT = 800;
                let width = img.width;
                let height = img.height;

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

                const canvas = document.createElement('canvas');
                canvas.width = width;
                canvas.height = height;
                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0, width, height);

                const komprimiertesBase64 = canvas.toDataURL('image/jpeg', 0.7);
                hiddenInput.value = komprimiertesBase64;

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