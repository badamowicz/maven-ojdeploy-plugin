/*  _______________________________________
 * < Maven OJDeploy Plugin                 >
 * < Copyright 2014 Bernd Adamowicz        >
 * < mailto:info AT bernd-adamowicz DOT de >
 *  ---------------------------------------
 *  \
 *   \   \_\_    _/_/
 *    \      \__/
 *           (oo)\_______
 *           (__)\       )\/\
 *               ||----w |
 *               ||     ||
 *
 * Maven OJDeploy Plugin is free software: you can redistribute it and/or modify
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
package com.github.badamowicz.maven.ojaudit.plugin.executor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.badamowicz.maven.ojaudit.plugin.mojos.OjdeployMojo;

/**
 * Test cases for {@link OjdeployExecutor}.
 * 
 * @author bernd
 *
 */
public class OjdeployExecutorTest {

    private OjdeployExecutor     executor = null;
    private OjdeployMojo         mojo     = null;
    private static final boolean DRY_RUN  = true;

    @BeforeClass
    public void beforeClass() {

        mojo = new OjdeployMojo();
        executor = new OjdeployExecutor();
        executor.setDryRun(DRY_RUN);
        executor.setMojo(mojo);
    }

    @Test
    public void getMojo() {

        assertSame(executor.getMojo(), mojo, "Not the same Mojo returned!");
    }

    @Test
    public void getProps() {

        assertNotNull(executor.getProps(), "Properties not initialized!");
    }

    @Test
    public void isDryRun() {

        assertEquals(executor.isDryRun(), DRY_RUN, "Dry run value set wrong!");
    }
}
