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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller to give access to YUI modules
 */
@Controller
public class YuiController {
    /** The content type to use */
    private static final String CONTENT_TYPE = "text/javascript";
    /** The character set to use */
    private static final String CHARSET = "utf-8";
    /** Pattern to get the module name and filter out of the query string for javascript modules */
    private static final Pattern COMBO_MODULE_PATTERN = Pattern.compile("(.*)\\/\\1(-(.*))?\\.js");
    /** Pattern to get the module name and filter out of the query string for language modules */
    private static final Pattern COMBO_LANGUAGE_PATTERN = Pattern.compile("(.*)\\/lang\\/\\1(_(.*?))?(-(.*))?\\.js");
    /** the module groups to support */
    @NotNull
    @Size(min=1)
    private Collection<ModuleGroup> groups;
    /** The means to generate YUI configuration */
    @NotNull
    private YuiConfigurer yuiConfigurer;
    /** The means to generate YUI module files */
    @NotNull
    private ModuleBuilder moduleBuilder;
    /** The means to generate YUI language files */
    @NotNull
    private LanguageModuleBuilder languageModuleBuilder;

    /**
     * Sets new The means to generate YUI configuration.
     *
     * @param yuiConfigurer New value of The means to generate YUI configuration.
     */
    public void setYuiConfigurer(YuiConfigurer yuiConfigurer) {
        this.yuiConfigurer = yuiConfigurer;
    }

    /**
     * Sets new the module groups to support.
     *
     * @param groups New value of the module groups to support.
     */
    public void setGroups(Collection<ModuleGroup> groups) {
        this.groups = groups;
    }

    /**
     * Sets new The means to generate YUI module files.
     *
     * @param moduleBuilder New value of The means to generate YUI module files.
     */
    public void setModuleBuilder(ModuleBuilder moduleBuilder) {
        this.moduleBuilder = moduleBuilder;
    }

    /**
     * Set the language module builder to use
     * @param languageModuleBuilder the language module builder to use
     */
    public void setLanguageModuleBuilder(LanguageModuleBuilder languageModuleBuilder) {
        this.languageModuleBuilder = languageModuleBuilder;
    }

    /**
     * Get the YUI configuration
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/config.js")
    public void getYuiConfig(HttpServletResponse response) throws IOException {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARSET);
        JsonNode config = yuiConfigurer.buildLoaderConfig(groups);
        response.getWriter().write("YUI_config = " + config.toString() + ";");
    }

    /**
     * Perform the combo request for a set of modules
     * @param group the group to request
     * @param webRequest the means to get the module list
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/combo/{group}")
    public void getComboModules(@PathVariable("group") String group, WebRequest webRequest,
                                HttpServletResponse response) throws IOException {
        Iterator<String> names = webRequest.getParameterNames();
        boolean missing = false;
        while (names.hasNext()) {
            String next = names.next();
            Matcher matcher = COMBO_MODULE_PATTERN.matcher(next);
            if (matcher.matches()) {
                String moduleName = matcher.group(1);
                Filter filter = Filter.RAW;
                if (matcher.group(3) != null) {
                    switch (matcher.group(3)) {
                        case "debug":
                            filter = Filter.DEBUG;
                            break;
                        case "min":
                            filter = Filter.MINIFY;
                            break;
                    }
                }
                getModule(group, moduleName, filter, response);
            }
            else {
                matcher = COMBO_LANGUAGE_PATTERN.matcher(next);
                if (matcher.matches()) {
                    String moduleName = matcher.group(1);
                    String language = matcher.group(3);
                    Filter filter = Filter.RAW;
                    if (matcher.group(5) != null) {
                        switch (matcher.group(5)) {
                            case "debug":
                                filter = Filter.DEBUG;
                                break;
                            case "min":
                                filter = Filter.MINIFY;
                                break;
                        }
                    }
                    getModuleLanguage(group, moduleName, language, filter, response);
                }
                else {
                    missing = true;
                }
            }
        }
        if (missing) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No valid modules were requested");
        }
    }

    /**
     * Get the RAW version of the module
     * @param group the group of the module
     * @param module the module itself
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/modules/{group}/{module}/{module}.js")
    public void getRawFile(@PathVariable("group") String group, @PathVariable("module") String module,
                           HttpServletResponse response) throws IOException {
        getModule(group, module, Filter.RAW, response);
    }

    /**
     * Get the RAW version of the module
     * @param group the group of the module
     * @param module the module itself
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/modules/{group}/{module}/{module}-debug.js")
    public void getDebugFile(@PathVariable("group") String group, @PathVariable("module") String module,
                             HttpServletResponse response) throws IOException {
        getModule(group, module, Filter.DEBUG, response);
    }

    /**
     * Get the RAW version of the module
     * @param group the group of the module
     * @param module the module itself
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/modules/{group}/{module}/{module}-min.js")
    public void getMinFile(@PathVariable("group") String group, @PathVariable("module") String module,
                           HttpServletResponse response) throws IOException {
        getModule(group, module, Filter.MINIFY, response);
    }

    /**
     * Get the RAW version of the module
     * @param group the group of the module
     * @param module the module itself
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/modules/{group}/{module}/lang/{module}_{lang}.js")
    public void getRawLanguageFile(@PathVariable("group") String group, @PathVariable("module") String module,
                                   @PathVariable("lang") String lang, HttpServletResponse response) throws IOException {
        getModuleLanguage(group, module, lang, Filter.RAW, response);
    }

    /**
     * Get the RAW version of the module
     * @param group the group of the module
     * @param module the module itself
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/modules/{group}/{module}/lang/{module}_{lang}-debug.js")
    public void getDebugLanguageFile(@PathVariable("group") String group, @PathVariable("module") String module,
                                     @PathVariable("lang") String lang, HttpServletResponse response)
            throws IOException {
        getModuleLanguage(group, module, lang, Filter.DEBUG, response);
    }

    /**
     * Get the RAW version of the module
     * @param group the group of the module
     * @param module the module itself
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    @RequestMapping("/modules/{group}/{module}/lang/{module}_{lang}-min.js")
    public void getMinLanguageFile(@PathVariable("group") String group, @PathVariable("module") String module,
                                   @PathVariable("lang") String lang, HttpServletResponse response) throws IOException {
        getModuleLanguage(group, module, lang, Filter.MINIFY, response);
    }

    /**
     * Handle the boilerplate of getting the contents of a module
     * @param group the group of the module
     * @param module the module itself
     * @param filter the filter to apply
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    private void getModule(String group, String module, Filter filter, HttpServletResponse response)
            throws IOException {
        String contents = moduleBuilder.getModuleFile(group, module);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARSET);
        response.getWriter().write(contents);
    }

    /**
     * Handle the boilerplate of getting the language file for a module
     * @param group the group of the module
     * @param module the module itself
     * @param language the language to get
     * @param filter the filter to apply
     * @param response the response to write to
     * @throws IOException if an error occurs
     */
    private void getModuleLanguage(String group, String module, String language, Filter filter,
                                   HttpServletResponse response) throws IOException {
        String contents = languageModuleBuilder.getModuleFile(group, module, language);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARSET);
        response.getWriter().write(contents);
    }
}
