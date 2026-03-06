<%@ page import="de.hwg_lu.bwi520.beans.AccountBean" %>
<%
    // Cache aus
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // Sicherheitsnetz gegen Loop:
    String uri = request.getRequestURI();

    // Diese Seiten dürfen IMMER ohne Login erreichbar sein
    boolean isPublic =
           uri.endsWith("/LoginView.jsp")
        || uri.endsWith("/RegView.jsp")
        || uri.endsWith("/HomepageView.jsp")
        || uri.endsWith("/NavbarAppl.jsp")
        || uri.endsWith("/SucheView.jsp")
        || uri.endsWith("/SucheAppl.jsp");

    if (isPublic) {
        return;
    }

    AccountBean acc = (AccountBean) session.getAttribute("myAccount");
    boolean loggedIn = (acc != null && acc.getLogedIn());

    if (!loggedIn) {
        response.sendRedirect(request.getContextPath() + "/jsp/LoginView.jsp");
        return;
    }
%>