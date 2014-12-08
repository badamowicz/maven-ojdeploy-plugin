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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.github.badamowicz.maven.ojdeploy.plugin.exceptions.OjdeployExecutionExeption;
import com.github.badamowicz.maven.ojdeploy.plugin.mojos.OjdeployMojo;

/**
 * This class actually performs the ojdeploy commands. It provides sufficient mappings and error handling if things go wrong.
 * Beware that validating arguments to ojdeploy is not subject to this class! It's up to the calling Mojo to check if given
 * parameters are valid.
 * 
 * @author bernd
 *
 */
public class OjdeployExecutor {

    private static final String OJDEPLOY_BIN_LIN = "ojdeploy";

    private static final String OJDEPLOY_BIN_WIN = "ojdeploy.exe";

    static final Logger         LOG              = Logger.getLogger(OjdeployExecutor.class);

    private static final String PROPS_FILE       = "executor.properties";

    private Properties          props            = null;
    private OjdeployMojo        mojo             = null;
    private boolean             dryRun           = false;
    private CommandLine         cmdLine          = null;
    private String              ojdeployBinary   = null;
    private long                timeout          = 30000l;

    /**
     * Constructor performs necessary initializations of internal data and mappings.
     * 
     * @throws OjdeployExecutionExeption if initialization of internal data failed.
     */
    public OjdeployExecutor() throws OjdeployExecutionExeption {

        initProperties();
        initOjdeployBinary();
    }

    /**
     * Initialize the name of the ojdeploy binary depending on the operating system used. Currently only Windows and Linux are
     * supported.
     */
    void initOjdeployBinary() {

        String osName = null;

        osName = System.getProperty("os.name");

        if (osName.toLowerCase().contains("win"))
            setOjdeployBinary(OJDEPLOY_BIN_WIN);
        else if (osName.toLowerCase().contains("linux"))
            setOjdeployBinary(OJDEPLOY_BIN_LIN);
        else
            throw new OjdeployExecutionExeption("Operating system" + osName + " is not supported!");

        LOG.debug("Initialized ojdeploy binary for operating system " + osName + " with '" + getOjdeployBinary() + "'.");
    }

    /**
     * Actually executes the ojdeploy command with all the arguments provided by the given Mojo.
     * 
     * @param mojo The {@link OjdeployMojo} whose parameters will be used to initialize the ojdeploy command line.
     * @param dryRun If set to true, no action will be taken. Instead only the command line as would have been executed will be
     *        shown.
     */
    public void execute(OjdeployMojo mojo, boolean dryRun, long timeout) {

        try {
            setMojo(mojo);
            setDryRun(dryRun);
            prepareCommandLine();

            if (isDryRun()) {

                LOG.info("Dry run option is set. Would execute this command:");
                LOG.info(getCmdLine());

            } else {

                exec();
            }

        } catch (Exception e) {

            throw new OjdeployExecutionExeption("Was not able to execute ojdeploy!\n", e);
        }
    }

    /**
     * Actually execute the ojdeploy command which has been prepared before.
     * 
     * @throws IOException if some stream error occurred.
     * @throws ExecuteException if execution of ojdeploy failed.
     */
    private void exec() throws ExecuteException, IOException {

        ExecuteWatchdog watchdog = null;
        FileOutputStream fos = null;
        PumpStreamHandler pStreamHandler = null;
        DefaultExecutor executor = null;
        int exitVal = -1;

        LOG.info("Start executing ojdeploy now with command:");
        LOG.info(getCmdLine());
        watchdog = new ExecuteWatchdog(getTimeout());
        fos = new FileOutputStream(new File(getProps().getProperty("ojdeploy.build.log.file")));
        pStreamHandler = new PumpStreamHandler(fos);
        executor = new DefaultExecutor();
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(pStreamHandler);
        executor.setExitValue(Integer.valueOf(getProps().getProperty("exit.value")));
        exitVal = executor.execute(getCmdLine());
        fos.flush();
        fos.close();
        LOG.info("Finished executing ojdeploy with exit value: " + exitVal);
    }

    /**
     * Prepare the command line used internally for being executed later.
     */
    void prepareCommandLine() {

        String executable = null;

        LOG.debug("Start initializing ojdeploy command line.");

        if (getMojo().getJdevBinPath() != null)
            executable = getMojo().getJdevBinPath().getAbsolutePath() + "/" + getOjdeployBinary();
        else
            executable = getOjdeployBinary();

        setCmdLine(new CommandLine(executable));
        LOG.debug("Base command initialized with: " + executable);

        if (getMojo().getBuildFile() != null) {

            getCmdLine().addArgument(getProps().getProperty("buildFile"));
            getCmdLine().addArgument(getMojo().getBuildFile().getAbsolutePath());

        } else if (getMojo().getBuildFileSchema() != null) {

            getCmdLine().addArgument(getProps().getProperty("buildFileSchema"));
            getCmdLine().addArgument(getMojo().getBuildFileSchema().toString());

        } else if (getMojo().getProfile() != null) {

            getCmdLine().addArgument(getProps().getProperty("profile"));
            getCmdLine().addArgument(getMojo().getProfile());
        }

        if (getMojo().getWorkspaceFile() != null) {

            getCmdLine().addArgument(getProps().getProperty("workspaceFile"));
            getCmdLine().addArgument(getMojo().getWorkspaceFile().getAbsolutePath());
        }

        if (getMojo().getOutputFile() != null) {

            getCmdLine().addArgument(getProps().getProperty("outputFile"));
            getCmdLine().addArgument(getMojo().getOutputFile().getAbsolutePath());
        }

        if (getMojo().getProject() != null) {

            getCmdLine().addArgument(getProps().getProperty("project"));
            getCmdLine().addArgument(getMojo().getProject());
        }

        if (getMojo().getBaseDir() != null) {

            getCmdLine().addArgument(getProps().getProperty("baseDir"));
            getCmdLine().addArgument(getMojo().getBaseDir().getAbsolutePath());
        }

        if (getMojo().getNocompile() != null && getMojo().getNocompile().booleanValue() == true) {

            getCmdLine().addArgument(getProps().getProperty("nocompile"));
        }

        if (getMojo().getNodependents() != null && getMojo().getNodependents().booleanValue() == true) {

            getCmdLine().addArgument(getProps().getProperty("nodependents"));
        }

        if (getMojo().getClean() != null && getMojo().getClean().booleanValue() == true) {

            getCmdLine().addArgument(getProps().getProperty("clean"));
        }

        if (getMojo().getNodatasources() != null && getMojo().getNodatasources().booleanValue() == true) {

            getCmdLine().addArgument(getProps().getProperty("nodatasources"));
        }

        if (getMojo().getForceRewrite() != null && getMojo().getForceRewrite().booleanValue() == true) {

            getCmdLine().addArgument(getProps().getProperty("forceRewrite"));
        }

        if (getMojo().getUpdateWebxmlEJBRefs() != null && getMojo().getUpdateWebxmlEJBRefs().booleanValue() == true) {

            getCmdLine().addArgument(getProps().getProperty("updateWebxmlEJBRefs"));
        }

        if (getMojo().getStatusLogFile() != null) {

            getCmdLine().addArgument(getProps().getProperty("statusLogFile"));
            getCmdLine().addArgument(getMojo().getStatusLogFile().getAbsolutePath());
        }

        if (getMojo().getTimeout() != null) {

            getCmdLine().addArgument(getProps().getProperty("timeout"));
            getCmdLine().addArgument(getMojo().getTimeout().toString());
        }

        addDefines();

        LOG.debug("Finished initializing ojdeploy command line call with: " + getCmdLine().toString());
    }

    /**
     * Add the 'defines' argument to the command line if available.
     */
    private void addDefines() {

        Iterator<String> iterDefines = null;
        StringBuilder builder = null;

        if (getMojo().getDefines() != null && getMojo().getDefines().size() > 0) {

            getCmdLine().addArgument(getProps().getProperty("defines"));

            iterDefines = getMojo().getDefines().iterator();
            builder = new StringBuilder("'");

            while (iterDefines.hasNext()) {

                builder.append(iterDefines.next());

                if (iterDefines.hasNext())
                    builder.append(",");
            }

            builder.append("'");
            getCmdLine().addArgument(builder.toString());
        }

    }

    private void initProperties() throws OjdeployExecutionExeption {

        InputStream is = null;
        Properties props = null;

        try {

            LOG.debug("Initializing properties for executing ojdeploy.");
            is = OjdeployExecutor.class.getClassLoader().getResourceAsStream(PROPS_FILE);
            props = new Properties();
            props.load(is);
            setProps(props);
            LOG.debug("Finished initializing properties for executing ojdeploy.");

        } catch (Exception e) {

            throw new OjdeployExecutionExeption("Was not able to initialize properties for ojdeploy executor!\n", e);

        } finally {

            IOUtils.closeQuietly(is);
        }
    }

    Properties getProps() {

        return props;
    }

    void setProps(Properties props) {

        this.props = props;
    }

    OjdeployMojo getMojo() {

        return mojo;
    }

    void setMojo(OjdeployMojo mojo) {

        this.mojo = mojo;
    }

    boolean isDryRun() {

        return dryRun;
    }

    void setDryRun(boolean dryRun) {

        this.dryRun = dryRun;
    }

    CommandLine getCmdLine() {

        return cmdLine;
    }

    void setCmdLine(CommandLine cmdLine) {

        this.cmdLine = cmdLine;
    }

    String getOjdeployBinary() {

        return ojdeployBinary;
    }

    void setOjdeployBinary(String ojdeployBinary) {

        this.ojdeployBinary = ojdeployBinary;
    }

    long getTimeout() {

        return timeout;
    }

    void setTimeout(long timeout) {

        this.timeout = timeout;
    }
}
