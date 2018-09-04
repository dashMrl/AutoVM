package com.dashmrl.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;

/**
 * @author xavier <a href="mailto:xinliugm@gmail.com">Contact me.</a>
 * @since 2018/8/19 22:38
 */
public class VMCollector {
    private Map<String, List<ExecutableElement>> map = new HashMap<>();

    public void addConstructor(String canonicalName, ExecutableElement constructor) {
        if (!map.containsKey(canonicalName)) {
            map.put(canonicalName, new ArrayList<>());
        }
        map.get(canonicalName).add(constructor);
    }

    public Set<String> vmClasses() {
        return map.keySet();
    }

    public List<ExecutableElement> getVmConstructors(String canonicalName) {
        return map.get(canonicalName);
    }

}
