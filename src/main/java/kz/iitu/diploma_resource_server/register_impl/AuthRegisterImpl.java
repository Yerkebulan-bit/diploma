package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.model.Organization;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.UserType;
import kz.iitu.diploma_resource_server.register.AuthRegister;
import kz.iitu.diploma_resource_server.sql.OrgTable;
import kz.iitu.diploma_resource_server.sql.UserTable;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AuthRegisterImpl implements AuthRegister {

    private final DataSource dataSource;

    public AuthRegisterImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @SneakyThrows
    public UserType identifyUserType(String username) {
        Strings.requiresNotNullOrEmpty(username);

        try (var connection = dataSource.getConnection()) {
            var user = SqlSelectTo.theClass(User.class)
                    .sql(UserTable.SELECT_USER_BY_NICKNAME)
                    .param(username)
                    .applyTo(connection);

            if (!user.isEmpty()) {
                return UserType.USER;
            }

            var org = SqlSelectTo.theClass(Organization.class)
                    .sql(OrgTable.SELECT_ORG_BY_USERNAME)
                    .param(username)
                    .applyTo(connection);

            if (!org.isEmpty()) {
                return UserType.ORG;
            }
        }

        return UserType.NONE;

    }
}
