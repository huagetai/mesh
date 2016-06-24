package com.gentics.mesh.core.data.impl;

import static com.gentics.mesh.core.data.relationship.GraphRelationships.HAS_ROLE;
import static com.gentics.mesh.core.data.search.SearchQueueEntryAction.DELETE_ACTION;
import static com.gentics.mesh.core.data.search.SearchQueueEntryAction.STORE_ACTION;
import static com.gentics.mesh.core.rest.error.Errors.conflict;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gentics.mesh.cli.BootstrapInitializer;
import com.gentics.mesh.context.InternalActionContext;
import com.gentics.mesh.core.data.Group;
import com.gentics.mesh.core.data.MeshVertex;
import com.gentics.mesh.core.data.Role;
import com.gentics.mesh.core.data.generic.AbstractMeshCoreVertex;
import com.gentics.mesh.core.data.generic.MeshVertexImpl;
import com.gentics.mesh.core.data.relationship.GraphPermission;
import com.gentics.mesh.core.data.search.SearchQueueBatch;
import com.gentics.mesh.core.data.search.SearchQueueEntryAction;
import com.gentics.mesh.core.rest.role.RoleReference;
import com.gentics.mesh.core.rest.role.RoleResponse;
import com.gentics.mesh.core.rest.role.RoleUpdateRequest;
import com.gentics.mesh.etc.MeshSpringConfiguration;
import com.gentics.mesh.graphdb.spi.Database;
import com.syncleus.ferma.FramedGraph;
import com.tinkerpop.blueprints.Edge;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import rx.Observable;

/**
 * @see Role
 */
public class RoleImpl extends AbstractMeshCoreVertex<RoleResponse, Role> implements Role {

	private static final Logger log = LoggerFactory.getLogger(RoleImpl.class);

	public static void checkIndices(Database database) {
		database.addVertexType(RoleImpl.class, MeshVertexImpl.class);
	}

	@Override
	public RoleReference createEmptyReferenceModel() {
		return new RoleReference();
	}

	@Override
	public String getType() {
		return Role.TYPE;
	}

	@Override
	public String getName() {
		return getProperty("name");
	}

	@Override
	public void setName(String name) {
		setProperty("name", name);
	}

	@Override
	public List<? extends Group> getGroups() {
		return out(HAS_ROLE).has(GroupImpl.class).toListExplicit(GroupImpl.class);
	}

	@Override
	public Set<GraphPermission> getPermissions(MeshVertex vertex) {
		Set<GraphPermission> permissions = new HashSet<>();
		for (GraphPermission permission : GraphPermission.values()) {
			if (hasPermission(permission, vertex.getImpl())) {
				permissions.add(permission);
			}
		}
		return permissions;
	}

	@Override
	public boolean hasPermission(GraphPermission permission, MeshVertex vertex) {
		FramedGraph graph = Database.getThreadLocalGraph();
		Iterable<Edge> edges = graph.getEdges("e." + permission.label(),
				MeshSpringConfiguration.getInstance().database().createComposedIndexKey(getId(), vertex.getImpl().getId()));
		return edges.iterator().hasNext();
	}

	@Override
	public void grantPermissions(MeshVertex vertex, GraphPermission... permissions) {
		for (GraphPermission permission : permissions) {
			if (!hasPermission(permission, vertex)) {
				addFramedEdge(permission.label(), vertex.getImpl());
			}
		}
	}

	@Override
	public Observable<RoleResponse> transformToRestSync(InternalActionContext ac, int level, String... languageTags) {
		Set<Observable<RoleResponse>> obs = new HashSet<>();

		RoleResponse restRole = new RoleResponse();
		restRole.setName(getName());

		for (Group group : getGroups()) {
			restRole.getGroups().add(group.transformToReference());
		}

		// Add common fields
		obs.add(fillCommonRestFields(ac, restRole));

		// Role permissions
		obs.add(setRolePermissions(ac, restRole));

		// Merge and complete
		return Observable.merge(obs).last();
	}

	@Override
	public void revokePermissions(MeshVertex node, GraphPermission... permissions) {
		for (GraphPermission permission : permissions) {
			outE(permission.label()).mark().inV().retain((MeshVertexImpl) node).back().removeAll();
		}
	}

	@Override
	public void delete(SearchQueueBatch batch) {
		// TODO don't allow deletion of admin role
		batch.addEntry(this, DELETE_ACTION);
		for (Group group : getGroups()) {
			batch.addEntry(group, STORE_ACTION);
		}
		getVertex().remove();
	}

	@Override
	public Observable<? extends Role> update(InternalActionContext ac) {
		RoleUpdateRequest requestModel = ac.fromJson(RoleUpdateRequest.class);
		Database db = MeshSpringConfiguration.getInstance().database();

		BootstrapInitializer boot = BootstrapInitializer.getBoot();
		if (shouldUpdate(requestModel.getName(), getName())) {
			// Check for conflict
			Role roleWithSameName = boot.roleRoot().findByName(requestModel.getName()).toBlocking().single();
			if (roleWithSameName != null && !roleWithSameName.getUuid().equals(getUuid())) {
				throw conflict(roleWithSameName.getUuid(), requestModel.getName(), "role_conflicting_name");
			}

			return db.trx(() -> {
				setName(requestModel.getName());
				return createIndexBatch(STORE_ACTION);
			}).process().map(b -> this);
		}
		// No update required
		return Observable.just(this);
	}

	@Override
	public void addRelatedEntries(SearchQueueBatch batch, SearchQueueEntryAction action) {
		for (Group group : getGroups()) {
			batch.addEntry(group, STORE_ACTION);
		}
	}

}
