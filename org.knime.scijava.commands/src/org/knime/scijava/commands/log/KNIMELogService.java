package org.knime.scijava.commands.log;

import org.knime.core.node.NodeLogger;
import org.scijava.Prioritized;
import org.scijava.Priority;
import org.scijava.log.LogService;
import org.scijava.service.AbstractService;

/**
 * Implementation of KNIMELogService to manually set to NodeModules to delegate
 * logging from withing wrapped Scijava modules to KNIME.
 *
 * This class is not intededed to be instantiated by Scijava plugin managers but
 * rather manually.
 *
 * @author Jonathan Hale, University of Konstanz
 */
public class KNIMELogService extends AbstractService implements LogService {

    public static final double PRIORITY = Priority.HIGH_PRIORITY;

    private final NodeLogger logger;

    /**
     * Constructor.
     *
     * @param logger
     *            KNIME {@link NodeLogger} to delegate log to.
     */
    public KNIMELogService(final NodeLogger logger) {
        this.logger = logger;
    }

    @Override
    public int compareTo(final Prioritized o) {
        return new Double(PRIORITY).compareTo(o.getPriority());
    }

    @Override
    public LogService log() {
        return this;
    }

    @Override
    public String getIdentifier() {
        return getClass().getName();
    }

    @Override
    public void debug(final Object msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(final Throwable t) {
        logger.debug(t);
    }

    @Override
    public void debug(final Object msg, final Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public void error(final Object msg) {
        logger.error(msg);
    }

    @Override
    public void error(final Throwable t) {
        logger.error(t);
    }

    @Override
    public void error(final Object msg, final Throwable t) {
        logger.error(msg, t);
    }

    @Override
    public void info(final Object msg) {
        logger.info(msg);
    }

    @Override
    public void info(final Throwable t) {
        logger.info(t);
    }

    @Override
    public void info(final Object msg, final Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public void trace(final Object msg) {
        // not supported
    }

    @Override
    public void trace(final Throwable t) {
        // not supported
    }

    @Override
    public void trace(final Object msg, final Throwable t) {
        // not supported
    }

    @Override
    public void warn(final Object msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(final Throwable t) {
        logger.warn(t);
    }

    @Override
    public void warn(final Object msg, final Throwable t) {
        logger.error(msg, t);
    }

    @Override
    public boolean isDebug() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public boolean isInfo() {
        return true;
    }

    @Override
    public boolean isTrace() {
        return false; // not supported
    }

    @Override
    public boolean isWarn() {
        return true;
    }

    @Override
    public int getLevel() {
        return logger.getLevel().ordinal();
    }

    @Override
    public void setLevel(final int level) {
        // We do not want to allow Scijava commands to modify the node logger.
    }

    @Override
    public void setLevel(final String classOrPackageName, final int level) {
        // We do not want to allow Scijava commands to modify the node logger.
    }

}
