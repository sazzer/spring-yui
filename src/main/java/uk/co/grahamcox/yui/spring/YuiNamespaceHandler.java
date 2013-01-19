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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class YuiNamespaceHandler extends NamespaceHandlerSupport {
    /**
     * Invoked by the {@link org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader} after
     * construction but before any custom elements are parsed.
     *
     * @see org.springframework.beans.factory.xml.NamespaceHandlerSupport#registerBeanDefinitionParser(String,
     * org.springframework.beans.factory.xml.BeanDefinitionParser)
     */
    @Override
    public void init() {
       registerBeanDefinitionParser("controller", new ControllerBeanDefinitionParser());
    }
}
