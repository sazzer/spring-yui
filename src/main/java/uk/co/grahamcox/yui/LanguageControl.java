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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * The Resource Bundle Control object to use to load messages
 */
public class LanguageControl extends ResourceBundle.Control {
    /**
     * Returns a <code>List</code> of <code>String</code>s containing
     * formats to be used to load resource bundles for the given
     * <code>baseName</code>. The <code>ResourceBundle.getBundle</code>
     * factory method tries to load resource bundles with formats in the
     * order specified by the list. The list returned by this method
     * must have at least one <code>String</code>. The predefined
     * formats are <code>"java.class"</code> for class-based resource
     * bundles and <code>"java.properties"</code> for {@linkplain
     * java.util.PropertyResourceBundle properties-based} ones. Strings starting
     * with <code>"java."</code> are reserved for future extensions and
     * must not be used by application-defined formats.
     * <p/>
     * <p>It is not a requirement to return an immutable (unmodifiable)
     * <code>List</code>.  However, the returned <code>List</code> must
     * not be mutated after it has been returned by
     * <code>getFormats</code>.
     * <p/>
     * <p>The default implementation returns {@link #FORMAT_DEFAULT} so
     * that the <code>ResourceBundle.getBundle</code> factory method
     * looks up first class-based resource bundles, then
     * properties-based ones.
     *
     * @param baseName the base name of the resource bundle, a fully qualified class
     *                 name
     * @return a <code>List</code> of <code>String</code>s containing
     *         formats for loading resource bundles.
     * @throws NullPointerException if <code>baseName</code> is null
     * @see #FORMAT_DEFAULT
     * @see #FORMAT_CLASS
     * @see #FORMAT_PROPERTIES
     */
    @Override
    public List<String> getFormats(String baseName) {
        return Arrays.asList("properties");
    }

    /**
     * Instantiates a resource bundle for the given bundle name of the
     * given format and locale, using the given class loader if
     * necessary. This method returns <code>null</code> if there is no
     * resource bundle available for the given parameters. If a resource
     * bundle can't be instantiated due to an unexpected error, the
     * error must be reported by throwing an <code>Error</code> or
     * <code>Exception</code> rather than simply returning
     * <code>null</code>.
     * <p/>
     * <p>If the <code>reload</code> flag is <code>true</code>, it
     * indicates that this method is being called because the previously
     * loaded resource bundle has expired.
     * <p/>
     * <p>The default implementation instantiates a
     * <code>ResourceBundle</code> as follows.
     * <p/>
     * <ul>
     * <p/>
     * <li>The bundle name is obtained by calling {@link
     * #toBundleName(String, java.util.Locale) toBundleName(baseName,
     * locale)}.</li>
     * <p/>
     * <li>If <code>format</code> is <code>"java.class"</code>, the
     * {@link Class} specified by the bundle name is loaded by calling
     * {@link ClassLoader#loadClass(String)}. Then, a
     * <code>ResourceBundle</code> is instantiated by calling {@link
     * Class#newInstance()}.  Note that the <code>reload</code> flag is
     * ignored for loading class-based resource bundles in this default
     * implementation.</li>
     * <p/>
     * <li>If <code>format</code> is <code>"java.properties"</code>,
     * {@link #toResourceName(String, String) toResourceName(bundlename,
     * "properties")} is called to get the resource name.
     * If <code>reload</code> is <code>true</code>, {@link
     * ClassLoader#getResource(String) load.getResource} is called
     * to get a {@link java.net.URL} for creating a {@link
     * java.net.URLConnection}. This <code>URLConnection</code> is used to
     * {@linkplain java.net.URLConnection#setUseCaches(boolean) disable the
     * caches} of the underlying resource loading layers,
     * and to {@linkplain java.net.URLConnection#getInputStream() get an
     * <code>InputStream</code>}.
     * Otherwise, {@link ClassLoader#getResourceAsStream(String)
     * loader.getResourceAsStream} is called to get an {@link
     * java.io.InputStream}. Then, a {@link
     * java.util.PropertyResourceBundle} is constructed with the
     * <code>InputStream</code>.</li>
     * <p/>
     * <li>If <code>format</code> is neither <code>"java.class"</code>
     * nor <code>"java.properties"</code>, an
     * <code>IllegalArgumentException</code> is thrown.</li>
     * <p/>
     * </ul>
     *
     * @param baseName the base bundle name of the resource bundle, a fully
     *                 qualified class name
     * @param locale   the locale for which the resource bundle should be
     *                 instantiated
     * @param format   the resource bundle format to be loaded
     * @param loader   the <code>ClassLoader</code> to use to load the bundle
     * @param reload   the flag to indicate bundle reloading; <code>true</code>
     *                 if reloading an expired resource bundle,
     *                 <code>false</code> otherwise
     * @return the resource bundle instance,
     *         or <code>null</code> if none could be found.
     * @throws NullPointerException        if <code>bundleName</code>, <code>locale</code>,
     *                                     <code>format</code>, or <code>loader</code> is
     *                                     <code>null</code>, or if <code>null</code> is returned by
     *                                     {@link #toBundleName(String, java.util.Locale) toBundleName}
     * @throws IllegalArgumentException    if <code>format</code> is unknown, or if the resource
     *                                     found for the given parameters contains malformed data.
     * @throws ClassCastException          if the loaded class cannot be cast to <code>ResourceBundle</code>
     * @throws IllegalAccessException      if the class or its nullary constructor is not
     *                                     accessible.
     * @throws InstantiationException      if the instantiation of a class fails for some other
     *                                     reason.
     * @throws ExceptionInInitializerError if the initialization provoked by this method fails.
     * @throws SecurityException           If a security manager is present and creation of new
     *                                     instances is denied. See {@link Class#newInstance()}
     *                                     for details.
     * @throws java.io.IOException         if an error occurred when reading resources using
     *                                     any I/O operations
     */
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        StringBuilder bundleFilename = new StringBuilder(baseName);
        if (locale != null && !locale.toString().isEmpty()) {
            bundleFilename.append("_").append(locale.toString());
        }
        bundleFilename.append(".").append(format);
        URL url = new URL(bundleFilename.toString());
        InputStream inputStream = url.openStream();
        try {
            InputStreamReader reader = new InputStreamReader(inputStream);
            PropertyResourceBundle resourceBundle = new PropertyResourceBundle(reader);
            return resourceBundle;
        }
        finally {
            inputStream.close();
        }
    }
}
