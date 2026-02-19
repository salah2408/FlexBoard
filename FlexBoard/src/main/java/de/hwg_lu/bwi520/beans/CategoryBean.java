package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;
import de.hwg_lu.bwi520.classes.Category;

public class CategoryBean {
	Vector<Category> allCategories;
    Connection dbConn;

    public CategoryBean() throws ClassNotFoundException, SQLException {
        this.allCategories = new Vector<Category>();
        this.dbConn = new PostgreSQLAccess().getConnection();

        this.readAllCategoriesFromDB();
}
    
    
    
	// Abschnitt Kategorien
    
    
    
    public void readAllCategoriesFromDB() throws SQLException {
        this.allCategories.clear();

        String sql = "SELECT id, name, description FROM category ORDER BY name";
        PreparedStatement prep = this.dbConn.prepareStatement(sql);

        ResultSet dbRes = prep.executeQuery();
        while (dbRes.next()) {

            long id = dbRes.getLong("id");
            String name = dbRes.getString("name");
            String description = dbRes.getString("description");

            this.allCategories.add(
                new Category(id, name, description)
            );
        }
    }
    
 // Abschnitt getHtml
    
    public String getCategoriesDropdownHtml() {

        String html = "";

        html += "<label class='form-label'>Kategorie</label>";
        html += "<select id='category' name='catid' class='form-control' required onchange='getCategory()'>";
        html += "<option value=''>Bitte auswählen</option>";

        for (Category cat : this.allCategories) {
            html += "<option value='" + cat.getId() + "'>"
                    + cat.getName()
                    + "</option>";
        }

        html += "</select>";

        return html;
    }

    
    
    
    
 // Abschnitt Getter und Setter
    
    public Vector<Category> getAllCategories() {
        return this.allCategories;
    }
    
    
    
    
}

