<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page import="de.randi2.model.fachklassen.beans.BenutzerkontoBean"
	import="java.util.GregorianCalendar"
	import="java.text.SimpleDateFormat" import="java.util.Locale"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="de.randi2.model.fachklassen.beans.*"
	import="java.util.Iterator" import="java.util.*"%>
<%@page import="de.randi2.utility.*" import="de.randi2.utility.Parameter" %>
<%@page import="de.randi2.randomisation.Randomisation"%>
<%

	StudieBean aStudie_this = (StudieBean)request.getSession().getAttribute(DispatcherServlet.sessionParameter.AKTUELLE_STUDIE.toString());

	boolean allesAendern = false;

	if (aStudie_this.getStatus()!=Studie.Status.INVORBEREITUNG && aStudie_this.getStatus()!=Studie.Status.AKTIV) {
		
		%><b>Verbotener Zugriff!</b><%
		
	} else {
		
		allesAendern = (aStudie_this.getStatus()==Studie.Status.INVORBEREITUNG);


	String aName_o = aStudie_this.getName();
	String aBeschreibung_o = aStudie_this.getName();
	GregorianCalendar aStartdatum_o = aStudie_this.getStartDatum();
	GregorianCalendar aEnddatum_o = aStudie_this.getEndDatum();
	Randomisation.Algorithmen aAlgorithmus_o = aStudie_this.getAlgorithmus();
	Vector<StudienarmBean> aStudienarme_o = aStudie_this.getStudienarme();
	int aBlockgroesse_o = aStudie_this.getBlockgroesse();
	Vector<StrataBean> aStrata_o = aStudie_this.getStrata();
	
	String aName = "";
	String aBeschreibung = "";
	String aStartdatum = "";
	String aEnddatum = "";
	String aAlgorithmus = Randomisation.Algorithmen.VOLLSTAENDIGE_RANDOMISATION.toString();
	String aArmbeschreibung = "";
	String aArmbezeichnung = "";
	int aBlockgroesse = 0;
	String aStrataname = "";
	String aStratabeschreibung = "";
	String aStrataauspraegungen = "";
	
	GregorianCalendar heute = new GregorianCalendar();

	request.setAttribute(DispatcherServlet.requestParameter.TITEL
			.toString(), JspTitel.STUDIE_AENDERN.toString());

	boolean aArmeEntfernenMoeglich = true;
	boolean aStrataEntfernenMoeglich = true;

	if (request
			.getAttribute(DispatcherServlet.requestParameter.ANZAHL_ARME
			.toString()) == null) {

		if (aStudienarme_o!=null) {
			request.setAttribute(
					DispatcherServlet.requestParameter.ANZAHL_ARME
							.toString(), new Integer(aStudienarme_o.size()).toString());
			aArmeEntfernenMoeglich = true;		
			
			if (aStudienarme_o.size() < 3) {
				
			
				request.setAttribute(
						DispatcherServlet.requestParameter.ANZAHL_ARME
								.toString(), "2");
						aArmeEntfernenMoeglich = false;

			}
			
		} else {
			
			request.setAttribute(
					DispatcherServlet.requestParameter.ANZAHL_ARME
							.toString(), "2");
					aArmeEntfernenMoeglich = false;
			
		}

	} else {

		if ((Integer
		.parseInt((String) request
				.getAttribute(DispatcherServlet.requestParameter.ANZAHL_ARME
				.toString()))) < 3) {

			request.setAttribute(
			DispatcherServlet.requestParameter.ANZAHL_ARME
			.toString(), "2");
			aArmeEntfernenMoeglich = false;

		}

	}

	if (request
			.getAttribute(DispatcherServlet.requestParameter.ANZAHL_STRATA
			.toString()) == null) {

		if (aStrata_o!=null) {
			request.setAttribute(
					DispatcherServlet.requestParameter.ANZAHL_STRATA
							.toString(), new Integer(aStrata_o.size()).toString());
			aStrataEntfernenMoeglich = true;
			if (aStrata_o.size() < 2) {
				
			
				request.setAttribute(
						DispatcherServlet.requestParameter.ANZAHL_STRATA
								.toString(), "1");
						aStrataEntfernenMoeglich = false;

			}
			
		} else {
			
			request.setAttribute(
					DispatcherServlet.requestParameter.ANZAHL_STRATA
							.toString(), "1");
			aStrataEntfernenMoeglich = false;
			
		}

	} else {

		if ((Integer
		.parseInt((String) request
				.getAttribute(DispatcherServlet.requestParameter.ANZAHL_STRATA
				.toString()))) < 2) {

			request.setAttribute(
			DispatcherServlet.requestParameter.ANZAHL_STRATA
			.toString(), "1");
			aStrataEntfernenMoeglich = false;

		}

	}	
	
	if (request.getAttribute(Parameter.studie.NAME.name()) == null) {

		if (aName_o!=null) {
		
			if (aName_o.trim().equals("")) {
			
				aName = "";
				
			} else {
				
				aName = aName_o;
				
			}
		
		}

	} else {

		aName = (String) request.getAttribute(Parameter.studie.NAME
		.name());

	}

	if (request.getAttribute(Parameter.studie.BLOCKGROESSE.name()) == null) {

		if (aBlockgroesse_o!=NullKonstanten.NULL_INT) {
			
			aBlockgroesse = aBlockgroesse_o;
		
		} else {
			
			aBlockgroesse = 0;
			
		}

	} else {

		aBlockgroesse = (Integer.parseInt((String) request
		.getAttribute(Parameter.studie.BLOCKGROESSE.name())));

	}
	if (request.getAttribute(Parameter.studie.BESCHREIBUNG.name()) == null) {

		if (aBeschreibung_o!=null) {
			
			if (aBeschreibung_o.trim().equals("")) {
			
				aBeschreibung = "";
				
			} else {
				
				aBeschreibung = aBeschreibung_o;
				
			}
		
		}

	} else {

		aBeschreibung = (String) request
		.getAttribute(Parameter.studie.BESCHREIBUNG.name());

	}

	if (request.getAttribute(Parameter.studie.STARTDATUM.name()) == null) {

		if (aStartdatum_o!=null) {
			
			aStartdatum = "new Date(" + aStartdatum_o.get(GregorianCalendar.YEAR)
			+ "," + aStartdatum_o.get(GregorianCalendar.MONTH) + ","
			+ aStartdatum_o.get(GregorianCalendar.DAY_OF_MONTH) + ")";
			
		} else {
		
			
			aStartdatum = "new Date(" + heute.get(GregorianCalendar.YEAR)
			+ "," + heute.get(GregorianCalendar.MONTH) + ","
			+ heute.get(GregorianCalendar.DAY_OF_MONTH) + ")";
			
		}

	} else {

		aStartdatum = (String) request
		.getAttribute(Parameter.studie.STARTDATUM.name());

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy",
			Locale.GERMAN);
			sdf.setCalendar(Calendar.getInstance());
			Date d = sdf.parse(aStartdatum);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(d);

			aStartdatum = "new Date(" + gc.get(GregorianCalendar.YEAR)
			+ "," + gc.get(GregorianCalendar.MONTH) + ","
			+ gc.get(GregorianCalendar.DAY_OF_MONTH) + ")";

		} catch (Exception e) {
			aStartdatum = "new Date("
			+ heute.get(GregorianCalendar.YEAR) + ","
			+ heute.get(GregorianCalendar.MONTH) + ","
			+ heute.get(GregorianCalendar.DAY_OF_MONTH) + ")";
		} finally {

		}

	}

	if (request.getAttribute(Parameter.studie.ENDDATUM.name()) == null) {

		if (aEnddatum_o!=null) {
			
			aEnddatum = "new Date(" + aEnddatum_o.get(GregorianCalendar.YEAR)
			+ "," + aEnddatum_o.get(GregorianCalendar.MONTH) + ","
			+ aEnddatum_o.get(GregorianCalendar.DAY_OF_MONTH) + ")";
			
		} else {
			
			aEnddatum = "new Date(" + heute.get(GregorianCalendar.YEAR)
			+ "," + heute.get(GregorianCalendar.MONTH) + ","
			+ heute.get(GregorianCalendar.DAY_OF_MONTH) + ")";
			
		}

	} else {

		aEnddatum = (String) request
		.getAttribute(Parameter.studie.ENDDATUM.name());

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy",
			Locale.GERMAN);
			sdf.setCalendar(Calendar.getInstance());
			Date d = sdf.parse(aEnddatum);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(d);

			aEnddatum = "new Date(" + gc.get(GregorianCalendar.YEAR)
			+ "," + gc.get(GregorianCalendar.MONTH) + ","
			+ gc.get(GregorianCalendar.DAY_OF_MONTH) + ")";

		} catch (Exception e) {
			aEnddatum = "new Date(" + heute.get(GregorianCalendar.YEAR)
			+ "," + heute.get(GregorianCalendar.MONTH) + ","
			+ heute.get(GregorianCalendar.DAY_OF_MONTH) + ")";
		} finally {

		}

	}

	if (request.getAttribute(Parameter.studie.RANDOMISATIONSALGORITHMUS
			.name()) == null) {

		if(aAlgorithmus_o!=null) {
			
			aAlgorithmus = aAlgorithmus_o.toString();
			
		} else {
		
			aAlgorithmus = Randomisation.Algorithmen.VOLLSTAENDIGE_RANDOMISATION
			.toString();
		
		}

	} else {

		aAlgorithmus = (String) request
		.getAttribute(Parameter.studie.RANDOMISATIONSALGORITHMUS
				.name());

	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Randi2 :: <%=request
									.getAttribute(DispatcherServlet.requestParameter.TITEL
											.toString())%></title>
<%@include file="include/inc_extjs.jsp"%>
<script>
Ext.onReady(function(){

	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

    var form_studie_anlegen = new Ext.form.Form({
        labelAlign: 'left',
        labelWidth: 250,
		buttonAlign: 'right',
		fileUpload: true,
		id:'form_studie_anlegen'
    });

    var studie_name = new Ext.form.TextField({
        fieldLabel: 'Name der Studie *',
        name: '<%=Parameter.studie.NAME.name() %>',
        allowBlank:false,
        minLength:3,
        maxLength:50,
        invalidText:'Dieser Name ist ung&uuml;ltig!',
        blankText:'Der Name darf nicht leer sein!',
        maxLengthText:'Der Name muss 3 bis 50 Zeichen lang sein!',
        minLengthText:'Der Name muss 3 bis 50 Zeichen lang sein!',
        value:'<%=aName%>',
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
        width:190
    });

    var studie_beschreibung = new Ext.form.TextArea({
        fieldLabel: 'Beschreibung der Studie',
        name: '<%=Parameter.studie.BESCHREIBUNG.name() %>',
        allowBlank:true,
        value:'<%=aBeschreibung.replaceAll("\\n","\\\\n").replaceAll("\\r","\\\\r")%>',
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>        
        width:300
    });

    var studie_startdatum = new Ext.form.DateField({
        fieldLabel: 'Startdatum *',
        name: '<%=Parameter.studie.STARTDATUM.name() %>',
        width:100,
        allowBlank:false,
        blankText:'Bitte das Startdatum w&auml;hlen!',
		format:'d.m.Y',
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
		value:<%=aStartdatum%>
    });
    
    var studie_enddatum = new Ext.form.DateField({
        fieldLabel: 'Enddatum *',
        name: '<%=Parameter.studie.ENDDATUM.name() %>',
        width:100,
        allowBlank:false,
        blankText:'Bitte das Enddatum w&auml;hlen!',
		format:'d.m.Y',
		value:<%=aEnddatum%>
    });

    
	var fileField = new Ext.form.TextField({
		msgTarget: 'side',
		allowBlank: true,
		inputType: 'file',
		readOnly: false,
		name: '<%=Parameter.studie.STUDIENPROTOKOLL.name() %>',
		fieldLabel: 'Studienprotokoll *',
		width: 250
	});    

    form_studie_anlegen.fieldset(
        {legend:'Angaben zur Studie'},
		studie_name,
		studie_beschreibung,
		studie_startdatum,
		studie_enddatum,
		fileField
	);
	
	var algorithmus = new Ext.form.MiscField({
        fieldLabel: 'Randomisationsalgorithmus *',
        value: '<select class=\'x-combo-list x-combo-list-hd\' name=\'<%=Parameter.studie.RANDOMISATIONSALGORITHMUS.name()%>\' onChange=\'refreshRandomisation()\'<% if (!allesAendern) { %> disabled<% } %>><%
				StringBuffer algorithmus = new StringBuffer();
				for (int i = 0; i < Randomisation.Algorithmen.values().length; i++) {
					algorithmus.append(Randomisation.Algorithmen.values()[i].toString());
			%><option value=\'<%=algorithmus%>\'<% if (algorithmus.toString().equals(aAlgorithmus)) {%> selected<% } %>><%=algorithmus%></option><%
					algorithmus.delete(0, algorithmus.length());
				}
			%></select>',
        width:190
	});    	
    
	<% if(aAlgorithmus.equals(Randomisation.Algorithmen.BLOCKRANDOMISATION_MIT_STRATA.toString()) || aAlgorithmus.equals(Randomisation.Algorithmen.BLOCKRANDOMISATION_OHNE_STRATA.toString()) ) { %>

	var blockgroesse = new Ext.form.NumberField({
		msgTarget: 'side',
		allowBlank: false,
        allowNegative: false,
        allowDecimals: false,
		readOnly: false,
		name: '<%=Parameter.studie.BLOCKGROESSE.name() %>',
		fieldLabel: 'Blockgr&ouml;sse *',
		value:'<%=aBlockgroesse%>',
		width: 100,
        minValue:0,
        maxValue:10000,
        invalidText:'Bitte einen Wert zwischen 0-10000 eingeben!',
        maxText:'Bitte einen Wert zwischen 0-10000 eingeben!',
        minText:'Bitte einen Wert zwischen 0-10000 eingeben!',
        blankText:'Bitte die Blockgroesse eintragen!',
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
        nanText:'Bitte einen Wert zwischen 0-10000 eingeben!'
	});    	
	
	form_studie_anlegen.fieldset({legend:'Randomisation'},algorithmus,blockgroesse);
	
	<% } else { %>
	
	form_studie_anlegen.fieldset({legend:'Randomisation'},algorithmus);	
	
	
	<% } %>
	
	<% if(aAlgorithmus.equals(Randomisation.Algorithmen.BLOCKRANDOMISATION_MIT_STRATA.toString())) { %>

	        

	form_studie_anlegen.fieldset({legend:'Stratakonfiguration <% if (allesAendern) { %><img src="images/add-page-green.gif" style="cursor:pointer" onmousedown="addStrata()">&nbsp;<% if (aStrataEntfernenMoeglich) { %><img src="images/omit-page-green.gif" style="cursor:pointer" onmousedown="delStrata()"><% } %><% } %>',labelAlign:'top'});
	<%
	
		for(int i=1;i<(Integer.parseInt((String)request.getAttribute(DispatcherServlet.requestParameter.ANZAHL_STRATA.toString())))+1;i++) {
			
			aStrataname = "";
			aStratabeschreibung = "";
			aStrataauspraegungen = "";
			StrataBean aStratabean = null;
			if(i<=aStrata_o.size()) {
				
				aStratabean = aStrata_o.get(i-1);
				
			}
			
			if (request.getAttribute(Parameter.strata.NAME.name()+i)==null) {
				
				if (aStratabean!=null) {
					
					if (aStratabean.getName()!=null) {
						
						if(aStratabean.getName().trim()!="") {
							
							aStrataname = aStratabean.getName();
							
						} else {
							aStrataname = "";
						}
						
					} else {
						aStrataname = "";
					}
					
				} else {
					aStrataname = "";
				}
				
				
			} else {
				
				aStrataname = (String)request.getAttribute(Parameter.strata.NAME.name()+i);
				
			}
			
			if (request.getAttribute(Parameter.strata.STRATABESCHREIBUNG.name()+i)==null) {
				
					if (aStratabean!=null) {
					
					if (aStratabean.getBeschreibung()!=null) {
						
						if(aStratabean.getBeschreibung().trim()!="") {
							
							aStratabeschreibung = aStratabean.getBeschreibung();
							
						} else {
							aStratabeschreibung = "";
						}
						
					} else {
						aStratabeschreibung = "";
					}
					
				} else {
					aStratabeschreibung = "";
				}
			
				
			} else {
				
				aStratabeschreibung = (String)request.getAttribute(Parameter.strata.STRATABESCHREIBUNG.name()+i);
				
			}
			
			if (request.getAttribute(Parameter.strata.AUSPRAEGUNGEN.name()+i)==null) {
				
				if (aStratabean!=null) {
				
					if(aStratabean.getAuspraegungen()!=null) {
						
						Iterator it = aStratabean.getAuspraegungen().iterator();
						
						while(it.hasNext()) {
							
							StrataAuspraegungBean aAuspraegung = (StrataAuspraegungBean)it.next();
							
							aStrataauspraegungen += aAuspraegung.getName() + "\r\n";
							
						}
						
					} else {
						
						aStrataauspraegungen = "";
						
					}
					
				} else {
				
				aStrataauspraegungen = "";
				
				}
				
			} else {
				
				aStrataauspraegungen = (String)request.getAttribute(Parameter.strata.AUSPRAEGUNGEN.name()+i);
				
			}
			
	%>
	<!--  Hier wird die Strata konfiguriert	-->
	
	var strata_name<%=i%> = new Ext.form.TextField({
		msgTarget: 'side',
		allowBlank: false,
		name: '<%=Parameter.strata.NAME.name() %><%=i%>',
		fieldLabel: 'Name',
		width: 250,
		value: '<%=aStrataname%>',
        minLength:1,
        maxLength:100,
        invalidText:'Dieser Name ist ung&uuml;ltig!',
        maxLengthText:'Der Name muss 1 bis 100 Zeichen lang sein!',
        minLengthText:'Der Name muss 1 bis 100 Zeichen lang sein!',
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
		blankText: 'Ein Name wird ben&ouml;tigt!'
	});    
	
	var strata_beschreibung<%=i%> = new Ext.form.TextArea({
		msgTarget: 'side',
		allowBlank: true,
		name: '<%=Parameter.strata.STRATABESCHREIBUNG.name() %><%=i%>',
		fieldLabel: 'Beschreibung',
		value: '<%=aStratabeschreibung.replaceAll("\\n","\\\\n").replaceAll("\\r","\\\\r")%>',
		width: 250,
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
		height:50
	});    
	
	var strata_auspraegung<%=i%> = new Ext.form.TextArea({
		msgTarget: 'side',
		allowBlank: false,
		name: '<%=Parameter.strata.AUSPRAEGUNGEN.name() %><%=i%>',
		fieldLabel: 'Auspr&auml;gungen (pro Zeile eine m&ouml;gliche Auspr&auml;gung)',
		value: '<%=aStrataauspraegungen.replaceAll("\\n","\\\\n").replaceAll("\\r","\\\\r")%>',
		width: 250,
		height:100,
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
		blankText: 'Mindestens eine Auspr&auml;gung wird ben&ouml;tigt!'
	});    	
	
	
    form_studie_anlegen.fieldset({legend:'Strata Nr. <%=i%>'});
    var linksoben<%=i%> = new Ext.form.Column({width:'300'});
    var rechtsoben<%=i%> = new Ext.form.Column({width:'300'});
    
    form_studie_anlegen.start(linksoben<%=i%>);
    form_studie_anlegen.add(strata_name<%=i%>);
    form_studie_anlegen.add(strata_beschreibung<%=i%>);
    form_studie_anlegen.end(linksoben<%=i%>);
    form_studie_anlegen.start(rechtsoben<%=i%>);
    form_studie_anlegen.add(strata_auspraegung<%=i%>);
    form_studie_anlegen.end(rechtsoben<%=i%>);
	form_studie_anlegen.end();
	
	
    <!--  Hier wird die Strata konfiguriert	(ENDE) -->
    <%
    
		}
    
    %>
    form_studie_anlegen.end();
    <%
    
	} // pruefen auf strata algorithmus
	
	%>
    form_studie_anlegen.fieldset({legend:'Studienarme <% if (allesAendern) { %><img src="images/add-page-red.gif" style="cursor:pointer" onmousedown="addStudienarm()">&nbsp;<% if(aArmeEntfernenMoeglich) { %><img src="images/omit-page-red.gif" style="cursor:pointer" onmousedown="delStudienarm()"><% } %><% } %>',labelAlign:'top'});
	<%
	
		for(int i=1;i<(Integer.parseInt((String)request.getAttribute(DispatcherServlet.requestParameter.ANZAHL_ARME.toString())))+1;i++) {
			
			aArmbezeichnung = "";
			aArmbeschreibung = "";
			StudienarmBean aStudienarmBean = null;
			if(i<=aStudienarme_o.size()) {
				
				aStudienarmBean = aStudienarme_o.get(i-1);
				
			}
			if (request.getAttribute(Parameter.studienarm.BEZEICHNUNG.name()+i)==null) {
				
				if (aStudienarmBean!=null) {
					
					if (aStudienarmBean.getBezeichnung()!=null) {
						
						if(aStudienarmBean.getBezeichnung().trim()!="") {
							
							aArmbezeichnung = aStudienarmBean.getBezeichnung();
							
						} else {
							aArmbezeichnung = "";
						}
						
					} else {
						aArmbezeichnung = "";
					}
					
				} else {
					aArmbezeichnung = "";
				}
			
				
			} else {
				
				aArmbezeichnung = (String)request.getAttribute(Parameter.studienarm.BEZEICHNUNG.name()+i);
				
			}
			
			if (request.getAttribute(Parameter.studienarm.ARMBESCHREIBUNG.name()+i)==null) {
				
				if (aStudienarmBean!=null) {
					
					if (aStudienarmBean.getBeschreibung()!=null) {
						
						if(aStudienarmBean.getBeschreibung().trim()!="") {
							
							aArmbeschreibung = aStudienarmBean.getBeschreibung();
							
						} else {
							aArmbeschreibung = "";
						}
						
					} else {
						aArmbeschreibung = "";
					}
					
				} else {
					aArmbeschreibung = "";
				}
			
				
			} else {
				
				aArmbeschreibung = (String)request.getAttribute(Parameter.studienarm.ARMBESCHREIBUNG.name()+i);
				
			}
			
	%>
	<!--  Hier werden die Studienarme konfiguriert	-->
	
	var sa_bezeichnung<%=i%> = new Ext.form.TextField({
		msgTarget: 'side',
		allowBlank: false,
		name: '<%=Parameter.studienarm.BEZEICHNUNG.name() %><%=i%>',
		fieldLabel: 'Bezeichnung',
		value: '<%=aArmbezeichnung%>',
        minLength:3,
        maxLength:50,
        invalidText:'Diese Bezeichnung ist ung&uuml;ltig!',
        blankText:'Die Bezeichnung darf nicht leer sein!',
        maxLengthText:'Die Bezeichnung muss 3 bis 50 Zeichen lang sein!',
        minLengthText:'Die Bezeichnung muss 3 bis 50 Zeichen lang sein!',
		width: 250,
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
		blankText: 'Eine Bezeichnung wird ben&ouml;tigt!'
	});    
	
	var sa_beschreibung<%=i%> = new Ext.form.TextArea({
		msgTarget: 'side',
		allowBlank: true,
		name: '<%=Parameter.studienarm.ARMBESCHREIBUNG.name() %><%=i%>',
		fieldLabel: 'Beschreibung',
		value: '<%=aArmbeschreibung.replaceAll("\\n","\\\\n").replaceAll("\\r","\\\\r")%>',
		width: 250,
        <% if (!allesAendern) { %>
        readOnly:true,
        <% } %>
		height:50
	});    
	

    form_studie_anlegen.fieldset({legend:'Studienarm Nr. <%=i%>'});
    var linksoben<%=i%> = new Ext.form.Column({width:'300'});
    var rechtsoben<%=i%> = new Ext.form.Column({width:'300'});
    
    form_studie_anlegen.start(linksoben<%=i%>);
    form_studie_anlegen.add(sa_bezeichnung<%=i%>);
    form_studie_anlegen.end(linksoben<%=i%>);
    form_studie_anlegen.start(rechtsoben<%=i%>);
    form_studie_anlegen.add(sa_beschreibung<%=i%>);
    form_studie_anlegen.end(rechtsoben<%=i%>);
	form_studie_anlegen.end();

	
    <!--  Hier werden die Studienarme konfiguriert	(ENDE) -->
	
	<%
	
		}
	
	%>
	
	form_studie_anlegen.end();
	
	form_studie_anlegen.addButton('Best&auml;tigen', function(){
		if (this.isValid()) {

            var frm = document.getElementById(this.id);
            frm.method = 'POST';
            frm.action = 'fileUpload';
            frm.encoding = 'multipart/form-data';
			frm.submit();
			
		}else{
			Ext.MessageBox.alert('Fehler', 'Die Eingaben waren fehlerhaft!');
		}
	}, form_studie_anlegen);
	
			
	form_studie_anlegen.render('studie_anlegen');
	
	<!--  Die ANFRAGE_ID fuer SUBMIT wird hier gesetzt. dhaehn	-->
	form_studie_anlegen.el.createChild({tag: 'input', name: '<%=Parameter.anfrage_id %>', type:'hidden', value: '<%=DispatcherServlet.anfrage_id.JSP_STUDIE_AENDERN.name() %>'});	

	form_studie_anlegen.el.createChild({tag: 'input', name: '<%=DispatcherServlet.requestParameter.ANZAHL_ARME.toString() %>', type:'hidden', value: '<%=request.getAttribute(DispatcherServlet.requestParameter.ANZAHL_ARME.toString()) %>'});	
	form_studie_anlegen.el.createChild({tag: 'input', name: '<%=DispatcherServlet.requestParameter.ANZAHL_STRATA.toString() %>', type:'hidden', value: '<%=request.getAttribute(DispatcherServlet.requestParameter.ANZAHL_STRATA.toString()) %>'});	



});
</script>
<script type="text/javascript">
<!--
function addStudienarm() {

	var frm = document.getElementById('form_studie_anlegen');
	frm.<%=Parameter.anfrage_id %>.value = '<%=DispatcherServlet.anfrage_id.JSP_STUDIE_AENDERN_ADD_STUDIENARM.name() %>';
	frm.submit();
	
}

function delStudienarm() {

	var frm = document.getElementById('form_studie_anlegen');
	frm.<%=Parameter.anfrage_id %>.value = '<%=DispatcherServlet.anfrage_id.JSP_STUDIE_AENDERN_DEL_STUDIENARM.name() %>';
	frm.submit();

}

function addStrata() {

	var frm = document.getElementById('form_studie_anlegen');
	frm.<%=Parameter.anfrage_id %>.value = '<%=DispatcherServlet.anfrage_id.JSP_STUDIE_AENDERN_ADD_STRATA.name() %>';
	frm.submit();
	
}

function delStrata() {

	var frm = document.getElementById('form_studie_anlegen');
	frm.<%=Parameter.anfrage_id %>.value = '<%=DispatcherServlet.anfrage_id.JSP_STUDIE_AENDERN_DEL_STRATA.name() %>';
	frm.submit();

}

function refreshRandomisation() {

	var frm = document.getElementById('form_studie_anlegen');
	frm.<%=Parameter.anfrage_id %>.value = '<%=DispatcherServlet.anfrage_id.JSP_STUDIE_AENDERN_REFRESH.name() %>';
	frm.submit();

}

-->


</script>

<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<%@include file="include/inc_header.jsp"%>
<%
if (!allesAendern) {
	
	request.setAttribute(DispatcherServlet.FEHLERNACHRICHT.toString(),"Da die Studie schon gestartet wurde, k&ouml;nnen nur das Enddatum sowie das Studienprotokoll ge&auml;ndert werden!");
	
}
%>
<div id="content"><%@include file="include/inc_nachricht.jsp"%>
<h1>Studie &auml;ndern</h1>
<div id="studie_anlegen"></div>
<br><br>
Das Studienprotokoll wird ersetzt, wenn eine neue Datei ausgew&auml;hlt wird.<br>
<%@include file="include/inc_footer.jsp"%></div>
<%@include file="include/inc_menue.jsp"%>
</body>
</html>
<%

	}

%>