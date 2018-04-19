package com.gentics.mesh.plugin.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.gentics.mesh.plugin.Plugin;
import com.gentics.mesh.plugin.PluginManifest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Abstract implementation for a Gentics Mesh plugin verticle.
 */
public abstract class AbstractPluginVerticle extends AbstractVerticle implements Plugin {

	private PluginManifest manifest;

	private List<Callable<RestExtension>> endpoints = new ArrayList<>();

	public AbstractPluginVerticle() {
	}

	public PluginManifest getManifest() {
		return manifest;
	}

	public void setManifest(PluginManifest manifest) {
		this.manifest = manifest;
	}

	public List<Callable<RestExtension>> getExtensions() {
		return endpoints;
	}

	public void addExtension(Callable<RestExtension> extension) {
		endpoints.add(extension);
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		registerExtensions();
		startFuture.complete();
	}

	/**
	 * Method which will register the extensions of the plugin.
	 */
	public abstract void registerExtensions();
}
