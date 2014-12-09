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
package com.github.badamowicz.maven.ojdeploy.plugin.helper;

import java.io.File;
import java.util.ArrayList;

import com.github.badamowicz.maven.ojdeploy.plugin.mojos.OjdeployMojo;

/**
 * Abstract super class which provides some fields an values for testing ojdeploy stuff.
 * 
 * @author bernd
 *
 */
public abstract class AbstractOjdeployHelper {

    protected static final File              JDEV_BIN_PATH     = new File("/some/path");
    protected static final File              WORKSPACE_FILE    = new File("/some/workspace.jws");
    protected static final File              BUILD_FILE        = new File("/some/build.file");
    protected static final String            PROFILE           = "profileXY";
    protected static final String            PROJECT           = "some.jpr";
    protected static final File              BASE_DIR          = new File(".");
    protected static final File              OUTFILE           = new File("out.txt");
    protected static final Boolean           NOCOMPILE         = Boolean.TRUE;
    protected static final Boolean           NODEPENDENTS      = Boolean.TRUE;
    protected static final Boolean           CLEAN             = Boolean.TRUE;
    protected static final Boolean           NODATASOURCES     = Boolean.TRUE;
    protected static final Boolean           FORCEWRITE        = Boolean.TRUE;
    protected static final Boolean           UPDATE_WEB_XML    = Boolean.TRUE;
    protected static final Boolean           VERBOSE           = Boolean.TRUE;
    protected static final ArrayList<String> DEFINES           = new ArrayList<String>() {
                                                                   private static final long serialVersionUID = -3675484013465075644L;

                                                                   {
                                                                       add("key1=value1");
                                                                       add("key2=value2");
                                                                   }
                                                               };
    protected static final File              STATUS_LOG_FILE   = new File("/some/path/status.log");
    protected static final Long              TIMEOUT           = 300l;
    protected static final Boolean           BUILD_FILE_SCHEMA = Boolean.TRUE;
    protected static final Boolean           DRY_RUN           = Boolean.TRUE;
    protected OjdeployMojo                   mojo              = null;

    protected void prepareDefaultMojo() {

        mojo = new OjdeployMojo();
        mojo.setBaseDir(BASE_DIR);
        mojo.setBuildFile(BUILD_FILE);
        mojo.setClean(CLEAN);
        mojo.setDefines(DEFINES);
        mojo.setForceRewrite(FORCEWRITE);
        mojo.setJdevBinPath(JDEV_BIN_PATH);
        mojo.setNocompile(NOCOMPILE);
        mojo.setNodatasources(NODATASOURCES);
        mojo.setNodependents(NODEPENDENTS);
        mojo.setOutputFile(OUTFILE);
        mojo.setProfile(PROFILE);
        mojo.setProject(PROJECT);
        mojo.setStatusLogFile(STATUS_LOG_FILE);
        mojo.setTimeout(TIMEOUT);
        mojo.setBuildFileSchema(BUILD_FILE_SCHEMA);
        mojo.setUpdateWebxmlEJBRefs(UPDATE_WEB_XML);
        mojo.setWorkspaceFile(WORKSPACE_FILE);
        mojo.setDryRun(DRY_RUN);
        mojo.setVerbose(VERBOSE);
    }

}
