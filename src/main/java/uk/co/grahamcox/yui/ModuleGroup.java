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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Representation of a group of modules
 */
public class ModuleGroup {
    /** The name of the group */
    @NotNull
    @Size(min=1)
    private String name;

    /** The URL base of the group */
    @NotNull
    @Size(min=1)
    private String base;

    /** The collection of modules in the group */
    @NotNull
    @Size(min=1)
    private Collection<Module> modules;

    /**
     * Gets The URL base of the group.
     *
     * @return Value of The URL base of the group.
     */
    public String getBase() {
        return base;
    }

    /**
     * Gets The name of the group.
     *
     * @return Value of The name of the group.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets new The URL base of the group.
     *
     * @param base New value of The URL base of the group.
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * Sets new The collection of modules in the group.
     *
     * @param modules New value of The collection of modules in the group.
     */
    public void setModules(Collection<Module> modules) {
        this.modules = modules;
    }

    /**
     * Gets The collection of modules in the group.
     *
     * @return Value of The collection of modules in the group.
     */
    public Collection<Module> getModules() {
        return modules;
    }

    /**
     * Sets new The name of the group.
     *
     * @param name New value of The name of the group.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Find the module with the given name in this group
     * @param name the name
     * @return the module
     */
    public Module findModule(String name) {
        Module module = null;
        for (Module m : modules) {
            if (name.equals(m.getName())) {
                module = m;
                break;
            }
        }
        return module;
    }
}
