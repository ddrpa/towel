package cc.ddrpa.towel.generator;

import cc.ddrpa.towel.ColumnDetails;

public interface IGeneratorFactory {
    IGenerator build(ColumnDetails columnDetails);

    String getName();

    String getDescription();

    String getUsage();
}