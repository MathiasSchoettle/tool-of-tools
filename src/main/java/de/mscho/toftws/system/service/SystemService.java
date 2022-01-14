package de.mscho.toftws.system.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@Service
public class SystemService {

    private MBeanServer mbs;

    private ObjectName name;

    @PostConstruct
    public void postConstruct() throws MalformedObjectNameException {
        mbs = ManagementFactory.getPlatformMBeanServer();
        name = ObjectName.getInstance("java.lang:type=OperatingSystem");
    }

    public Map<String, Object> getSystemMetadata() throws ReflectionException, InstanceNotFoundException {

        Map<String, Object> systemMetaData = new HashMap<>();

        systemMetaData.put("CPU", getSystemCpuLoad());

        return systemMetaData;
    }

    private Double getSystemCpuLoad() throws ReflectionException, InstanceNotFoundException {

        AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });

        if(list == null || list.isEmpty())
            return 0d;

        Attribute attribute =  (Attribute) list.get(0);
        return (Double) attribute.getValue();
    }
}
