package com.gentics.mesh.plugin.factory;

import static com.gentics.mesh.test.TestSize.FULL;

import org.junit.Test;

import com.gentics.mesh.test.context.AbstractMeshTest;
import com.gentics.mesh.test.context.MeshTestSetting;

@MeshTestSetting(useElasticsearch = false, testSize = FULL, startServer = true)
public class PluginFactoryTest extends AbstractMeshTest {

	@Test
	public void testLocalDeployment() {
		vertx().deployVerticle("plugin:com.gentics.mesh::hello-world");
	}

}
