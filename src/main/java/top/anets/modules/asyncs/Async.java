package top.anets.modules.asyncs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author ftm
 * @date 2023/4/12 0012 12:52
 */
@RestController
public class Async {
    @GetMapping("/myendpoint")
    public CompletableFuture<String[]> myEndpoint() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 第一个需要异步执行的方法
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // 第二个需要异步执行的方法
            return "Result 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            // 第三个需要异步执行的方法
            return "Result 3";
        });

        CompletableFuture<String[]> completableFuture = CompletableFuture.allOf(future1, future2, future3)
                .thenApply(v -> new String[]{future1.join(), future2.join(), future3.join()});

        return completableFuture.exceptionally(ex -> {
            // 处理异常
            throw new RuntimeException("Async execution failed: " + ex.getMessage(), ex);
        });
    }



    public CompletableFuture<Map> myEndpoints() {
        Map map =new HashMap();
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 第一个需要异步执行的方法
            return "Result 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // 第二个需要异步执行的方法
            return "Result 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            // 第三个需要异步执行的方法
            return "Result 3";
        });

        CompletableFuture<Map> completableFuture = CompletableFuture.allOf(future1, future2, future3)
                .thenApply(v -> {
                    map.put("1", future1.join());
                    map.put("2", future2.join());
                    map.put("3", future3.join());
                    return map;
                });

        return completableFuture.exceptionally(ex -> {
            // 处理异常
            throw new RuntimeException("Async execution failed: " + ex.getMessage(), ex);
        });
    }

}
