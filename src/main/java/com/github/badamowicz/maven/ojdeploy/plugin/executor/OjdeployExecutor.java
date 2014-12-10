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
import java.util.List;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.github.badamowicz.maven.ojdeploy.plugin.exceptions.OjdeployExecutionException;

/**
 * This class actually performs the ojdeploy commands. It provides sufficient mappings and error handling if things go wrong.
 * Beware that validating arguments to ojdeploy is not subject to this class! It's up to the calling Mojo to check if given
 * parameters are valid.
 * 
 * @author bernd
 *
 */
public class OjdeployExecutor {

    private static final String OJDEPLOY_BIN_LIN    = "ojdeploy";
    private static final String OJDEPLOY_BIN_WIN    = "ojdeploy.exe";
    private static final String JDEV_BIN_PATH_PARAM = "jdevBinPath";

    private static final Logger LOG                 = Logger.getLogger(OjdeployExecutor.class);

    private static final String PROPS_FILE          = "executor.properties";

    private Properties          props               = null;
    private boolean             dryRun              = false;
    private CommandLine         cmdLine             = null;
    private String              ojdeployBinary      = null;

    /**
     * Constructor performs necessary initializations of internal data and mappings.
     */
    public OjdeployExecutor() {

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
            throw new OjdeployExecutionException("Operating system" + osName + " is not supported!");

        LOG.debug("Initialized ojdeploy binary for operating system " + osName + " with '" + getOjdeployBinary() + "'.");
    }

    public void execute(List<MojoParameter> ojdParams, boolean dryRun) {

        try {
            setDryRun(dryRun);
            prepareCommandLine(ojdParams);

            if (isDryRun())
                LOG.info("Dry run option is set. Would execute this command:\n" + getCmdLine());
            else
                exec();

        } catch (Exception e) {

            throw new OjdeployExecutionException("Was not able to execute ojdeploy!\n", e);
        }
    }

    /**
     * Prepare the command line required for running ojdeploy.
     * 
     * @param ojdParams The list of parameters available.
     */
    void prepareCommandLine(List<MojoParameter> ojdParams) {

        prepareJdevCommand(ojdParams);
        prepareCommandArguments(ojdParams);
    }

    /**
     * Prepare all the arguments necessary for running ojdeploy.
     * 
     * @param ojdParams The list of parameters available.
     */
    private void prepareCommandArguments(List<MojoParameter> ojdParams) {

        LOG.debug("Start initializing ojdeploy arguments.");

        for (MojoParameter currParam : ojdParams) {

            if (isBooleanTrue(currParam)) {

                LOG.debug("Adding boolean " + currParam.getParameterName());
                addMappedCmdLineArgument(currParam.getParameterName());

            } else if (!isBoolean(currParam) && !isJdevBinPath(currParam)) {

                LOG.debug("Adding non-boolean " + currParam.getParameterName());
                addMappedCmdLineArgument(currParam.getParameterName());
                getCmdLine().addArgument(currParam.getParameterValue().toString());
            }
        }

        LOG.debug("Finished initializing ojdeploy arguments.");
    }

    /**
     * Detect if the given Mojo parameter is of type {@link Boolean}. It is <b>not</b> taken into account whether the value is
     * true or false. Only the type matters! See {@link #isBooleanTrue(MojoParameter)} for also checking the value.
     * 
     * @param param A {@link MojoParameter}.
     * @return true, if the given parameter is of type {@link Boolean}
     * @see #isBooleanTrue(MojoParameter)
     */
    boolean isBoolean(MojoParameter param) {

        return param.getType().isAssignableFrom(Boolean.class);
    }

    /**
     * Detect if the given Mojo parameter is of type {@link Boolean} <b>and</b> its value is set to true.
     * 
     * @param param A {@link MojoParameter}.
     * @return true, if the given parameter is of type {@link Boolean} <b>and</b> has a value of {@link Boolean#TRUE}.
     * @see #isBoolean(MojoParameter)
     */
    boolean isBooleanTrue(MojoParameter param) {

        return param.getType().isAssignableFrom(Boolean.class) && ((Boolean) param.getParameterValue()).booleanValue();
    }

    /**
     * Prepare the start command for ojdeploy.
     * 
     * @param ojdParams The list of parameters available.
     */
    private void prepareJdevCommand(List<MojoParameter> ojdParams) {

        String executable = null;
        boolean binPathFound = false;

        LOG.debug("Start initializing ojdeploy command line.");

        for (MojoParameter currParam : ojdParams) {

            if (isJdevBinPath(currParam)) {

                binPathFound = true;

                if (!((String) currParam.getParameterValue()).isEmpty())
                    executable = currParam.getParameterValue() + "/" + getOjdeployBinary();
                else
                    executable = getOjdeployBinary();
            }

            if (binPathFound)
                break;
        }

        setCmdLine(new CommandLine(executable));
        LOG.debug("Base command initialized with: " + executable);
    }

    private boolean isJdevBinPath(MojoParameter currParam) {

        return currParam.getParameterName().equals(JDEV_BIN_PATH_PARAM);
    }

    /**
     * Actually execute the ojdeploy command which has been prepared before.
     * 
     * @throws IOException if execution of external process failed.
     */
    private void exec() throws IOException {

        FileOutputStream fos = null;
        PumpStreamHandler pStreamHandler = null;
        DefaultExecutor executor = null;
        int exitVal = -1;

        LOG.info("Start executing ojdeploy now with command:");
        LOG.info(getCmdLine());
        fos = new FileOutputStream(new File(getProps().getProperty("ojdeploy.build.log.file")));
        pStreamHandler = new PumpStreamHandler(fos);
        executor = new DefaultExecutor();
        executor.setStreamHandler(pStreamHandler);
        executor.setExitValue(Integer.valueOf(getProps().getProperty("exit.value")));
        exitVal = executor.execute(getCmdLine());
        fos.flush();
        fos.close();
        LOG.info("Finished executing ojdeploy with exit value: " + exitVal);
    }

    /**
     * Add a new mapped argument to the command line.
     * 
     * @param mojoParam The name of the Mojo's parameter. It will be mapped to the associated ojdeploy argument. For example the
     *        Mojo parameter <i>updateWebxmlEJBRefs</i> will be mapped to the command line argument <i>-updatewebxmlejbrefs</i>.
     */
    private void addMappedCmdLineArgument(String mojoParam) {

        getCmdLine().addArgument(getProps().getProperty(mojoParam));
    }

    private void initProperties() {

        InputStream is = null;
        Properties properties = null;

        try {

            LOG.debug("Initializing properties for executing ojdeploy.");
            is = OjdeployExecutor.class.getClassLoader().getResourceAsStream(PROPS_FILE);
            properties = new Properties();
            properties.load(is);
            setProps(properties);
            LOG.debug("Finished initializing properties for executing ojdeploy.");

        } catch (Exception e) {

            throw new OjdeployExecutionException("Was not able to initialize properties for ojdeploy executor!\n", e);

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
}
