package org.knime.scijava.commands.exp.testdriven.table2table.v2;

import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.Map;
import org.knime.scijava.commands.exp.testdriven.table2table.v2.Interfaces.TableProxy;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;

public class ModuleFlatMap implements Map<TableProxy, TableProxy> {

    public ModuleInfo moduleInfo;

    public TableProxy input;

    public TableProxy output;

    public ModuleFlatMap(final ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    // this should correspond to a NAryInput -> MAryOutput map.
    @Override
    public TableProxy map(final TableProxy in) {
        try {
            final Module m = moduleInfo.createModule();
            set(in, m);
            return get(m);
        } catch (final ModuleException exc) {
            throw new IllegalStateException("");
        }

    }

    private TableProxy get(final Module m) {
        // TODO Auto-generated method stub
        return null;
    }

    private void set(final TableProxy in, final Module m) {
        // todo
    }

}
