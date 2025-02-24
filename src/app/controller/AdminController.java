package app.controller;

import java.sql.Connection;

import app.db.Connexion;
import app.model.Admin;
import app.service.AdminService;
import mg.itu.prom16.Controller;
import mg.itu.prom16.Get;
import mg.itu.prom16.ModelView;
import mg.itu.prom16.MySession;
import mg.itu.prom16.Post;
import mg.itu.prom16.ReqBody;
import mg.itu.prom16.URL;
import mg.itu.prom16.authorization.Role;
import mg.itu.prom16.validation.annotation.Validate;

@Controller
public class AdminController 
{
    MySession session;
    
    @Post
    @URL(url = "/admin/login")
    public ModelView login(@ReqBody Admin admin) throws Exception
    {
        try(Connection connection = Connexion.getConnexion()){
            boolean isAuthenticated = AdminService.isValidAdmin(connection, admin);
            if (isAuthenticated) {
                ModelView mv = new ModelView("view/admin/home.jsp");
                session.add("isAuthenticated", Boolean.TRUE);
                session.add("userRole", new String[]{Role.ADMIN});
                return mv;
            }
        }
        ModelView mv = new ModelView("view/admin/login.jsp");
        mv.addObject("error", "Identifiant ou Mot de passe incorrect");
        return mv;
    }

    @Get
    @URL(url = "/base/admin")
    public ModelView adminLogin()
    {
        return new ModelView("view/admin/login.jsp");
    }
}