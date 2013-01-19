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

/**
 * Exception thrown if the requested YUI Module is unknown
 */
public class UnknownModuleException extends RuntimeException {
    /** The group that was requested */
    private String group;
    /** The module that was requested */
    private String module;

    /**
     * Create the exception
     * @param group the group that was requested
     */
    public UnknownModuleException(String group, String module) {
        super("Unknown YUI Module " + module + " requested from group: " + group);
        this.group = group;
        this.module = module;
    }

    /**
     * Gets The group that was requested.
     *
     * @return Value of The group that was requested.
     */
    public String getGroup() {
        return group;
    }

    /**
     * Gets The module that was requested.
     *
     * @return Value of The module that was requested.
     */
    public String getModule() {
        return module;
    }
}
