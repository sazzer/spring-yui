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

import uk.co.grahamcox.yui.ModuleGroup;
import uk.co.grahamcox.yui.JsonModuleGroupLoader;
import uk.co.grahamcox.yui.Module;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Unit tests for the JSON Module Group Loader
 */
public class JsonModuleGroupLoaderTest {
    @Test
    public void testLoad() throws IOException {
        URL url = getClass().getResource("/uk/co/grahamcox/yui/test-group");
        JsonModuleGroupLoader loader = new JsonModuleGroupLoader();
        ModuleGroup group = loader.load("test", url);
        Assert.assertNotNull(group);
        Assert.assertEquals("test", group.getName());
        Assert.assertEquals("/test", group.getBase());
        Assert.assertEquals(3, group.getModules().size());

        List<Module> modules = new ArrayList<Module>(group.getModules());
        Collections.sort(modules, new Comparator<Module>() {
            @Override
            public int compare(Module o1, Module o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Module module = modules.get(0);
        Assert.assertEquals("module-a", module.getName());
        Assert.assertEquals("/module-a", module.getBase());
        Assert.assertEquals("1.0.0", module.getVersion());
        Assert.assertEquals(3, module.getDependencies().size());
        Assert.assertTrue(module.getDependencies().contains("module-b"));
        Assert.assertTrue(module.getDependencies().contains("module-c"));
        Assert.assertTrue(module.getDependencies().contains("intl"));
        Assert.assertEquals(1, module.getFiles().size());
        Assert.assertTrue(module.getFiles().contains(new URL(url + "/module-a/module-a.js")));
        Assert.assertEquals(2, module.getLanguages().size());
        Assert.assertTrue(module.getLanguages().contains("en"));
        Assert.assertTrue(module.getLanguages().contains("fr"));

        module = modules.get(1);
        Assert.assertEquals("module-b", module.getName());
        Assert.assertEquals("/module-b", module.getBase());
        Assert.assertEquals("2", module.getVersion());
        Assert.assertEquals(2, module.getDependencies().size());
        Assert.assertTrue(module.getDependencies().contains("module-c"));
        Assert.assertTrue(module.getDependencies().contains("widget"));
        Assert.assertEquals(1, module.getFiles().size());
        Assert.assertTrue(module.getFiles().contains(new URL(url + "/module-b/module-b.js")));
        Assert.assertEquals(0, module.getLanguages().size());

        module = modules.get(2);
        Assert.assertEquals("module-c", module.getName());
        Assert.assertEquals("/module-c", module.getBase());
        Assert.assertEquals("1.0.0", module.getVersion());
        Assert.assertEquals(1, module.getDependencies().size());
        Assert.assertTrue(module.getDependencies().contains("base"));
        Assert.assertEquals(1, module.getFiles().size());
        Assert.assertTrue(module.getFiles().contains(new URL(url + "/c/c.js")));
        Assert.assertEquals(0, module.getLanguages().size());
    }
}
