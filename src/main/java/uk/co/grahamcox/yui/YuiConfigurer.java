/*
 * Copyright (C) 15/12/12 graham
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
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import java.util.Collection;

/**
 * Mechanism to produce the Yui Loader configuration
 */
public class YuiConfigurer {
    /** The key for the Filter option */
    private static final String FILTER_KEY = "filter";
    /** The key for the groups */
    private static final String GROUPS_KEY = "groups";
    /** The key for the Comboing option */
    private static final String COMBINE_KEY = "combine";
    /** The key for the base of a group */
    private static final String GROUP_BASE_KEY = "base";
    /** The key for the modules of a group */
    private static final String MODULES_KEY = "modules";
    /** The key for the dependencies of a module */
    private static final String MODULE_DEPENDENCIES_KEY = "requires";
    public static final String PATH_SEPARATOR = "/";
    public static final String COMBO_BASE_KEY = "comboBase";
    public static final String COMBO_ROOT_KEY = "root";

    /** Flag to determine if Comboing should be supported or not */
    private boolean comboSupported;
    /** The base path of the YUI Loader/Combo Handler */
    private String loaderBase;
    /** The filter to request */
    private String filter = "raw";

    /**
     * Build the JSON Node that represents the configuration for the given set of groups
     * @param groups the groups
     * @return the JSON Node for the config
     */
    public JsonNode buildLoaderConfig(Collection<ModuleGroup> groups) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put(FILTER_KEY, filter);
        for (ModuleGroup group : groups) {
            ObjectNode groupNode = rootNode.with(GROUPS_KEY).with(group.getName());
            groupNode.put(COMBINE_KEY, comboSupported);
            if (comboSupported) {
                groupNode.put(COMBO_BASE_KEY, loaderBase + group.getBase() + "?");
                groupNode.put(COMBO_ROOT_KEY, "");
            }
            else {
                groupNode.put(GROUP_BASE_KEY, loaderBase + group.getBase() + PATH_SEPARATOR);
            }
            for (Module module : group.getModules()) {
                ObjectNode moduleNode = groupNode.with(MODULES_KEY).with(module.getName());
                ArrayNode requiresNode = moduleNode.putArray(MODULE_DEPENDENCIES_KEY);
                for (String dependency : module.getDependencies()) {
                    requiresNode.add(dependency);
                }
            }
        }
        return rootNode;
    }

    /**
     * Gets The base path of the YUI LoaderCombo Handler.
     *
     * @return Value of The base path of the YUI LoaderCombo Handler.
     */
    public String getLoaderBase() {
        return loaderBase;
    }

    /**
     * Gets Flag to determine if Comboing should be supported or not.
     *
     * @return Value of Flag to determine if Comboing should be supported or not.
     */
    public boolean isComboSupported() {
        return comboSupported;
    }

    /**
     * Sets new Flag to determine if Comboing should be supported or not.
     *
     * @param comboSupported New value of Flag to determine if Comboing should be supported or not.
     */
    public void setComboSupported(boolean comboSupported) {
        this.comboSupported = comboSupported;
    }

    /**
     * Sets new The base path of the YUI LoaderCombo Handler.
     *
     * @param loaderBase New value of The base path of the YUI LoaderCombo Handler.
     */
    public void setLoaderBase(String loaderBase) {
        this.loaderBase = loaderBase;
    }

    /**
     * Sets new The filter to request.
     *
     * @param filter New value of The filter to request.
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /**
     * Gets The filter to request.
     *
     * @return Value of The filter to request.
     */
    public String getFilter() {
        return filter;
    }
}
