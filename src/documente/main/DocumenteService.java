package documente.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import documente.beans.Articol;
import documente.beans.ArticolDocument;
import documente.beans.Document;
import documente.beans.DocumentTip;
import documente.beans.Furnizor;
import documente.beans.Login;
import documente.beans.Status;
import documente.beans.User;
import documente.connection.UserDAO;
import documente.model.OperatiiArticole;
import documente.model.OperatiiDocumente;

@Path("documente")
public class DocumenteService {

	@Path("testService")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String testService() {
		return "Evrika!";
	}

	@Path("test")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String test2(@QueryParam("codArticol") String codArticol) {

		System.out.println("test2 param1: " + codArticol);
		return "Hello World!";
	}

	@Path("login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String params) {

		User user = null;

		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(params);
			Login login = new Login((String) jsonObject.get("userName"), (String) jsonObject.get("password"));
			user = new UserDAO().validateUser(login);

		} catch (ParseException e) {
			// MailOperations.sendMail(Utils.getStackTrace(e));

			System.out.println(e.toString());
		}

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(user).build();
	}

	@Path("adaugaDocument")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response adaugaDocument(@FormParam("codArticol") String codArticol,
			@FormParam("tipDocument") String tipDocument, @FormParam("document") String document) {
		Status status = new OperatiiDocumente().adaugaDocument(codArticol, tipDocument, document);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(status.toString()).build();
	}

	@Path("documenteArticol")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response verificaDocumenteArticol(@QueryParam("codArticol") String codArticol) {

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity("").build();

	}

	@Path("cautaArticol")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response cautaArticol(@QueryParam("tipArticol") String tipArticol,
			@QueryParam("codArticol") String codArticol, @QueryParam("textArticol") String textArticol) {

		List<Articol> listArticole = new OperatiiArticole().getListArticole(tipArticol, codArticol, textArticol);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listArticole).build();

	}

	@POST
	@Path("uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response testLoad(@FormDataParam("file") InputStream fis,
			@FormDataParam("file") FormDataContentDisposition fdcd, @FormDataParam("articol") String articol,
			@FormDataParam("tipDocument") String tipDocument, @FormDataParam("dataStart") String dataStart,
			@FormDataParam("dataStop") String dataStop, @FormDataParam("furnizor") String furnizor) {

		Status status = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try {
			int read;
			byte[] bytes = new byte[1024];

			while ((read = fis.read(bytes)) != -1) {
				buffer.write(bytes, 0, read);
			}

			buffer.flush();
			byte[] byteArray = buffer.toByteArray();

			status = new OperatiiDocumente().adaugaDocument(articol, tipDocument, byteArray, dataStart, dataStop, furnizor);

		} catch (Exception iox) {
			iox.printStackTrace();
		}

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(status).build();
	}

	@Path("getDocumente")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocumente(@QueryParam("codArticol") String codArticol) {

		List<Document> listDocumente = new OperatiiDocumente().getDocumenteArticol(codArticol);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listDocumente).build();

	}

	@Path("getDocumentByte")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDocumentByte(@QueryParam("codArticol") String codArticol,
			@QueryParam("tipDocument") String tipDocument, @QueryParam("codFurnizor") String codFurnizor) {

		
		StreamingOutput fileStream =  new StreamingOutput() 
        {
            @Override
            public void write(java.io.OutputStream output) throws IOException
            {
                try
                {
                    byte[] data = new OperatiiDocumente().getDocumentByte(codArticol, tipDocument, codFurnizor);
                    output.write(data);
                    output.flush();
                } 
                catch (Exception e) 
                {
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };
		
		
        return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(fileStream).build();
        
		
	}
	
	
	@Path("getArticoleDocument")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticoleDocument(@QueryParam("nrDocument") String nrDocument){
		
		List<ArticolDocument> listArticole = new OperatiiArticole().getArticoleDocument(nrDocument);
		
		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listArticole).build();
		
		
	}
	
	
	@Path("getFurnizoriArticol")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFurnizoriArticol(@QueryParam("codArticol") String codArticol){
		
		List<Furnizor> listFurnizori = new OperatiiArticole().getFurnizori(codArticol);
		
		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listFurnizori).build();
		
		
	}
	
	@Path("getDocumenteTip")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocumenteTip(@QueryParam("codArticol") String codArticol, @QueryParam("tipDocument") String tipDocument) {

		List<DocumentTip> listDocumente = new OperatiiDocumente().getDocumenteArticolTip(codArticol, tipDocument);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listDocumente).build();

	}

}
