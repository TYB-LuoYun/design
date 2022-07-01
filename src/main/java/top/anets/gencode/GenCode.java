package top.anets.gencode;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

/**
 * @author LuoYun
 * @since 2022/6/30 16:22
 */
public class GenCode {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.1.229:3306/db_manage_overruns?useSSL=false&autoReconnect=true&characterEncoding=utf8", "root", "zcits123456")
                .globalConfig(builder ->
                        builder.author("LuoYun") // 设置作者
//                                .enableSwagger() // 开启 swagger 模式
                )
                .packageConfig(builder ->
                        builder.parent("top.anets.modules") // 设置父包名
                                .moduleName("zcht") // 设置父包模块名
                )
                .strategyConfig(builder ->
                        builder.addInclude("t_illegal_case_record")
                )
                .execute();
    }
}
