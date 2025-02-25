package app.controller;

import mg.itu.prom16.Controller;
import mg.itu.prom16.Get;
import mg.itu.prom16.ModelView;
import mg.itu.prom16.URL;

@Controller
public class BaseController 
{
    @Get
    @URL(url = "/base/index")
    public ModelView index()
    {
        return new ModelView("view/index.jsp");
    }

    @Get
    @URL(url = "/base/admin")
    public ModelView adminLogin()
    {
        return new ModelView("view/admin/login.jsp");
    }
}