package cc.ddrpa.towel.generator;

import cc.ddrpa.towel.ColumnDetail;

public interface IGeneratorFactory {
    IGenerator build(ColumnDetail columnDetail);

    String getName();

    String getDescription();

    String getUsage();
}