# Towel

Towel 是一款基于命令行的随机数据生成工具（支持插件扩展数据类型）。

首先，我是从一个叫做 [Random Lists](https://www.randomlists.com/) 的网站的 Random things 板块找的这个名字。然后在 Banana 和 Towel 之间选择了后者，因为根据《The Hitchhiker's Guide to the Galaxy》：

> A towel … is about the most massively useful thing an interstellar hitchhiker can have.

该项目受到 [Ecleti](http://www.tensionsoftware.com/) 的 Data Creator 启发，后者是一款 macOS 平台上的随机数据创建软件，不过它 1）主要使用 GUI 交互；2）不能在其他平台上运行；3）似乎不太好扩展；4）是一款收费软件，于是就有了 Towel。

希望 Towel 能帮助你：

1. 做产品原型时填充点看起来真实的数据
2. 做功能冒烟测试时不要再敲 `123123` 了

## 开始使用

Towel 被设计成一款命令行工具，部分原因是我对 GUI 部分没有什么想法（不过我们可以说成「Towel 是对以 Shell 命令和脚本为核心的工作流友好的」来掩饰过去）。

你需要使用一个 YAML 格式的配置文件告诉 Towel 生成哪些数据、生成多少以及用什么格式输出。Towel 会通过标准输出（`stdout`）吐出结果，你可以使用管道重定向到其他工具，或者写入文件。

代码使用了 `Record` 类型，`var` 动态类型推断之类的东西，因此你需要 Java 17+ 的 JRE 来运行（或者做一番并不困难的改造来适配 Java 8）。

### 快速示例

这个示例将生成包含 3 个 JSON 对象的数组，每个对象包含人名、地址、出生日期 3 个元素。

```yaml
# towel.yaml
# 生成 3 条数据
rows: 3
# 定义生成内容
columns:
  - name: name # 数据的列名
    type: person-name # 使用哪个数据生成器
  - name: contact-adddress
    type: address
    prefix: "浙江省衢州市" # 可以指定生成的数据添加前缀 prefix 和后缀 suffix
    province: false # 不同的数据生成器可能会有额外配置
    city: false
  - name: birthday
    type: date
    format: "yyyy-MM-dd"
# 导出配置
export:
  preset: json # 使用内置 JSON 输出格式预设
```

你可以使用 `java` 命令来运行 towel，不过我更推荐在 Shell 配置中添加一个 `towel` 的 alias，少打不少字。

```sh
# in my .zshrc
alias towel='java -cp $HOME/.towel/towel-cli-1.0-SNAPSHOT.jar cc.ddrpa.towel.CLIRunner'

# use shell command
towel roll ./towel.yaml > ./whoa.json && cat ./whoa.json
>>> [
>>>   {
>>>     "name": "萧河康",
>>>     "contact": "浙江省衢州市海港区桥西路324号",
>>>     "birthday": "2013-09-13"
>>>   }, {
>>>     "name": "苏敬固",
>>>     "contact": "浙江省衢州市新华区复兴路221号",
>>>     "birthday": "2040-12-02"
>>>   }, {
>>>     "name": "鲁诚",
>>>     "contact": "浙江省衢州市静海区长安路838号",
>>>     "birthday": "2007-10-31"
>>>   }
>>> ]
```

不要问为什么苏敬固在 2040 年出生，`date` 生成器的默认范围是 1453 年（没错）到 2077 年（没错）。

### 数据生成器类型

使用 `alias` 设置了 `towel` 命令后，你可以使用 `towel ls` 查看支持的数据生成器：

```sh
towel ls

>>> Available built-in generators:
>>> address       省份/城市/区县/街道/门牌号
>>> date          日期与时间
>>> idcard        身份证号码
>>> integer       整数
>>> person-name   姓名
>>> snowflake-id  雪花 ID
>>> whatever      提供一个枚举列表，我们会随机从里面抽取
>>>
>>> Available custom generators:
>>> ddrpa:starwars-quote  星球大战系列电影人物台词
```

使用 `towel man <generator>` 则可以查看详细的使用说明：

```sh
towel man address

>>> 生成地址，可以指定是否包含省份、城市、区县、街道、门牌号。
>>> 由于省份/城市/区县/街道的 token 池是分别随机生成的，可能会出现不合理的组合。
>>> 
>>> province: Boolean # 是否包含省份，默认为 true
>>> city: Boolean # 是否包含城市，默认为 true
>>> district: Boolean # 是否包含区县，默认为 true
>>> street: Boolean # 是否包含街道，默认为 true
>>> streetNumbers: Boolean # 是否包含门牌号，默认为 true
>>> 
>>> 要把地址限定在某个区域，可以使用 prefix 参数搭配部分生成。示例：
>>> - name: 地址
>>>   type: address
>>>   prefix: 北京市海淀区
>>>   province: false
>>>   city: false
```

### 我有说过输出好用吗

借助 `export` 配置，你可以输出 CSV、SQL、HTML 表格、XML、JSON、Markdown ……  一般来说设置 `preset: {json|csv|sql}`足矣，不过想如果需要输出更复杂的格式，例如一个组装好的 curl 命令，只需按图拆解成不同的部分，配置 `export` 选项即可。

![20231029163429.png](doc%2F20231029163429.png)

`field-open-quote` 和 `field-close-quote` 中的 `%c` 会在 towel 在输出值的时替换为对应的列名（如果要使用 `%` 字符，用 `%%` 就好），一般在生成 JSON 和 XML 片段时使用。

```yaml
export:  
  start-of-file: "curl --request POST \
  --url https://ddrpa.cc/what-a-nice-api \
  --header 'Content-Type: application/json' \
  --data '[
  {\n"
  field-open-quote: "    \"%c\": "
  ……
```

## 开发插件

towel 已经内置了大部分常用的随机数据类型，如果您需要的类型不在这个池子里，也可以通过插件机制实现自己的生成器。

首先创建一个 Java 项目，引入 `cc.ddrpa.towel:towel-core` 依赖。由于我没有发布到 Maven 中央仓库，您需要下载本项目的源代码，然后执行 `mvn install` 安装到本地仓库。

您需要在 `cc.ddrpa.towel.generator` package 下为 `cc.ddrpa.towel.generator.IGeneratorFactory` 接口实现工厂类。这个工厂类将会负责：

- 返回插件的名称（以便在 `towel.yaml` 中指定），最好以 `author:data-type` 命名
- 接受 `cc.ddrpa.towel.ColumnDetail` 类型的用户配置（其中自定义配置都会放在 `Map<String, Object> additionalConfig` 中）并创建生成器实例
- 通过 `String getDescription()` 和 `String getUsage()` 返回友好的简介和使用说明

生成器没有特别的要求，只需要实现 `cc.ddrpa.towel.generator.IGenerator` 接口，其中的 `next()` 方法每次调用会返回一个数据。

将您的插件发布 JAR 文件或者其他什么类型，然后使用：

```sh
java -cp towel-starwars-quote-plugin-1.0-SNAPSHOT.jar:towel-cli.jar cc.ddrpa.towel.CLIRunner
```

返回星球大战系列电影中的人物台词，项目中的示例插件会。

## 当前的问题

Towel 运行的大部分时间都用在通过反射机制寻找插件实现上了，这导致 shell 补全之类的东西不会特别好用。
