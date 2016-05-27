package org.knime.scijava.commands.simplemapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

@Plugin(type = SimpleColumnMappingService.class)
public class DefaultSimpleColumMappingService extends AbstractService
		implements SimpleColumnMappingService {

	private final Map<String, String> m_mappings = new HashMap<>();
	
	@Override
	public String getMappedColumn(final String input) {
		return m_mappings.get(input);
	}

	@Override
	public String mappedColumn(final String input)
			throws NoSuchElementException {
		final String column = m_mappings.get(input);
		if (column == null) {
			throw new NoSuchElementException(
					"Can't locate a mapping for input: " + input);
		}
		return column;
	}

	@Override
	public void setMappedColumn(final String input, final String column) {
		m_mappings.put(input, column);
	}

	@Override
	public List<String> getMappedInputs() {
		final List<String> mappedInputs = new ArrayList<>();
		for (final Entry<String, String> item : m_mappings.entrySet()) {
			if (item.getValue() != null) {
				mappedInputs.add(item.getKey());
			}
		}
		return mappedInputs;
	}

	@Override
	public void clear() {
		m_mappings.clear();
	}

	@Override
	public String[] serialize() {
		final List<String> out = new ArrayList<>();

		for (final Entry<String, String> item : m_mappings.entrySet()) {
			out.add(item.getKey() + "\n" + item.getValue());
		}
		return out.toArray(new String[out.size()]);
	}

	@Override
	public void deserialize(final String[] serializedMappings) {
		m_mappings.clear();
		for (final String s : serializedMappings) {
			final String[] names = s.split("\n");

			if (names.length != 2) {
				// Invalid format!
				throw new IllegalArgumentException(
						"Unable to deserialize settings: invalid amount of input tokens!");
			}
			// format is [0] module input name [1] column input name
			m_mappings.put(names[0], names[1]);
		}
	}

}
