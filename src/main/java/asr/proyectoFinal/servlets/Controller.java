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
@WebServlet(urlPatterns = {"/listar", "/insertar", "/mostrar"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		out.println("<html><head><meta charset=\"UTF-8\"></head><body>");
		
		CloudantPalabraStore store = new CloudantPalabraStore();
		System.out.println(request.getServletPath());
		switch(request.getServletPath())
		{
			case "/listar":
				if(store.getDB() == null)
					  out.println("No hay DB");
				else {

					out.println("Palabras en la BD Cloudant:<br />"  );
					Iterator<Palabra> iterator = store.getAll().iterator();
					String s;
					int i=5;
					while (iterator.hasNext()&&i>0) {
						s=iterator.next().getName();
				        out.println("<a href=\"insertar?palabra=" +s+"\">"+s+"</a> <br>");
				        i--;
				    }
				}
				break;
				
			case "/insertar":
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
						palabra.setName(odbcode);
						store.persist(palabra);
					    out.println("información guardada correctamente, <a href=\"listar\">mostrar favoritos</a> <br> ");		
					    //out.println(Traductor.translate("hola"));
						} catch (Exception e) {
							out.println("error en el codigo de fallo "+e.toString());
						}
					}
				}
				break;
			case "/mostrar":
				Palabra palabra1 = new Palabra();
				String odbcode1 = request.getParameter("codigo");
				String vin1 = request.getParameter("vin");

				if(odbcode1==null)
				{
					out.println("usage: /mostrar?codigo=codigo");
				}
				else
				{
					if(vin1==null)
					{
						vin1="WBAES26C05D";//vin de muestra
					}
					try {
						String fallo = odbrequest.getodbcode(odbcode1,vin1,"EN");
						palabra1.setName(odbcode1);
					    out.println(String.format("resultado:<br> %s <br><a href=\"insertar?codigo=%s&vin=%s\">Guardar en favoritos</a>", fallo,odbcode1,vin1));		
					    //out.println(Traductor.translate("hola"));
						} catch (Exception e) {
							out.println("error en el codigo de fallo "+e.toString());
						}
					
				}
				break;
		}
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
