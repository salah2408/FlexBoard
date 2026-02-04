<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Flexboard - Posteingang</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="../css/Nachrichten.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>	
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />

	<jsp:getProperty property="navbarHtml" name="myAccount"/>
	

<div class="container-fluid chat-container">
  <div class="row h-100">
    <div class="col-12 col-md-4 col-lg-3 chat-list p-0">
      <div class="list-group list-group-flush">
        <a href="#" class="list-group-item list-group-item-action active">
          Max Mustermann
        </a>
        <a href="#" class="list-group-item list-group-item-action">
          Anna Schmidt
        </a>
        <a href="#" class="list-group-item list-group-item-action">
          Peter Müller
        </a>
        <a href="#" class="list-group-item list-group-item-action">
          Julia Weber
        </a>
      </div>
    </div>


    <div class="col-12 col-md-8 col-lg-9 d-flex flex-column p-0">

      <div class="border-bottom p-3 fw-bold">
        Max Mustermann
      </div>

      <div class="chat-messages flex-grow-1">
        <div class="bg-light rounded p-2 mb-2 message-left">
          Hallo! Wie geht’s?
        </div>

        <div class="bg-primary text-white rounded p-2 mb-2 message-right">
          Alles gut 😊 und bei dir?
        </div>

        <div class="bg-light rounded p-2 mb-2 message-left">
          Super! Hast du kurz Zeit?
        </div>
      </div>

      <div class="border-top p-3">
        <div class="input-group">
          <form action="./NachrichtenAppl.jsp" method="get">
          	<input class="form-control" type="text" name="text" value="" placeholder="Nachricht schreiben...">
          	<input class="btn btn-primary" type="submit" name="action" value="Senden">
          </form> 
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>