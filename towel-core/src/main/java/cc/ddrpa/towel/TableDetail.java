package cc.ddrpa.towel;

import java.util.List;

public record TableDetail(List<ColumnDetail> columns, Integer rows) {
}