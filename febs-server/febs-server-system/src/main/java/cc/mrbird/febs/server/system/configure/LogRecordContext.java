package cc.mrbird.febs.server.system.configure;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/30 20:15
 */
@Component
public class LogRecordContext {

    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    public void putVariable(String name, Object value) {
        if (variableMapStack.get() == null) {
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack.size() == 0) {
            variableMapStack.get().push(new HashMap<>());
        }
        variableMapStack.get().peek().put(name, value);
    }

    public Object getVariable(String key) {
        Stack<Map<String, Object>> maps = variableMapStack.get();
        if (maps == null) {
            return null;
        }
        Map<String, Object> variableMap = maps.peek();
        return variableMap.get(key);
    }

    public Map<String, Object> getVariables() {
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        return mapStack.peek();
    }

    public void clear() {
        if (variableMapStack.get() != null) {
            variableMapStack.get().pop();
        }
    }

    /**
     * 日志使用方不需要使用到这个方法
     * 每进入一个方法初始化一个 span 放入到 stack中，方法执行完后 pop 掉这个span
     */
    public static void putEmptySpan() {
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack == null) {
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        variableMapStack.get().push(Maps.newHashMap());

    }
}
