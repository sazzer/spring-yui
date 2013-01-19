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

import uk.co.grahamcox.yui.ModuleGroup;
import uk.co.grahamcox.yui.JsonModuleGroupLoader;
import uk.co.grahamcox.yui.YuiConfigurer;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Collections;

/**
 * Tests for the YUI Loader
 */
public class YuiConfigurerTest {
    /** The configurer to use */
    private YuiConfigurer configurer;

    /**
     * Create the configurer
     */
    @Before
    public void setup() {
        configurer = new YuiConfigurer();
        configurer.setLoaderBase("/yui");
    }

    /**
     * Test loading the test-group json file and using that
     */
    @Test
    public void testUsingTestGroupFile() throws Exception {
        JsonModuleGroupLoader loader = new JsonModuleGroupLoader();
        URL moduleUrl = getClass().getResource("/uk/co/grahamcox/yui/test-group");
        URL expectedUrl = getClass().getResource("/uk/co/grahamcox/yui/test-group/loader-config-without-comboing.json");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.readValue(expectedUrl, ObjectNode.class);

        ModuleGroup moduleGroup = loader.load("test", moduleUrl);

        JsonNode node = configurer.buildLoaderConfig(Collections.singleton(moduleGroup));
        Assert.assertNotNull(node);
        Assert.assertEquals(rootNode, node);
    }
}
