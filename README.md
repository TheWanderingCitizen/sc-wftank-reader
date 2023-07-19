# 商品数据导出工具
其他数据也可以导出，思路一致，不过没写完

## 运行环境
- jdk17+ [adoptOpenjdk](https://adoptium.net/zh-CN/temurin/releases/)（不要求具体厂商，随只是便贴个方便你下载）
## 使用
- 下载[unp4k](https://github.com/dolkensp/unp4k)（想用其他解包工具都可以，只要保证解压出来的目录结构一致即可，目前只需要P4K中的xml、json、ini文件，过滤掉其他模型等文件后就会减少不小的体积，unp4k我下面的bat脚本里有过滤，用其他工具的自行过滤）
- 将[unpak-pu.bat](unpak-pu.bat)**修改后**放入任意文件夹并运行
- 编辑[source_config.txt](source_config.txt)，配置unpak-pu.bat中配置的Data目录
- 编辑[system_location_cn.txt](system_location_cn.txt)和[system_location_en.txt](system_location_en.txt)，其中大部分我已配置好，3.10版本之后新出的位置可以配到里面，当然你看生成的excel里如果中文或者英文名称显示的没问题可以不用加
- 执行[ReadXmlApp.java](src%2Fmain%2Fjava%2Ftools%2Fstarcitizen%2FReadXmlApp.java)的main方法
- 生成出的starcitizen.xlsx打开就是商品数据库
- 生成出的Data和jsonfile文件夹里是每种商品的详情和索引文件，供[星际公民商品搜索器](https://wftank.cn/search)以及[wftank-qq机器人](https://github.com/herokillerJ/wftank-qqrobot)使用的数据源
- logs就是日志，方便排查问题，也可以直接看控制台
## 注意
- 中文翻译依赖P4K数据目录中Localization\chineses\global.ini，从汉化中拿出来或者向汉化组要，没有代码报错，会改可以自己注掉只要英文
- [system_location_cn.txt](system_location_cn.txt)和[system_location_en.txt](system_location_en.txt)需要手动映射地点名称，否则直接用属性名。global.ini中有的就用@开头引用，没有的就直接打英文或者汉字
- [shopname_mapping.txt](shopname_mapping.txt)用来映射无法匹配的商店名称
- [excel_config.txt](excel_config.txt)是配置化提取p4k中数据并生成excel列的文件，目前源码里屏蔽掉了，想用的话看源码
- [unknown_shop.txt](unknown_shop.txt)是列出没有匹配的商店名称，如果想映射直接配置

- 其他配置文件干啥用的看源码或等我后续补上说明

## 代码结构概述
总的来说就是用各种[reader](src%2Fmain%2Fjava%2Ftools%2Fstarcitizen%2Freader)读取各个目录下的xml、通过对应的converter[converter](src%2Fmain%2Fjava%2Ftools%2Fstarcitizen%2Fconverter)来转换原始数据对象，[processor](src%2Fmain%2Fjava%2Ftools%2Fstarcitizen%2Fprocessor)processor用于将转换后的对象处理成任何形式的输出。
[context](src%2Fmain%2Fjava%2Ftools%2Fstarcitizen%2Fcontext)目录中是各种Map用来在处理数据的过程中进行本地化（global.ini）、地理位置等匹配

