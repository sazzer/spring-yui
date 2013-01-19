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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The means to actually build a Module file
 */
public class ModuleBuilder {
    /** The collection of module groups we support */
    private Map<String, ModuleGroup> groups;

    /**
     * Set the module groups
     * @param groups the module groups
     */
    public void setModuleGroups(Collection<ModuleGroup> groups) {
        this.groups = new HashMap<>();
        for (ModuleGroup group : groups) {
            this.groups.put(group.getName(), group);
        }
    }

    /**
     * Get the module file requested
     * @param group the group of the module
     * @param file the file of the module
     * @return the contents of the module
     * @throws IOException if an error occurs loading module content
     */
    public String getModuleFile(String group, String file) throws IOException {
        if (groups.containsKey(group)) {
            ModuleGroup moduleGroup = groups.get(group);
            Module module = moduleGroup.findModule(file);
            if (module != null) {
                StringBuilder moduleOutput = new StringBuilder();
                moduleOutput.append("YUI.add('").append(module.getName()).append("', function (Y, NAME) {\n");
                for (URL moduleFile : module.getFiles()) {
                    moduleOutput.append(getFileContents(moduleFile));
                }
                moduleOutput.append("}, '").append(module.getVersion()).append("', {'requires': [");
                boolean isFirst = true;
                for (String dependency : module.getDependencies()) {
                    if (!isFirst) {
                        moduleOutput.append(",");
                    }
                    isFirst = false;
                    moduleOutput.append("'").append(dependency).append("'");
                }
                moduleOutput.append("]});\n");
                return moduleOutput.toString();
            }
            else {
                throw new UnknownModuleException(group, file);
            }
        }
        else {
            throw new UnknownGroupException(group);
        }
    }

    /**
     * Get the contents of the given URL
     * @param url the URL
     * @return the contents
     * @throws IOException if an error occurs
     */
    private String getFileContents(URL url) throws IOException {
        InputStream stream = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        stream.close();
        return output.toString();
    }
}
