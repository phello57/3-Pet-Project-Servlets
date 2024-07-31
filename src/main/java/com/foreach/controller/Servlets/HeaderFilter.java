package com.foreach.controller.Servlets;

import com.foreach.Settings;
import com.foreach.controller.Validate;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;


@WebFilter(urlPatterns = {"/currency", "/rate", "/rates", "/currencies", "/exchange"})
public class HeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        JSONObject retJson = new JSONObject();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            httpResponse.setStatus(Settings.HTTP_SERVER_ERROR);
            retJson.put("message", Validate.errorTextDriverFall);
            return;
        }
        chain.doFilter(request, httpResponse);

    }
}
