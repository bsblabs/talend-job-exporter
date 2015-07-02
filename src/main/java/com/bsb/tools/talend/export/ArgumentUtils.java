package com.bsb.tools.talend.export;

import org.eclipse.equinox.app.IApplicationContext;

/**
 * Bunch of utility methods for the program command arguments.
 *
 * @author Sebastien Gerard
 */
public final class ArgumentUtils {

    private ArgumentUtils() {
    }

    /**
     * Returns the value for the parameter having the specified name.
     * <p/>
     * The command line has the following syntax: -[parameterName] [parameterValue].
     * In this case the parameter name and its value are contained in two separate
     * array cells.
     *
     * @return the matching parameter
     * @throws IllegalArgumentException if there is no matching parameter
     */
    public static String getMandatoryParameterValue(IApplicationContext context, String parameterName) {
        final String parameterValue = getParameterValue(context, parameterName);

        if (parameterValue == null) {
            throw new IllegalArgumentException("Missing program parameter [" + parameterName + "].");
        }

        return parameterValue;
    }

    /**
     * Returns the value for the parameter having the specified name.
     * <p/>
     * The command line has the following syntax: -[parameterName] [parameterValue].
     * In this case the parameter name and its value are contained in two separate
     * array cells.
     *
     * @return the matching parameter, or <tt>null</tt> if not found
     */
    public static String getParameterValue(IApplicationContext context, String parameterName) {
        final String[] commandLineArgs = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
        for (int i = 0; i < commandLineArgs.length; i++) {
            if (("-" + parameterName).equals(commandLineArgs[i]) && ((i + 1) < commandLineArgs.length)) {
                return commandLineArgs[i + 1];
            }
        }

        return null;
    }
}
