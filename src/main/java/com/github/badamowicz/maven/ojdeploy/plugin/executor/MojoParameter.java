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

/**
 * Helper bean for transferring command line arguments from Mojo to executor.
 * 
 * @author bernd
 *
 */
public class MojoParameter {

    String   parameterName  = null;
    Object   parameterValue = null;
    Class<?> type           = null;

    public MojoParameter(String parameterName, Object parameterValue, Class<?> type) {

        super();
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.type = type;
    }

    public String getParameterName() {

        return parameterName;
    }

    public void setParameterName(String parameterName) {

        this.parameterName = parameterName;
    }

    public Object getParameterValue() {

        return parameterValue;
    }

    public void setParameterValue(Object parameterValue) {

        this.parameterValue = parameterValue;
    }

    public Class<?> getType() {

        return type;
    }

    public void setType(Class<?> type) {

        this.type = type;
    }

    @Override
    public String toString() {

        return "MojoParameter[" + parameterName + ":" + parameterValue + ":" + type + "]";
    }
}
