package kz.iitu.diploma_resource_server.register_impl;

import kz.iitu.diploma_resource_server.email.EmailService;
import kz.iitu.diploma_resource_server.exception.ExpiredCodeException;
import kz.iitu.diploma_resource_server.exception.WrongCodeException;
import kz.iitu.diploma_resource_server.model.Code;
import kz.iitu.diploma_resource_server.model.Organization;
import kz.iitu.diploma_resource_server.model.User;
import kz.iitu.diploma_resource_server.model.UserType;
import kz.iitu.diploma_resource_server.register.AuthRegister;
import kz.iitu.diploma_resource_server.sql.CodeTable;
import kz.iitu.diploma_resource_server.sql.OrgTable;
import kz.iitu.diploma_resource_server.sql.UserTable;
import kz.iitu.diploma_resource_server.util.SecurityUtil;
import kz.iitu.diploma_resource_server.util.Strings;
import kz.iitu.diploma_resource_server.util.sql.SqlSelectTo;
import kz.iitu.diploma_resource_server.util.sql.SqlUpsert;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthRegisterImpl implements AuthRegister {

    private final static int OTP_EXPIRATION = 10;

    private final DataSource dataSource;
    private final EmailService emailService;

    @Autowired
    public AuthRegisterImpl(DataSource dataSource, EmailService emailService) {
        this.dataSource = dataSource;
        this.emailService = emailService;
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

    @Override
    public String verifyCode(String codeId, String code) {
        Strings.requiresNotNullOrEmpty(codeId);
        Strings.requiresNotNullOrEmpty(code);

        var codeObject = SqlSelectTo.theClass(Code.class)
                .sql(CodeTable.SELECT_CODE_BY_ID)
                .param(codeId)
                .applyTo(dataSource)
                .stream()
                .findFirst()
                .orElseThrow();

        if (System.currentTimeMillis() > codeObject.expiresAt) {
            throw new ExpiredCodeException();
        }

        if (!Objects.equals(code, codeObject.code)) {
            throw new WrongCodeException();
        }

        SqlUpsert.into("users")
                .key("username", codeObject.username)
                .field("enabled", true)
                .toUpdate()
                .ifPresent(u -> u.applyTo(dataSource));

        SqlUpsert.into(CodeTable.TABLE_NAME)
                .key(CodeTable.ID, codeId)
                .field(CodeTable.ACTUAL, false)
                .toUpdate()
                .ifPresent(u -> u.applyTo(dataSource));

        return codeObject.userId;
    }

    @Override
    public String sendVerificationCode(String username, String userId, String email) {
        var code = SecurityUtil.generateOTP(4);

        var message = "Ваш код авторизации: " + code;

        emailService.sendEmail(message, email);

        var codeId = UUID.randomUUID().toString();

        SqlUpsert.into(CodeTable.TABLE_NAME)
                .key(CodeTable.ID, codeId)
                .field(CodeTable.CODE, code)
                .field(CodeTable.USER_ID, userId)
                .field(CodeTable.USERNAME, username)
                .field(CodeTable.EXPIRES_AT, SecurityUtil.getOtpExpirationTime(OTP_EXPIRATION))
                .toUpdate()
                .ifPresent(u -> u.applyTo(dataSource));

        return codeId;
    }

}
