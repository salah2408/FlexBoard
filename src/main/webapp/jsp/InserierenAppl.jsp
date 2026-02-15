<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>NavbarAppl</title>
</head>
<body>

<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

<%
    String action = request.getParameter("action");
    if(action == null) action = "";

    // =========================================
    // 1) SAVE LISTING (kommt VOR den Redirect-Fällen!)
    // =========================================
    if(action.equals("saveListing")) {

        // Wenn nicht eingeloggt -> erst Login, danach weiterleiten zur Inserieren-Seite
        if(!myAccount.getLogedIn()) {
            myWeiter.setLink("./InserierenView.jsp");
            response.sendRedirect("./LoginView.jsp");
            return;
        }

        String title = request.getParameter("title");
        String descr = request.getParameter("descr");

        int catid = Integer.parseInt(request.getParameter("catid"));
        int price = Integer.parseInt(request.getParameter("price"));

        String zip = request.getParameter("zip");
        String city = request.getParameter("city");

        // userid ist bei euch die Email vom eingeloggten User
        String userid = myAccount.getEmail();

        boolean ok = listingBean.saveListing(userid, catid, title, descr, price, zip, city);

        if(ok) {
            response.sendRedirect("./HomepageView.jsp");
        } else {
            response.sendRedirect("./InserierenView.jsp");
        }
        return;
    }

    // =========================================
    // 2) NAVIGATION / WEITERLEITUNG
    // =========================================
    if(action.equals("zurHomepage")) {
        response.sendRedirect("./HomepageView.jsp");
        return;
    }
    else if(action.equals("zumLogin")) {
        response.sendRedirect("./LoginView.jsp");
        return;
    }
    else if(action.equals("zurReg")) {
        response.sendRedirect("./RegView.jsp");
        return;
    }
    else if(action.equals("zumInserieren")) {
        if(myAccount.getLogedIn()) {
            response.sendRedirect("./InserierenView.jsp");
        } else {
            myWeiter.setLink("./InserierenView.jsp");
            response.sendRedirect("./LoginView.jsp");
        }
        return;
    }
    else if(action.equals("zurSuche")) {
        response.sendRedirect("./SucheView.jsp");
        return;
    }
    else if(action.equals("zurPost")) {
        if(myAccount.getLogedIn()) {
            response.sendRedirect("./NachrichtenView.jsp");
        } else {
            myWeiter.setLink("./NachrichtenView.jsp");
            response.sendRedirect("./LoginView.jsp");
        }
        return;
    }
    else {
        response.sendRedirect("./HomepageView.jsp");
        return;
    }
%>

</body>
</html>