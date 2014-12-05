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
package com.github.badamowicz.maven.ojaudit.plugin.mojos;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.badamowicz.maven.ojaudit.plugin.helper.AbstractOjdeployHelper;

/**
 * Test cases for {@link OjdeployMojo}.
 * 
 * @author bernd
 *
 */
public class OjdeployMojoTest extends AbstractOjdeployHelper {

    private OjdeployMojo messyMojo1 = null;
    private OjdeployMojo messyMojo2 = null;
    private OjdeployMojo messyMojo3 = null;

    @BeforeClass
    public void beforeClass() {

        prepareDefaultMojo();
        prepareMessyMojos();
    }

    /**
     * Provide some Mojos which are configured with mutual exclusive arguments.
     */
    private void prepareMessyMojos() {

        messyMojo1 = new OjdeployMojo();
        messyMojo2 = new OjdeployMojo();
        messyMojo3 = new OjdeployMojo();

        messyMojo1.setBuildFileSchema(BUILD_FILE_SCHEMA);
        messyMojo1.setBuildFile(BUILD_FILE);

        messyMojo2.setBuildFileSchema(BUILD_FILE_SCHEMA);
        messyMojo2.setProfile(PROFILE_FILE);

        messyMojo3.setBuildFile(BUILD_FILE);
        messyMojo3.setProfile(PROFILE_FILE);
    }

    @DataProvider(name = "MessyMojoProvider")
    public Object[][] provideMessyMojos() {

        return new Object[][] { { messyMojo1, null }, { messyMojo2, null }, { messyMojo3, null } };
    }

    @Test(expectedExceptions = MojoExecutionException.class, dataProvider = "MessyMojoProvider")
    public void checkMutualExclusives(OjdeployMojo mojo, Object notUsed) throws MojoExecutionException, MojoFailureException {

        mojo.execute();
    }

    @Test
    private void getDryRun() {

        assertEquals(mojo.getDryRun(), DRY_RUN, "Dry run not set as expected!");
    }

    @Test
    public void getBaseDir() {

        assertSame(mojo.getBaseDir(), BASE_DIR, "Basedir not set as expected!");
    }

    @Test
    public void getBuildFile() {

        assertSame(mojo.getBuildFile(), BUILD_FILE, "Build file not set as expected!");
    }

    @Test
    public void getBuildFileSchema() {

        assertEquals(mojo.getBuildFileSchema(), BUILD_FILE_SCHEMA, "Flag for build file schema not set as expected!");
    }

    @Test
    public void getClean() {

        assertEquals(mojo.getClean(), CLEAN, "Clean paranmeter not set as expected!");
    }

    @Test
    public void getDefines() {

        assertNotNull(mojo.getDefines(), "Define values not set!");
        assertEquals(mojo.getDefines().size(), 2, "Amount of define elements not as expected!");
    }

    @Test
    public void getForcerewrite() {

        assertEquals(mojo.getForceRewrite(), FORCEWRITE, "Force write not set as expected!");
    }

    @Test
    public void getJdevBinPath() {

        assertSame(mojo.getJdevBinPath(), JDEV_BIN_PATH, "Binary path not set as expected!");
    }

    @Test
    public void getNocompile() {

        assertEquals(mojo.getNocompile(), NOCOMPILE, "No compile flag not set as exected!");
    }

    @Test
    public void getNodatasources() {

        assertEquals(mojo.getNodatasources(), NODATASOURCES, "No datasources flag not set as expected!");
    }

    @Test
    public void getNodependents() {

        assertEquals(mojo.getNodependents(), NODEPENDENTS, "No dependents flag not set as expected!");
    }

    @Test
    public void getOutputFile() {

        assertEquals(mojo.getOutputFile(), OUTFILE, "Output file not set as expected!");
    }

    @Test
    public void getProfile() {

        assertEquals(mojo.getProfile(), PROFILE_FILE, "Profile file not set as expected!");
    }

    @Test
    public void getProject() {

        assertEquals(mojo.getProject(), PROJECT, "Project name not set as expected!");
    }

    @Test
    public void getStatusLogFile() {

        assertSame(mojo.getStatusLogFile(), STATUS_LOG_FILE, "Status log file not set as expected!");
    }

    @Test
    public void getTimeout() {

        assertEquals(mojo.getTimeout(), TIMEOUT, "Timeout value not set as expected!");
    }

    @Test
    public void getUpdateWebxmlEJBRefs() {

        assertEquals(mojo.getUpdateWebxmlEJBRefs(), UPDATE_WEB_XML, "Updating web.xml not set as expected!");
    }

    @Test
    public void getWorkspaceFile() {

        assertSame(mojo.getWorkspaceFile(), WORKSPACE_FILE, "Workspace file not set as expected!");
    }
}
