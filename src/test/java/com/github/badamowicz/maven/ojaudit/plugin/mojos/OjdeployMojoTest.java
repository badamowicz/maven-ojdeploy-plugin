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

import java.io.File;
import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test cases for {@link OjdeployMojo}.
 * 
 * @author bernd
 *
 */
public class OjdeployMojoTest {

    private static final File              JDEV_BIN_PATH     = new File("/some/path");
    private static final File              WORKSPACE_FILE    = new File("/some/workspace.jws");
    private static final File              BUILD_FILE        = new File("/some/build.file");
    private static final File              PROFILE_FILE      = new File("profile.file");
    private static final String            PROJECT           = "some.jpr";
    private static final File              BASE_DIR          = new File(".");
    private static final File              OUTFILE           = new File("out.txt");
    private static final Boolean           NOCOMPILE         = Boolean.TRUE;
    private static final Boolean           NODEPENDENTS      = Boolean.FALSE;
    private static final Boolean           CLEAN             = Boolean.TRUE;
    private static final Boolean           NODATASOURCES     = Boolean.FALSE;
    private static final Boolean           FORCEWRITE        = Boolean.TRUE;
    private static final Boolean           UPDATE_WEB_XML    = Boolean.FALSE;

    private static final ArrayList<String> DEFINES           = new ArrayList<String>() {
                                                                 private static final long serialVersionUID = -3675484013465075644L;

                                                                 {
                                                                     add("key1=value1");
                                                                     add("key2=value2");
                                                                 }
                                                             };

    private static final File              STATUS_LOG_FILE   = new File("/some/path/status.log");
    private static final Long              TIMEOUT           = 300l;
    private static final Boolean           BUILD_FILE_SCHEMA = Boolean.FALSE;

    private OjdeployMojo                   mojo              = null;
    private OjdeployMojo                   messyMojo1        = null;
    private OjdeployMojo                   messyMojo2        = null;
    private OjdeployMojo                   messyMojo3        = null;

    @BeforeClass
    public void beforeClass() {

        mojo = new OjdeployMojo();
        mojo.setBaseDir(BASE_DIR);
        mojo.setBuildFile(BUILD_FILE);
        mojo.setClean(CLEAN);
        mojo.setDefines(DEFINES);
        mojo.setForcerewrite(FORCEWRITE);
        mojo.setJdevBinPath(JDEV_BIN_PATH);
        mojo.setNocompile(NOCOMPILE);
        mojo.setNodatasources(NODATASOURCES);
        mojo.setNodependents(NODEPENDENTS);
        mojo.setOutputFile(OUTFILE);
        mojo.setProfile(PROFILE_FILE);
        mojo.setProject(PROJECT);
        mojo.setStatusLogFile(STATUS_LOG_FILE);
        mojo.setTimeout(TIMEOUT);
        mojo.setBuildFileSchema(BUILD_FILE_SCHEMA);
        mojo.setUpdateWebxmlEJBRefs(UPDATE_WEB_XML);
        mojo.setWorkspaceFile(WORKSPACE_FILE);

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

        assertEquals(mojo.getForcerewrite(), FORCEWRITE, "Force write not set as expected!");
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
