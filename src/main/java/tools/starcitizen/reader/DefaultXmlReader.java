package tools.starcitizen.reader;

import tools.starcitizen.entity.StarCitizenEntity;
import tools.starcitizen.reader.product.ProductXmlReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wftank
 * @Date: 2020/10/5
 * @Description: 默认星际公民xml解析器，将解析的所有组件物品放入一个map中
 */
public class DefaultXmlReader implements XmlReader {

    private String basePath;

    private List<XmlReader> xmlReaders;
    private Map<Class, Map<String, StarCitizenEntity>> itemsMap = new ConcurrentHashMap<>();

    public DefaultXmlReader(String basePath) {
        this.basePath = basePath;
        init();
    }

    public void init(){
        //加载解析器
        xmlReaders = new ArrayList<>();
//        xmlReaders.add(new CoolerXmlReader(basePath));
//        xmlReaders.add(new ShieldGeneratorXmlReader(basePath));
//        xmlReaders.add(new PowerplantXmlReader(basePath));
//        xmlReaders.add(new FuelTanksXmlReader(basePath));
//        xmlReaders.add(new QuantumDriveXmlReader(basePath));
        xmlReaders.add(new ProductXmlReader());
    }

    @Override
    public Map<Class, Map<String, StarCitizenEntity>> read() {
        itemsMap.clear();
        //通过子类型解析器解析所有文件
        CompletableFuture[] completableFutures = new CompletableFuture[xmlReaders.size()];
        for (int i = 0; i < xmlReaders.size(); i++) {
            int index = i;
            completableFutures[i] =
                    CompletableFuture.supplyAsync(() -> xmlReaders.get(index).read())
                            .thenAccept(result -> this.itemsMap.putAll(result));
        }
        CompletableFuture.allOf(completableFutures).join();
        return itemsMap;
    }
}
