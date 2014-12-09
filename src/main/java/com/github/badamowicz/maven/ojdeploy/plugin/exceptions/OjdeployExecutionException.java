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
package com.github.badamowicz.maven.ojdeploy.plugin.exceptions;

/**
 * Exceptions of this type are thrown if it was not possible - for whatever reason - to execute an ojdeploy command.
 * 
 * @author bernd
 *
 */
public class OjdeployExecutionException extends RuntimeException {

    private static final long serialVersionUID = 9199740922863187821L;

    public OjdeployExecutionException() {

        super();
    }

    public OjdeployExecutionException(String message) {

        super(message);
    }

    public OjdeployExecutionException(Throwable cause) {

        super(cause);
    }

    public OjdeployExecutionException(String message, Throwable cause) {

        super(message, cause);
    }

    public OjdeployExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }

}
