package module.mybatis.plus;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lubin
 * @date 2021/5/11
 */
public class Generator {


    private static GlobalConfig globalConfig(String outputDir) {
        GlobalConfig res = new GlobalConfig();
        res.setOutputDir(outputDir);
        res.setAuthor("lubin");
        res.setOpen(false);
        res.setFileOverride(true);
        res.setDateType(DateType.SQL_PACK);
        return res;
    }

    private static DataSourceConfig dataSourceConfig() {
        DataSourceConfig res = new DataSourceConfig();
        res.setUrl("jdbc:mysql://192.168.32.129:12336/manage?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        res.setDriverName("com.mysql.cj.jdbc.Driver");
        res.setUsername("root");
        res.setPassword("MElb@2020");
        return res;
    }

    private static PackageConfig packageConfig(String outputDir) {
        PackageConfig res = new PackageConfig();
        res.setParent("module.mybatis.plus");
        return res;
    }

    private static InjectionConfig injectionConfig(String projectPath) {
        InjectionConfig res = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        res.setFileOutConfigList(focList);
        return res;
    }

    private static StrategyConfig baseStrategyConfig() {
        StrategyConfig res = new StrategyConfig();
        res.setNaming(NamingStrategy.underline_to_camel);
        res.setColumnNaming(NamingStrategy.underline_to_camel);
        res.setEntityLombokModel(true);
        res.setEntityTableFieldAnnotationEnable(true);
        res.setChainModel(true);
        res.setRestControllerStyle(true);
        return res;
    }

    private static StrategyConfig strategyConfig() {
        StrategyConfig res = baseStrategyConfig();
        res.setInclude("manage_permission");
        //res.setTablePrefix("manage");
        //res.setInclude("iot_physical_model");
        return res;
    }


    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 项目路径
        String projectPath = System.getProperty("user.dir") + "/module-entity";
        String outputDir = projectPath + "/src/main/java";

        // 全局配置
        mpg.setGlobalConfig(globalConfig(outputDir));

        // 数据源配置
        mpg.setDataSource(dataSourceConfig());

        // 包配置
        mpg.setPackageInfo(packageConfig(outputDir));

        // 自定义配置
        mpg.setCfg(injectionConfig(projectPath));
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        mpg.setStrategy(strategyConfig());

        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }


}
