package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.Organization;
import kz.iitu.diploma_resource_server.model.OrganizationToSave;

public interface OrganizationRegister {

    String saveOrg(OrganizationToSave org);

    Organization loadOrgByUsername(String username);

    Organization loadOrgById(String id);

}
