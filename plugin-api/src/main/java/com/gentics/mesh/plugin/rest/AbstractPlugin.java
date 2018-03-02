package com.gentics.mesh.plugin.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.gentics.mesh.plugin.Plugin;
import com.gentics.mesh.plugin.PluginManifest;

import io.vertx.core.AbstractVerticle;

/**
 * Abstract implementation for a Gentics Mesh plugin.
 */
public abstract class AbstractPlugin extends AbstractVerticle implements Plugin {

	private PluginManifest manifest;

	private List<Callable<RestExtension>> endpoints = new ArrayList<>();

	public AbstractPlugin() {
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
}
