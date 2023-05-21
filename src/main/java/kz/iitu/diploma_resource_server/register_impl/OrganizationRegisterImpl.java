package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.Organization;
import kz.iitu.diploma_resource_server.model.OrganizationToSave;
import kz.iitu.diploma_resource_server.register.OrganizationRegister;
import kz.iitu.diploma_resource_server.sql.OrgTable;
import kz.iitu.diploma_resource_server.util.StringUtils;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.UUID;

@Component
public class OrganizationRegisterImpl implements OrganizationRegister {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OrganizationRegisterImpl(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    @Override
    public String saveOrg(OrganizationToSave org) {
        Objects.requireNonNull(org);

        var isCreate = StringUtils.isNullOrEmpty(org.id);

        var orgId = isCreate
                ? UUID.randomUUID().toString()
                : org.id;

        try (var connection = dataSource.getConnection()) {
            SqlUpsert.into(OrgTable.TABLE_NAME)
                    .key(OrgTable.ID, orgId)
                    .field(OrgTable.NAME, org.name)
                    .field(OrgTable.ADDRESS, org.address)
                    .field(OrgTable.EMAIL, org.email)
                    .field(OrgTable.SHORT_DESCRIPTION, org.shortDescription)
                    .field(OrgTable.DESCRIPTION, org.description)
                    .field(OrgTable.IMAGE_ID, org.imageId)
                    .field(OrgTable.PHONE, org.phone)
                    .field(OrgTable.SITE, org.site)
                    .field(OrgTable.USERNAME, org.username)
                    .toUpdate().ifPresent(u -> u.applyTo(dataSource));

            if (isCreate) {
                SqlUpsert.into("users")
                        .key("username", org.username)
                        .field("password", passwordEncoder.encode(org.password))
                        .field("enabled", true)
                        .field("kind", "org")
                        .toUpdate()
                        .ifPresent(u -> u.applyTo(connection));

                SqlUpsert.into("authorities")
                        .key("username", org.username)
                        .field("authority", "read")
                        .toUpdate()
                        .ifPresent(u -> u.applyTo(connection));
            }
        }

        return orgId;
    }

    @Override
    public Organization loadOrgByUsername(String username) {
        Strings.requiresNotNullOrEmpty(username);

        return SqlSelectTo.theClass(Organization.class)
                .sql(OrgTable.SELECT_ORG_BY_USERNAME)
                .param(username)
                .applyTo(dataSource)
                .stream().findFirst().orElseThrow();
    }

    @Override
    public Organization loadOrgById(String id) {
        Strings.requiresNotNullOrEmpty(id);

        return SqlSelectTo.theClass(Organization.class)
                .sql(OrgTable.SELECT_ORG_BY_ID)
                .param(id)
                .applyTo(dataSource)
                .stream().findFirst().orElseThrow();
    }

}
