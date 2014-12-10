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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test cases for {@link MojoParameter}.
 * 
 * @author bernd
 *
 */
public class MojoParameterTest {

    private static final Class<Boolean> BOOL_CLASS           = Boolean.class;
    private static final String         PARAM_NAME_BOOL_TRUE = "boolTrue";
    private static final Boolean        BOOL_TRUE            = Boolean.TRUE;

    private MojoParameter               paramTrue            = null;

    @BeforeClass
    public void beforeClass() {

        paramTrue = new MojoParameter(PARAM_NAME_BOOL_TRUE, BOOL_TRUE, BOOL_CLASS);
    }

    @Test
    public void getParameterName() {

        assertEquals(paramTrue.getParameterName(), PARAM_NAME_BOOL_TRUE, "Name of parameter not as expected!");
    }

    @Test
    public void getParameterValue() {

        assertEquals(paramTrue.getParameterValue(), BOOL_TRUE, "Wrong value for parameter returned!");
    }

    @Test
    public void getType() {

        assertEquals(paramTrue.getType(), BOOL_CLASS, "Wrong type returned!");
    }
}
