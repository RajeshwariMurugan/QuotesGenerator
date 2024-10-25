package com;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper; // Jackson for parsing JSON

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@jakarta.servlet.annotation.WebServlet("/quote")  // URL pattern for the servlet
public class QuotesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static List<Quotes> quotesList = null;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Received POST request from client.");
        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "Guest";
        }
        
        if (quotesList == null) {
            loadQuotes(); // Load the quotes from the JSON file
        }

        if (quotesList == null || quotesList.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: No quotes available.");
            return;
        }

        // Select a random quote
        // Select a random quote
     // Select a random quote
        Random random = new Random();
        Quotes randomQuote = quotesList.get(random.nextInt(quotesList.size()));

        // Redirect to the new HTML page and pass the quote data as query parameters
        response.sendRedirect("quoteResult.html?name=" + URLEncoder.encode(escapeHtml(name), "UTF-8")
                            + "&quote=" + URLEncoder.encode(escapeHtml(randomQuote.getQuote()), "UTF-8")
                            + "&author=" + URLEncoder.encode(escapeHtml(randomQuote.getAuthor()), "UTF-8"));

    }

    // Helper method to load quotes from the JSON file
    private void loadQuotes() throws IOException {
        // Loading the file from the /WEB-INF/classes/Quote.json or /src/main/resources
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/resources/Quote.json")) {
            if (inputStream == null) {
                throw new IOException("JSON file not found.");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            quotesList = objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Quotes.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Escapes HTML to prevent XSS attacks
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
