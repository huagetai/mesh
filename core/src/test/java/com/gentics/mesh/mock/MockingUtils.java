package com.gentics.mesh.mock;

import static com.gentics.mesh.util.UUIDUtil.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mockito;

import com.gentics.mesh.core.data.Group;
import com.gentics.mesh.core.data.Language;
import com.gentics.mesh.core.data.NodeGraphFieldContainer;
import com.gentics.mesh.core.data.Project;
import com.gentics.mesh.core.data.Role;
import com.gentics.mesh.core.data.SchemaContainer;
import com.gentics.mesh.core.data.Tag;
import com.gentics.mesh.core.data.TagFamily;
import com.gentics.mesh.core.data.User;
import com.gentics.mesh.core.data.impl.GroupImpl;
import com.gentics.mesh.core.data.impl.LanguageImpl;
import com.gentics.mesh.core.data.impl.NodeGraphFieldContainerImpl;
import com.gentics.mesh.core.data.impl.ProjectImpl;
import com.gentics.mesh.core.data.impl.RoleImpl;
import com.gentics.mesh.core.data.impl.SchemaContainerImpl;
import com.gentics.mesh.core.data.impl.TagFamilyImpl;
import com.gentics.mesh.core.data.impl.TagImpl;
import com.gentics.mesh.core.data.impl.UserImpl;
import com.gentics.mesh.core.data.node.Node;
import com.gentics.mesh.core.data.node.field.basic.BooleanGraphField;
import com.gentics.mesh.core.data.node.field.basic.DateGraphField;
import com.gentics.mesh.core.data.node.field.basic.HtmlGraphField;
import com.gentics.mesh.core.data.node.field.basic.NumberGraphField;
import com.gentics.mesh.core.data.node.field.basic.StringGraphField;
import com.gentics.mesh.core.data.node.field.impl.basic.BooleanGraphFieldImpl;
import com.gentics.mesh.core.data.node.field.impl.basic.DateGraphFieldImpl;
import com.gentics.mesh.core.data.node.field.impl.basic.HtmlGraphFieldImpl;
import com.gentics.mesh.core.data.node.field.impl.basic.NumberGraphFieldImpl;
import com.gentics.mesh.core.data.node.field.impl.basic.StringGraphFieldImpl;
import com.gentics.mesh.core.data.node.field.impl.nesting.NodeGraphFieldImpl;
import com.gentics.mesh.core.data.node.field.list.BooleanGraphFieldList;
import com.gentics.mesh.core.data.node.field.list.DateGraphFieldList;
import com.gentics.mesh.core.data.node.field.list.HtmlGraphFieldList;
import com.gentics.mesh.core.data.node.field.list.NodeGraphFieldList;
import com.gentics.mesh.core.data.node.field.list.NumberGraphFieldList;
import com.gentics.mesh.core.data.node.field.list.StringGraphFieldList;
import com.gentics.mesh.core.data.node.field.list.impl.BooleanGraphFieldListImpl;
import com.gentics.mesh.core.data.node.field.list.impl.DateGraphFieldListImpl;
import com.gentics.mesh.core.data.node.field.list.impl.HtmlGraphFieldListImpl;
import com.gentics.mesh.core.data.node.field.list.impl.NodeGraphFieldListImpl;
import com.gentics.mesh.core.data.node.field.list.impl.NumberGraphFieldListImpl;
import com.gentics.mesh.core.data.node.field.list.impl.StringGraphFieldListImpl;
import com.gentics.mesh.core.data.node.field.nesting.NodeGraphField;
import com.gentics.mesh.core.data.node.impl.NodeImpl;
import com.gentics.mesh.core.rest.schema.Schema;
import com.gentics.mesh.core.rest.schema.impl.BooleanFieldSchemaImpl;
import com.gentics.mesh.core.rest.schema.impl.DateFieldSchemaImpl;
import com.gentics.mesh.core.rest.schema.impl.HtmlFieldSchemaImpl;
import com.gentics.mesh.core.rest.schema.impl.ListFieldSchemaImpl;
import com.gentics.mesh.core.rest.schema.impl.NodeFieldSchemaImpl;
import com.gentics.mesh.core.rest.schema.impl.NumberFieldSchemaImpl;
import com.gentics.mesh.core.rest.schema.impl.SchemaImpl;
import com.gentics.mesh.core.rest.schema.impl.StringFieldSchemaImpl;

public final class MockingUtils {

	private MockingUtils() {

	}

	public static Project mockProject(User user) {
		Project project = mock(ProjectImpl.class);
		when(project.getName()).thenReturn("dummyProject");
		when(project.getCreator()).thenReturn(user);
		when(project.getCreationTimestamp()).thenReturn(System.currentTimeMillis());
		when(project.getEditor()).thenReturn(user);
		when(project.getLastEditedTimestamp()).thenReturn(System.currentTimeMillis());
		return project;
	}

	public static Language mockLanguage(String code) {
		Language language = mock(LanguageImpl.class);
		when(language.getLanguageTag()).thenReturn("de");
		return language;
	}

	public static Node mockNodeBasic(String schemaType) {
		Node node = mock(NodeImpl.class);
		when(node.getUuid()).thenReturn(randomUUID());
		SchemaContainer schemaContainer = mockSchemaContainer(schemaType);
		when(node.getSchemaContainer()).thenReturn(schemaContainer);
		return node;
	}

	public static Role mockRole(String roleName, User creator) {
		Role role = mock(RoleImpl.class);
		when(role.getCreator()).thenReturn(creator);
		when(role.getCreationTimestamp()).thenReturn(System.currentTimeMillis());
		when(role.getEditor()).thenReturn(creator);
		when(role.getLastEditedTimestamp()).thenReturn(System.currentTimeMillis());
		when(role.getName()).thenReturn(roleName);
		when(role.getUuid()).thenReturn(randomUUID());
		return role;
	}

	public static Group mockGroup(String groupName, User creator) {
		Group group = mock(GroupImpl.class);
		when(group.getCreator()).thenReturn(creator);
		when(group.getCreationTimestamp()).thenReturn(System.currentTimeMillis());
		when(group.getEditor()).thenReturn(creator);
		when(group.getLastEditedTimestamp()).thenReturn(System.currentTimeMillis());
		when(group.getName()).thenReturn(groupName);
		when(group.getUuid()).thenReturn(randomUUID());
		return group;
	}

	public static User mockUser(String username, String firstname, String lastname) {
		return mockUser(username, firstname, lastname, null);
	}

	public static User mockUser(String username, String firstname, String lastname, User creator) {
		User user = mock(UserImpl.class);
		when(user.getUsername()).thenReturn(username);
		when(user.getFirstname()).thenReturn(firstname);
		when(user.getLastname()).thenReturn(lastname);
		when(user.getUuid()).thenReturn(randomUUID());
		when(user.getCreationTimestamp()).thenReturn(System.currentTimeMillis());
		when(user.getLastEditedTimestamp()).thenReturn(System.currentTimeMillis());
		if (creator != null) {
			when(user.getCreator()).thenReturn(creator);
			when(user.getEditor()).thenReturn(creator);
		}
		return user;
	}

	public static TagFamily mockTagFamily(String name, User user) {
		TagFamily tagFamily = mock(TagFamilyImpl.class);
		when(tagFamily.getCreator()).thenReturn(user);
		when(tagFamily.getCreationTimestamp()).thenReturn(System.currentTimeMillis());
		when(tagFamily.getEditor()).thenReturn(user);
		when(tagFamily.getLastEditedTimestamp()).thenReturn(System.currentTimeMillis());
		when(tagFamily.getName()).thenReturn(name);
		when(tagFamily.getUuid()).thenReturn(randomUUID());
		return tagFamily;
	}

	public static Tag mockTag(String name, User user, TagFamily tagFamily) {
		Tag tag = mock(TagImpl.class);
		when(tag.getCreator()).thenReturn(user);
		when(tag.getCreationTimestamp()).thenReturn(System.currentTimeMillis());
		when(tag.getEditor()).thenReturn(user);
		when(tag.getLastEditedTimestamp()).thenReturn(System.currentTimeMillis());
		when(tag.getName()).thenReturn(name);
		when(tag.getUuid()).thenReturn(randomUUID());
		when(tag.getTagFamily()).thenReturn(tagFamily);
		return tag;
	}

	public static SchemaContainer mockSchemaContainer(String name) {
		SchemaContainer container = mock(SchemaContainerImpl.class);
		when(container.getName()).thenReturn(name);
		when(container.getUuid()).thenReturn(randomUUID());
		when(container.getSchema()).thenReturn(mockContentSchema());
		return container;
	}

	public static Schema mockContentSchema() {
		Schema schema = new SchemaImpl();
		schema.setName("content");
		schema.setDescription("Content schema");

		// basic types
		schema.addField(new StringFieldSchemaImpl().setName("string").setRequired(true));
		schema.addField(new NumberFieldSchemaImpl().setName("number").setRequired(true));
		schema.addField(new BooleanFieldSchemaImpl().setName("boolean").setRequired(true));
		schema.addField(new DateFieldSchemaImpl().setName("date").setRequired(true));
		schema.addField(new HtmlFieldSchemaImpl().setName("html").setRequired(true));
		schema.addField(new NodeFieldSchemaImpl().setName("node").setRequired(true));

		// lists types
		schema.addField(new ListFieldSchemaImpl().setListType("string").setName("stringList").setRequired(true));
		schema.addField(new ListFieldSchemaImpl().setListType("number").setName("numberList").setRequired(true));
		schema.addField(new ListFieldSchemaImpl().setListType("boolean").setName("booleanList").setRequired(true));
		schema.addField(new ListFieldSchemaImpl().setListType("date").setName("dateList").setRequired(true));
		schema.addField(new ListFieldSchemaImpl().setListType("html").setName("htmlList").setRequired(true));
		schema.addField(new ListFieldSchemaImpl().setListType("node").setName("nodeList").setRequired(true));

		return schema;
	}

	public static Node mockNode(Node parentNode, Project project, User user, Language language, Tag tagA, Tag tagB) {
		Node node = mock(NodeImpl.class);

		when(node.getParentNode()).thenReturn(parentNode);
		when(node.getProject()).thenReturn(project);

		List<? extends Tag> tagList = Arrays.asList(tagA, tagB);
		Mockito.<List<? extends Tag>> when(node.getTags()).thenReturn(tagList);

		SchemaContainer schemaContainer = mockSchemaContainer("content");
		when(node.getSchemaContainer()).thenReturn(schemaContainer);

		when(node.getCreator()).thenReturn(user);
		when(node.getEditor()).thenReturn(user);
		when(node.getUuid()).thenReturn(randomUUID());
		Schema schema = schemaContainer.getSchema();
		when(node.getSchema()).thenReturn(schema);

		NodeGraphFieldContainer container = mockContainer(language);
		Mockito.<List<? extends NodeGraphFieldContainer>> when(node.getGraphFieldContainers()).thenReturn(Arrays.asList(container));

		return node;
	}

	public static NodeGraphFieldContainer mockContainer(Language language) {
		NodeGraphFieldContainer container = mock(NodeGraphFieldContainerImpl.class);
		when(container.getLanguage()).thenReturn(language);

		// String field
		StringGraphField stringField = mock(StringGraphFieldImpl.class);
		when(stringField.getString()).thenReturn("The name value");
		when(container.getString("string")).thenReturn(stringField);

		// Number field
		NumberGraphField numberField = mock(NumberGraphFieldImpl.class);
		when(numberField.getNumber()).thenReturn("0.146");
		when(container.getNumber("number")).thenReturn(numberField);

		// Date field
		DateGraphField dateField = mock(DateGraphFieldImpl.class);
		when(dateField.getDate()).thenReturn(System.currentTimeMillis() / 1000);
		when(container.getDate("date")).thenReturn(dateField);

		// Boolean field
		BooleanGraphField booleanField = mock(BooleanGraphFieldImpl.class);
		when(booleanField.getBoolean()).thenReturn(true);
		when(container.getBoolean("boolean")).thenReturn(booleanField);

		// Node field
		NodeGraphField nodeField = mock(NodeGraphFieldImpl.class);
		Node nodeRef = mockNodeBasic("folder");
		when(nodeField.getNode()).thenReturn(nodeRef);
		when(container.getNode("node")).thenReturn(nodeField);

		// Html field
		HtmlGraphField htmlField = mock(HtmlGraphFieldImpl.class);
		when(htmlField.getHTML()).thenReturn("some<b>html");
		when(container.getHtml("html")).thenReturn(htmlField);

		//Node List Field
		NodeGraphFieldList nodeListField = mock(NodeGraphFieldListImpl.class);
		Mockito.<List<? extends NodeGraphField>> when(nodeListField.getList()).thenReturn(Arrays.asList(nodeField, nodeField, nodeField));
		when(container.getNodeList("nodeList")).thenReturn(nodeListField);

		// String List Field
		StringGraphFieldList stringListField = mock(StringGraphFieldListImpl.class);
		Mockito.<List<? extends StringGraphField>> when(stringListField.getList()).thenReturn(Arrays.asList(stringField, stringField, stringField));
		when(container.getStringList("stringList")).thenReturn(stringListField);

		// Boolean List Field
		BooleanGraphFieldList booleanListField = mock(BooleanGraphFieldListImpl.class);
		Mockito.<List<? extends BooleanGraphField>> when(booleanListField.getList())
				.thenReturn(Arrays.asList(booleanField, booleanField, booleanField));
		when(container.getBooleanList("booleanList")).thenReturn(booleanListField);

		// Date List Field
		DateGraphFieldList dateListField = mock(DateGraphFieldListImpl.class);
		Mockito.<List<? extends DateGraphField>> when(dateListField.getList()).thenReturn(Arrays.asList(dateField, dateField, dateField));
		when(container.getDateList("dateList")).thenReturn(dateListField);

		// Number List Field
		NumberGraphFieldList numberListField = mock(NumberGraphFieldListImpl.class);
		Mockito.<List<? extends NumberGraphField>> when(numberListField.getList()).thenReturn(Arrays.asList(numberField, numberField, numberField));
		when(container.getNumberList("numberList")).thenReturn(numberListField);

		// Html List Field
		HtmlGraphFieldList htmlListField = mock(HtmlGraphFieldListImpl.class);
		Mockito.<List<? extends HtmlGraphField>> when(htmlListField.getList()).thenReturn(Arrays.asList(htmlField, htmlField, htmlField));
		when(container.getHTMLList("htmlList")).thenReturn(htmlListField);

		//TODO add microschema and select fields

		return container;
	}
}