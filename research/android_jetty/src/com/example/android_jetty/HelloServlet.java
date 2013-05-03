package com.example.android_jetty;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: kimukou.buzz
 * Date: 2013/05/03
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);    //To change body of overridden methods use File | Settings | File Templates.


        PrintWriter out = resp.getWriter();
        out.println("<HTML>");
        out.println("<BODY>");
        out.println("<H3>Hello World!</H3>");
        out.println("</BODY>");
        out.println("</HTML>");
    }
}
