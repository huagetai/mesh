package com.gentics.mesh.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.io.IOUtils;

import com.gentics.mesh.Mesh;
import com.gentics.mesh.etc.config.MeshOptions;
import com.gentics.mesh.json.JsonUtil;
import com.gentics.mesh.plugin.manager.api.PluginManager;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

@Singleton
public class PluginManagerImpl implements PluginManager {

	private static final String PLUGIN_MANIFEST_FILENAME = "mesh-plugin.json";

	private static final String PLUGIN_JAR_FILENAME = "plugin.jar";

	private static final Logger log = LoggerFactory.getLogger(PluginManagerImpl.class);

	private String pluginFolder;

	@Inject
	public PluginManagerImpl() {
	}

	public void init(MeshOptions options) {

		// this.pluginFolder = options.getPluginDirectory();
		// try {
		// // Search for installed plugins
		// Stream<File> zipFiles = Files.list(Paths.get(pluginFolder)).filter(Files::isRegularFile).filter((f) -> {
		// return f.getFileName().toString().endsWith(".zip");
		// }).map(p -> p.toFile());
		// zipFiles.forEach(file -> {
		// registerPlugin(file);
		// });
		// } catch (IOException e) {
		// log.error("Error while reading plugins from plugin folder {" + pluginFolder + "}", e);
		// }
	}

	private void registerPlugin(Plugin plugin) {
		System.out.println("REGISTER Plugin " + plugin.getName());
	}

	private void registerPlugin(File file) {
		// Check for plugin collisions

		log.info("Registering plugin {" + file + "}");
		try (ZipFile zipFile = new ZipFile(file)) {

			// Load the manifest
			ZipEntry manifestEntry = zipFile.getEntry(PLUGIN_MANIFEST_FILENAME);
			if (manifestEntry == null) {
				throw new PluginException("The plugin manifest file {" + PLUGIN_MANIFEST_FILENAME + "} could not be found in the plugin archive.");
			}
			try (InputStream ins = zipFile.getInputStream(manifestEntry)) {
				String json = IOUtils.toString(ins);
				PluginManifest manifest = JsonUtil.readValue(json, PluginManifest.class);
				manifest.validate();
			}

			// Load the jar
			ZipEntry jarEntry = zipFile.getEntry(PLUGIN_JAR_FILENAME);
			if (jarEntry == null) {
				throw new PluginException("The plugin jar file {" + PLUGIN_JAR_FILENAME + "} could not be found in the plugin archive.");
			}
			try (InputStream ins = zipFile.getInputStream(jarEntry)) {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Extract the static resources
		} catch (Exception e) {
			log.error("Error while loading plugin from file {" + file + "}", e);
		}

	}

	@Override
	public void deployPlugin(String mavenCoordinates, Handler<AsyncResult<String>> completionHandler) {
		Mesh.mesh().getVertx().deployVerticle("maven:" + mavenCoordinates, completionHandler);
	}

	@Override
	public void deployPlugin(Plugin plugin) {
		Mesh.mesh().getVertx().deployVerticle(plugin);
	}

}
