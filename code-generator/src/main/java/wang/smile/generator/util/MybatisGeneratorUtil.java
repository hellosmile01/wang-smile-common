package wang.smile.generator.util;

import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成类
 * @author wangsy
 * @date 2018-5-25
 */
public class MybatisGeneratorUtil {

    private static Object JAVA_PATH = "\\src\\main\\java";
    /**
     * 资源文件路径
     */
    private static final String RESOURCES_PATH = "\\src\\main\\resources";

    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

    private static final String AUTHOR = "wangsy";

    /**
     * 根据模板生成generatorConfig.xml文件
     * @param jdbcDiverClassName     驱动路径
     * @param jdbcUrl                  链接
     * @param jdbcUsername             帐号
     * @param jdbcPassword             密码
     * @param basePackage              包名
     * @param tableName                 表名
     */
    public static void generator(String jdbcDiverClassName, String jdbcUrl, String jdbcUsername, String jdbcPassword, String projectPath,
                                 String moduleName, String basePackage, String modelName, String tableName, String templateFilePath, boolean genService, boolean genController) {

        Context context = new Context(ModelType.FLAT);
        context.setId("Potato");
        context.setTargetRuntime("MyBatis3Simple");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");

        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        jdbcConnectionConfiguration.setConnectionURL(jdbcUrl);
        jdbcConnectionConfiguration.setUserId(jdbcUsername);
        jdbcConnectionConfiguration.setPassword(jdbcPassword);
        jdbcConnectionConfiguration.setDriverClass(jdbcDiverClassName);
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        String mapperInterfaceReference = "wang.smile.common.base.Mapper";
        pluginConfiguration.addProperty("mappers", mapperInterfaceReference);
        context.addPluginConfiguration(pluginConfiguration);

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(projectPath + "/" + moduleName + JAVA_PATH);
        javaModelGeneratorConfiguration.setTargetPackage(basePackage + ".model");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(projectPath + "/"+moduleName + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("mapper");
        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(projectPath + "/" + moduleName + JAVA_PATH);
        javaClientGeneratorConfiguration.setTargetPackage(basePackage + ".mapper");
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        if (StringUtils.isNotEmpty(modelName)) {
            tableConfiguration.setDomainObjectName(modelName);
        }
        tableConfiguration.setGeneratedKey(new GeneratedKey("id", "Mysql", true, null));
        context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            DefaultShellCallback callback = new DefaultShellCallback(true);
            warnings = new ArrayList<>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
        if (StringUtils.isEmpty(modelName)) {
            modelName = tableNameConvertUpperCamel(tableName);
        }
        System.out.println(modelName + ".java 生成成功");
        System.out.println(modelName + "Mapper.java 生成成功");
        System.out.println(modelName + "Mapper.xml 生成成功");

        String packagePathService = packageConvertPath(basePackage + ".service");
        String packagePathServiceImpl = packageConvertPath(basePackage + ".service.impl");
        String packagePathController = packageConvertPath(basePackage + ".controller");
        String packagePathDto = packageConvertPath(basePackage + ".dto");
        String packagePathVo = packageConvertPath(basePackage + ".vo");

        genModelDto(tableName, modelName, templateFilePath,
                basePackage, projectPath+"/"+moduleName, packagePathDto, packagePathVo);

        if(genService) {
            genService(tableName, modelName, templateFilePath,
                    basePackage, projectPath+"/"+moduleName, packagePathService, packagePathServiceImpl);
        }
        if(genController) {
           // genController(tableName, modelName, templateFilePath, basePackage, projectPath+"/"+moduleName, packagePathController);
        }
    }

    private static void genService(String tableName,
                                   String modelName,
                                   String templateFilePath,
                                   String basePackage,
                                   String projectPath,
                                   String packagePathService,
                                   String packagePathServiceImpl) {
        try {
            freemarker.template.Configuration cfg = getConfiguration(templateFilePath);

            Map<String, Object> data = new HashMap<>(2);
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            String modelDtoNameUpperCamel = (StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName) + "Dto";
            String modelVoNameUpperCamel = (StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName) + "Vo";
            data.put("modelVoNameUpperCamel", modelVoNameUpperCamel);
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelDtoNameUpperCamel", modelDtoNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
            data.put("basePackage", basePackage);

            File file = new File(projectPath + JAVA_PATH + packagePathService + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data,
                    new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(projectPath + JAVA_PATH + packagePathServiceImpl + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            cfg.getTemplate("service-impl.ftl").process(data,
                    new FileWriter(file1));
            System.out.println(modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

    private static void genModelDto(String tableName,
                                    String modelName,
                                    String templateFilePath,
                                    String basePackage,
                                    String projectPath,
                                    String packagePathDto,
                                    String packagePathVo) {
        try {

            freemarker.template.Configuration cfg = getConfiguration(templateFilePath);

            Map<String, Object> data = new HashMap<>(2);
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            String modelDtoNameUpperCamel = (StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName) + "Dto";
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelDtoNameUpperCamel", modelDtoNameUpperCamel);
            data.put("basePackage", basePackage);

            File file = new File(projectPath + JAVA_PATH + packagePathDto + modelNameUpperCamel + "Dto.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("model-dto.ftl").process(data, new FileWriter(file));


            String modelVoNameUpperCamel = (StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName) + "Vo";

            data.put("modelVoNameUpperCamel", modelVoNameUpperCamel);

            File fileVo = new File(projectPath + JAVA_PATH + packagePathVo + modelNameUpperCamel + "Vo.java");
            if (!fileVo.getParentFile().exists()) {
                fileVo.getParentFile().mkdirs();
            }
            cfg.getTemplate("model-vo.ftl").process(data, new FileWriter(fileVo));

            System.out.println(modelDtoNameUpperCamel + "Dto.java 和 "+modelDtoNameUpperCamel + "Vo.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成DTO和Vo失败", e);
        }

    }

    private static void genController(String tableName, String modelName, String templateFilePath, String basePackage, String projectPath, String packagePathController) {
        try {
            freemarker.template.Configuration cfg = getConfiguration(templateFilePath);

            Map<String, Object> data = new HashMap<>(2);
            data.put("date", DATE);
            data.put("author", AUTHOR);
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", basePackage);

            File file = new File(projectPath + JAVA_PATH + packagePathController + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("controller.ftl").process(data, new FileWriter(file));

            System.out.println(modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

    private static freemarker.template.Configuration getConfiguration(String templateFilePath) throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(templateFilePath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

    }

    private static String tableNameConvertMappingPath(String tableName) {
        tableName = tableName.toLowerCase();
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }

    private static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

}
