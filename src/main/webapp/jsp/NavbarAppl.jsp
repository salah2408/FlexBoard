<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="de.hwg_lu.bwi520.beans.AccountBean" %>
<%@ page import="de.hwg_lu.bwi520.beans.WeiterleitungsBean" %>
<%@ page import="de.hwg_lu.bwi520.beans.ListingBean" %>
<%@ page import="de.hwg_lu.bwi520.beans.CategoryBean" %>

<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />
<jsp:useBean id="categoryBean" class="de.hwg_lu.bwi520.beans.CategoryBean" scope="session"/>

<%
    // Wichtig gegen "Zurück"-Cache nach Logout
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String action = request.getParameter("action");
    if (action == null) action = "";

    String currSite = request.getParameter("currSite");
    if (currSite != null && !currSite.trim().isEmpty()) {
        myWeiter.setLink(currSite);
    }

    boolean loggedIn = (myAccount != null && myAccount.getLogedIn());

    // Geschützte Aktionen (nur eingeloggt)
    boolean requiresLogin =
           "zumInserieren".equals(action)
        || "zurPost".equals(action)
        || "zurMeineInserate".equals(action)
        || "bearbeiteListing".equals(action)
        || "deaktiviereListing".equals(action)
        || "aktiviereListing".equals(action)
        || "loescheListing".equals(action);

    if (requiresLogin && !loggedIn) {
        String ziel = "./HomepageView.jsp";

        if ("zumInserieren".equals(action)) {
            ziel = "./InserierenView.jsp";
        } else if ("zurPost".equals(action)) {
            ziel = "./NachrichtenView.jsp";
        } else if ("zurMeineInserate".equals(action)
                || "bearbeiteListing".equals(action)
                || "deaktiviereListing".equals(action)
                || "aktiviereListing".equals(action)
                || "loescheListing".equals(action)) {
            ziel = "./MeineInserateView.jsp";
        }

        myWeiter.setLink(ziel);
        response.sendRedirect("./LoginView.jsp");
        return;
    }

    // ===== Öffentliche Actions =====
    if ("zurHomepage".equals(action)) {
        response.sendRedirect("./HomepageView.jsp");
        return;

    } else if ("zumLogin".equals(action)) {
        response.sendRedirect("./LoginView.jsp");
        return;

    } else if ("zurReg".equals(action)) {
        response.sendRedirect("./RegView.jsp");
        return;

    } else if ("zurSuche".equals(action)) {
        String q = request.getParameter("q");
        if (q == null) q = "";
        response.sendRedirect("./SucheAppl.jsp?action=Finden&q=" + URLEncoder.encode(q, "UTF-8"));
        return;

    } else if ("abmelden".equals(action)) {
        try {
            myAccount.abmelden();
        } catch (Exception e) {
            // optional: loggen
        }

        // Session komplett beenden
        session.invalidate();

        // No-Cache nochmal setzen
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.sendRedirect("./HomepageView.jsp");
        return;
    }

    // ===== Geschützte Actions =====
    else if ("zumInserieren".equals(action)) {
        // Neuer Eintrag => Edit-Mode zurücksetzen
        listingBean.resetEditMode();
        response.sendRedirect("./InserierenView.jsp");
        return;

    } else if ("zurPost".equals(action)) {
        myAccount.readAlleNachrichtenFromDB();
        response.sendRedirect("./NachrichtenView.jsp");
        return;

    } else if ("zurMeineInserate".equals(action)) {
        response.sendRedirect("./MeineInserateView.jsp");
        return;

    } else if ("zumListing".equals(action)) {
        String idStr = request.getParameter("id");
        try {
            int listingId = Integer.parseInt(idStr);
            listingBean.setAktListingId(listingId);
            listingBean.setAccount(myAccount);
            response.sendRedirect("./InseratDetailView.jsp");
            return;
        } catch (Exception e) {
            response.sendRedirect("./HomepageView.jsp");
            return;
        }

    } else if ("bearbeiteListing".equals(action)) {
        String idStr = request.getParameter("id");
        try {
            int listingId = Integer.parseInt(idStr);

            listingBean.setAccount(myAccount);
            boolean ok = listingBean.loadListingForEdit(listingId, myAccount.getEmail());

            if (ok) {
                categoryBean.setListingBean(listingBean);
                categoryBean.setSelectedCategoryId(listingBean.getEditCatId());
                response.sendRedirect("./InserierenView.jsp");
            } else {
                response.sendRedirect("./MeineInserateView.jsp");
            }
            return;
        } catch (Exception e) {
            response.sendRedirect("./MeineInserateView.jsp");
            return;
        }

    } else if ("deaktiviereListing".equals(action)) {
        String idStr = request.getParameter("id");
        try {
            int listingId = Integer.parseInt(idStr);
            listingBean.setAccount(myAccount);
            listingBean.setAktListingId(listingId);
            listingBean.deaktiviereListing();
        } catch (Exception e) {
            // optional: loggen
        }
        response.sendRedirect("./MeineInserateView.jsp");
        return;

    } else if ("aktiviereListing".equals(action)) {
        String idStr = request.getParameter("id");
        try {
            int listingId = Integer.parseInt(idStr);
            listingBean.setAccount(myAccount);
            listingBean.setAktListingId(listingId);
            listingBean.aktiviereListing();
        } catch (Exception e) {
            // optional: loggen
        }
        response.sendRedirect("./MeineInserateView.jsp");
        return;

    } else if ("loescheListing".equals(action)) {
        String idStr = request.getParameter("id");
        try {
            int listingId = Integer.parseInt(idStr);
            listingBean.setAccount(myAccount);
            listingBean.setAktListingId(listingId);
            listingBean.loescheListing();
        } catch (Exception e) {
            // optional: loggen
        }
        response.sendRedirect("./MeineInserateView.jsp");
        return;
    }

    // Fallback
    response.sendRedirect("./HomepageView.jsp");
    return;
%>