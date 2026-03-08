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

    // 2. Aktive Kategorie auslesen
    let selectElement = document.getElementById('categorySelect');
    if (!selectElement) return;
    
    let selectedValue = selectElement.value;

    // 3. Block anzeigen und Felder aktivieren
    if (selectedValue !== '') {
        let aktiverBlock = document.getElementById('catID' + selectedValue);
        if (aktiverBlock) {
            aktiverBlock.hidden = false;
            
            let neueFelder = aktiverBlock.querySelectorAll('input, select, textarea');
            neueFelder.forEach(function(feld) {
                if (feld.type !== 'checkbox' && feld.type !== 'hidden' && feld.type !== 'button' && feld.type !== 'submit') {
                    feld.required = true;
                }
            });
        }
    }

    // 4. BUGFIX: Wir rufen hier NUR die Funktion auf, die zur aktuellen Kategorie gehört!
    if (selectedValue === '6') checkEventPreis();
    if (selectedValue === '4') checkJobVerguetung();
    if (selectedValue === '2') checkNachhilfePreis();
    if (selectedValue === '9') checkSonstigesPreis();
}

function checkEventPreis() {
    let selectElement = document.getElementById('eventEintritt');
    let preisBlock = document.getElementById('eventPreisBlock');
    let preisInput = document.getElementById('eventPreisInput');
	
    if (!selectElement || !preisBlock) return;

    if (selectElement.value === 'Kostenpflichtig') {
        preisBlock.hidden = false;
        if (preisInput) preisInput.required = true;
    } else {
        preisBlock.hidden = true;
        if (preisInput) {
            preisInput.value = '';
            preisInput.required = false;
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
        }
    } else {
        verguetungBlock.hidden = false;
        if (verguetungInput) verguetungInput.required = true;
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
    } else {
        preisBlock.hidden = true;
        if (preisInput) {
            preisInput.value = '';
            preisInput.required = false;
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
    } else {
        preisBlock.hidden = true;
        if (preisInput) {
            preisInput.value = '';
            preisInput.required = false;
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // 1. Zuerst die Formular-Funktionen aufrufen
    try {
        zeigeZusatzFelder();
        // BUGFIX: Hier habe ich die 4 check... Aufrufe gelöscht. 
        // Sie werden jetzt durch zeigeZusatzFelder() automatisch richtig aufgerufen!
    } catch (e) {
        console.error("Fehler in den Formular-Funktionen:", e);
    }
    
    // 2. Jetzt kümmern wir uns isoliert um den Bild-Upload
    console.log("--- BILD UPLOAD INITIALISIERUNG ---");
    const fileInput = document.getElementById('bildUpload');
    const container = document.getElementById('vorschauContainer');
    const hiddenInput = document.getElementById('imageBase64Input');

    console.log("Prüfe HTML-Elemente:");
    console.log("1. fileInput gefunden?", fileInput !== null);
    console.log("2. container gefunden?", container !== null);
    console.log("3. hiddenInput gefunden?", hiddenInput !== null);

    if (!fileInput || !container || !hiddenInput) {
        console.error("ABBRUCH: Eines der Bild-Elemente fehlt im HTML! Überprüfe die IDs.");
        return; 
    }

    console.log("Alle Bild-Elemente gefunden. Aktiviere Listener...");

    fileInput.addEventListener('change', function(event) {
        console.log("Datei wurde ausgewählt!");
        container.innerHTML = ''; 
        hiddenInput.value = '';
        const dateien = event.target.files;

        if (!dateien || dateien.length === 0) {
            console.log("Auswahl abgebrochen.");
            return;
        }
		
        const datei = dateien[0];
        console.log("Ausgewählte Datei: " + datei.name + " (Typ: " + datei.type + ")");
		
        // Sicherheits-Check: Ist es wirklich ein Bild?
        if (!datei.type.startsWith('image/')) {
            alert('Bitte wähle ausschließlich Bilddateien (wie JPG oder PNG) aus.');
            fileInput.value = ''; // Feld wieder leeren
            return;
        }

        const reader = new FileReader();
        
        reader.onload = function(e) {
            console.log("Datei im Speicher gelesen, beginne Komprimierung...");
            const img = new Image();
            
            img.onload = function() {
                const MAX_WIDTH = 800;
                const MAX_HEIGHT = 800;
                let width = img.width;
                let height = img.height;

                // Saubere Mathematik für das Seitenverhältnis
                if (width > height) {
                    if (width > MAX_WIDTH) {
                        height = Math.round(height * (MAX_WIDTH / width));
                        width = MAX_WIDTH;
                    }
                } else {
                    if (height > MAX_HEIGHT) {
                        width = Math.round(width * (MAX_HEIGHT / height));
                        height = MAX_HEIGHT;
                    }
                }

                const canvas = document.createElement('canvas');
                canvas.width = width;
                canvas.height = height;
                const ctx = canvas.getContext('2d');
                ctx.drawImage(img, 0, 0, width, height);

                // Bild zu Base64 machen
                const komprimiertesBase64 = canvas.toDataURL('image/jpeg', 0.7);
                
                // 1. In die DB (verstecktes Feld) schreiben!
                hiddenInput.value = komprimiertesBase64;
                console.log("Erfolg! Bild komprimiert und in hiddenInput geschrieben.");

                // 2. Vorschau erzeugen!
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
        
        reader.onerror = function() {
            console.error("Fehler beim Einlesen der Datei!");
            alert("Die Datei konnte nicht gelesen werden.");
        };

        reader.readAsDataURL(datei);
    });
});