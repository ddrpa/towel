package cc.ddrpa.towel;

import java.util.List;

public record TableDetails(List<ColumnDetails> columns, Integer rows) {
}