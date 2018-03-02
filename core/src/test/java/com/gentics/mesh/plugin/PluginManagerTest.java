package com.gentics.mesh.plugin;

import org.junit.Test;

import com.gentics.mesh.Mesh;
import com.gentics.mesh.OptionsLoader;
import com.gentics.mesh.etc.config.MeshOptions;
import com.gentics.mesh.plugin.manager.api.PluginManager;

public class PluginManagerTest {

	@Test
	public void testManager() throws Exception {
		MeshOptions options = OptionsLoader.createOrloadOptions();
		options.getStorageOptions().setDirectory(null);
		options.getSearchOptions().setStartEmbedded(false);
		options.getSearchOptions().setUrl(null);
		Mesh mesh = Mesh.mesh(options).run(false);

		System.setProperty("vertx.maven.remoteRepos", "https://maven.gentics.com/maven2/");

		PluginManager manager = new PluginManagerImpl();
		// manager.init(new MeshOptions());
		manager.deployPlugin("com.gentics.mesh:mesh-hello-world-plugin:0.18.0-SNAPSHOT", rh -> {
			try {
				if (rh.failed()) {
					rh.cause().printStackTrace();
					mesh.shutdown();
				} else {
					System.out.println("ID:" + rh.result());
					System.out.println("Done");
					mesh.shutdown();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		mesh.dontExit();
	}
}
