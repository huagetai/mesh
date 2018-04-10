package com.gentics.mesh.plugin.factory;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.aether.artifact.Artifact;

import com.gentics.mesh.Mesh;
import com.gentics.mesh.etc.config.MeshOptions;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.maven.MavenCoords;
import io.vertx.maven.Resolver;
import io.vertx.maven.ResolverOptions;
import io.vertx.maven.resolver.ResolutionOptions;

@Singleton
public class PluginVerticleFactory implements VerticleFactory {

	private static final Logger log = LoggerFactory.getLogger(PluginVerticleFactory.class);

	public static final String LOCAL_REPO_SYS_PROP = ResolverOptions.LOCAL_REPO_SYS_PROP;
	public static final String REMOTE_REPOS_SYS_PROP = ResolverOptions.REMOTE_REPOS_SYS_PROP;
	public static final String HTTP_PROXY_SYS_PROP = ResolverOptions.HTTP_PROXY_SYS_PROP;
	public static final String HTTPS_PROXY_SYS_PROP = ResolverOptions.HTTPS_PROXY_SYS_PROP;
	public static final String REMOTE_SNAPSHOT_POLICY_SYS_PROP = ResolverOptions.REMOTE_SNAPSHOT_POLICY_SYS_PROP;

	private static final String USER_HOME = System.getProperty("user.home");
	private static final String FILE_SEP = System.getProperty("file.separator");
	private static final String DEFAULT_MAVEN_LOCAL = USER_HOME + FILE_SEP + ".m2" + FILE_SEP + "repository";
	private static final String DEFAULT_MAVEN_REMOTES = "https://repo.maven.apache.org/maven2/ https://oss.sonatype.org/content/repositories/snapshots/";

	private String pluginDir;

	private Vertx vertx;

	private Resolver resolver;

	@Inject
	public PluginVerticleFactory() {
		MeshOptions options = Mesh.mesh().getOptions();
		ResolverOptions resolverOptions = new ResolverOptions()
			.setHttpProxy(System.getProperty(HTTP_PROXY_SYS_PROP))
			.setHttpsProxy(System.getProperty(HTTPS_PROXY_SYS_PROP))
			.setLocalRepository(System.getProperty(LOCAL_REPO_SYS_PROP, DEFAULT_MAVEN_LOCAL))
			.setRemoteRepositories(
				Arrays.asList(System.getProperty(REMOTE_REPOS_SYS_PROP, DEFAULT_MAVEN_REMOTES).split(" ")));
		resolver = Resolver.create(resolverOptions);
		this.pluginDir = options.getPluginDirectory();
	}

	@Override
	public void init(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public String prefix() {
		return "plugin";
	}

	@Override
	public void resolve(String identifier, DeploymentOptions deploymentOptions, ClassLoader classLoader, Future<String> resolution) {
		String identifierNoPrefix = VerticleFactory.removePrefix(identifier);
		String coordsString = identifierNoPrefix;
		String serviceName = null;
		int pos = identifierNoPrefix.lastIndexOf("::");
		if (pos != -1) {
			coordsString = identifierNoPrefix.substring(0, pos);
			serviceName = identifierNoPrefix.substring(pos + 2);
		}
		MavenCoords coords = new MavenCoords(coordsString);
		if (coords.version() == null) {
			throw new IllegalArgumentException("Invalid service identifier, missing version: " + coordsString);
		}

		List<Artifact> artifacts;
		try {
			artifacts = resolver.resolve(coordsString, new ResolutionOptions());
		} catch (NullPointerException e) {
			// Sucks, but aether throws a NPE if repository name is invalid....
			throw new IllegalArgumentException("Cannot find module " + coordsString + ". Maybe repository URL is invalid?");
		}

		for(Artifact id : artifacts) {
			System.out.println(id.getArtifactId());
		}

		System.out.println("Resolve");
	}

	@Override
	public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
		System.out.println("Jow");
		// 1. Check local plugin folder for zip
		// 2. Check local plugin folder for extracted plugin
		// 3. Check maven update site (com.gentics.mesh.plugin.xyz)
		return null;
	}

}
