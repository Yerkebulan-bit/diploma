package kz.iitu.diploma_resource_server.register;

import kz.iitu.diploma_resource_server.model.Organization;
import kz.iitu.diploma_resource_server.model.OrganizationToSave;

import java.util.List;

public interface OrganizationRegister {

    String saveOrg(OrganizationToSave org);

    Organization loadOrgByUsername(String username);

    List<Organization> loadOrganizations();

    Organization loadOrgById(String id);

}
