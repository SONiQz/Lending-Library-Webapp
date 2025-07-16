package org.example.library.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;

public class CustomNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    // An attempt th handle naming schema of columns vs application variables - However non-functional
    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return new Identifier(name.getText().toUpperCase(), name.isQuoted());
    }
}
