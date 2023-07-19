package tools.starcitizen;

import lombok.extern.slf4j.Slf4j;
import tools.starcitizen.config.SourceConfig;
import tools.starcitizen.entity.StarCitizenEntity;
import tools.starcitizen.processor.Processor;
import tools.starcitizen.processor.excel.DefaultExcelProcessor;
import tools.starcitizen.reader.DefaultXmlReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
@Slf4j
public class ReadXmlApp {

    public static final String BASE_PATH= SourceConfig.BATH_PATH;

    private static List<Processor> processors = new ArrayList<>();

    static {
        processors.add(new DefaultExcelProcessor());
    }

    public static void main( String[] args ) throws Exception {
        Map<Class, Map<String, StarCitizenEntity>> data = new DefaultXmlReader(BASE_PATH).read();
        processors.forEach(processor -> processor.process(data));
        System.out.println("finish");

    }
}
