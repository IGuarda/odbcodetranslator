package asr.proyectoFinal.servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import asr.proyectoFinal.dao.CloudantPalabraStore;
import asr.proyectoFinal.dominio.Palabra;
import asr.proyectoFinal.services.Traductor;
import asr.proyectoFinal.odbapi.odbrequest;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = {"/listar", "/insertar", "/mostrar", "/iotreceive", "/iotsend"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		out.println("<html><head><meta charset=\"UTF-8\"></head><body><center><a href=\"http://odbcodetranslator.apic.eu-gb.mybluemix.net/asrTomcatEjemploCloudant\"><img src=\"head.png\" ></a><br><br>");
		
		CloudantPalabraStore store = new CloudantPalabraStore();
		System.out.println(request.getServletPath());
		switch(request.getServletPath())
		{
			case "/listar":
				if(store.getDB() == null)
					  out.println("No hay DB");
				else {

					out.println("<H2>Favoritos</H2><br />"  );
					Iterator<Palabra> iterator = store.getAll().iterator();
					String s;
					int i=100;
					while (iterator.hasNext()&&i>0) {
						s=iterator.next().getName();
						if (s.charAt(0)!='$'){
					        out.println("<a href=\"mostrar?codigo=" +s+"\">"+s+"</a> <br>");
					        i--;
						}
				    }
				}
				break;
			case "/iotreceive":
				if(store.getDB() == null)
					  out.println("No hay DB");
				else {
					String url="http://odbcodetranslator.apic.eu-gb.mybluemix.net/asrTomcatEjemploCloudant/iot.jsp";
					out.println("<H2>Receptor en movil</H2><br /><iframe src=\"https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="+url+"\" height=\"150\" width=\"150\"></iframe><br>"  );
					Iterator<Palabra> iterator = store.getAll().iterator();
					Palabra s;
					int send=0;
					while (iterator.hasNext()) {
						s=iterator.next();
						String cod=s.getName();
						if (cod.charAt(0)=='$'){
							send=1;
							store.delete(s.get_id());
							try {
								String fallo1 = odbrequest.getodbcode(cod.substring(1),"WBAES26C05D","EN");

						        out.println("<br>"+fallo1+"<br> <a href=\"iotreceive\">recibir siguiente</a> ");
							} catch (Exception e) {
								out.println("servicio temporalmente no disponible");
							}					        
						}
				    }
					if(send==0) {
						out.println("<br>No se ha recibido informaci&oacute;n, esta p&aacute;gina se actualiza cada 10 segundos.<script language=\"javascript\">" + 
								"setTimeout(function(){\r\n" + 
								"   window.location.reload(1);\r\n" + 
								"}, 30000);\r\n" + 
								"</script>");
					}
				}
				break;
			case "/iotsend":
				Palabra palabra = new Palabra();
				String odbcode = request.getParameter("codigo");
				String vin = request.getParameter("vin");

				if(odbcode==null)
				{
					out.println("usage: /insertar?codigo=codigo_a_insertarr");
				}
				else
				{
					if(vin==null)
					{
						vin="WBAES26C05D";//vin de muestra
					}
					if(store.getDB() == null) 
					{
						out.println("Error en la base de datos");
					}
					else
					{
						
						try {
						palabra.setName("$"+odbcode);
						store.persist(palabra);
					    out.println("Informaci&oacute;n enviada,<br> <a href=\"iot.jsp\">enviar mas informaci&oacute;n</a> <br> ");		
					    //out.println(Traductor.translate("hola"));
						} catch (Exception e) {
							out.println("error en el codigo de fallo "+e.toString());
						}
					}
				}
				break;
			case "/insertar":
				Palabra palabra1 = new Palabra();
				String odbcode1 = request.getParameter("codigo");
				String vin1 = request.getParameter("vin");

				if(odbcode1==null)
				{
					out.println("usage: /insertar?codigo=codigo_a_insertarr");
				}
				else
				{
					if(vin1==null)
					{
						vin1="WBAES26C05D";//vin de muestra
					}
					if(store.getDB() == null) 
					{
						out.println("Error en la base de datos");
					}
					else
					{
						
						try {
						palabra1.setName(odbcode1);
						store.persist(palabra1);
					    out.println("Informaci&oacute;n guardada correctamente, <br><a href=\"listar\">mostrar favoritos</a> <br> ");		
					    //out.println(Traductor.translate("hola"));
						} catch (Exception e) {
							out.println("Error en el codigo de fallo "+e.toString());
						}
					}
				}
				break;
			case "/mostrar":
				Palabra palabra11 = new Palabra();
				String odbcode11 = request.getParameter("codigo");
				String vin11 = request.getParameter("vin");
				String lan = request.getParameter("lan");

				if(odbcode11==null)
				{
					out.println("usage: /mostrar?codigo=codigo");
				}
				else
				{
					if(vin11==null)
					{
						vin11="WBAES26C05D";//vin de muestra
					}
					if(lan==null)
					{
						lan="EN";//vin de muestra
					}
					try {
						String fallo = odbrequest.getodbcode(odbcode11,vin11,lan);
						palabra11.setName(odbcode11);
					    out.println(String.format("resultado:<br> %s <br><a href=\"insertar?codigo=%s&vin=%s\">Guardar en favoritos</a>", fallo,odbcode11,vin11));		
					    //out.println(Traductor.translate("hola"));
						} catch (Exception e) {
							out.println("error en el codigo de fallo "+e.toString());
						}
					
				}
				break;
		}
		out.println("</center></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
