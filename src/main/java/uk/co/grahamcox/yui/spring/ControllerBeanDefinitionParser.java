/*
 * Copyright (C) 19/01/13 graham
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
package uk.co.grahamcox.yui.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import uk.co.grahamcox.yui.JsonModuleGroupLoader;
import uk.co.grahamcox.yui.LanguageModuleBuilder;
import uk.co.grahamcox.yui.Module;
import uk.co.grahamcox.yui.ModuleBuilder;
import uk.co.grahamcox.yui.ModuleGroup;
import uk.co.grahamcox.yui.ModuleGroupLoader;
import uk.co.grahamcox.yui.YuiConfigurer;
import uk.co.grahamcox.yui.YuiController;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ControllerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    /** The resource loader to use */
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    /**
     * Determine the bean class corresponding to the supplied {@link org.w3c.dom.Element}.
     * <p>Note that, for application classes, it is generally preferable to
     * override {@link #getBeanClassName} instead, in order to avoid a direct
     * dependence on the bean implementation class. The BeanDefinitionParser
     * and its NamespaceHandler can be used within an IDE plugin then, even
     * if the application classes are not available on the plugin's classpath.
     *
     * @param element the <code>Element</code> that is being parsed
     * @return the {@link Class} of the bean that is being defined via parsing
     *         the supplied <code>Element</code>, or <code>null</code> if none
     * @see #getBeanClassName
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return YuiController.class;
    }

    /**
     * Parse the supplied {@link org.w3c.dom.Element} and populate the supplied
     * {@link org.springframework.beans.factory.support.BeanDefinitionBuilder} as required.
     * <p>The default implementation does nothing.
     *
     * @param element the XML element being parsed
     * @param builder used to define the <code>BeanDefinition</code>
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        boolean combo = true;
        String comboAttr = element.getAttribute("combo");
        if (comboAttr != null && !comboAttr.isEmpty()) {
            combo = Boolean.parseBoolean(comboAttr);
        }

        String filterAttr = element.getAttribute("filter");
        if (filterAttr == null || filterAttr.isEmpty()) {
            filterAttr = "raw";
        }

        String urlBase = element.getAttribute("base");
        Map<String, String> groupElements = new HashMap<>();
        for (Element groupElement : DomUtils.getChildElementsByTagName(element, "group")) {
            String key = groupElement.getAttribute("name");
            String url = groupElement.getAttribute("base");
            groupElements.put(key, url);
        }

        try {
            Collection<ModuleGroup> groups = buildGroups(groupElements);

            builder.addPropertyValue("yuiConfigurer", buildConfigurer(filterAttr, combo, urlBase));
            builder.addPropertyValue("moduleBuilder", buildModuleBuilder(groups));
            builder.addPropertyValue("languageModuleBuilder", buildLanguageModuleBuilder(groups));
            builder.addPropertyValue("groups", groups);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Build the Yui Configurer to use
     * @param filter the filter settings to use
     * @param combo the combo settings to use
     * @param urlBase the URL Base that the servlet runs on
     * @return the yui Configurer
     */
    private YuiConfigurer buildConfigurer(String filter, boolean combo, String urlBase) {
        YuiConfigurer result = new YuiConfigurer();
        result.setComboSupported(combo);
        result.setFilter(filter);
        result.setLoaderBase(urlBase + (combo ? "/combo" : "/modules"));
        return result;
    }

    /**
     * Build the Module Builder to use
     * @param groups the groups to work with
     * @return the module builder
     */
    private ModuleBuilder buildModuleBuilder(Collection<ModuleGroup> groups) {
        ModuleBuilder moduleBuilder = new ModuleBuilder();
        moduleBuilder.setModuleGroups(groups);
        return moduleBuilder;
    }

    /**
     * Build the Module Builder to use
     * @param groups the groups to work with
     * @return the module builder
     */
    private LanguageModuleBuilder buildLanguageModuleBuilder(Collection<ModuleGroup> groups) {
        LanguageModuleBuilder moduleBuilder = new LanguageModuleBuilder();
        moduleBuilder.setModuleGroups(groups);
        return moduleBuilder;
    }

    /**
     * Build the groups to use
     * @param groups the map of group details
     * @return the groups to use
     */
    private Collection<ModuleGroup> buildGroups(Map<String, String> groups) throws IOException {
        Set<ModuleGroup> result = new HashSet<>();
        ModuleGroupLoader loader = new JsonModuleGroupLoader();
        for (String key : groups.keySet()) {
            Resource resource = resourceLoader.getResource(groups.get(key));
            result.add(loader.load(key, resource.getURL()));
        }
        return result;
    }
}
