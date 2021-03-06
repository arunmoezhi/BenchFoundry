package de.tuberlin.ise.benchfoundry.tracegeneration.tpccinspiredbenchmark.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tinyint extends Domain {

	protected final int min;
	protected final int max;
	protected final boolean unsigned;
	protected final boolean zerofill;
	protected final Map<Integer, Integer> leased;
	protected final Random generator = new Random();

	public Tinyint(boolean unique, boolean unsigned, boolean zerofill) {
		super(unique);
		this.leased = new HashMap<>();
		if (unsigned) {
			this.unsigned = true;
			min = 0;
			max = 255;
		} else {
			this.unsigned = false;
			min = -128;
			max = 127;
		}
		if (zerofill)
			this.zerofill = true;
		else
			this.zerofill = false;
	}

	@Override
	public String getName() {
		return Tinyint.class.getName().toUpperCase();
	}

	@Override
	public String nextInsertField() {
		if (super.unique) {
			for (Integer i = min; i <= max; i++) {
				if (!leased.containsKey(i)) {
					leased.put(i, i);
					return i.toString();
				}
			}
			throw new RuntimeException("Cannot generate a new field for unique Domain class " + getName()
					+ " all fields of the domain have a lease.");
		} else {
			if (unsigned)
				return String.valueOf(generator.nextInt(255));
			else
				return String.valueOf(generator.nextInt(255) - 128);
		}
	}

	@Override
	public String nextReadField() {
		Object[] fields = leased.values().toArray();
		return ((Integer) fields[generator.nextInt(fields.length)]).toString();
	}

	@Override
	public String toDdl() {
		String ddl = "TINYINT";
		if(unsigned)
			ddl += " UNSIGNED";
		if(zerofill)
			ddl += " ZEROFILL";
		if (unique)
			ddl += " UNIQUE";
		return ddl;
	}

}
