package de.mscho.toftws.system.controller;

import de.mscho.toftws.entity.Birthday;
import de.mscho.toftws.service.BirthdayService;
import de.mscho.toftws.system.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.management.ReflectionException;
import java.util.Map;

@Controller
@RequestMapping(path = "/system")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping(path = "/metadata")
    @ResponseBody
    public Map<String, Object> getSystemMetadata() throws ReflectionException, InstanceNotFoundException {
        return systemService.getSystemMetadata();
    }
}
