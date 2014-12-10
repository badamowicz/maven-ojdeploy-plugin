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
package com.github.badamowicz.maven.ojdeploy.plugin.executor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.badamowicz.maven.ojdeploy.plugin.helper.AbstractOjdeployHelper;

/**
 * Test cases for {@link OjdeployExecutor}.
 * 
 * @author bernd
 *
 */
public class OjdeployExecutorTest extends AbstractOjdeployHelper {

    private OjdeployExecutor                 executorSimple      = null;
    private OjdeployExecutor                 executorCmdLineTest = null;
    private static final boolean             DRY_RUN             = true;
    private CommandLine                      cmdLine             = null;

    private static final MojoParameter       BOOL_TEST_PARAM_1   = new MojoParameter("booltrue", Boolean.TRUE, Boolean.class);
    private static final MojoParameter       BOOL_TEST_PARAM_2   = new MojoParameter("boolfalse", Boolean.FALSE, Boolean.class);
    private static final MojoParameter       BOOL_TEST_PARAM_3   = new MojoParameter("nobool", "gedoens", String.class);

    private static final MojoParameter       M_PARAM_1           = new MojoParameter("jdevBinPath", "/some/path", String.class);
    private static final MojoParameter       M_PARAM_2           = new MojoParameter("verbose", Boolean.TRUE, Boolean.class);
    private static final MojoParameter       M_PARAM_3           = new MojoParameter("defines", "x=2y,a=3b", String.class);
    private static final List<MojoParameter> MOJO_PARAMS         = new ArrayList<MojoParameter>() {

                                                                     private static final long serialVersionUID = -1247247846260635366L;

                                                                     {
                                                                         add(M_PARAM_1);
                                                                         add(M_PARAM_2);
                                                                         add(M_PARAM_3);
                                                                     }
                                                                 };

    @BeforeClass
    public void beforeClass() {

        prepareDefaultMojo();

        cmdLine = new CommandLine("gedoens.sh");

        executorSimple = new OjdeployExecutor();
        executorSimple.setDryRun(DRY_RUN);
        executorSimple.setCmdLine(cmdLine);

        executorCmdLineTest = new OjdeployExecutor();
    }

    @Test
    public void isBooleanTrue() {

        assertTrue(executorSimple.isBooleanTrue(BOOL_TEST_PARAM_1), "Boolean parameter with value true not detected!");
        assertFalse(executorSimple.isBooleanTrue(BOOL_TEST_PARAM_2),
                "Boolean parameter with value false should not have been detected!");
        assertFalse(executorSimple.isBooleanTrue(BOOL_TEST_PARAM_3), "Non-boolean parameter should not have been detected!");
    }

    @Test
    public void isBoolean() {

        assertTrue(executorSimple.isBoolean(BOOL_TEST_PARAM_1), "Boolean parameter not detected!");
        assertTrue(executorSimple.isBoolean(BOOL_TEST_PARAM_2), "Boolean parameter not detected!");
        assertFalse(executorSimple.isBoolean(BOOL_TEST_PARAM_3), "Non-boolean parameter should not have been detected!");
    }

    /**
     * Beware this test will not create a working command line since the combination of arguments will not fit real world
     * scenarios. Instead this test is only about evaluating if all parameters of the Mojo are handed over to the command line.
     */
    @Test
    public void prepareCommandLine() {

        executorCmdLineTest.prepareCommandLine(MOJO_PARAMS);
        assertEquals(executorCmdLineTest.getCmdLine().toString(), "/some/path/ojdeploy -verbose -define x=2y,a=3b");
    }

    @Test
    public void getOjdeployBinary() {

        String osNameBackup = null;

        osNameBackup = System.getProperty("os.name");

        System.setProperty("os.name", "Windoofs");
        executorSimple.initOjdeployBinary();
        assertEquals(executorSimple.getOjdeployBinary(), "ojdeploy.exe");

        System.setProperty("os.name", "Linux");
        executorSimple.initOjdeployBinary();
        assertEquals(executorSimple.getOjdeployBinary(), "ojdeploy");

        System.setProperty("os.name", osNameBackup);
    }

    @Test
    public void getProps() {

        assertNotNull(executorSimple.getProps(), "Properties not initialized!");
    }

    @Test
    public void isDryRun() {

        assertEquals(executorSimple.isDryRun(), DRY_RUN, "Dry run value set wrong!");
    }

    @Test
    public void getCmdLine() {

        assertSame(executorSimple.getCmdLine(), cmdLine, "Wrong command line object returned!");
    }
}
