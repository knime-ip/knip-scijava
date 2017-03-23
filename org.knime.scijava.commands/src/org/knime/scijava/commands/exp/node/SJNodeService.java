package org.knime.scijava.commands.exp.node;

public interface SJNodeService {

    <IN> SJProxy<IN> createProxy(Class<? extends IN> inType);

}
