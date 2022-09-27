package top.anets.thread;

/**
 * @author LuoYun
 * @since 2022/7/4 11:07
 */
public class TestExample {
    public static void main(String[] args) {
        System.out.println("==================start");
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: 2022/7/4
                    for (int i=0;i<100;i++){
                        Thread.sleep(100);
                        System.out.println(i);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                }
            }
        });
        System.out.println("==================end");
    }
}
