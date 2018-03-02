package com.gentics.mesh.plugin.manager.api;

import com.gentics.mesh.plugin.Plugin;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public interface PluginManager {

	/**
	 * Deploy the plugin with the given maven coordinates.
	 * 
	 * @param mavenCoordinates
	 * @param completionHandler
	 */
	void deployPlugin(String mavenCoordinates, Handler<AsyncResult<String>> completionHandler);

	/**
	 * Deploy the given plugin.
	 * 
	 * @param plugin
	 */
	void deployPlugin(Plugin plugin);

}
