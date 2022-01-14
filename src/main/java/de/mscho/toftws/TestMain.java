package de.mscho.toftws;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class TestMain {

    public static void main(String[] args) throws InterruptedException, ReflectionException, MalformedObjectNameException, InstanceNotFoundException {

        while(true) {
            System.out.println( getProcessCpuLoad() );

            Thread.sleep(1000);
        }
    }

    public static double getProcessCpuLoad() throws MalformedObjectNameException, ReflectionException, InstanceNotFoundException {

        MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
        ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
        AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });

        if (list.isEmpty())     return Double.NaN;

        Attribute att = (Attribute)list.get(0);

        return (Double)att.getValue() * 100;
    }
}
