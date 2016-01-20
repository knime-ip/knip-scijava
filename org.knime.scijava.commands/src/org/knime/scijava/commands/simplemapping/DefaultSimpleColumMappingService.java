package org.knime.scijava.commands.simplemapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;

@Plugin(type=SimpleColumnMappingService.class)
public class DefaultSimpleColumMappingService extends AbstractService
		implements SimpleColumnMappingService {

	private final Map<String, String> m_mappings = new HashMap<>();

	@Override
	public String getMappedColumn(String input) {
		return m_mappings.get(input);
	}

	@Override
	public String mappedColumn(String input) throws NoSuchElementException {
		String column = m_mappings.get(input);
		if (column == null) {
			throw new NoSuchElementException(
					"Can't locate a mapping for input: " + input);
		}
		return column;
	}

	@Override
	public void setMappedColumn(String input, String column) {
		m_mappings.put(input, column);
	}

	@Override
	public List<String> getMappedInputs() {
		List<String> mappedInputs = new ArrayList<>();
		for(Entry<String, String> item : m_mappings.entrySet() ){
			if(item.getValue() != null){
				mappedInputs.add(item.getKey());
			}
		}
		return mappedInputs;
	}

	@Override
	public void clear() {
		m_mappings.clear();
	}

}
