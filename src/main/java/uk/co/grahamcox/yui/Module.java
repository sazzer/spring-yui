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
import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 * Representation of a single YUI module
 */
public class Module {
    /** The name of the module */
    @NotNull
    @Size(min=1)
    private String name;
    /** The module version */
    @NotNull
    @Size(min=1)
    private String version;
    /** The base directory of the module */
    @NotNull
    @Size(min=1)
    private String base;
    /** The files in the module */
    @NotNull
    @Size(min=1)
    private List<URL> files;
    /** The module dependencies */
    @NotNull
    private Collection<String> dependencies;

    /**
     * Sets new The name of the module.
     *
     * @param name New value of The name of the module.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets new The files in the module.
     *
     * @param files New value of The files in the module.
     */
    public void setFiles(List<URL> files) {
        this.files = files;
    }

    /**
     * Sets new The module dependencies.
     *
     * @param dependencies New value of The module dependencies.
     */
    public void setDependencies(Collection<String> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Gets The files in the module.
     *
     * @return Value of The files in the module.
     */
    public List<URL> getFiles() {
        return files;
    }

    /**
     * Sets new The base directory of the module.
     *
     * @param base New value of The base directory of the module.
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     * Gets The name of the module.
     *
     * @return Value of The name of the module.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets The base directory of the module.
     *
     * @return Value of The base directory of the module.
     */
    public String getBase() {
        return base;
    }

    /**
     * Gets The module version.
     *
     * @return Value of The module version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets new The module version.
     *
     * @param version New value of The module version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets The module dependencies.
     *
     * @return Value of The module dependencies.
     */
    public Collection<String> getDependencies() {
        return dependencies;
    }
}
