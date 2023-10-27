package cc.ddrpa.towel;

import cc.ddrpa.towel.configuration.TaskConfiguration;
import cc.ddrpa.towel.generator.ColumnTask;
import cc.ddrpa.towel.generator.IGeneratorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class CLIRunner {
    private static final Map<String, IGeneratorFactory> factoryInstanceMap = new HashMap<>();

    public static void main(String[] args) throws IOException, ArgumentParserException {
        // register all generator factories
        registerAllGeneratorFactories();

        // parse arguments
        ArgumentParser parser = ArgumentParsers.newFor("towel")
                .build()
                .defaultHelp(true)
                .description("Generate random stuff")
                .version("${prog} 0.0.1");
        Subparsers subparsers = parser.addSubparsers()
                .dest("subcommand")
                .title("subcommands");
        Subparser rollParser = subparsers.addParser("roll")
                .help("-- Specify towel(YAML, of course) to generate random stuff");
        rollParser.addArgument("towel")
                .type(String.class)
                .required(true);
        Subparser listParser = subparsers.addParser("ls");
        listParser.help("-- List all available generators");
        Subparser manParser = subparsers.addParser("man")
                .help("-- Show manual of given generator");
        manParser.addArgument("generator")
                .required(true)
                .type(String.class);
        Namespace operate = parser.parseArgs(args);

        switch (operate.getString("subcommand")) {
            case "ls":
                listGenerators();
                break;
            case "man":
                String generator = operate.getString("generator");
                showUsage(generator);
                break;
            case "roll":
                String taskTowelPath = operate.getString("towel");
                randomStuff(Path.of(taskTowelPath));
                break;
        }
    }

    private static void showUsage(String generator) {
        var factory = factoryInstanceMap.get(generator);
        if (null == factory) {
            System.out.println("Unknown generator: " + generator);
            System.exit(1);
        }
        System.out.println(factory.getUsage());
    }

    private static void randomStuff(Path towelPath) throws IOException {
        // load task yaml
        TaskConfiguration taskConfig = loadTaskConfiguration(towelPath);
        TableDetails tableDetails = taskConfig.tableDetails();
        ExportDetails exportDetails = taskConfig.exportDetails();
        // 根据配置创建随机器
        var tasks = tableDetails.columns().stream()
                .map(columnDetails -> {
                    var factory = factoryInstanceMap.get(columnDetails.getGeneratorIdentifier());
                    if (null == factory) {
                        System.out.println("Unknown column type: " + columnDetails.getGeneratorIdentifier());
                        System.exit(1);
                    }
                    return new ColumnTask(factory.build(columnDetails), columnDetails.getColumnName(), columnDetails.getValueWrapper());
                })
                .toList();
        // 生成数据
        Table table = Table.create();
        tasks.forEach(task -> {
            var columnValueWrapper = task.ValueWrapper();
            var columnValueGenerator = task.generator();
            var dataSlice = IntStream.range(0, tableDetails.rows())
                    .mapToObj(rowIndex -> columnValueWrapper.apply(columnValueGenerator.next()))
                    .toArray(String[]::new);
            table.addColumns(StringColumn.create(task.columnName(), dataSlice));
        });
        // 输出到控制台
        var fieldWrapperList = makeFieldWrapperList(tableDetails.columns(), exportDetails);
        boolean hasRowDelimiter = !exportDetails.getRowDelimiter().isEmpty();
        boolean hasContentInStartOfRow = !exportDetails.getStartOfRow().isEmpty();
        boolean hasContentInEndOfRow = !exportDetails.getEndOfRow().isEmpty();

        if (!exportDetails.getStartOfFile().isEmpty()) {
            System.out.print(exportDetails.getStartOfFile());
        }
        var rowIterator = table.stream().iterator();
        while (rowIterator.hasNext()) {
            var row = rowIterator.next();
            // 重复判断，待优化
            if (hasContentInStartOfRow) {
                System.out.print(exportDetails.getStartOfRow());
            }
            var rowContent = IntStream.range(0, tasks.size())
                    .mapToObj(fieldIndex -> {
                        var fieldValue = row.getString(fieldIndex);
                        Function<String, String> wrapper = fieldWrapperList.get(fieldIndex);
                        return wrapper.apply(fieldValue);
                    })
                    .collect(Collectors.joining(exportDetails.getFieldDelimiter()));
            System.out.print(rowContent);
            // 重复判断，待优化
            if (hasContentInEndOfRow) {
                System.out.print(exportDetails.getEndOfRow());
            }
            if (hasRowDelimiter && rowIterator.hasNext()) {
                System.out.print(exportDetails.getRowDelimiter());
            }
        }
        if (!exportDetails.getEndOfFile().isEmpty()) {
            System.out.print(exportDetails.getEndOfFile());
        }
    }

    private static List<Function<String, String>> makeFieldWrapperList(List<ColumnDetails> columnDetails, ExportDetails exportDetails) {
        var baseFieldWrapper = exportDetails.getFieldWrapper();
        List<Function<String, String>> fieldWrapperList = new ArrayList<>(columnDetails.size());
        for (int columnIndex = 0; columnIndex < columnDetails.size(); columnIndex++) {
            var columnName = columnDetails.get(columnIndex).getColumnName();
            Function<String, String> columnFieldWrapper = (fieldValue) -> baseFieldWrapper.apply(columnName, fieldValue);
            fieldWrapperList.add(columnFieldWrapper);
        }
        return fieldWrapperList;
    }

    /**
     * 列出所有可用的随机生成器
     */
    private static void listGenerators() {
        var generatorNames = factoryInstanceMap.keySet().stream().collect(groupingBy(name -> name.contains(":")));
        var builtIns = generatorNames.get(false).stream().sorted().toList();
        System.out.println("Available built-in generators:");
        var builtInGeneratorMaxNameWidth = builtIns.stream().map(String::length).max(Integer::compareTo).get() + 2;
        builtIns.forEach(name ->
                System.out.println(String.format("%-" + builtInGeneratorMaxNameWidth + "s", name) + factoryInstanceMap.get(name).getDescription()));
        if (null != generatorNames.get(true) && !generatorNames.get(true).isEmpty()) {
            var customs = generatorNames.get(true).stream().sorted().toList();
            System.out.println("Available custom generators:");
            var customGeneratorMaxNameWidth = customs.stream().map(String::length).max(Integer::compareTo).get() + 2;
            customs.forEach(name ->
                    System.out.println(String.format("%-" + customGeneratorMaxNameWidth + "s", name) + factoryInstanceMap.get(name).getDescription()));
        } else {
            System.out.println("No custom generators found.");
        }
    }

    /**
     * 解析任务配置文件
     *
     * @param configFilePath
     * @return
     * @throws IOException
     */
    private static TaskConfiguration loadTaskConfiguration(Path configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        TaskConfiguration configuration = mapper.readValue(Files.readString(configFilePath), TaskConfiguration.class);
        return configuration;
    }

    /**
     * 注册所有随机生成器工厂类
     */
    private static void registerAllGeneratorFactories() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(IGeneratorFactory.class.getPackage().getName())
                .setScanners(new SubTypesScanner()));
        Set<Class<? extends IGeneratorFactory>> factoryClassSet = reflections.getSubTypesOf(IGeneratorFactory.class);
        factoryClassSet.forEach(clazz -> {
            try {
                var instance = clazz.getDeclaredConstructor().newInstance();
                factoryInstanceMap.put(instance.getName(), instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
