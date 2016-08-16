package com.gentics.mesh.rest.client.handler.impl;

import com.gentics.mesh.core.rest.common.GenericMessageResponse;
import com.gentics.mesh.core.rest.node.NodeDownloadResponse;
import com.gentics.mesh.json.JsonUtil;
import com.gentics.mesh.rest.client.MeshRestClientHttpException;
import com.gentics.mesh.rest.client.handler.AbstractMeshResponseHandler;

import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MeshBinaryResponseHandler extends AbstractMeshResponseHandler<NodeDownloadResponse> {

	private static final Logger log = LoggerFactory.getLogger(MeshBinaryResponseHandler.class);

	/**
	 * Create a new response handler.
	 * 
	 * @param classOfT
	 *            Expected response POJO class
	 * @param method
	 *            Method that was used for the request
	 * @param uri
	 *            Uri that was queried
	 */
	public MeshBinaryResponseHandler(HttpMethod method, String uri) {
		super(method, uri);
	}

	@Override
	public void handle(HttpClientResponse rh) {

		int code = rh.statusCode();
		if (code >= 200 && code < 300) {
			NodeDownloadResponse response = new NodeDownloadResponse();
			String contentType = rh.getHeader(HttpHeaders.CONTENT_TYPE.toString());
			response.setContentType(contentType);
			String disposition = rh.getHeader("content-disposition");
			String filename = disposition.substring(disposition.indexOf("=") + 1);
			response.setFilename(filename);

			rh.bodyHandler(buffer -> {
				response.setBuffer(buffer);
				future.complete(response);
			});
		} else {
			rh.bodyHandler(bh -> {
				String body = bh.toString();
				if (log.isDebugEnabled()) {
					log.debug(body);
				}

				log.error("Request failed with statusCode {" + code + "} statusMessage {" + rh.statusMessage() + "} {" + body + "} for method {"
						+ getMethod() + "} and uri {" + getUri() + "}");

				try {
					GenericMessageResponse responseMessage = JsonUtil.readValue(body, GenericMessageResponse.class);
					future.fail(new MeshRestClientHttpException(rh.statusCode(), rh.statusMessage(), responseMessage));
					return;

				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.debug("Could not deserialize response {" + body + "}.", e);
					}
				}

				future.fail(new MeshRestClientHttpException(rh.statusCode(), rh.statusMessage()));
				return;

			});
		}
	}

}
