package de.randi2.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.randi2.datenbank.DatenbankFactory;
import de.randi2.datenbank.exceptions.DatenbankExceptions;
import de.randi2.model.exceptions.BenutzerException;
import de.randi2.model.exceptions.PersonException;
import de.randi2.model.exceptions.ZentrumException;
import de.randi2.model.fachklassen.Zentrum;
import de.randi2.model.fachklassen.beans.BenutzerkontoBean;
import de.randi2.model.fachklassen.beans.PersonBean;
import de.randi2.model.fachklassen.beans.ZentrumBean;
import de.randi2.utility.Jsp;
import de.randi2.utility.KryptoUtil;
import de.randi2.utility.Parameter;

/**
 * Diese Klasse repraesentiert das ZENTRUMSERVLET, welches Aktionen an die
 * Zentrum-Fachklasse und an den DISPATCHER weiterleitet.
 * 
 * @version $Id: ZentrumServlet.java 2418 2007-05-04 14:37:12Z jthoenes $
 * @author Andreas Freudling [afreudling@hs-heilbronn.de]
 * 
 */
public class ZentrumServlet extends javax.servlet.http.HttpServlet {

	/**
	 * Default Serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Die Anfrage_id zur Verwendung im Dispatcher Servlet
	 */
	public enum anfrage_id {

		/**
		 * Schritt, waehrend dem die Liste der Zentren geholt wird.
		 */
		CLASS_DISPATCHERSERVLET_BENUTZER_REGISTRIEREN_ZWEI,

		/**
		 * Pruefung des Zentrumpassworts bei Benutzerregistierung
		 */
		CLASS_DISPATCHERSERVLET_BENUTZER_REGISTRIEREN_DREI,

		/**
		 * Admin legt ein neues Zentrum an
		 */
		ClASS_DISPATCHERSERVLET_ZENTRUM_ANLEGEN,

		/**
		 * Zentrumsdaten aendern
		 */
		ZENTRUM_AENDERN
	}

	/**
	 * Konstruktor.
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ZentrumServlet() {
		super();
	}

	/**
	 * Diese Methode nimmt HTTP-POST-Request gemaess HTTP-Servlet Definition
	 * entgegen. Hier werden Anfragen verarbeitet, die Zentren betreffen.
	 * 
	 * @param request
	 *            Der Request fuer das Servlet.
	 * @param response
	 *            Der Response Servlet.
	 * @throws IOException
	 *             Falls Fehler in den E/A-Verarbeitung.
	 * @throws ServletException
	 *             Falls Fehler in der HTTP-Verarbeitung auftreten.
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = (String) request.getParameter("anfrage_id");
		String idAttribute = (String) request.getAttribute("anfrage_id");
		if (idAttribute != null) {
			id = idAttribute;
		}
		Logger.getLogger(this.getClass()).debug(id);

		// Benutzer registrieren
		// Schritt 2.1
		if (id.equals(ZentrumServlet.anfrage_id.CLASS_DISPATCHERSERVLET_BENUTZER_REGISTRIEREN_ZWEI.name())) {
			this.classDispatcherservletBenutzerRegistrierenZwei(request, response);

		}
		// Schritt 3.1: ZENTRUMAUSWAHL: Filterung
		// Schritt 3.2 ZENTRUMAUSWAHL->BENUTZERDATEN_EINGEBEN
		else if (id.equals(ZentrumServlet.anfrage_id.CLASS_DISPATCHERSERVLET_BENUTZER_REGISTRIEREN_DREI.name())) {
			this.classDispatcherservletBenutzerRegistrierenDrei(request, response);
		}
		// Neues Zentrum anlegen
		else if (id.equals(anfrage_id.ClASS_DISPATCHERSERVLET_ZENTRUM_ANLEGEN.name())) {
			this.classDispatcherservletZentrumAnlegen(request, response);

		} else if (id.equals(ZentrumServlet.anfrage_id.ZENTRUM_AENDERN.name())) {
			aendernZentrum(request, response);
		} else {
			// TODO Hier muss noch entschieden werden,was passiert
		}
	}

	/**
	 * Setzt aenderbare Daten eines vorhandenen Zentrums neu.
	 * 
	 * @param request
	 *            Requestobjekt
	 * @param response
	 *            Responseobjekt
	 * @throws IOException
	 *             Fehler bei E/A
	 * @throws ServletException
	 *             Fehler bei HTTP
	 */
	private void aendernZentrum(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Alle aenderbaren Attribute des request inititalisieren
		String institution = request.getParameter("Institution");
		String abteilung = request.getParameter("Abteilung");
		String ort = request.getParameter("Ort");
		String plz = request.getParameter("PLZ");
		String strasse = request.getParameter("Strasse");
		String hausnr = request.getParameter("Hausnummer");
		String nachnameA = request.getParameter("NachnameA");
		String vornameA = request.getParameter("VornameA");
		String telefonA = request.getParameter("TelefonA");
		String faxA = request.getParameter("FaxA");
		String emailA = request.getParameter("EmailA");
		// String geschlechtA = ...
		String passwort = null;

		// Wiederholte Passworteingabe pruefen
		if (request.getParameter("Passwort") != null && request.getParameter("Passwort_wh") != null) {
			if (request.getParameter("Passwort").equals(request.getParameter("Passwort_wh"))) {
				passwort = request.getParameter("Passwort");
			} else {
				passwort = "";
			}
		}

		BenutzerkontoBean aBenutzer = (BenutzerkontoBean) (request.getSession()).getAttribute("aBenutzer");
		try {
			ZentrumBean aZentrum = aBenutzer.getZentrum();
			try {
				aZentrum.setInstitution(institution);
				aZentrum.setAbteilung(abteilung);
				aZentrum.setPlz(plz);
				aZentrum.setOrt(ort);
				aZentrum.setStrasse(strasse);
				aZentrum.setHausnr(hausnr);
				if (aZentrum.getAnsprechpartner() != null) {
					aZentrum.getAnsprechpartner().setNachname(nachnameA);
					aZentrum.getAnsprechpartner().setVorname(vornameA);
					aZentrum.getAnsprechpartner().setTelefonnummer(telefonA);
					aZentrum.getAnsprechpartner().setFax(faxA);
					aZentrum.getAnsprechpartner().setEmail(emailA);
					// aZentrum.getAnsprechpartner().setGeschlecht(geschlechtA);
					DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(aZentrum.getAnsprechpartner());
				} else {
					PersonBean aPerson = new PersonBean();
					aPerson.setNachname(nachnameA);
					aPerson.setVorname(vornameA);
					aPerson.setTelefonnummer(telefonA);
					aPerson.setFax(faxA);
					aPerson.setEmail(emailA);
					aPerson.setGeschlecht('m');
					aPerson = DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(aPerson);
					aZentrum.setAnsprechpartnerId(aPerson.getId());
				}
				if (passwort != null) {
					if (!(passwort.trim().equals(""))) {
						String hash = KryptoUtil.getInstance().hashPasswort(passwort);
						aZentrum.setPasswort(hash);
					}
				}
			} catch (ZentrumException e) {
				request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());
			} catch (PersonException e) {
				request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());
			}
			DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(aZentrum);
			// TODO hier noch erfolgreich nachricht einfuegen twillert
			request.getRequestDispatcher("global_welcome.jsp").forward(request, response);
		} catch (DatenbankExceptions e) {
			request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());
		}
	}

	/**
	 * Methode ermittelt die Liste alle vorhandenen Zentren, setzt sie als das
	 * Attribut listeZentren, und zeigt diese dem Benutzer beim registrieren an.
	 * 
	 * @param request
	 *            Requestobjekt
	 * @param response
	 *            Responseobjekt
	 * @throws ServletException
	 *             Fehler in der Http-Verarbeitung
	 * @throws IOException
	 *             Fehler in der IO-Verarbaitung
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	private void classDispatcherservletBenutzerRegistrierenZwei(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Nach allen vorhandenen Zentren suchen
		// @Andy - wieder die gleiche Geschichte, mit der Konstante
		// (siehe
		// mein Kommentar in dem BenutzerServlet Zeile: 171)
		ZentrumBean sZentrum = new ZentrumBean();
		sZentrum.setIstAktiviert(true);
		sZentrum.setFilter(true);
		Vector<ZentrumBean> gZentrum = null;

		try {
			gZentrum = Zentrum.suchenZentrum(sZentrum);
		} catch (DatenbankExceptions e) {
			request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());
		}
		request.setAttribute("listeZentren", gZentrum);
		// Schritt 2.1.3
		request.getRequestDispatcher("/benutzer_anlegen_zwei.jsp").forward(request, response);
	}

	/**
	 * Methode wird aufgerufen um bei der Benutzerregistrierung nach Zentren zu
	 * filtern, bzw. das Zentrumpasswort zu ueberpruefen
	 * 
	 * @param request
	 *            Requestobjekt
	 * @param response
	 *            Responseobjekt
	 * @throws ServletException
	 *             Fehler in der Http-Verarbeitung
	 * @throws IOException
	 *             Fehler in der IO-Verarbaitung
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 *      HttpServletResponse response)
	 */
	private void classDispatcherservletBenutzerRegistrierenDrei(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Filterung
		if (((String) request.getParameter("Filtern")) != null) {
			try {
				Vector<ZentrumBean> gZentrum = null;
				if (((String) request.getParameter("name_institution")) != "" && ((String) request.getParameter("name_abteilung")) != "") {
					ZentrumBean sZentrum = new ZentrumBean();

					// Filter setzen
					sZentrum.setFilter(true);
					sZentrum.setInstitution(request.getParameter("name_institution"));
					sZentrum.setAbteilung(request.getParameter("name_abteilung"));
					sZentrum.setIstAktiviert(true);
					gZentrum = Zentrum.suchenZentrum(sZentrum);

				} else {
					ZentrumBean sZentrum = new ZentrumBean();
					sZentrum.setFilter(true);
					gZentrum = Zentrum.suchenZentrum(sZentrum);
				}
				request.setAttribute("listeZentren", gZentrum);
			} catch (BenutzerException e) {
				request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());
			} catch (DatenbankExceptions e) {
				request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());
			}

			request.getRequestDispatcher("/benutzer_anlegen_zwei.jsp").forward(request, response);
		} else {
			try {
				// Erstmal alle vorhandenen Zentren suchen
				ZentrumBean sZentrum = new ZentrumBean();
				sZentrum.setFilter(true);
				sZentrum.setIstAktiviert(true);
				Vector<ZentrumBean> gZentrum = null;

				gZentrum = Zentrum.suchenZentrum(sZentrum);

				Iterator<ZentrumBean> itgZentrum = gZentrum.iterator();
				while (itgZentrum.hasNext()) {
					ZentrumBean aZentrumBean = itgZentrum.next();
					String suche = "bestaetigen" + aZentrumBean.getId();
					if (request.getParameter(suche) != null) {
						Zentrum aZentrum = new Zentrum(aZentrumBean);
						// Zentrum Passwort richtig
						if (aZentrum.pruefenPasswort(request.getParameter("zentrum_passwort" + aZentrumBean.getId()))) {
							// Zentrum an die Session binden
							request.getSession()
									.setAttribute(DispatcherServlet.sessionParameter.ZENTRUM_BENUTZER_ANLEGEN.toString(), aZentrum.getZentrumBean());
							request.setAttribute("aZentrum", aZentrum.getZentrumBean().getId());
							request.getRequestDispatcher("/benutzer_anlegen_drei.jsp").forward(request, response);
						}
						// Zentrum Passwort falsch
						else {
							request.setAttribute("listeZentren", gZentrum);
							request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, "Falsches Zentrumpasswort");
							request.getRequestDispatcher("/benutzer_anlegen_zwei.jsp").forward(request, response);
						}
					}
				}
			} catch (DatenbankExceptions e) {
				// Fehler zurück!
				request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());
				request.getRequestDispatcher("/benutzer_anlegen_zwei.jsp").forward(request, response);
			}
		}

		// keine Filterung
	}

	private void classDispatcherservletZentrumAnlegen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Zentrum zusammenbauen
		ZentrumBean aZentrum = new ZentrumBean();
		PersonBean aPerson = new PersonBean();
		try {
			// Zentrum setzen
			aZentrum.setInstitution(request.getParameter(Parameter.zentrum.INSTITUTION.name()));
			aZentrum.setAbteilung(request.getParameter(Parameter.zentrum.ABTEILUNGSNAME.name()));
			aZentrum.setStrasse(request.getParameter(Parameter.zentrum.STRASSE.name()));
			aZentrum.setHausnr(request.getParameter(Parameter.zentrum.HAUSNUMMER.name()));
			aZentrum.setPlz(request.getParameter(Parameter.zentrum.PLZ.name()));
			aZentrum.setOrt(request.getParameter(Parameter.zentrum.ORT.name()));
			aZentrum.setIstAktiviert(true);
			String passwort = KryptoUtil.getInstance().generatePasswort(KryptoUtil.ZENTRUM_PASSWORT_LAENGE);
			aZentrum.setPasswortKlartext(passwort);

			// Person setzen
			aPerson.setVorname(request.getParameter(Parameter.person.VORNAME.name()));
			aPerson.setNachname(request.getParameter(Parameter.person.NACHNAME.name()));
			//Wurde das Geschlecht eingetragen
			if (request.getParameter(Parameter.person.GESCHLECHT.name()) != null) {
				aPerson.setGeschlecht(request.getParameter(Parameter.person.GESCHLECHT.name()).charAt(0));
			}
			//Fehlt das Geschlecht
			else{
				throw new PersonException(PersonException.GESCHLECHT_FEHLT);
			}
		
			aPerson.setTelefonnummer(request.getParameter(Parameter.person.TELEFONNUMMER.name()));
			aPerson.setFax(request.getParameter(Parameter.person.FAX.name()));
			aPerson.setEmail(request.getParameter(Parameter.person.EMAIL.name()));
			// Person speichern
			aPerson = DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(aPerson);

			// Zentrum speichern
			aZentrum.setAnsprechpartnerId(aPerson.getId());
			aZentrum = DatenbankFactory.getAktuelleDBInstanz().schreibenObjekt(aZentrum);
			request.setAttribute(DispatcherServlet.NACHRICHT_OK, "Das Zentrum: " + aZentrum.getInstitution() + "wurde erfolgreich angelegt.\t" + "Passwort:\t"
					+ passwort);
			request.getRequestDispatcher(Jsp.ZENTRUM_ANLEGEN).forward(request, response);

		} catch (BenutzerException e) {
			// Zentrum zurückschicken
			request.setAttribute(Parameter.zentrum.INSTITUTION.name(), request.getParameter(Parameter.zentrum.INSTITUTION.name()));
			request.setAttribute(Parameter.zentrum.ABTEILUNGSNAME.name(), request.getParameter(Parameter.zentrum.ABTEILUNGSNAME.name()));
			request.setAttribute(Parameter.zentrum.STRASSE.name(), request.getParameter(Parameter.zentrum.STRASSE.name()));
			request.setAttribute(Parameter.zentrum.HAUSNUMMER.name(), request.getParameter(Parameter.zentrum.HAUSNUMMER.name()));
			request.setAttribute(Parameter.zentrum.PLZ.name(), request.getParameter(Parameter.zentrum.PLZ.name()));
			request.setAttribute(Parameter.zentrum.ORT.name(), request.getParameter(Parameter.zentrum.ORT.name()));
			request.setAttribute(DispatcherServlet.FEHLERNACHRICHT, e.getMessage());

			// Person zurückschicken
			request.setAttribute(Parameter.person.VORNAME.name(), request.getParameter(Parameter.person.VORNAME.name()));
			request.setAttribute(Parameter.person.NACHNAME.name(), request.getParameter(Parameter.person.NACHNAME.name()));
			//Falls das Geschlecht bereits gesetzt wurde wieder eintragen
			if (request.getParameter(Parameter.person.GESCHLECHT.name()) != null) {
				System.out.println(request.getParameter(Parameter.person.GESCHLECHT.name()));
			request.setAttribute(Parameter.person.GESCHLECHT.name(), request.getParameter(Parameter.person.GESCHLECHT.name()));
			}
			request.setAttribute(Parameter.person.TELEFONNUMMER.name(), request.getParameter(Parameter.person.TELEFONNUMMER.name()));
			request.setAttribute(Parameter.person.FAX.name(), request.getParameter(Parameter.person.FAX.name()));
			request.setAttribute(Parameter.person.EMAIL.name(), request.getParameter(Parameter.person.EMAIL.name()));
			request.getRequestDispatcher(Jsp.ZENTRUM_ANLEGEN).forward(request, response);
		}

	}
}
