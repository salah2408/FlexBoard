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

    if (auswahl === 'Kostenpflichtig') {
        preisBlock.hidden = false;
    } else {
        preisBlock.hidden = true;
        preisInput.value = ''; // Wichtig für die Blank Space Übertragung
    }
}

// Für Jobs & Praktika
function checkJobVerguetung() {
    let auswahl = document.getElementById('jobTypSelect').value;
    let verguetungBlock = document.getElementById('jobVerguetungBlock');
    let verguetungInput = document.getElementById('jobVerguetungInput');

    if (auswahl === 'Suche') {
        verguetungBlock.hidden = true;
        verguetungInput.value = ''; // Wichtig für die Blank Space Übertragung
    } else {
        // Bei "Biete" oder wenn noch nichts gewählt ist, anzeigen
        verguetungBlock.hidden = false;
    }
}

// Für Nachhilfe
function checkNachhilfePreis() {
    let auswahl = document.getElementById('nachhilfeTypSelect').value;
    let preisBlock = document.getElementById('nachhilfePreisBlock');
    let preisInput = document.getElementById('nachhilfePreisInput');

    if (auswahl === 'Biete Nachhilfe') {
        preisBlock.hidden = false;
    } else {
        preisBlock.hidden = true;
        preisInput.value = ''; // Wichtig für die Blank Space Übertragung
    }
}

document.addEventListener('DOMContentLoaded', function() {
    
    const fileInput = document.getElementById('bildUpload');
    const container = document.getElementById('vorschauContainer');

    // Diagnose: Prüfen, ob das Script die HTML-Elemente überhaupt findet
    if (!fileInput || !container) {
        console.error("Fehler: Das Script findet 'bildUpload' oder 'vorschauContainer' nicht auf der Seite.");
        return; 
    }

    console.log("Upload-Script aktiv und bereit!");

    fileInput.addEventListener('change', function(event) {
        container.innerHTML = ''; 
        const dateien = event.target.files;

        if (!dateien || dateien.length === 0) return;

        let ungueltigeDateiGefunden = false;

        for (let i = 0; i < dateien.length; i++) {
            const datei = dateien[i];
            
            // STRIKTE KONTROLLE: Ist es wirklich ein Bild?
            if (!datei.type.startsWith('image/')) {
                ungueltigeDateiGefunden = true;
                break; // Schleife sofort abbrechen
            }

            // VORSCHAU GENERIEREN
            const bildUrl = URL.createObjectURL(datei);
            const img = document.createElement('img');
            img.src = bildUrl;
            img.style.height = '100px';
            img.style.width = '100px';
            img.style.objectFit = 'cover';
            img.style.borderRadius = '8px';
            img.style.border = '1px solid #dee2e6';
            
            container.appendChild(img);
        }

        // WENN EINE FALSCHE DATEI DABEI WAR: Alles löschen!
        if (ungueltigeDateiGefunden) {
            alert('Bitte wähle ausschließlich Bilddateien aus.');
            fileInput.value = ''; // Leert das Feld sofort (wird als Blank Space übertragen)
            container.innerHTML = ''; // Löscht eventuell schon generierte Vorschauen
        }
    });
});