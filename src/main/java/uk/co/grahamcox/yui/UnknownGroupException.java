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
 * Exception thrown if the requested YUI Group is unknown
 */
public class UnknownGroupException extends RuntimeException {
    /** The group that was requested */
    private String group;

    /**
     * Create the exception
     * @param group the group that was requested
     */
    public UnknownGroupException(String group) {
        super("Unknown YUI Group requested: " + group);
        this.group = group;
    }

    /**
     * Gets The group that was requested.
     *
     * @return Value of The group that was requested.
     */
    public String getGroup() {
        return group;
    }
}
