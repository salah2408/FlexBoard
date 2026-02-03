<%@page import="de.hwg_lu.bwi520.messages.RegMessage"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="de">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>FlexBoard - Registrieren</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:useBean id="myMsg" class="de.hwg_lu.bwi520.messages.RegMessage" scope="session" />


<nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <a class="navbar-brand" href="index.html">FlexBoard</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item"><a class="nav-link" href="index.html">Home</a></li>
        <li class="nav-item"><a class="nav-link" href="login.html">Login</a></li>
        <li class="nav-item"><a class="nav-link active" href="register.html">Registrieren</a></li>
      </ul>
    </div>
  </div>
</nav>

<main class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card shadow-sm p-4"> 
      	<h2 class="mb-4"><jsp:getProperty name="myMsg" property="msg" /></h2>
      	<h4 class="mb-4"><jsp:getProperty name="myMsg" property="action" /></h4>
        
        <form action="./RegAppl.jsp" method="get">
          <div class="mb-3">
            <label for="regFirstName" class="form-label">Vorname</label>
            <input type="text" name="vorname" class="form-control" id="regFirstName" required>
          </div>

          <div class="mb-3">
            <label for="regLastName" class="form-label">Nachname</label>
            <input type="text" name="nachname" class="form-control" id="regLastName" required>
          </div>

          <div class="mb-3">
            <label for="exampleInputEmail1" class="form-label">Email address</label>
            <input type="email" name="email" class="form-control" id="exampleInputEmail1" required>
          </div>

          <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">Password</label>
            <input type="password" name="passwort" class="form-control" id="exampleInputPassword1" required>
          </div>

          <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="exampleCheck1" required>
            <label class="form-check-label" for="exampleCheck1">Ich akzeptiere die Nutzungsbedingungen</label>
          </div>

          <div class="d-grid">
            <input class="btn btn-primary" type="submit" name="action" value="Registrieren"/>
          </div>
          
          <br>
          <p class="mt-3">
          	Schon Registriert? Melden sie sich <a href="./RegAppl.jsp?action=zumLogin">hier</a> an
          </p>
        </form>
      </div>
    </div>
  </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>