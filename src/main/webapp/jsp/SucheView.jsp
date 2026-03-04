<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="de.hwg_lu.bwi520.beans.CategoryBean" %>
<%@ page import="de.hwg_lu.bwi520.classes.Category" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Flexboard - Suche</title>
<link href="../css/Suche.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<style>
.hover-card {
    transition: transform 0.2s, box-shadow 0.2s;
}
.hover-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important;
}
.sidebar {
    background-color: #f8f9fa;
    min-height: 100vh;
    padding: 20px;
}
.sidebar h5 {
    margin-top: 20px;
    margin-bottom: 15px;
}
.sidebar h6 {
    margin-top: 15px;
    margin-bottom: 10px;
    font-size: 0.95rem;
    font-weight: 600;
}
.filter-item {
    margin-bottom: 10px;
}
.sidebar-link {
    color: #495057;
    text-decoration: none;
    display: block;
    padding: 8px 12px;
    border-radius: 5px;
    transition: background-color 0.2s;
}
.sidebar-link:hover {
    background-color: #e9ecef;
    color: #0d6efd;
}
.sidebar-link.active {
    background-color: #0d6efd;
    color: white;
}
.search-bar {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 30px 0;
}
</style>
</head>

<body>
    <jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
    <jsp:useBean id="mySearch" class="de.hwg_lu.bwi520.beans.SearchBean" scope="session" />
    <jsp:useBean id="myCategory" class="de.hwg_lu.bwi520.beans.CategoryBean" scope="request" />

    <jsp:getProperty property="navbarHtml" name="myAccount" />

    <!-- SUCHLEISTE -->
    <form action="./SucheAppl.jsp" method="get">
        <div class="container-fluid search-bar">
            <div class="container">
                <div class="row g-2 align-items-center">

                    <div class="col-md-4">
                        <input type="text" class="form-control" name="q"
                            value="<jsp:getProperty property='suchbegriff' name='mySearch' />" 
                            placeholder="Was suchst du?">
                    </div>

                    <div class="col-md-3">
                        <select class="form-select" name="kategorie">
                            <option value="">Alle Kategorien</option>
                            <%
                                for (Category cat : myCategory.getAllCategories()) {
                                    String selected = cat.getName().equals(mySearch.getKategorie()) ? "selected" : "";
                            %>
                                <option value="<%= cat.getName() %>" <%= selected %>><%= cat.getName() %></option>
                            <%
                                }
                            %>
                        </select>
                    </div>

                    <div class="col-md-3">
                        <input type="text" class="form-control" name="plzOrt" 
                            value="<jsp:getProperty property='plzOrt' name='mySearch' />"
                            placeholder="PLZ oder Ort">
                    </div>

                    <div class="col-md-2 d-grid">
                        <button class="btn btn-light" type="submit" name="action" value="Finden">
                            <i class="bi bi-search"></i> Finden
                        </button>
                    </div>

                </div>
            </div>
        </div>
    </form>


    <div class="container-fluid">
        <div class="row">
            
            <!-- SIDEBAR FILTER -->
            <div class="col-md-3 col-lg-2 sidebar">

                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">Filter</h5>
                    <a href="./SucheAppl.jsp?action=reset" class="btn btn-sm btn-outline-secondary">
                        Zurücksetzen
                    </a>
                </div>

                <h6>Kategorien</h6>
                <%
                    String[] icons = {
                        "bi-book",           // Lernmaterial
                        "bi-people",         // Nachhilfe
                        "bi-house",          // Wohnen
                        "bi-briefcase",      // Jobs
                        "bi-laptop",         // Technik
                        "bi-calendar-event", // Events
                        "bi-arrow-left-right", // Tauschen
                        "bi-tools"           // Dienstleistungen
                    };
                    
                    int iconIndex = 0;
                    for (Category cat : myCategory.getAllCategories()) {
                        String icon = iconIndex < icons.length ? icons[iconIndex] : "bi-circle";
                        String activeClass = cat.getName().equals(mySearch.getKategorie()) ? "active" : "";
                %>
                    <div class="filter-item">
                        <a class="sidebar-link <%= activeClass %>" 
                           href="./SucheAppl.jsp?action=filterKategorie&kategorie=<%= java.net.URLEncoder.encode(cat.getName(), "UTF-8") %>">
                            <i class="bi <%= icon %>"></i> <%= cat.getName() %>
                        </a>
                    </div>
                <%
                        iconIndex++;
                    }
                %>

                <h6>Preis</h6>
                <form action="./SucheAppl.jsp" method="get">
                    <input type="hidden" name="q" value="<jsp:getProperty property='suchbegriff' name='mySearch' />">
                    <input type="hidden" name="kategorie" value="<jsp:getProperty property='kategorie' name='mySearch' />">
                    <input type="hidden" name="plzOrt" value="<jsp:getProperty property='plzOrt' name='mySearch' />">
                    
                    <div class="mb-2">
                        <input type="number" class="form-control mb-2" name="minPreis" 
                            placeholder="Min €" min="0" 
                            value="<%= mySearch.getMinPreis() != null ? mySearch.getMinPreis() : "" %>">
                        <input type="number" class="form-control mb-2" name="maxPreis" 
                            placeholder="Max €" min="0"
                            value="<%= mySearch.getMaxPreis() != null ? mySearch.getMaxPreis() : "" %>">
                        <button type="submit" name="action" value="Finden" class="btn btn-sm btn-primary w-100">
                            Anwenden
                        </button>
                    </div>
                </form>

                <h6>Anbieter</h6>
                <div class="filter-item">
                    <a class="sidebar-link <%= "Privat".equals(mySearch.getAnbieterTyp()) ? "active" : "" %>" 
                       href="./SucheAppl.jsp?action=filterAnbieter&anbieter=Privat">
                        <i class="bi bi-person"></i> Privat
                    </a>
                </div>
                <div class="filter-item">
                    <a class="sidebar-link <%= "Gewerblich".equals(mySearch.getAnbieterTyp()) ? "active" : "" %>" 
                       href="./SucheAppl.jsp?action=filterAnbieter&anbieter=Gewerblich">
                        <i class="bi bi-building"></i> Gewerblich
                    </a>
                </div>

            </div>
            
            <!-- ERGEBNISSE -->
            <div class="col-md-9 col-lg-10 p-4">
                
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4>
                        <%= mySearch.getAnzahlErgebnisse() %> <%= mySearch.getAnzahlErgebnisse() == 1 ? "Ergebnis" : "Ergebnisse" %>
                    </h4>
                    
                    <% if (!mySearch.getSuchbegriff().isEmpty() || !mySearch.getKategorie().isEmpty()) { %>
                        <span class="text-muted">
                            <% if (!mySearch.getSuchbegriff().isEmpty()) { %>
                                für "<strong><jsp:getProperty property='suchbegriff' name='mySearch' /></strong>"
                            <% } %>
                            <% if (!mySearch.getKategorie().isEmpty()) { %>
                                in <strong><jsp:getProperty property='kategorie' name='mySearch' /></strong>
                            <% } %>
                        </span>
                    <% } %>
                </div>
                
                <!-- Suchergebnisse HTML -->
                <%= mySearch.getSuchergebnisseHtml() %>
                
            </div>
        </div>
    </div>

</body>
</html>