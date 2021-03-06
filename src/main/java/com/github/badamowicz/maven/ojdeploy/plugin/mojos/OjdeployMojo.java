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
package com.github.badamowicz.maven.ojdeploy.plugin.mojos;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.github.badamowicz.maven.ojdeploy.plugin.exceptions.OjdeployExecutionException;
import com.github.badamowicz.maven.ojdeploy.plugin.executor.MojoParameter;
import com.github.badamowicz.maven.ojdeploy.plugin.executor.OjdeployExecutor;

/**
 * Mojo is capable of executing OJDeploy commands. It will do nothing else than execute the <i>ojdeploy</i> binary on the command
 * line and hand over all the parameters and options given to this Mojo to this binary.
 * <p>
 * Basically this mojo takes all the options and parameters used by ojdeploy. Wherever possible, the names are not changed.
 * However, where applicable, Java style was introduced by using camel case. For example, the option <i>basedir</i> of ojdeploy
 * has turned into the Mojo parameter <i>baseDir</i>.
 * <p>
 * Created Dec 4, 2014 02:42:05 AM by bernd
 * 
 * @goal ojdeploy
 * @requiresProject true
 *
 */
public class OjdeployMojo extends AbstractMojo {

    private OjdeployExecutor          executor            = null;
    private static final Logger       LOG                 = Logger.getLogger(OjdeployMojo.class);

    /**
     * These are the parameters which must be handed over to ojdeploy as command line arguments. Be sure to have 'verbose' as the
     * first argument, otherwise it will have no effect on ojdeploy.
     */
    private static final List<String> OJDEPLOY_PARAMS     = Arrays.asList(new String[] { "verbose", "jdevBinPath",
            "workspaceFile", "buildFile", "outputFile", "profile", "project", "buildFileSchema", "baseDir", "nocompile",
            "nodependents", "clean", "nodatasources", "forceRewrite", "updateWebxmlEJBRefs", "defines", "statusLogFile",
            "timeout"                                    });

    /**
     * If set to true, only the ojdeploy-command that would have been executed will be issued. Beware that this parameter will
     * <b>not</b> be handed over to the ojdeploy command!
     * 
     * @parameter property="dryRun" default-value="false"
     */
    private Boolean                   dryRun              = null;

    /**
     * The path pointing to the directory containing the ojdeploy binary. Example:
     * <i>/opt/Oracle/Middleware/jdeveloper/jdev/bin</i>. If not given, it is assumed ojdeploy is available inside the system's
     * path variable.
     * 
     * @parameter property="jdevBinPath"
     */
    private File                      jdevBinPath         = null;

    /**
     * Full path to the JDeveloper Workspace file (.jws).
     * 
     * @parameter property="workspaceFile"
     */
    private File                      workspaceFile       = null;

    /**
     * Full path to a build file for batch deploy. This parameter is mutual exclusive with 'buildFileSchema' and 'profile'.
     * 
     * @parameter property="buildFile"
     */
    private File                      buildFile           = null;

    /**
     * The report's file name.
     * 
     * parameter property="outputFile"
     */
    private File                      outputFile          = null;

    /**
     * The profile file to be used. This parameter is mutual exclusive with 'buildFile' and 'buildFileSchema'.
     * 
     * @parameter property="profile"
     * @required
     */
    private String                    profile             = null;

    /**
     * Name of the JDeveloper Project within the .jws where the Profile can be found. If omitted, the Profile is assumed to be in
     * the Workspace.
     * 
     * @parameter property="project"
     */
    private String                    project             = null;

    /**
     * Print XML Schema for the build file. This property is mutual exclusive with the parameters 'profile' and 'buildFile'.
     * 
     * @parameter property="buildFileSchema"
     */
    private Boolean                   buildFileSchema     = null;

    /**
     * Path for workspace relative to a base directory.
     * 
     * @parameter property="baseDir"
     */
    private File                      baseDir             = null;

    /**
     * Skip compilation of Project or Workspace.
     * 
     * @parameter property="nocompile"
     */
    private Boolean                   nocompile           = null;

    /**
     * Do not deploy dependent profiles.
     * 
     * @parameter property="nodependents"
     */
    private Boolean                   nodependents        = null;

    /**
     * Clean output directories before compiling.
     * 
     * @parameter property="clean"
     */
    private Boolean                   clean               = null;

    /**
     * Do not include datasources from IDE.
     * 
     * @parameter property="nodatasources"
     */
    private Boolean                   nodatasources       = null;

    /**
     * Rewrite output file even if it is identical to existing file.
     * 
     * @parameter property="forceRewrite"
     */
    private Boolean                   forceRewrite        = null;

    /**
     * Update EJB references in web.xml.
     * 
     * @parameter property="updateWebxmlEJBRefs"
     */
    private Boolean                   updateWebxmlEJBRefs = null;

    /**
     * Define variables as </i>key=value pairs</i> as nested elements. This parameter conforms to the original 'define' argument
     * of ojaudit.
     * 
     * @parameter property="defines"
     */
    private List<String>              defines             = null;

    /**
     * Full path to an output file for status summary. No macros allowed.
     * 
     * @parameter property="statusLogFile"
     */
    private File                      statusLogFile       = null;

    /**
     * Time in seconds allowed for each deployment task.
     * 
     * @parameter property="timeout" default-value="30000"
     */
    private Long                      timeout             = null;

    /**
     * Make ojdeploy issue more information.
     * 
     * @parameter property="verbose"
     */
    private Boolean                   verbose             = null;

    @Override
    public void execute() throws MojoExecutionException {

        try {

            executor = new OjdeployExecutor();
            executor.execute(getParameterList(), getDryRun());

        } catch (Exception e) {

            throw new MojoExecutionException("Failed executing ojdeploy!\n", e);
        }

    }

    /**
     * Prepare the list of internal arguments and values handed over to {@link OjdeployExecutor}.
     * 
     * @return A list of Mojo parameters.
     */
    List<MojoParameter> getParameterList() {

        List<MojoParameter> mojoParams = null;
        MojoParameter currMojoParam = null;

        try {
            mojoParams = new ArrayList<MojoParameter>();

            for (String currParam : OJDEPLOY_PARAMS) {

                currMojoParam = convertFieldToParam(getClass().getDeclaredField(currParam), currParam);

                if (currMojoParam != null) {
                    mojoParams.add(currMojoParam);
                    LOG.debug("New mojo parameter added to list: " + currMojoParam);
                }

                currMojoParam = null;
            }
        } catch (Exception e) {

            throw new OjdeployExecutionException("Was not able to generate Mojo's parameter list!", e);
        }

        LOG.debug("Gathered " + mojoParams.size() + " arguments for ojdeploy.");
        return mojoParams;
    }

    /**
     * Convert the given field into a {@link MojoParameter} object.
     * 
     * @param field The {@link Field} to be converted.
     * @param param The parameter name to be used for the {@link MojoParameter} object which is created.
     * @return A new {@link MojoParameter} object or <b>null</b> if it was not possible to generate the object.
     * @throws IllegalAccessException if some of the reflection Voodoo done here will fail.
     */
    private MojoParameter convertFieldToParam(Field field, String param) throws IllegalAccessException {

        MojoParameter mojoParam = null;

        if (fieldIsInitialized(field)) {

            if (field.getType().isAssignableFrom(List.class)) {

                mojoParam = createListMojoParameter(field, param);

            } else if (field.getType().isAssignableFrom(File.class)) {

                mojoParam = createFileMojoParameter(field, param);

            } else {

                mojoParam = new MojoParameter(param, field.get(this), field.getType());
            }
        }

        return mojoParam;
    }

    /**
     * Fields containing {@link File} objects need some special handling for converting to regular {@link String} objects. Method
     * performs all necessary operations.
     * 
     * @param field The {@link Field} containing the {@link File} object.
     * @param param The name of the parameter which will be handed over to the returned {@link MojoParameter}.
     * @return A new {@link MojoParameter} object or <b>null</b> if it was not possible to generate the object.
     */
    private MojoParameter createFileMojoParameter(Field field, String param) {

        MojoParameter mojoParam = null;

        try {

            mojoParam = new MojoParameter(param, ((File) field.get(this)).getAbsolutePath(), String.class);

        } catch (Exception e) {

            LOG.error("Could not convert File field for parameter " + param + " into parameter object!\n", e);
        }

        return mojoParam;
    }

    /**
     * Fields containing {@link List} objects need some special handling for converting list elements to a single string object.
     * Method performs all necessary operations and creates an appropriate {@link MojoParameter} object.
     * 
     * @param field The {@link Field} containing the {@link List} object.
     * @param param The name of the parameter which will be handed over to the returned {@link MojoParameter}.
     * @return A new {@link MojoParameter} object or <b>null</b> if the list object did not contain any elements.
     * @throws IllegalAccessException if some of the reflection Voodoo done here will fail.
     */
    @SuppressWarnings("unchecked")
    private MojoParameter createListMojoParameter(Field field, String param) throws IllegalAccessException {

        MojoParameter mojoParam = null;
        List<String> list = null;
        Iterator<String> iter = null;
        StringBuilder value = null;

        list = (List<String>) field.get(this);

        if (!list.isEmpty()) {

            value = new StringBuilder();
            iter = list.iterator();

            while (iter.hasNext()) {

                value.append(iter.next());

                if (iter.hasNext())
                    value.append(",");
            }

            mojoParam = new MojoParameter(param, value, String.class);

        } else
            LOG.debug("List contains no elements.");

        return mojoParam;
    }

    private boolean fieldIsInitialized(Field currField) throws IllegalAccessException {

        return currField != null && currField.get(this) != null;
    }

    public void setJdevBinPath(File jdevBinPath) {

        this.jdevBinPath = jdevBinPath;
    }

    public void setWorkspaceFile(File workspaceFile) {

        this.workspaceFile = workspaceFile;
    }

    public void setBuildFile(File buildFile) {

        this.buildFile = buildFile;
    }

    public void setOutputFile(File outputFile) {

        this.outputFile = outputFile;
    }

    public void setProfile(String profile) {

        this.profile = profile;
    }

    public void setProject(String project) {

        this.project = project;
    }

    public void setBuildFileSchema(Boolean buildFileSchema) {

        this.buildFileSchema = buildFileSchema;
    }

    public void setBaseDir(File baseDir) {

        this.baseDir = baseDir;
    }

    public void setNocompile(Boolean nocompile) {

        this.nocompile = nocompile;
    }

    public void setNodependents(Boolean nodependents) {

        this.nodependents = nodependents;
    }

    public void setClean(Boolean clean) {

        this.clean = clean;
    }

    public void setNodatasources(Boolean nodatasources) {

        this.nodatasources = nodatasources;
    }

    public void setForceRewrite(Boolean forcerewrite) {

        this.forceRewrite = forcerewrite;
    }

    public void setUpdateWebxmlEJBRefs(Boolean updateWebxmlEJBRefs) {

        this.updateWebxmlEJBRefs = updateWebxmlEJBRefs;
    }

    public void setDefines(List<String> defines) {

        this.defines = defines;
    }

    public void setStatusLogFile(File statusLogFile) {

        this.statusLogFile = statusLogFile;
    }

    public void setTimeout(Long timeout) {

        this.timeout = timeout;
    }

    public File getJdevBinPath() {

        return jdevBinPath;
    }

    public File getWorkspaceFile() {

        return workspaceFile;
    }

    public File getBuildFile() {

        return buildFile;
    }

    public File getOutputFile() {

        return outputFile;
    }

    public String getProfile() {

        return profile;
    }

    public String getProject() {

        return project;
    }

    public Boolean getBuildFileSchema() {

        return buildFileSchema;
    }

    public File getBaseDir() {

        return baseDir;
    }

    public Boolean getNocompile() {

        return nocompile;
    }

    public Boolean getNodependents() {

        return nodependents;
    }

    public Boolean getClean() {

        return clean;
    }

    public Boolean getNodatasources() {

        return nodatasources;
    }

    public Boolean getForceRewrite() {

        return forceRewrite;
    }

    public Boolean getUpdateWebxmlEJBRefs() {

        return updateWebxmlEJBRefs;
    }

    public List<String> getDefines() {

        return defines;
    }

    public File getStatusLogFile() {

        return statusLogFile;
    }

    public Long getTimeout() {

        return timeout;
    }

    public Boolean getDryRun() {

        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {

        this.dryRun = dryRun;
    }

    public Boolean getVerbose() {

        return verbose;
    }

    public void setVerbose(Boolean verbose) {

        this.verbose = verbose;
    }

}
