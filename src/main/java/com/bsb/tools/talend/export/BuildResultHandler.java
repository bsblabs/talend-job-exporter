package com.bsb.tools.talend.export;

/**
 * @author Sebastien Gerard
 */
public interface BuildResultHandler<T> {

    T handle(BuildResult buildResult);

}
