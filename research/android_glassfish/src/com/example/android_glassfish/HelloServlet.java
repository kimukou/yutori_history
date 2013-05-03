package com.example.android_glassfish;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    @EJB
    HelloService service;

    @Resource
    UserTransaction userTransaction;
/*
    @Resource(name = "jdbc/gftest")
    DataSource dataSource;
*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // UserTransactionもDataSourceも取って来れてる様子
        System.out.println(userTransaction);
        //System.out.println(dataSource);

        resp.setContentType(MediaType.TEXT_PLAIN);
        String name = req.getParameter("name");
        if (name == null) {
            name = "Guest";
        }
        String said = service.say(name);
        resp.getWriter().print(said);
    }
}
