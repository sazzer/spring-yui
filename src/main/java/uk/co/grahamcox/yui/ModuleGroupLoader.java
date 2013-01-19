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

import java.io.IOException;
import java.net.URL;

/**
 * Loader to load a ModuleGroup from a data file
 */
public interface ModuleGroupLoader {
    /**
     * Load the definitions from the provided URL
     * @param name The name of the group
     * @param url the URL to load from
     * @return the ModuleGroup
     * @throws IOException if an error occurs
     */
    public ModuleGroup load(String name, URL url) throws IOException;
}
