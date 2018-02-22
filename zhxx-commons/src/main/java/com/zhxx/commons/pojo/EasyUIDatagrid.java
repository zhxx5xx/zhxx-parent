package com.zhxx.commons.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyUIDatagrid implements Serializable {
	private long total;
	private List<?> rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public EasyUIDatagrid(long total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public EasyUIDatagrid() {
		super();
	}

	@Override
	public String toString() {
		return "EasyUIDatagrid [total=" + total + ", rows=" + rows + "]";
	}

}
