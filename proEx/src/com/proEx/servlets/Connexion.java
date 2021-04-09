package com.proEx.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.proEx.beans.Utilisateur;
import com.proEx.forms.ConnexionForm;

/**
 * Servlet implementation class Connexion
 */
// @WebServlet( "/Connexion" )
public class Connexion extends HttpServlet {
    private static final long  serialVersionUID          = 1L;
    public static final String ATT_USER                  = "utilisateur";
    public static final String ATT_FORM                  = "form";
    public static final String ATT_SESSION_USER          = "sessionUtilisateur";
    public static final String COOKIE_DERNIERE_CONNEXION = "derniereConnexion";
    public static final String FORMAT_DATE               = "dd/MM/yyyy HH:mm:ss";
    public static final String ATT_INTERVALLE_CONNEXIONS = "intervalleConnexions";
    public static final String VUE                       = "/WEB-INF/Connexion.jsp";

    public static final String CHAMP_MEMOIRE             = "memoire";
    public static final int    COOKIE_MAX_AGE            = 60 * 60 * 24 * 365;      // 1an

    public Connexion() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        String derniereConnexion = getCookieValue( request, COOKIE_DERNIERE_CONNEXION );
        if ( derniereConnexion != null ) {
            // date courante
            DateTime dtCourante = new DateTime();
            // Date Derniere Connexion
            DateTimeFormatter formatter = DateTimeFormat.forPattern( FORMAT_DATE );
            DateTime dtDerniConn = formatter.parseDateTime( derniereConnexion );

            Period periode = new Period( dtDerniConn, dtCourante );
            // Calcul Duree Periode
            PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                    .appendYears().appendSuffix( " an ", " ans " )
                    .appendMonths().appendSuffix( " mois " )
                    .appendDays().appendSuffix( " jour ", " jours " )
                    .appendHours().appendSuffix( " heure ", " heures " )
                    .appendMinutes().appendSuffix( " minute ", " minutes " )
                    .appendSeparator( "et " )
                    .appendSeconds().appendSuffix( " seconde", " secondes" )
                    .toFormatter();
            String intervalConn = periodFormatter.print( periode );

            request.setAttribute( ATT_INTERVALLE_CONNEXIONS, intervalConn );
        }
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

    /**
     * Méthode utilitaire gérant la récupération de la valeur d'un cookie donné
     * depuis la requête HTTP.
     */
    private String getCookieValue( HttpServletRequest request, String nom ) {

        Cookie[] cookies = request.getCookies();
        if ( cookies != null ) {
            for ( Cookie cookie : cookies ) {
                if ( cookie != null && nom.equals( cookie.getName() ) ) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        ConnexionForm form = new ConnexionForm();

        /*
         * Appel au traitement et à la validation de la requête, et récupération
         * du bean en résultant
         */
        Utilisateur utilisateur = form.connecterUtilisateur( request );

        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();

        /**
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        if ( form.getErreurs().isEmpty() ) {
            session.setAttribute( ATT_SESSION_USER, utilisateur );
        } else {
            session.setAttribute( ATT_SESSION_USER, null );
        }

        if ( request.getParameter( CHAMP_MEMOIRE ) != null ) {
            /* Récupération de la date courante */
            DateTime dt = new DateTime();
            /* Formatage de la date et conversion en texte */
            DateTimeFormatter formatter = DateTimeFormat.forPattern( FORMAT_DATE );
            String dateDerniereConnexion = dt.toString( formatter );
            /* Création du cookie, et ajout à la réponse HTTP */
            setCookie( response, COOKIE_DERNIERE_CONNEXION, dateDerniereConnexion, COOKIE_MAX_AGE );
        } else {
            /* Demande de suppression du cookie du navigateur */
            setCookie( response, COOKIE_DERNIERE_CONNEXION, "", 0 );
        }

        // Stockage du formulaire et du bean dans l'objet request
        request.setAttribute( ATT_USER, utilisateur );
        request.setAttribute( ATT_FORM, form );

        // Transmission vers la page JSP
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

    /*
     * Méthode utilitaire gérant la création d'un cookie et son ajout à la
     * réponse HTTP.
     */
    private static void setCookie( HttpServletResponse response, String nom, String value, int maxAge )
            throws IOException {
        Cookie cookie = new Cookie( nom, value );
        cookie.setMaxAge( maxAge );
        response.addCookie( cookie );
    }

}
