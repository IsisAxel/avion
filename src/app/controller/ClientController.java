package app.controller;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

import app.db.Connexion;
import app.model.DetailReservation;
import app.model.Reservation;
import app.model.TypeSiege;
import app.model.Vol;
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
public class ClientController 
{
    MySession session;
    
    @Get
    @URL(url = "/client/vol/all")
    public ModelView getAllVol() throws Exception
    {
        try(Connection connection = Connexion.getConnexion()){
            ModelView mv = new ModelView("view/client/home.jsp");
            mv.addObject("vols", VolService.getAllVol(connection));
            mv.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
            mv.addObject("avions", AvionService.getAllAvions(connection));
            mv.addObject("villes", VilleService.getAllVilles(connection));
            return mv;
        }
    }

    @Post
    @URL(url = "/client/vol/search")
    public ModelView searchVol(@ReqBody Vol vol) throws Exception
    {
        try(Connection connection = Connexion.getConnexion()){
            ModelView mv = new ModelView("view/client/home.jsp");
            mv.addObject("vols", VolService.searchVols(connection, vol));
            mv.addObject("type_sieges", TypeSiegeService.getAllTypeSiege(connection));
            mv.addObject("avions", AvionService.getAllAvions(connection));
            mv.addObject("villes", VilleService.getAllVilles(connection));
            return mv;
        }
    }

    @Get
    @URL(url = "/client/vol/prepareReservation")
    public ModelView prepareReserveVol(@ReqParam int idVol) throws Exception {
        try (Connection connection = Connexion.getConnexion()) {
            ModelView mv = new ModelView("view/client/reservation.jsp");
            Vol vol = VolService.getVolById(connection, idVol);
            List<TypeSiege> typesSiege = TypeSiegeService.getAllTypeSiege(connection);
            mv.addObject("vol", vol);
            mv.addObject("type_sieges", typesSiege);
            return mv;
        }
    }

    @Post
    @URL(url = "/client/vol/reserve")
    public ModelView reserveVol(@ReqBody Reservation reservation, @ReqBody List<DetailReservation> detailReservations) throws Exception {
        try (Connection connection = Connexion.getConnexion()) {
            ModelView mv = new ModelView("view/client/home.jsp");
            reservation.setNombrePersonnes(detailReservations.size());
            List<TypeSiege> typesSiege = TypeSiegeService.getAllTypeSiege(connection);
            Vol vol = VolService.getVolById(connection, reservation.getIdVol());
            for (DetailReservation detailReservation : detailReservations) {
                double prix = vol.getVolDetailsByIdTypeSiege(detailReservation.getTypeSiege().getIdTypeSiege()).getPrix();
                detailReservation.setPrix(prix);
            }
            reservation.setMontantTotal(detailReservations.stream().mapToDouble(DetailReservation::getPrix).sum());
            boolean success = VolService.reserveVol(connection, reservation, detailReservations);
            if (success) {
                mv.addObject("message", "Reservation reussie");
            } else {
                mv.addObject("error", "echec de la reservation");
            }
            mv.addObject("vols", VolService.getAllVol(connection));
            mv.addObject("type_sieges", typesSiege);
            mv.addObject("avions", AvionService.getAllAvions(connection));
            mv.addObject("villes", VilleService.getAllVilles(connection));
            return mv;
        }
    }

    @Get
    @URL(url = "/client/reservation/all")
    public ModelView listReservations() throws Exception {
        try (Connection connection = Connexion.getConnexion()) {
            ModelView mv = new ModelView("view/client/listReservation.jsp");
            List<Reservation> reservations = VolService.getAllReservation(connection);
            mv.addObject("reservations", reservations);
            return mv;
        }
    }

    @Get
    @URL(url = "/client/reservation/prepareCancel")
    public ModelView prepareCancelReservation(@ReqParam int idReservation) throws Exception {
        try (Connection connection = Connexion.getConnexion()) {
            ModelView mv = new ModelView("view/client/cancelReservation.jsp");
            Reservation reservation = VolService.getReservationById(connection, idReservation);
            mv.addObject("reservation", reservation);
            return mv;
        }
    }

    @Post
    @URL(url = "/client/reservation/cancel")
    public ModelView cancelReservation(@ReqParam int idReservation, @ReqBody List<Integer> detailsToCancel) throws Exception {
        System.out.println("Andrana a " + detailsToCancel.size());
        try (Connection connection = Connexion.getConnexion()) {
            VolService.cancelReservationDetails(connection, idReservation, detailsToCancel);
            ModelView mv = new ModelView("view/client/listReservation.jsp");
            List<Reservation> reservations = VolService.getAllReservation(connection);
            mv.addObject("reservations", reservations);
            return mv;
        }
    }
}