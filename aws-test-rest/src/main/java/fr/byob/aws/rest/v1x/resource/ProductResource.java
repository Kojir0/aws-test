package fr.byob.aws.rest.v1x.resource;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import com.google.inject.Inject;

import fr.byob.aws.commons.guice.InjectLogger;
import fr.byob.aws.db.DAOException;
import fr.byob.aws.db.dao.ProductDAO;
import fr.byob.aws.domain.Product;
import fr.byob.aws.rest.exception.IllegalRequestException;
import fr.byob.aws.rest.v1x.V1XConstants;


/**
 * 
 * REST endpoint for the Product CRUD
 * 
 * @author gpereira
 *
 */
@Path(V1XConstants.PATH + "/product/")
public class ProductResource {

	@InjectLogger
	private Logger log;

	private final ProductDAO dao;

	@Inject
	public ProductResource(final ProductDAO dao) {
		this.dao = dao;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/add")
	public Product add(final Product product) {
		log.info("ADD " + product.getId());
		try {
			dao.createProduct(product);
		} catch (final DAOException e) {
			log.error("ADD failed", e);
			throw new IllegalRequestException(e);
		}
		return product;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/get/{id}")
	public Product get(@PathParam("id") final Integer id) {
		log.info("GET " + id);
		try {
			return dao.retrieveProduct(id);
		} catch (DAOException e) {
			log.error("GET failed", e);
			throw new IllegalRequestException(e);
		}
	}

	@DELETE
	// @GET Does not work with some web browsers
	@Path("/delete/{id}")
	public void delete(@PathParam("id") final Integer id) {
		log.info("DELETE " + id);
		try {
			dao.deleteProduct(id);
		} catch (final DAOException e) {
			log.error("DELETE failed", e);
			throw new IllegalRequestException(e);
		}
	}

	@GET
	@Path("/test")
	@Produces({ MediaType.TEXT_PLAIN })
	public String test() {
		log.info("TEST");
		try {
			return getHostname();
		} catch (final UnknownHostException e) {
			log.error("TEST failed", e);
			throw new IllegalRequestException(e);
		}
	}

	private static String getHostname() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}
}
