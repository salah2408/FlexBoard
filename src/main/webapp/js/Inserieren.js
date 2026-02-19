function getCategory(){
	const aktCategory = document.getElementById("category");
	const aktCatID = aktCategory.value;
	
	let html = "Test Test";
	
	if(aktCatID == 1){
		html = "<div class='row row-gap'>"
				+ "<div class='col-md-6'>"
				+	"<label>Studiengang</label> <input type='text' class='form-control'"
				+		"name='studiengang' placeholder='z.B. Informatik'>"
				+"</div>"
				+"<div class='col-md-6'>"
				+"<label>Modulname</label> <input type='text' class='form-control'"
				+		"name='modul' placeholder='z.B. Analysis 1'>"
				+"</div>"
			+"</div>"

			+"<div class='row row-gap'>"
			+"	<div class='col-md-4'>"
			+		"<label>Hochschule</label> <input type='text' class='form-control'"
			+			"name='studiengang' placeholder='HWG LU'>"
			+	"</div>"
			+	"<div class='col-md-4'>"
			+		"<label>Semester</label> <input type='text' class='form-control'"
			+			"name='modul' placeholder=''>"
			+	"</div>"
			+	"<div class='col-md-4'>"
			+		"<label>Format</label> <input type='text' class='form-control'"
			+			"name='modul' placeholder='Digital, Gedruckt...'>"
			+	"</div>"
			+"</div>"
	}
	
	const extraInput = document.getElementById("categoryInput");
	extraInput.innerHTML = html;
}