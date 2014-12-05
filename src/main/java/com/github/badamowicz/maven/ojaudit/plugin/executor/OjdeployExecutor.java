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

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.github.badamowicz.maven.ojaudit.plugin.exeptions.OjdeployExecutionExeption;
import com.github.badamowicz.maven.ojaudit.plugin.mojos.OjdeployMojo;

/**
 * This class actually performs the ojdeploy commands. It provides sufficient mappings and error handling if things go wrong.
 * Beware that validating arguments to ojdeploy is not subject to this class! It's up to the calling Mojo to check if given
 * parameters are valid.
 * 
 * @author bernd
 *
 */
public class OjdeployExecutor {

    static final Logger         LOG        = Logger.getLogger(OjdeployExecutor.class);

    private static final String PROPS_FILE = "executor.properties";

    private Properties          props      = null;
    private OjdeployMojo        mojo       = null;
    private boolean             dryRun     = false;

    /**
     * Constructor performs necessary initializations of internal data and mappings.
     * 
     * @throws OjdeployExecutionExeption if initialization of internal data failed.
     */
    public OjdeployExecutor() throws OjdeployExecutionExeption {

        initProperties();
    }

    /**
     * Actually executes the ojdeploy command with all the arguments provided by the given mojo.
     * 
     * @param mojo The {@link OjdeployMojo} whose parameters will be used to initialize the ojdeploy command line.
     * @param dryRun If set to true, no action will be taken. Instead only the command line as would have been executed will be
     *        shown.
     */
    public void execute(OjdeployMojo mojo, boolean dryRun) {

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
}
