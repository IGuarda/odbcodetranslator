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
@WebServlet(urlPatterns = {"/listar", "/insertar", "/hablar"})
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
				String odbcode = request.getParameter("palabra");
				String vin = request.getParameter("vin");

				if(odbcode==null)
				{
					out.println("usage: /insertar?palabra=palabra_a_traducir");
				}
				if(odbcode==null)
				{
					vin="WBAES26C05D";//vin de muestra
				}
				else
				{
					if(store.getDB() == null) 
					{
						out.println(String.format("Palabra: %s", palabra));
					}
					else
					{
						//parametro = Traductor.translate(parametro, "es", "en", false);
						//parametro = Traductor.translate(parametro);
						try {
						String fallo = odbrequest.getodbcode(odbcode,vin,"EN");
						palabra.setName(odbcode);
						store.persist(palabra);
					    out.println(String.format("resultado:<br> %s", fallo));		
					    out.println(Traductor.translate("hola"));
						} catch (Exception e) {
							out.println("error en el codigo de fallo "+e.toString());
						}
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
