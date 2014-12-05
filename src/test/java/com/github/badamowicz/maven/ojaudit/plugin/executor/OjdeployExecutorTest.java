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

import org.apache.commons.exec.CommandLine;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.badamowicz.maven.ojaudit.plugin.helper.AbstractOjdeployHelper;
import com.github.badamowicz.maven.ojaudit.plugin.mojos.OjdeployMojo;

/**
 * Test cases for {@link OjdeployExecutor}.
 * 
 * @author bernd
 *
 */
public class OjdeployExecutorTest extends AbstractOjdeployHelper {

    private OjdeployExecutor     executorSimple = null;
    private OjdeployExecutor     executorFilled = null;
    private OjdeployMojo         mojoSimple     = null;
    private static final boolean DRY_RUN        = true;
    private CommandLine          cmdLine        = null;

    @BeforeClass
    public void beforeClass() {

        prepareDefaultMojo();

        mojoSimple = new OjdeployMojo();
        cmdLine = new CommandLine("gedoens.sh");

        executorSimple = new OjdeployExecutor();
        executorSimple.setDryRun(DRY_RUN);
        executorSimple.setMojo(mojoSimple);
        executorSimple.setCmdLine(cmdLine);

        executorFilled = new OjdeployExecutor();
        executorFilled.setMojo(mojo);
        executorFilled.setDryRun(true);
    }

    @Test
    public void prepareCommandLine() {

        String expectedCmdLine = null;

        executorFilled.prepareCommandLine();
        expectedCmdLine = executorFilled.getCmdLine().toString();
        assertNotNull(expectedCmdLine, "Command line not initialized!");
        assertEquals(
                expectedCmdLine,
                "/some/path/ojdeploy -buildfile /some/build.file -workspace /some/workspace.jws -outputfile /home/bernd/THEMEN/BA/DEV/maven-ojdeploy-plugin/out.txt -project some.jpr -basedir /home/bernd/THEMEN/BA/DEV/maven-ojdeploy-plugin/. -nocompile true -nodependents false -clean true -nodatasources true -forcerewrite true -updatewebxmlejbrefs false -statuslogfile /some/path/status.log -timeout 300 -define key1=value1,key2=value2");

        // FIXME: check if cmd line is really as expected
    }

    @Test
    public void getOjdeployBinary() {

        String osName = null;

        osName = System.getProperty("os.name");
        executorSimple.initOjdeployBinary();

        if (osName.toLowerCase().contains("win"))
            assertEquals(executorSimple.getOjdeployBinary(), "ojdeploy.exe");
        else if (osName.toLowerCase().contains("linux"))
            assertEquals(executorSimple.getOjdeployBinary(), "ojdeploy");

    }

    @Test
    public void getMojo() {

        assertSame(executorSimple.getMojo(), mojoSimple, "Not the same Mojo returned!");
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
