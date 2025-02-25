package app.controller;

import java.sql.Connection;
import java.util.List;

import app.db.Connexion;
import app.model.Admin;
import app.model.Vol;
import app.model.VolDetails;
import app.service.AdminService;
import app.service.AvionService;
import app.service.TypeSiegeService;
import app.service.VilleService;
import app.service.VolService;
import mg.itu.prom16.Controller;
import mg.itu.prom16.Get;
import mg.itu.prom16.ModelView;
import mg.itu.prom16.MySession;
import mg.itu.prom16.Post;
import mg.itu.prom16.ReqBody;
import mg.itu.prom16.ReqParam;
import mg.itu.prom16.URL;
import mg.itu.prom16.authorization.Role;
import mg.itu.prom16.authorization.annotation.AuthorizedRoles;
import mg.itu.prom16.authorization.annotation.LoginRequired;

@Controller
public class AdminController 
{
    MySession session;
    
    @Post
    @URL(url = "/admin/login")
    public ModelView login(@ReqBody Admin admin) throws Exception
    {
        if (admin != null) {
            System.out.println(admin.getUsername());
            System.out.println(admin.getPassword());
        }
        try(Connection connection = Connexion.getConnexion()){
            boolean isAuthenticated = AdminService.isValidAdmin(connection, admin);
            if (isAuthenticated) {
                ModelView mv = new ModelView("view/admin/home.jsp");
                session.add("isAuthenticated", Boolean.TRUE);
                session.add("userRole", new String[]{Role.ADMIN});
                mv.addObject("vols", VolService.getAllVol(connection));
                mv.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
                mv.addObject("avions", AvionService.getAllAvions(connection));
                mv.addObject("villes", VilleService.getAllVilles(connection));
                return mv;
            }
        }
        ModelView mv = new ModelView("view/admin/login.jsp");
        mv.addObject("error", "Identifiant ou Mot de passe incorrect");
        return mv;
    }

    @Get
    @URL(url = "/admin/vol/prepareInsert")
    @LoginRequired
    @AuthorizedRoles(roles = {Role.ADMIN})
    public ModelView prepareInsertVol() throws Exception
    {
        ModelView mv = new ModelView("view/admin/addVol.jsp");
        try(Connection connection = Connexion.getConnexion()){
            mv.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
            mv.addObject("avions", AvionService.getAllAvions(connection));
            mv.addObject("villes", VilleService.getAllVilles(connection));
        }
        return mv;
    }

    @Post
    @URL(url = "/admin/vol/insert")
    @LoginRequired
    @AuthorizedRoles(roles = {Role.ADMIN})
    public ModelView insertVol(@ReqBody Vol vol , @ReqBody List<VolDetails> volDetails) throws Exception
    {
        try(Connection connection = Connexion.getConnexion()){
            try {
                ModelView mv = new ModelView("view/admin/home.jsp");
                VolService.insertVolWithDetails(connection, vol, volDetails);
                mv.addObject("vols", VolService.getAllVol(connection));
                return mv;
            } catch (Exception e) {
                e.printStackTrace();
                ModelView modelView = new ModelView("view/admin/addVol.jsp");
                modelView.addObject("error", "Erreur lors de l'insertion");
                modelView.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
                modelView.addObject("avions", AvionService.getAllAvions(connection));
                modelView.addObject("villes", VilleService.getAllVilles(connection));
                return modelView;
            }
        } 
    }

    @Get
    @URL(url = "/admin/vol/prepareUpdate")
    @LoginRequired
    @AuthorizedRoles(roles = {Role.ADMIN})
    public ModelView prepareUpdateVol(@ReqParam int idVol) throws Exception
    {
        try(Connection connection = Connexion.getConnexion()){
            ModelView mv = new ModelView("view/admin/updateVol.jsp");
            mv.addObject("vol", VolService.getVolById(connection, idVol));
            mv.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
            mv.addObject("avions", AvionService.getAllAvions(connection));
            mv.addObject("villes", VilleService.getAllVilles(connection));
            return mv;
        }
    }

    @Post
    @URL(url = "/admin/vol/update")
    @LoginRequired
    @AuthorizedRoles(roles = {Role.ADMIN})
    public ModelView updateVol(@ReqBody Vol vol) throws Exception
    {
        try(Connection connection = Connexion.getConnexion()){
            try {
                ModelView mv = new ModelView("view/admin/home.jsp");
                VolService.updateVol(connection, vol);
                mv.addObject("vols", VolService.getAllVol(connection));
                return mv;
            } catch (Exception e) {
                e.printStackTrace();
                ModelView modelView = new ModelView("view/admin/updateVol.jsp");
                modelView.addObject("error", "Erreur lors de la mise a jour");
                modelView.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
                modelView.addObject("avions", AvionService.getAllAvions(connection));
                modelView.addObject("villes", VilleService.getAllVilles(connection));
                return modelView;
            }
        } 
    }

    @Post
    @URL(url = "/admin/vol/search")
    @LoginRequired
    @AuthorizedRoles(roles = {Role.ADMIN})
    public ModelView searchVol(@ReqBody Vol vol) throws Exception
    {
        try(Connection connection = Connexion.getConnexion()){
            ModelView mv = new ModelView("view/admin/home.jsp");
            mv.addObject("vols", VolService.searchVols(connection, vol));
            mv.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
            mv.addObject("avions", AvionService.getAllAvions(connection));
            mv.addObject("villes", VilleService.getAllVilles(connection));
            return mv;
        }
    }
}