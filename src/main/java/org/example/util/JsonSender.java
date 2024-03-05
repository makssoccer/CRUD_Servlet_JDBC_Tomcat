package org.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonSender {
    public static final Gson gson = new GsonBuilder().create();

    public static void sendAsJson(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(obj));
        out.flush();
    }
}
