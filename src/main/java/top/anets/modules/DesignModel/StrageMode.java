package top.anets.modules.DesignModel;

/**
 * @author ftm
 * @date 2022/9/23 0023 11:19
 * 策略模式：同一个方法，不同的实现策略，if，else 替代
 *
 *
 *
 * 策略模式的用意是针对一组算法或逻辑，将每一个算法或逻辑封装到具有共同接口的独立的类中，从而使得它们之间可以相互替换
 * 例如：我要做一个不同会员打折力度不同的三种策略，初级会员，中级会员，高级会员（三种不同的计算）
 * 例如：我要一个支付模块，我要有微信支付、支付宝支付、银联支付等
 */
public class StrageMode {

    public static void main(String[] args) {
        Context context;
        //使用支付逻辑A
        context = new Context(new PayStrategyA());
        context.algorithmInterface();
        //使用支付逻辑B
        context = new Context(new PayStrategyB());
        context.algorithmInterface();
        //使用支付逻辑C
        context = new Context(new PayStrategyC());
        context.algorithmInterface();
    }
}


//策略模式 定义抽象方法 所有支持公共接口
abstract class PayStrategy {
    // 支付逻辑方法
    abstract void algorithmInterface();
}


//定义实现微信支付
class PayStrategyA extends PayStrategy {
    void algorithmInterface() {
        System.out.println("微信支付");
    }
}

//定义实现支付宝支付
class PayStrategyB extends PayStrategy {
    void algorithmInterface() {
        System.out.println("支付宝支付");
    }
}


//定义实现银联支付
class PayStrategyC extends PayStrategy {
    void algorithmInterface() {
        System.out.println("银联支付");
    }
}

//定义下文维护算法策略（环境类）
class Context {
    PayStrategy strategy;
    public Context(PayStrategy strategy) {
        this.strategy = strategy;
    }
    public void algorithmInterface() {
        strategy.algorithmInterface();
    }
}