package org.knime.scijava.base.node;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.commons.lang3.ClassUtils;
import org.knime.core.data.BooleanValue;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataValue;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.IntValue;
import org.knime.core.data.LongValue;
import org.knime.core.data.StringValue;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.util.ColumnFilter;
import org.knime.core.node.util.ColumnSelectionPanel;
import org.knime.scijava.base.node.NodeInputStructInstance.ColumnSelectionMemberInstance;
import org.scijava.plugin.Plugin;
import org.scijava.struct2.ItemIO;
import org.scijava.struct2.MemberInstance;
import org.scijava.swing.widget2.SwingWidget;
import org.scijava.swing.widget2.SwingWidgetFactory;
import org.scijava.widget2.AbstractWidget;
import org.scijava.widget2.TextWidget;
import org.scijava.widget2.WidgetFactory;
import org.scijava.widget2.WidgetPanelFactory;

@Plugin(type = WidgetFactory.class)
public class SwingColumnSelectionWidgetFactory implements SwingWidgetFactory {

	@Override
	public boolean supports(final MemberInstance<?> model) {
		// TODO What should we do with output members?
		if (model.member().getIOType().equals(ItemIO.OUTPUT)) {
			return false;
		}
		return model instanceof ColumnSelectionMemberInstance;
	}

	@Override
	public SwingWidget create(final MemberInstance<?> model,
			final WidgetPanelFactory<? extends SwingWidget> panelFactory) {
		return new Widget(model);
	}

	// -- Helper classes --

	private class Widget extends AbstractWidget implements SwingWidget, TextWidget {

		private JPanel m_panel;

		private ColumnSelectionPanel m_colSelection;

		private Class<? extends DataValue> m_modelDataValueClass;

		private final ColumnSelectionMemberInstance<?> m_castedModelInstance = (ColumnSelectionMemberInstance<?>) model();

		public Widget(final MemberInstance<?> model) {
			super(model);
			inferModelDataValue();
		}

		// TODO probably should use converter framework
		private void inferModelDataValue() {
			Class<?> valueType = model().member().getRawType();
			if (valueType.isPrimitive()) {
				valueType = ClassUtils.primitiveToWrapper(valueType);
			}
			if (Boolean.class.equals(valueType)) {
				m_modelDataValueClass = BooleanValue.class;
			} else if (Double.class.equals(valueType) || Number.class.equals(valueType)) {
				m_modelDataValueClass = DoubleValue.class;
			} else if (Integer.class.equals(valueType)) {
				m_modelDataValueClass = IntValue.class;
			} else if (Long.class.equals(valueType)) {
				m_modelDataValueClass = LongValue.class;
			} else if (String.class.equals(valueType)) {
				m_modelDataValueClass = StringValue.class;
			}
		}

		@Override
		public JPanel getComponent() {
			if (m_panel != null)
				return m_panel;

			m_panel = new JPanel(new GridBagLayout());

			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.weightx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;

			m_colSelection = new ColumnSelectionPanel((Border) null, new ColumnFilter() {

				@Override
				public boolean includeColumn(final DataColumnSpec colSpec) {
					return colSpec.getType().isCompatible(m_modelDataValueClass);
				}

				@Override
				public String allFilteredMsg() {
					return "No input columns match ";
				}
			});
			m_panel.add(m_colSelection, gbc);

			m_colSelection.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(final ItemEvent e) {
					final DataTableSpec spec = m_castedModelInstance.getSpec();
					m_castedModelInstance
							.setSelectedColumnIndex(spec.findColumnIndex(m_colSelection.getSelectedColumn()));
				}
			});

			return m_panel;
		}

		// -- Model change event listener --

		@Override
		protected void modelChanged(final MemberInstance<?> source, final Object oldValue) {
			try {
				final DataTableSpec spec = m_castedModelInstance.getSpec();
				m_colSelection.update(spec,
						spec.getColumnSpec(m_castedModelInstance.getSelectedColumnIndex()).getName());
			} catch (final NotConfigurableException e) {
				e.printStackTrace();
			}
		}
	}
}
