/*
 * Copyright (C) 14/12/12 graham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.grahamcox.yui;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Loader that loads a JSON file of Module group definitions
 */
public class JsonModuleGroupLoader implements ModuleGroupLoader {
    /** The logger to use */
    private static final Logger LOG = LoggerFactory.getLogger(JsonModuleGroupLoader.class);
    public static final String DEFAULT_VERSION = "1.0.0";
    public static final String BASE_PREFIX = "/";
    public static final String PATH_SEPARATOR = "/";
    public static final String JS_EXTENSION = ".js";
    public static final String MESSAGES_FILE = "/lang/messages";

    /**
     * Load the definitions from the provided URL
     *
     * @param name the name of the group
     * @param base the URL to load from
     * @return the ModuleGroup
     * @throws java.io.IOException if an error occurs
     */
    @Override
    public ModuleGroup load(String name, URL base) throws IOException {
        URL url = new URL(base.toString() + "/modules.json");
        LOG.info("Loading group {} from file {}", name, url);
        ModuleGroup group = new ModuleGroup();
        group.setName(name);
        group.setBase("/" + name);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.readValue(url, ObjectNode.class);

        Set<Module> modules = new HashSet<>();
        Iterator<Map.Entry<String, JsonNode>> iter = rootNode.getFields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> next = iter.next();
            String moduleName = next.getKey();
            JsonNode nextNode = next.getValue();
            if (nextNode instanceof ObjectNode) {
                modules.add(build(moduleName, base, (ObjectNode)nextNode));
            }
            else {
                LOG.warn("Found non-object node at module level: {}", moduleName);
            }
        }
        group.setModules(modules);
        return group;
    }

    /**
     * Build a Module from a provided JSON Node
     * @param name the name of the module
     * @param base the base URL of the group
     * @param moduleNode the JSON node representing the module
     * @return the module
     * @throws MalformedURLException if the URL of a file is invalid
     */
    private Module build(String name, URL base, ObjectNode moduleNode) throws MalformedURLException {
        Module module = new Module();
        module.setName(name);
        module.setBase(BASE_PREFIX + name);
        String version = DEFAULT_VERSION;
        JsonNode versionNode = moduleNode.get("version");
        if (versionNode != null) {
            if (versionNode instanceof TextNode) {
                version = versionNode.asText();
            }
            else {
                LOG.warn("Entry 'version' in module {} is not a text node", name);
            }
        }
        module.setVersion(version);

        Set<String> languages = new HashSet<>();
        languages.addAll(loadStringArray("lang", name, moduleNode));
        module.setLanguages(languages);

        Set<String> dependencies = new HashSet<>();
        if (!languages.isEmpty()) {
            dependencies.add("intl");
        }
        dependencies.addAll(loadStringArray("depends", name, moduleNode));
        module.setDependencies(dependencies);

        List<String> files = new ArrayList<>();
        files.addAll(loadStringArray("files", name, moduleNode));
        if (files.isEmpty()) {
            // Np file defined, so we make a single filename based on the module name
            String moduleFile = name + PATH_SEPARATOR + name + JS_EXTENSION;
            files.add(moduleFile);
        }

        List<URL> urls = new ArrayList<>();
        for (String file : files) {
            if (file.startsWith(PATH_SEPARATOR)) {
                urls.add(new URL(base.toString() + file));
            }
            else {
                urls.add(new URL(base.toString() + PATH_SEPARATOR + file));
            }
        }
        module.setFiles(urls);

        module.setMessagesFile(new URL(base.toString() + PATH_SEPARATOR + name + MESSAGES_FILE));
        return module;
    }

    /**
     * Load the contents of the requested string array
     * @param elementName the name of the array to load
     * @param moduleName the name of the module
     * @param moduleNode the node containing the array
     * @return the contents of the array
     */
    private Collection<String> loadStringArray(String elementName, String moduleName, ObjectNode moduleNode) {
        List<String> target = new ArrayList<>();
        JsonNode dependsNode = moduleNode.get(elementName);
        if (dependsNode != null) {
            if (dependsNode instanceof ArrayNode) {
                ArrayNode dependsArray = (ArrayNode)dependsNode;
                for (int i = 0; i < dependsArray.size(); ++i) {
                    if (dependsArray.get(i) instanceof TextNode) {
                        target.add(dependsArray.get(i).asText());
                    }
                    else {
                        LOG.warn("Entry {} in {} of module {} is not a text node", i, elementName, moduleName);
                    }
                }
            }
            else {
                LOG.warn("Node '{}' in module '{}' is not an Array", elementName, moduleName);
            }
        }
        return target;
    }
}
