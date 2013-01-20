/*
 * Copyright (C) 20/01/13 graham
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

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Mechanism to build the language file for a module
 */
public class LanguageModuleBuilder {
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
     * @throws java.io.IOException if an error occurs loading module content
     */
    public String getModuleFile(String group, String file, String language) throws IOException {
        if (groups.containsKey(group)) {
            ModuleGroup moduleGroup = groups.get(group);
            Module module = moduleGroup.findModule(file);
            if (module != null) {
                StringBuilder moduleOutput = new StringBuilder();
                moduleOutput.append("YUI.add('lang/").append(module.getName()).append("_").append(language)
                        .append("', function (Y) {\n");
                moduleOutput.append("Y.Intl.add(\n");
                moduleOutput.append("'").append(module.getName()).append("',\n");
                moduleOutput.append("'").append(language).append("',\n");
                moduleOutput.append("{\n");

                String resourceKey = module.getMessagesFile().toString();
                Locale locale = Locale.forLanguageTag(language);

                ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceKey, locale, new LanguageControl());
                Enumeration<String> keys = resourceBundle.getKeys();
                boolean isFirst = true;
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    String value = resourceBundle.getString(key);
                    if (!isFirst) {
                        moduleOutput.append(",");
                    }
                    isFirst = false;
                    moduleOutput.append("'").append(key).append("': '").append(value).append("'");
                }

                moduleOutput.append("});\n");
                moduleOutput.append("}, '").append(module.getVersion()).append("');");
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
}
