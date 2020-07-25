package com.github.euler.opendistro;

import static org.elasticsearch.common.xcontent.ConstructingObjectParser.constructorArg;

import java.io.IOException;
import java.util.Collection;

import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ConstructingObjectParser;
import org.elasticsearch.common.xcontent.XContentParser;

import com.github.euler.api.security.AuthResponse;

public class OpenDistroAuthResponse implements AuthResponse {

    static final ParseField USER = new ParseField("user");
    static final ParseField USERNAME = new ParseField("user_name");
    static final ParseField BACKENDROLES = new ParseField("backend_roles");
    static final ParseField ROLES = new ParseField("roles");

    @SuppressWarnings("unchecked")
    private static final ConstructingObjectParser<OpenDistroAuthResponse, Void> PARSER = new ConstructingObjectParser<>("client_opensecurity_authinfo_response",
            true, a -> new OpenDistroAuthResponse((String) a[0], (String) a[1], (Collection<String>) a[2], (Collection<String>) a[3]));

    static {
        PARSER.declareString(constructorArg(), USER);
        PARSER.declareString(constructorArg(), USERNAME);
        PARSER.declareStringArray(constructorArg(), BACKENDROLES);
        PARSER.declareStringArray(constructorArg(), ROLES);
    }

//    private String user;
    private String userName;
    private Collection<String> backendRoles;
//    private Collection<String> roles;

    public OpenDistroAuthResponse(String user, String userName, Collection<String> backendRoles, Collection<String> roles) {
        super();
//        this.user = user;
        this.userName = userName;
        this.backendRoles = backendRoles;
//        this.roles = roles;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public Collection<String> getRoles() {
        return backendRoles;
    }

    public static OpenDistroAuthResponse fromXContent(XContentParser parser) throws IOException {
        return PARSER.parse(parser, null);
    }

}
