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

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.github.badamowicz.maven.ojaudit.plugin.executor.OjdeployExecutor;

/**
 * Mojo is capable of executing OJDeploy commands. It will do nothing else than execute the <i>ojdeploy</i> binary on the command
 * line and hand over all the parameters and options given to this Mojo to this binary.
 * <p>
 * Basically this mojo takes all the options and parameters used by ojdeploy. Wherever possible, the names are not changed.
 * However, where applicable, Java style was introduced by using camel case. For example, the option <i>basedir</i> of ojaudit has
 * turned into the Mojo parameter <i>baseDir</i>.
 * <p>
 * Created Dec 4, 2014 02:42:05 AM by bernd
 * 
 * @goal ojdeploy
 * @requiresProject true
 *
 */
public class OjdeployMojo extends AbstractMojo {

    private OjdeployExecutor executor            = null;

    /**
     * If set to true, only the ojdeploy-command that would have been executed will be issued. Beware that this parameter will
     * <b>not</b> be handed over to the ojdeploy command!
     * 
     * @parameter property="dryRun" default-value="false"
     */
    private Boolean          dryRun              = null;

    /**
     * The path pointing to the directory containing the ojdeploy binary. Example:
     * <i>/opt/Oracle/Middleware/jdeveloper/jdev/bin</i>. If not given, it is assumed ojdeploy is available inside the system's
     * path variable.
     * 
     * @parameter property="jdevBinPath"
     */
    private File             jdevBinPath         = null;

    /**
     * Full path to the JDeveloper Workspace file (.jws).
     * 
     * @parameter property="workspaceFile"
     */
    private File             workspaceFile       = null;

    /**
     * Full path to a build file for batch deploy. This parameter is mutual exclusive with 'buildFileSchema' and 'profile'.
     * 
     * @parameter property="buildFile"
     */
    private File             buildFile           = null;

    /**
     * The report's file name.
     * 
     * parameter property="outputFile"
     */
    private File             outputFile          = null;

    /**
     * The profile file to be used. This parameter is mutual exclusive with 'buildFile' and 'buildFileSchema'.
     * 
     * @parameter property="profile"
     * @required
     */
    private String           profile             = null;

    /**
     * Name of the JDeveloper Project within the .jws where the Profile can be found. If omitted, the Profile is assumed to be in
     * the Workspace.
     * 
     * @parameter property="project"
     */
    private String           project             = null;

    /**
     * Print XML Schema for the build file. This property is mutual exclusive with the parameters 'profile' and 'buildFile'.
     * 
     * @parameter property="buildFileSchema"
     */
    private Boolean          buildFileSchema     = null;

    /**
     * Path for workspace relative to a base directory.
     * 
     * @parameter property="baseDir"
     */
    private File             baseDir             = null;

    /**
     * Skip compilation of Project or Workspace.
     * 
     * @parameter property="nocompile"
     */
    private Boolean          nocompile           = null;

    /**
     * Do not deploy dependent profiles.
     * 
     * @parameter property="nodependents"
     */
    private Boolean          nodependents        = null;

    /**
     * Clean output directories before compiling.
     * 
     * @parameter property="clean"
     */
    private Boolean          clean               = null;

    /**
     * Do not include datasources from IDE.
     * 
     * @parameter property="nodatasources"
     */
    private Boolean          nodatasources       = null;

    /**
     * Rewrite output file even if it is identical to existing file.
     * 
     * @parameter property="forceRewrite"
     */
    private Boolean          forceRewrite        = null;

    /**
     * Update EJB references in web.xml.
     * 
     * @parameter property="updateWebxmlEJBRefs"
     */
    private Boolean          updateWebxmlEJBRefs = null;

    /**
     * Define variables as </i>key=value pairs</i> as nested elements. This parameter conforms to the original 'define' argument
     * of ojaudit.
     * 
     * @parameter property="defines"
     */
    private List<String>     defines             = null;

    /**
     * Full path to an output file for status summary. No macros allowed.
     * 
     * @parameter property="statusLogFile"
     */
    private File             statusLogFile       = null;

    /**
     * Time in seconds allowed for each deployment task.
     * 
     * @parameter property="timeout" default-value="30000"
     */
    private Long             timeout             = null;

    public void execute() throws MojoExecutionException, MojoFailureException {

        try {

            checkMutualExclusives();
            executor = new OjdeployExecutor();
            executor.execute(this, getDryRun(), getTimeout());

        } catch (Exception e) {

            throw new MojoExecutionException("Failed executing ojdeploy!\n", e);
        }

    }

    /**
     * Check if mutual exclusive commands are given in the same run.
     * 
     * @throws MojoExecutionException If more than one mutual exclusive command is given.
     */
    void checkMutualExclusives() throws MojoExecutionException {

        if ((getBuildFileSchema() != null && getBuildFile() != null) || (getBuildFileSchema() != null && getProfile() != null)
                || (getBuildFile() != null && getProfile() != null))
            throw new MojoExecutionException(
                    "Mutual exclusive arguments given! Only one of 'buildFile', 'profile' or 'buildFileSchema' must be given!");
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

}
