package kz.iitu.diploma_resource_server.controller;

import kz.iitu.diploma_resource_server.model.Organization;
import kz.iitu.diploma_resource_server.model.OrganizationToSave;
import kz.iitu.diploma_resource_server.register.OrganizationRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

    private final OrganizationRegister organizationRegister;

    @Autowired
    public OrganizationController(OrganizationRegister organizationRegister) {
        this.organizationRegister = organizationRegister;
    }

    @PostMapping("/save-org")
    public String saveOrg(@RequestBody OrganizationToSave organization) {
        return organizationRegister.saveOrg(organization);
    }

    @GetMapping("/load-org")
    public Organization loadOrgByUsername() {
        var context = SecurityContextHolder.getContext().getAuthentication();

        return organizationRegister.loadOrgByUsername(context.getName());
    }

}
