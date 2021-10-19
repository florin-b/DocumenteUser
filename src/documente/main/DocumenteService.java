package documente.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import documente.beans.DocumentRaport;
import documente.beans.DocumentTip;
import documente.beans.Furnizor;
import documente.beans.Login;
import documente.beans.Reper;
import documente.beans.RezultatDocArticol;
import documente.beans.Status;
import documente.beans.User;
import documente.connection.UserDAO;
import documente.model.OperatiiArticole;
import documente.model.OperatiiDocumente;
import documente.model.OperatiiFurnizori;

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
	public Response uploadFile(@FormDataParam("file") InputStream fis,
			@FormDataParam("file") FormDataContentDisposition fdcd, @FormDataParam("articol") String articol,
			@FormDataParam("tipDocument") String tipDocument, @FormDataParam("dataStart") String dataStart,
			@FormDataParam("dataStop") String dataStop, @FormDataParam("furnizor") String furnizor,
			@FormDataParam("nrSarja") String nrSarja, @FormDataParam("unitLog") String unitLog) {

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

			status = new OperatiiDocumente().uploadFile(articol, tipDocument, byteArray, dataStart, dataStop, furnizor,
					nrSarja, unitLog);
			
			/*

			if (!nrSarja.trim().isEmpty()) {
				if (!nrSarja.contains(","))
					status = new OperatiiDocumente().adaugaDocument(articol, tipDocument, byteArray, dataStart,
							dataStop, furnizor, nrSarja, unitLog);
				else {
					List<String> localListSarje = new ArrayList<>(Arrays.asList(nrSarja.split(",")));

					for (String localNrSarja : localListSarje)
						if (!localNrSarja.trim().isEmpty())
							status = new OperatiiDocumente().adaugaDocument(articol, tipDocument, byteArray, dataStart,
									dataStop, furnizor, localNrSarja, unitLog);
				}

			} else {
				if (!articol.contains(","))
					status = new OperatiiDocumente().adaugaDocument(articol, tipDocument, byteArray, dataStart,
							dataStop, furnizor, nrSarja, unitLog);
				else {
					List<String> localListArt = new ArrayList<>(Arrays.asList(articol.split(",")));

					for (String codArt : localListArt)
						if (!codArt.trim().isEmpty())
							status = new OperatiiDocumente().adaugaDocument(codArt, tipDocument, byteArray, dataStart,
									dataStop, furnizor, nrSarja, unitLog);
				}
			}
			
			*/

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
	public Response getDocumente(@QueryParam("codArticol") String codArticol,
			@QueryParam("tipArticol") String tipArticol) {

		RezultatDocArticol rezultat = new OperatiiDocumente().getDocumenteArticol(codArticol, tipArticol);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(rezultat).build();

	}

	@Path("getDocumentByte")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDocumentByte(@QueryParam("codArticol") String codArticol,
			@QueryParam("tipDocument") String tipDocument, @QueryParam("codFurnizor") String codFurnizor) {

		StreamingOutput fileStream = new StreamingOutput() {
			@Override
			public void write(java.io.OutputStream output) throws IOException {
				try {
					byte[] data = new OperatiiDocumente().getDocumentByte(codArticol, tipDocument, codFurnizor);
					output.write(data);
					output.flush();
				} catch (Exception e) {
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

	@Path("stergeDocument")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response stergeDocument(@FormDataParam("codArticol") String codArticol,
			@FormDataParam("codSarja") String codSarja, @FormDataParam("tipDocument") String tipDocument,
			@FormDataParam("codFurnizor") String codFurnizor, @FormDataParam("startValid") String startValid,
			@FormDataParam("stopValid") String stopValid) {

		Status status = new OperatiiDocumente().stergeDocument(codArticol, codSarja, tipDocument, codFurnizor,
				startValid, stopValid);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(status).build();

	}

	@Path("getArticoleDocument")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticoleDocument(@QueryParam("nrDocument") String nrDocument) {

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
	public Response getFurnizoriArticol(@QueryParam("codArticol") String codArticol,
			@QueryParam("tipArticol") String tipArticol) {

		List<Furnizor> listFurnizori = new OperatiiArticole().getFurnizori(codArticol, tipArticol);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listFurnizori).build();

	}

	@Path("getDocumenteTip")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocumenteTip(@QueryParam("codArticol") String codArticol,
			@QueryParam("tipDocument") String tipDocument) {

		List<DocumentTip> listDocumente = new OperatiiDocumente().getDocumenteArticolTip(codArticol, tipDocument);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listDocumente).build();

	}

	@Path("getSintetice")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSintetice(@QueryParam("codSintetic") String codSintetic) {

		List<Articol> listSintetice = new OperatiiArticole().getSintetice(codSintetic);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listSintetice).build();

	}

	@Path("adaugaTipDocSint")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response adaugaTipDocSintetic(@FormDataParam("codSintetic") String codSintetic,
			@FormDataParam("tipDoc") String tipDoc) {

		Status status = new OperatiiDocumente().adaugaTipDocSintetic(codSintetic, tipDoc);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(status).build();

	}

	@Path("getTipDocSint")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTipDocSint(@QueryParam("codSintetic") String codSintetic) {

		String tipDocs = new OperatiiDocumente().getTipDocSintetic(codSintetic);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(tipDocs).build();

	}

	@Path("getSinteticSarja")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSinteticSarja(@QueryParam("codSintetic") String codSintetic) {

		String isSinteticSarja = String.valueOf(new OperatiiArticole().isSinteticSarja(codSintetic));

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(isSinteticSarja).build();

	}

	@Path("setSinteticSarja")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response setSinteticSarja(@FormDataParam("codSintetic") String codSintetic,
			@FormDataParam("tipOp") String tipOp) {

		String setSinteticSarja = String.valueOf(new OperatiiArticole().setSinteticSarja(codSintetic, tipOp));

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(setSinteticSarja).build();

	}

	@Path("getArticoleSintetic")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticoleSintetic(@QueryParam("codSintetic") String codSintetic) {

		List<Articol> listArticole = new OperatiiArticole().getArticoleSintetic(codSintetic);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listArticole).build();

	}

	@Path("cautaFurnizor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response cautaFurnizor(@QueryParam("numeFurnizor") String numeFurnizor) {

		List<Furnizor> listFurnizori = new OperatiiFurnizori().cautaFurnizor(numeFurnizor);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listFurnizori).build();

	}

	@Path("getSinteticeFurnizor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSinteticeFurnizor(@QueryParam("codFurnizor") String codFurnizor,
			@QueryParam("codDepart") String codDepart) {

		List<Reper> listSintetice = new OperatiiFurnizori().getSinteticeFurnizor(codFurnizor, codDepart);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listSintetice).build();

	}

	@Path("getArticoleFurnizor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticoleFurnizor(@QueryParam("codFurnizor") String codFurnizor,
			@QueryParam("sintetice") String sintetice) {

		List<Reper> listSintetice = new OperatiiFurnizori().getArticoleFurnizor(codFurnizor, sintetice);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listSintetice).build();

	}

	@Path("getDocumenteFurnizor")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocumenteFurnizor(@QueryParam("codFurnizor") String codFurnizor,
			@QueryParam("sintetice") String sintetice, @QueryParam("articole") String articole) {

		List<DocumentRaport> listDocumente = new OperatiiDocumente().getDocumenteFurnizor(codFurnizor, sintetice,
				articole);

		return Response.status(200).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").entity(listDocumente).build();

	}

}
