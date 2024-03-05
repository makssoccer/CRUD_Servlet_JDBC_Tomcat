package org.example.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        urlPatterns = "/"
)
public class MainServlet extends HttpServlet {
    private static String pathInfo;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            PrintWriter out = response.getWriter();
            out.print("Hello Kitty");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



}